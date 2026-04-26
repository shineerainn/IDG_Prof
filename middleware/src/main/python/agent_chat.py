import json
import logging
import os
from typing import Dict, Any, List, Optional
import requests
from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from utils import separate_think_and_answer, remove_think_and_answer_tags

# 配置日志
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

agent_router = APIRouter()

# DeepSeek API 配置
DEEPSEEK_API_KEY = os.getenv("DEEPSEEK_API_KEY", "")
DEEPSEEK_API_URL = "https://api.deepseek.com/v1/chat/completions"

class ChatMessage(BaseModel):
    role: str  # "system", "user", "assistant"
    content: str

class ChatRequest(BaseModel):
    messages: List[ChatMessage]
    model_name: str = "deepseek-chat"
    temperature: Optional[float] = 0.7
    max_tokens: Optional[int] = 2000
    identity: Optional[str] = "student"
    profile_state: Optional[Dict[str, Any]] = None
    conversation_context: Optional[str] = None

class ChatResponse(BaseModel):
    content: str
    tokens_used: int
    model: str

class ThinkResponse(BaseModel):
    think: str
    answer: str
    tokens_used: int
    model: str

def convert_model_name(model_name: str) -> str:
    """
    将前端传来的模型名转换为 DeepSeek API 支持的模型名
    """
    model_lower = model_name.lower()

    # 如果包含 reasoner 或 r1,使用推理模型
    if "reasoner" in model_lower or "r1" in model_lower:
        return "deepseek-reasoner"

    # 默认使用对话模型
    return "deepseek-chat"


def route_agent(query: str, identity: str, profile_state: Optional[Dict[str, Any]]) -> str:
    text = (query or "").lower()
    identity = (identity or "student").lower()
    profile_state = profile_state or {}
    risk_level = str(profile_state.get("riskLevel", profile_state.get("risk_level", ""))).lower()
    scores = {
        "psychological_support": 0.0,
        "academic_diagnosis": 0.0,
        "decision_assistance": 0.0,
        "data_analysis": 0.0,
        "general_assistant": 0.0,
    }
    if any(keyword in text for keyword in ["心理", "焦虑", "压力", "情绪", "stress", "anxiety"]):
        scores["psychological_support"] += 2.0
    if any(keyword in text for keyword in ["成绩", "课程", "论文", "科研", "学业", "academic", "thesis"]):
        scores["academic_diagnosis"] += 2.0
    if any(keyword in text for keyword in ["建议", "规划", "选择", "导师", "毕业", "plan", "decision"]):
        scores["decision_assistance"] += 1.5
    if any(keyword in text for keyword in ["数据", "图表", "分析", "聚类", "画像", "chart", "cluster"]):
        scores["data_analysis"] += 1.5
    if risk_level == "high":
        scores["psychological_support"] += 0.8
        scores["academic_diagnosis"] += 0.5
    if identity in {"teacher", "supervisor", "counselor", "admin"}:
        scores["data_analysis"] += 0.4
        scores["decision_assistance"] += 0.4
    selected = max(scores, key=scores.get)
    return selected if scores[selected] > 0 else "general_assistant"


def build_identity_aware_messages(request: ChatRequest) -> List[Dict[str, str]]:
    messages = [{"role": msg.role, "content": msg.content} for msg in request.messages]
    latest_query = next((msg["content"] for msg in reversed(messages) if msg["role"] == "user"), "")
    agent_type = route_agent(latest_query, request.identity or "student", request.profile_state)
    profile_json = json.dumps(request.profile_state or {}, ensure_ascii=False)
    system_prompt = (
        "You are the IDG-Prof intervention module. "
        f"User identity: {request.identity or 'student'}. "
        f"Routed agent: {agent_type}. "
        f"Graduate profile state: {profile_json}. "
        f"Conversation context: {request.conversation_context or ''}. "
        "Respond with personalized, practical educational support. "
        "For high psychological risk, be supportive and recommend timely human counselor involvement."
    )
    if messages and messages[0]["role"] == "system":
        messages[0]["content"] = system_prompt + "\n" + messages[0]["content"]
    else:
        messages.insert(0, {"role": "system", "content": system_prompt})
    return messages


def build_auth_headers() -> Dict[str, str]:
    if not DEEPSEEK_API_KEY:
        raise RuntimeError("DEEPSEEK_API_KEY is not configured")
    return {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {DEEPSEEK_API_KEY}",
    }

@agent_router.post("/chat")
async def agent_chat(request: ChatRequest):
    """
    标准智能体对话接口 - 使用 DeepSeek API
    """
    try:
        logger.info(f"处理聊天请求,原始模型: {request.model_name}, 消息数量: {len(request.messages)}")

        # 转换消息格式
        messages = build_identity_aware_messages(request)

        # 自动转换模型名
        api_model = convert_model_name(request.model_name)
        logger.info(f"使用 DeepSeek API 模型: {api_model}")

        # 调用 DeepSeek API
        headers = build_auth_headers()

        payload = {
            "model": api_model,  # 使用转换后的模型名
            "messages": messages,
            "temperature": request.temperature,
            "max_tokens": request.max_tokens
        }

        logger.info(f"发送请求到 DeepSeek API: {DEEPSEEK_API_URL}")
        response = requests.post(DEEPSEEK_API_URL, headers=headers, json=payload, timeout=30)
        response.raise_for_status()

        result = response.json()

        # 提取响应内容
        raw_content = result["choices"][0]["message"]["content"]
        clean_content = remove_think_and_answer_tags(raw_content)

        # 获取 token 使用量
        tokens_used = result.get("usage", {}).get("total_tokens", 0)

        logger.info(f"聊天请求处理成功,回复长度: {len(clean_content)}, token: {tokens_used}")

        return {
            "status": "success",
            "data": {
                "content": clean_content,
                "tokens_used": tokens_used,
                "model": api_model
            }
        }

    except requests.exceptions.HTTPError as e:
        logger.error(f"DeepSeek API HTTP错误: {str(e)}")
        error_detail = ""
        try:
            if e.response is not None:
                error_detail = e.response.text
                logger.error(f"API 错误详情: {error_detail}")
        except:
            pass
        return {
            "status": "error",
            "message": f"API调用失败(HTTP {e.response.status_code if e.response else ''}): {str(e)}\n详情: {error_detail}"
        }
    except requests.exceptions.RequestException as e:
        logger.error(f"DeepSeek API 请求异常: {str(e)}")
        return {
            "status": "error",
            "message": f"API请求异常: {str(e)}"
        }
    except Exception as e:
        logger.error(f"聊天请求处理失败: {str(e)}")
        import traceback
        traceback.print_exc()
        return {
            "status": "error",
            "message": f"聊天服务处理失败: {str(e)}"
        }

@agent_router.post("/think-and-answer")
async def agent_think_and_answer(request: ChatRequest):
    """
    带思考过程的智能体对话接口 - 使用 DeepSeek API
    """
    try:
        logger.info(f"处理思考对话请求,原始模型: {request.model_name}, 消息数量: {len(request.messages)}")

        # 转换消息格式
        messages = build_identity_aware_messages(request)

        # 思考模式强制使用推理模型
        api_model = "deepseek-reasoner"
        logger.info(f"使用 DeepSeek 推理模型: {api_model}")

        # 调用 DeepSeek API
        headers = build_auth_headers()

        payload = {
            "model": api_model,
            "messages": messages,
            "temperature": request.temperature,
            "max_tokens": request.max_tokens
        }

        logger.info(f"发送思考请求到 DeepSeek API")
        response = requests.post(DEEPSEEK_API_URL, headers=headers, json=payload, timeout=30)
        response.raise_for_status()

        result = response.json()

        # 提取响应内容
        message_data = result["choices"][0]["message"]
        raw_content = message_data["content"]

        # 提取推理过程(DeepSeek Reasoner 特有)
        reasoning_content = message_data.get("reasoning_content", "")

        separated = {
            "think": reasoning_content if reasoning_content else "",
            "answer": raw_content
        }

        # 获取 token 使用量
        tokens_used = result.get("usage", {}).get("total_tokens", 0)

        logger.info(f"思考对话请求处理成功,思考长度: {len(separated['think'])}, 答案长度: {len(separated['answer'])}")

        return {
            "status": "success",
            "data": {
                "think": separated["think"],
                "answer": separated["answer"],
                "tokens_used": tokens_used,
                "model": api_model
            }
        }

    except requests.exceptions.HTTPError as e:
        logger.error(f"DeepSeek API HTTP错误: {str(e)}")
        error_detail = ""
        try:
            if e.response is not None:
                error_detail = e.response.text
                logger.error(f"API 错误详情: {error_detail}")
        except:
            pass
        return {
            "status": "error",
            "message": f"API调用失败(HTTP {e.response.status_code if e.response else ''}): {str(e)}\n详情: {error_detail}"
        }
    except requests.exceptions.RequestException as e:
        logger.error(f"DeepSeek API 请求异常: {str(e)}")
        return {
            "status": "error",
            "message": f"API请求异常: {str(e)}"
        }
    except Exception as e:
        logger.error(f"思考对话请求处理失败: {str(e)}")
        import traceback
        traceback.print_exc()
        return {
            "status": "error",
            "message": f"思考对话服务处理失败: {str(e)}"
        }

@agent_router.post("/chat/stream")
async def agent_chat_stream(request: ChatRequest):
    """
    流式聊天接口 - 使用 DeepSeek API
    """
    try:
        logger.info(f"处理流式聊天请求,模型: {request.model_name}")

        # 转换消息格式
        messages = build_identity_aware_messages(request)

        # 自动转换模型名
        api_model = convert_model_name(request.model_name)
        logger.info(f"使用 DeepSeek API 模型(流式): {api_model}")

        # 调用 DeepSeek API (流式)
        headers = build_auth_headers()

        payload = {
            "model": api_model,
            "messages": messages,
            "temperature": request.temperature,
            "max_tokens": request.max_tokens,
            "stream": True
        }

        response = requests.post(
            DEEPSEEK_API_URL,
            headers=headers,
            json=payload,
            stream=True,
            timeout=30
        )
        response.raise_for_status()

        def generate():
            for line in response.iter_lines():
                if line:
                    line_text = line.decode('utf-8')
                    if line_text.startswith('data: '):
                        data_str = line_text[6:]
                        if data_str.strip() == '[DONE]':
                            break
                        try:
                            data = json.loads(data_str)
                            if 'choices' in data and len(data['choices']) > 0:
                                delta = data['choices'][0].get('delta', {})
                                content = delta.get('content', '')
                                if content:
                                    clean_content = remove_think_and_answer_tags(content)
                                    yield json.dumps({
                                        "status": "success",
                                        "data": {"content": clean_content}
                                    }, ensure_ascii=False) + "\n"
                        except json.JSONDecodeError:
                            continue

        return generate()

    except Exception as e:
        logger.error(f"流式聊天请求处理失败: {str(e)}")
        return {
            "status": "error",
            "message": f"流式聊天服务处理失败: {str(e)}"
        }

@agent_router.post("/think-and-answer/stream")
async def agent_think_stream(request: ChatRequest):
    """
    流式思考对话接口 - 使用 DeepSeek API
    """
    try:
        logger.info(f"处理流式思考对话请求,模型: {request.model_name}")

        # 转换消息格式
        messages = build_identity_aware_messages(request)

        # 强制使用推理模型
        api_model = "deepseek-reasoner"
        logger.info(f"使用 DeepSeek 推理模型(流式): {api_model}")

        # 调用 DeepSeek API (流式)
        headers = build_auth_headers()

        payload = {
            "model": api_model,
            "messages": messages,
            "temperature": request.temperature,
            "max_tokens": request.max_tokens,
            "stream": True
        }

        response = requests.post(
            DEEPSEEK_API_URL,
            headers=headers,
            json=payload,
            stream=True,
            timeout=30
        )
        response.raise_for_status()

        def generate():
            for line in response.iter_lines():
                if line:
                    line_text = line.decode('utf-8')
                    if line_text.startswith('data: '):
                        data_str = line_text[6:]
                        if data_str.strip() == '[DONE]':
                            break
                        try:
                            data = json.loads(data_str)
                            if 'choices' in data and len(data['choices']) > 0:
                                delta = data['choices'][0].get('delta', {})

                                # 获取推理内容
                                reasoning = delta.get('reasoning_content', '')
                                if reasoning:
                                    yield json.dumps({
                                        "status": "success",
                                        "data": {"think": reasoning, "answer": ""}
                                    }, ensure_ascii=False) + "\n"

                                # 获取回答内容
                                content = delta.get('content', '')
                                if content:
                                    yield json.dumps({
                                        "status": "success",
                                        "data": {"think": "", "answer": content}
                                    }, ensure_ascii=False) + "\n"
                        except json.JSONDecodeError:
                            continue

        return generate()

    except Exception as e:
        logger.error(f"流式思考对话请求处理失败: {str(e)}")
        return {
            "status": "error",
            "message": f"流式思考对话服务处理失败: {str(e)}"
        }

@agent_router.get("/health")
async def health_check():
    """
    检查聊天服务状态
    """
    try:
        logger.info("执行健康检查")

        # 测试 API 连接
        headers = build_auth_headers()

        # 简单的测试请求
        payload = {
            "model": "deepseek-chat",
            "messages": [{"role": "user", "content": "hi"}],
            "max_tokens": 10
        }

        response = requests.post(DEEPSEEK_API_URL, headers=headers, json=payload, timeout=10)
        response.raise_for_status()

        logger.info("健康检查成功")
        return {
            "status": "success",
            "message": "DeepSeek API 连接正常",
            "available_models": ["deepseek-chat", "deepseek-reasoner"]
        }
    except requests.exceptions.HTTPError as e:
        error_detail = ""
        try:
            if e.response is not None:
                error_detail = e.response.text
        except:
            pass
        logger.error(f"健康检查失败: {str(e)}, 详情: {error_detail}")
        return {
            "status": "error",
            "message": f"DeepSeek API 连接失败(HTTP {e.response.status_code if e.response else ''}): {str(e)}\n详情: {error_detail}"
        }
    except Exception as e:
        logger.error(f"健康检查异常: {str(e)}")
        return {
            "status": "error",
            "message": f"DeepSeek API 异常: {str(e)}"
        }

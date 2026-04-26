package com.bupt.middleware.service;

import com.bupt.middleware.common.Result;
import com.bupt.middleware.dto.ChatResponse;
import com.bupt.middleware.entity.AgentConversation;
import com.bupt.middleware.entity.AgentMessage;
import com.bupt.middleware.entity.AgentTool;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AgentChatService {

    @Value("${python.url:http://127.0.0.1:8009}")
    private String pythonUrl;

    @Autowired
    private AgentConversationService agentConversationService;

    @Autowired
    private AgentMessageService agentMessageService;

    @Autowired
    private AgentToolService agentToolService;

    /**
     * 处理用户消息并获取AI回复
     */
    public Result<AgentMessage> processMessage(Long conversationId, String content, String modelName,
                                               String identity, Map<String, Object> profileState,
                                               String conversationContext) {
        try {
            // 1. 获取对话信息
            Result<AgentConversation> conversationResult = agentConversationService.getConversationById(conversationId);
            if (conversationResult.getCode() != 200) {
                return Result.error(conversationResult.getCode(), conversationResult.getMessage());
            }
            AgentConversation conversation = conversationResult.getData();

            // 2. 获取工具信息
            Result<AgentTool> toolResult = agentToolService.getToolById(conversation.getToolId());
            if (toolResult.getCode() != 200) {
                return Result.error(toolResult.getCode(), toolResult.getMessage());
            }
            AgentTool tool = toolResult.getData();

            // 3. 保存用户消息
            Result<AgentMessage> userMessageResult = agentMessageService.createMessage(
                    conversationId, AgentMessage.MessageRole.user, content, 0);
            if (userMessageResult.getCode() != 200) {
                return Result.error(userMessageResult.getCode(), userMessageResult.getMessage());
            }

            // 4. 获取对话历史
            Result<List<AgentMessage>> historyResult = agentMessageService.getConversationMessages(conversationId);
            if (historyResult.getCode() != 200) {
                return Result.error(historyResult.getCode(), historyResult.getMessage());
            }
            List<AgentMessage> messageHistory = historyResult.getData();

            // 5. 准备消息列表发送给AI
            List<Map<String, String>> messages = new ArrayList<>();

            // 添加系统提示词
            if (tool.getSystemPrompt() != null && !tool.getSystemPrompt().isEmpty()) {
                Map<String, String> systemMessage = new HashMap<>();
                systemMessage.put("role", "system");
                systemMessage.put("content", tool.getSystemPrompt());
                messages.add(systemMessage);
            }

            // 添加历史消息（最近10条）
            int historySize = messageHistory.size();
            int startIndex = Math.max(0, historySize - 10);
            for (int i = startIndex; i < historySize; i++) {
                AgentMessage msg = messageHistory.get(i);
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("role", msg.getRole().toString());
                messageMap.put("content", msg.getContent());
                messages.add(messageMap);
            }

            // 6. 调用Python服务获取AI回复
            String usedModel = modelName != null ? modelName : conversation.getModelName();
            Result<ChatResponse> chatResult = callPythonChatService(
                    messages, usedModel, identity, profileState, conversationContext);
            if (chatResult.getCode() != 200) {
                return Result.error(chatResult.getCode(), chatResult.getMessage());
            }
            ChatResponse chatResponse = chatResult.getData();

            // 7. 保存AI回复消息
            Result<AgentMessage> assistantMessageResult = agentMessageService.createMessage(
                    conversationId,
                    AgentMessage.MessageRole.assistant,
                    chatResponse.getContent(),
                    chatResponse.getTokensUsed());

            if (assistantMessageResult.getCode() != 200) {
                return Result.error(assistantMessageResult.getCode(), assistantMessageResult.getMessage());
            }

            log.info("消息处理完成，对话ID: {}, 使用模型: {}, 消耗token: {}",
                    conversationId, usedModel, chatResponse.getTokensUsed());

            return assistantMessageResult;

        } catch (Exception e) {
            log.error("处理消息时发生异常: ", e);
            return Result.error(500, "处理消息失败: " + e.getMessage());
        }
    }

    /**
     * 调用Python服务获取AI回复
     */
    private Result<ChatResponse> callPythonChatService(List<Map<String, String>> messages, String modelName,
                                                       String identity, Map<String, Object> profileState,
                                                       String conversationContext) {
        try {
            // 使用UTF-8字符集配置RestTemplate
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("messages", messages);
            requestBody.put("model_name", modelName);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 2000);
            requestBody.put("identity", identity != null && !identity.isBlank() ? identity : "student");
            requestBody.put("profile_state", profileState != null ? profileState : Map.of());
            requestBody.put("conversation_context", conversationContext != null ? conversationContext : "");

            // 序列化为JSON字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            log.info("发送JSON: {}", jsonBody);

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            String url = pythonUrl + "/agent/chat";
            log.info("请求URL: {}", url);

            try {
                ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
                log.info("响应状态: {}, 响应体: {}", response.getStatusCode(), response.getBody());

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);

                    String status = (String) responseMap.get("status");
                    if ("success".equals(status)) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> data = (Map<String, Object>) responseMap.get("data");

                        ChatResponse chatResponse = new ChatResponse();
                        chatResponse.setContent((String) data.get("content"));

                        Object tokensUsed = data.get("tokens_used");
                        chatResponse.setTokensUsed(tokensUsed instanceof Integer ? (Integer) tokensUsed : 0);
                        chatResponse.setModel(modelName);

                        log.info("成功获取AI回复，内容: {}", chatResponse.getContent());
                        return Result.success(chatResponse);
                    } else {
                        String errorMessage = (String) responseMap.get("message");
                        return Result.error(500, "Python服务返回错误: " + errorMessage);
                    }
                }

            } catch (HttpClientErrorException e) {
                log.error("Python服务返回4xx错误: {}, 响应体: {}", e.getStatusCode(), e.getResponseBodyAsString());
                return Result.error(500, "Python服务错误: " + e.getResponseBodyAsString());
            } catch (HttpServerErrorException e) {
                log.error("Python服务返回5xx错误: {}, 响应体: {}", e.getStatusCode(), e.getResponseBodyAsString());
                return Result.error(500, "Python服务内部错误: " + e.getResponseBodyAsString());
            }

            return Result.error(500, "Python服务调用失败");

        } catch (Exception e) {
            log.error("调用Python服务异常: ", e);
            return Result.error(500, "调用AI服务失败: " + e.getMessage());
        }
    }
}

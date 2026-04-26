# AI Agent Project (v 0.1.0)

智邮心语-智能体

---

## 技术栈

| 技术栈   | 版本      |
|----------|---------|
| Ollama    | 0.6.5  |
| DeepSeek | r1:1.5b  |

## 开发环境

- Python 3.10

## 快速启动

1. 下载安装Ollama: https://ollama.com/download

2. 安装完成后，在终端 (Terminal) 中运行命令
```shell
# ollama会自动下载模型到本地，并启动服务
ollama run deepseek-r1:1.5b
```

3. 不同版本的部署命令: https://ollama.com/library/deepseek-r1

4. 安装依赖库
```shell
pip install ollama
```

5. 进入项目目录

```shell
cd ./AIMH/middleware/src/main/python/agent
```

6. 运行

首先运行Agent服务
```shell
python agent.py
```
然后运行样例客户端
```shell
python -u agent-test.py
```

7. 按照样例模式进行开发

<template>
  <div class="agent-chat">
    <div class="chat-container">
      <!-- 侧边栏 -->
      <div class="chat-sidebar">
        <div class="sidebar-header">
          <button class="btn-new-chat" @click="createNewConversation">
            <i class="fas fa-plus"></i> 新建对话
          </button>
          <button class="btn-back" @click="goBack">
            <i class="fas fa-arrow-left"></i>
          </button>
        </div>

        <div class="conversation-list">
          <div
            v-for="conversation in conversations"
            :key="conversation.id"
            class="conversation-item"
            :class="{ active: conversation.id === currentConversationId }"
          >
            <div
              class="conversation-content"
              @click="loadConversation(conversation.id)"
            >
              <div class="conversation-title">{{ conversation.title }}</div>
              <div class="conversation-time">
                {{ formatTime(conversation.updatedAt) }}
              </div>
            </div>
            <button
              class="delete-btn"
              @click.stop="deleteConversation(conversation.id)"
              title="删除对话"
              v-if="conversations.length > 1"
            >
              <i class="fas fa-trash"></i>
            </button>
          </div>
        </div>
      </div>

      <!-- 主聊天区域 -->
      <div class="chat-main">
        <div class="chat-header">
          <div class="chat-title">
            <h3>{{ currentTool?.name || "智能助手" }}</h3>
            <span class="chat-model">{{ currentModel }}</span>
          </div>
          <div class="chat-actions">
            <button
              class="btn-think-mode"
              :class="{ active: isThinkMode }"
              @click="toggleThinkMode"
              title="思考模式"
            >
              <i class="fas fa-brain"></i>
            </button>
            <button
              class="btn-clear"
              @click="clearCurrentChat"
              title="清空对话"
            >
              <i class="fas fa-broom"></i>
            </button>
            <button
              class="btn-user-info"
              @click="showUserInfo"
              title="用户信息"
            >
              <i class="fas fa-user"></i>
            </button>
          </div>
        </div>

        <div class="chat-messages" ref="messagesContainer">
          <div class="message system">
            <div class="message-content welcome-message">
              <div class="welcome-icon">🤖</div>
              <div>
                欢迎使用 {{ currentTool?.name || "智能助手" }}！请输入您的问题。
                <br />当前用户: {{ userId || "未登录" }}
              </div>
            </div>
          </div>

          <div
            v-for="message in messages"
            :key="message.id"
            class="message"
            :class="message.role"
          >
            <div v-if="message.role !== 'system'" class="message-wrapper">
              <div class="message-avatar">
                <i v-if="message.role === 'user'" class="fas fa-user"></i>
                <i v-else class="fas fa-robot"></i>
              </div>
              <div class="message-content">
                <div v-if="message.thinking" class="thinking-process">
                  <div class="thinking-header">
                    <i class="fas fa-brain"></i>
                    思考过程
                  </div>
                  <div
                    class="thinking-content"
                    v-html="formatMessage(message.thinking)"
                  ></div>
                </div>
                <div
                  class="message-text"
                  v-html="formatMessage(message.content)"
                ></div>
              </div>
            </div>
          </div>

          <!-- 加载状态 -->
          <div v-if="isLoading" class="message assistant">
            <div class="message-wrapper">
              <div class="message-avatar">
                <i class="fas fa-robot"></i>
              </div>
              <div class="message-content">
                <div class="loading-indicator">
                  <span>{{ loadingText }}</span>
                  <div class="loading-dots">
                    <div class="loading-dot"></div>
                    <div class="loading-dot"></div>
                    <div class="loading-dot"></div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="chat-input">
          <div class="input-container">
            <div class="input-group">
              <textarea
                v-model="messageInput"
                @keydown="handleKeyDown"
                @input="autoResize"
                ref="messageTextarea"
                class="input-textarea"
                placeholder="输入消息...按回车发送，Shift+回车换行"
                rows="1"
                :disabled="isLoading"
              ></textarea>
              <button
                @click="sendMessage"
                :disabled="!messageInput.trim() || isLoading"
                class="send-button"
              >
                <i class="fas fa-paper-plane"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { agentApi } from "@/api/agent";

export default {
  name: "AgentChat",
  data() {
    return {
      toolId: null,
      userId: null, // 用户ID
      currentUserInfo: null, // 存储完整用户信息
      currentTool: null,
      conversations: [],
      currentConversationId: null,
      messages: [],
      messageInput: "",
      isLoading: false,
      loadingText: "思考中",
      isThinkMode: false,
      currentModel: "deepseek-r1:1.5b",
    };
  },
  async created() {
    console.log("=== AgentChat 初始化开始 ===");

    this.toolId = parseInt(this.$route.params.toolId);
    console.log("toolId:", this.toolId);

    // 验证toolId有效性
    if (!this.toolId || isNaN(this.toolId)) {
      console.error("toolId验证失败");
      this.$message.error("工具ID无效");
      this.$router.push("/agent/tools");
      return;
    }

    // 获取当前用户ID - 修复版本
    await this.initializeUser();

    if (!this.userId) {
      this.$message.error("无法获取用户信息，请先登录主系统");
      return;
    }

    console.log("最终用户ID:", this.userId);
    console.log("=== AgentChat 初始化结束 ===");

    try {
      await this.loadTool();
      await this.loadConversations();
      await this.createNewConversation();
    } catch (error) {
      console.error("初始化失败:", error);
    }
  },
  methods: {
    // 修复后的用户初始化方法
    async initializeUser() {
      console.log("=== 开始获取用户信息 ===");

      // 清除旧的缓存
      localStorage.removeItem("userInfo");
      sessionStorage.clear();

      try {
        // 方法1: 尝试直接调用主系统API
        console.log("尝试调用主系统 /user/current-user API...");
        try {
          const mainResponse = await fetch("/user/current-user", {
            method: "GET",
            credentials: "include",
            headers: {
              "Content-Type": "application/json",
            },
          });

          console.log("主系统API响应状态:", mainResponse.status);

          if (mainResponse.ok) {
            const result = await mainResponse.json();
            console.log("主系统API完整响应:", result);

            if (result.code === 200) {
              let userInfoString = null;

              // 关键修复：检查多个可能的字段
              if (result.data && typeof result.data === "string") {
                userInfoString = result.data;
                console.log("从data字段获取用户信息:", userInfoString);
              } else if (result.msg && typeof result.msg === "string") {
                userInfoString = result.msg;
                console.log("从msg字段获取用户信息:", userInfoString);
              } else if (
                typeof result.data === "object" &&
                result.data !== null
              ) {
                // 如果data是对象，直接使用
                console.log("data字段是对象，直接使用:", result.data);
                this.setUserInfo({
                  userId: result.data.userId,
                  userName: result.data.userName || result.data.userId,
                  role: result.data.role || "user",
                  source: "main_system_object",
                });
                return;
              }

              // 解析用户信息字符串
              if (userInfoString) {
                const userInfo = this.parseUserInfoString(userInfoString);
                console.log("解析后的用户信息:", userInfo);

                if (userInfo.userId) {
                  this.setUserInfo({
                    userId: userInfo.userId,
                    userName: userInfo.userId,
                    role: userInfo.role || "user",
                    loginTime: userInfo.loginTime,
                    source: "main_system",
                  });
                  console.log(
                    "=== 从主系统成功获取用户:",
                    userInfo.userId,
                    " ===",
                  );
                  return;
                }
              }
            }
          }
        } catch (mainError) {
          console.log("主系统API调用失败:", mainError);
        }

        console.warn("主系统API调用失败或返回空数据，尝试AgentChat API");

        // 方法2: 调用AgentChat API
        console.log("调用 agentApi.auth.getCurrentUser...");
        const response = await agentApi.auth.getCurrentUser();
        console.log("AgentAPI完整响应:", response);

        if (response && response.code === 200 && response.data) {
          const userData = response.data;
          console.log("获取到用户数据:", userData);

          if (userData.userId) {
            this.setUserInfo({
              userId: userData.userId,
              userName: userData.userName || userData.userId,
              role: userData.role,
              source: userData.source || "agent_api",
            });
            console.log("=== 从AgentAPI成功获取用户信息 ===");
            return;
          }
        }

        // 如果所有API都失败，显示错误
        console.error("所有获取用户信息的方法都失败了");
        throw new Error("无法获取用户信息");
      } catch (error) {
        console.error("获取用户信息失败:", error);
        this.showLoginRequired();
        throw error;
      }
    },

    // 解析主系统返回的用户信息字符串
    parseUserInfoString(userInfoStr) {
      console.log("开始解析用户信息字符串:", userInfoStr);
      const userInfo = {};

      try {
        // 解析格式如: "userId:pg,role:user,loginTime:1756369138742"
        const pairs = userInfoStr.split(",");
        pairs.forEach((pair) => {
          const [key, value] = pair.split(":");
          if (key && value) {
            userInfo[key.trim()] = value.trim();
          }
        });

        console.log("解析结果:", userInfo);
        return userInfo;
      } catch (error) {
        console.warn("解析用户信息字符串失败:", error);
        // 如果解析失败，尝试直接使用字符串作为用户ID
        return { userId: userInfoStr };
      }
    },

    // 设置用户信息 - 简化版本
    setUserInfo(userInfo) {
      console.log("设置当前用户:", userInfo);

      this.userId = userInfo.userId;
      this.currentUserInfo = {
        ...userInfo,
        createTime: new Date().toISOString(),
      };

      // 保存到localStorage供其他组件使用
      localStorage.setItem("userInfo", JSON.stringify(this.currentUserInfo));

      console.log("用户信息设置完成 - this.userId:", this.userId);
      console.log(
        "用户信息设置完成 - this.currentUserInfo:",
        this.currentUserInfo,
      );
    },

    // 显示需要登录的提示
    showLoginRequired() {
      console.warn("用户未认证，显示登录提示");

      this.$message({
        message: "请先登录主系统，然后再访问AgentChat",
        type: "warning",
        duration: 5000,
        showClose: true,
      });

      // 3秒后跳转到主系统
      setTimeout(() => {
        if (
          confirm("需要先登录主系统才能使用AgentChat，现在跳转到登录页面吗？")
        ) {
          // 跳转到主系统首页或登录页
          window.location.href = "/";
        }
      }, 3000);
    },

    // 显示用户信息
    showUserInfo() {
      if (!this.currentUserInfo) {
        this.$message.error("用户信息未加载");
        return;
      }

      let message = `用户ID: ${this.currentUserInfo.userId}`;

      if (
        this.currentUserInfo.userName &&
        this.currentUserInfo.userName !== this.currentUserInfo.userId
      ) {
        message += `\n用户名: ${this.currentUserInfo.userName}`;
      }

      if (this.currentUserInfo.role) {
        message += `\n角色: ${this.currentUserInfo.role}`;
      }

      message += `\n数据来源: ${this.currentUserInfo.source}`;

      if (this.currentUserInfo.createTime) {
        message += `\n初始化时间: ${new Date(this.currentUserInfo.createTime).toLocaleString()}`;
      }

      this.$message.info({
        message: message,
        duration: 6000,
        showClose: true,
      });
    },

    // 手动刷新用户信息
    async refreshUserInfo() {
      console.log("手动刷新用户信息...");

      try {
        // 清除缓存
        localStorage.removeItem("userInfo");
        this.userId = null;
        this.currentUserInfo = null;

        // 重新获取
        await this.initializeUser();

        this.$message.success(`用户信息已刷新，当前用户: ${this.userId}`);

        // 重新加载对话列表
        if (this.userId) {
          await this.loadConversations();
        }
      } catch (error) {
        this.$message.error("刷新用户信息失败: " + error.message);
      }
    },

    async loadTool() {
      try {
        const response = await agentApi.getToolById(this.toolId);
        if (response.code === 200) {
          this.currentTool = response.data;
        }
      } catch (error) {
        console.error("加载工具信息失败:", error);
      }
    },

    async loadConversations() {
      if (!this.userId) {
        console.error("userId为空，无法加载对话列表");
        return;
      }

      try {
        console.log("加载对话列表，使用用户ID:", this.userId);
        const response = await agentApi.getConversations(
          this.userId,
          this.toolId,
        );
        if (response.code === 200) {
          this.conversations = response.data;
        }
      } catch (error) {
        console.error("加载对话列表失败:", error);
      }
    },

    async createNewConversation() {
      if (!this.toolId) {
        console.error("toolId为空，无法创建对话");
        this.$message.error("工具ID缺失，无法创建对话");
        return;
      }

      if (!this.userId) {
        console.error("userId为空，无法创建对话");
        this.$message.error("用户ID缺失，无法创建对话");
        return;
      }

      const conversationData = {
        userId: this.userId,
        toolId: Number(this.toolId),
        title: "新对话",
      };

      console.log("准备创建对话，参数:", conversationData);

      try {
        const response = await agentApi.createConversation(conversationData);

        if (response.code === 200) {
          this.currentConversationId = response.data.id;
          this.messages = [];
          await this.loadConversations();
          console.log("对话创建成功，ID:", this.currentConversationId);
        } else {
          this.$message.error("创建对话失败: " + response.message);
        }
      } catch (error) {
        console.error("创建对话失败:", error);
        this.$message.error("创建对话失败");
      }
    },

    async loadConversation(conversationId) {
      this.currentConversationId = conversationId;
      await this.loadMessages();
    },

    async loadMessages() {
      try {
        const response = await agentApi.getMessages(this.currentConversationId);
        if (response.code === 200) {
          this.messages = response.data.filter((msg) => msg.role !== "system");
          await this.$nextTick();
          this.scrollToBottom();
        }
      } catch (error) {
        console.error("加载消息失败:", error);
      }
    },

    async deleteConversation(conversationId) {
      if (!confirm("确定要删除这个对话吗？删除后无法恢复。")) {
        return;
      }

      try {
        const response = await agentApi.deleteConversation(conversationId);
        if (response.code === 200) {
          this.$message.success("对话已删除");

          // 如果删除的是当前对话，切换到其他对话或创建新对话
          if (conversationId === this.currentConversationId) {
            await this.loadConversations();
            if (this.conversations.length > 0) {
              this.currentConversationId = this.conversations[0].id;
              await this.loadMessages();
            } else {
              await this.createNewConversation();
            }
          } else {
            await this.loadConversations();
          }
        } else {
          this.$message.error("删除失败: " + response.message);
        }
      } catch (error) {
        this.$message.error("删除失败: " + error.message);
      }
    },

    async sendMessage() {
      const content = this.messageInput.trim();
      if (!content || this.isLoading) return;

      // 添加用户消息到界面
      this.messages.push({
        id: Date.now(),
        role: "user",
        content,
        createdAt: new Date(),
      });

      this.messageInput = "";
      this.autoResize();
      this.setLoading(true);

      try {
        console.log("发送消息:", {
          conversationId: this.currentConversationId,
          content: content,
          model: this.currentModel,
        });

        const response = await agentApi.sendMessage(
          this.currentConversationId,
          content,
          this.currentModel,
        );

        console.log("收到完整响应:", response);

        // 检查响应格式并处理
        if (
          response &&
          (response.code === 200 || response.status === "success")
        ) {
          const responseData = response.data || response;

          console.log("响应数据:", responseData);

          // 构造AI回复消息
          const aiMessage = {
            id: responseData.id || Date.now(),
            role: "assistant",
            content: responseData.content || responseData.answer || "回复为空",
            thinking: this.isThinkMode
              ? responseData.thinking || responseData.think
              : null,
            createdAt: responseData.createdAt
              ? new Date(responseData.createdAt)
              : new Date(),
          };

          console.log("添加AI消息:", aiMessage);
          this.messages.push(aiMessage);

          await this.loadConversations(); // 更新对话列表
        } else {
          console.error("响应格式错误:", response);
          this.addSystemMessage(
            `服务器返回错误: ${response?.message || response?.error || "未知错误"}`,
          );
        }
      } catch (error) {
        console.error("发送消息失败:", error);

        // 详细的错误信息
        let errorMessage = "网络错误";
        if (error.response) {
          errorMessage = `服务器错误 (${error.response.status}): ${error.response.data?.message || error.message}`;
        } else if (error.request) {
          errorMessage = "无法连接到服务器，请检查网络连接";
        } else {
          errorMessage = `请求错误: ${error.message}`;
        }

        this.addSystemMessage(errorMessage);
      } finally {
        this.setLoading(false);
        await this.$nextTick();
        this.scrollToBottom();
      }
    },

    addSystemMessage(content) {
      this.messages.push({
        id: Date.now(),
        role: "system",
        content,
        createdAt: new Date(),
      });
    },

    setLoading(loading) {
      this.isLoading = loading;
      this.loadingText = loading
        ? this.isThinkMode
          ? "深度思考中"
          : "思考中"
        : "";
    },

    toggleThinkMode() {
      this.isThinkMode = !this.isThinkMode;
      this.$message.info(
        this.isThinkMode ? "已开启思考模式" : "已关闭思考模式",
      );
    },

    clearCurrentChat() {
      if (confirm("确定要清空当前对话吗？")) {
        this.messages = [];
        this.$message.success("对话已清空");
      }
    },

    handleKeyDown(event) {
      if (event.key === "Enter" && !event.shiftKey) {
        event.preventDefault();
        this.sendMessage();
      }
    },

    autoResize() {
      this.$nextTick(() => {
        const textarea = this.$refs.messageTextarea;
        if (textarea) {
          textarea.style.height = "auto";
          textarea.style.height = Math.min(textarea.scrollHeight, 120) + "px";
        }
      });
    },

    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$refs.messagesContainer;
        if (container) {
          container.scrollTop = container.scrollHeight;
        }
      });
    },

    formatMessage(content) {
      if (!content) return "";
      return content
        .replace(/\n/g, "<br>")
        .replace(/\*\*(.*?)\*\*/g, "<strong>$1</strong>")
        .replace(/\*(.*?)\*/g, "<em>$1</em>")
        .replace(/`(.*?)`/g, "<code>$1</code>");
    },

    formatTime(timestamp) {
      const date = new Date(timestamp);
      const now = new Date();
      const diff = now - date;

      if (diff < 60000) return "刚刚";
      if (diff < 3600000) return Math.floor(diff / 60000) + "分钟前";
      if (diff < 86400000) return Math.floor(diff / 3600000) + "小时前";
      return date.toLocaleDateString();
    },

    goBack() {
      this.$router.push("/agent/tools");
    },
  },
};
</script>

<style scoped>
/* 保持原有样式不变 */
.agent-chat {
  height: calc(100vh - 60px);
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  margin: 8px;
  border-radius: 12px;
  overflow: hidden;
}

.chat-container {
  display: flex;
  height: 100%;
  flex: 1;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
}

.chat-sidebar {
  width: 280px;
  background: linear-gradient(180deg, #f8f9fa 0%, #e9ecef 100%);
  border-right: 1px solid #dee2e6;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 16px;
  border-bottom: 1px solid #dee2e6;
  display: flex;
  gap: 8px;
  background: white;
  flex-shrink: 0;
}

.btn-new-chat,
.btn-back {
  flex: 1;
  padding: 10px 12px;
  border: none;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(102, 126, 234, 0.2);
}

.btn-new-chat:hover,
.btn-back:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.conversation-list {
  flex: 1;
  overflow-y: auto;
  min-height: 0;
}

.conversation-item {
  display: flex;
  align-items: center;
  border-bottom: 1px solid #f0f0f0;
  transition: all 0.2s ease;
  position: relative;
}

.conversation-item:hover {
  background: rgba(102, 126, 234, 0.05);
}

.conversation-item.active {
  background: linear-gradient(
    90deg,
    rgba(102, 126, 234, 0.1) 0%,
    transparent 100%
  );
  border-right: 3px solid #667eea;
}

.conversation-content {
  flex: 1;
  padding: 16px;
  cursor: pointer;
}

.conversation-title {
  font-weight: 600;
  margin-bottom: 4px;
  color: #2d3748;
  font-size: 14px;
}

.conversation-time {
  font-size: 12px;
  color: #718096;
}

.delete-btn {
  width: 32px;
  height: 32px;
  border: none;
  background: transparent;
  color: #a0aec0;
  cursor: pointer;
  border-radius: 6px;
  margin-right: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: all 0.2s ease;
}

.conversation-item:hover .delete-btn {
  opacity: 1;
}

.delete-btn:hover {
  background: #fed7d7;
  color: #e53e3e;
  transform: scale(1.1);
}

.chat-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: white;
  min-height: 0;
}

.chat-header {
  padding: 20px;
  border-bottom: 1px solid #e2e8f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(90deg, #f7fafc 0%, #edf2f7 100%);
  flex-shrink: 0;
}

.chat-title h3 {
  margin: 0 0 4px 0;
  color: #2d3748;
  font-size: 18px;
  font-weight: 700;
}

.chat-model {
  font-size: 12px;
  color: #718096;
  background: #e2e8f0;
  padding: 4px 12px;
  border-radius: 16px;
  font-weight: 500;
}

.chat-actions {
  display: flex;
  gap: 8px;
}

.btn-think-mode,
.btn-clear,
.btn-user-info {
  width: 40px;
  height: 40px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  background: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
  color: #4a5568;
}

.btn-think-mode:hover,
.btn-clear:hover,
.btn-user-info:hover {
  background: #f7fafc;
  border-color: #cbd5e0;
  transform: translateY(-1px);
}

.btn-think-mode.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-color: #667eea;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

.btn-user-info {
  color: #38a169;
}

.btn-user-info:hover {
  background: #f0fff4;
  border-color: #9ae6b4;
  color: #2f855a;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  min-height: 0;
  background: linear-gradient(180deg, #f7fafc 0%, #ffffff 100%);
}

.message {
  margin-bottom: 24px;
}

.message.system {
  display: flex;
  justify-content: center;
}

.welcome-message {
  background: linear-gradient(135deg, #e6fffa 0%, #b2f5ea 100%);
  color: #234e52;
  padding: 20px;
  border-radius: 16px;
  text-align: center;
  border: 1px solid #81e6d9;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  font-weight: 500;
  box-shadow: 0 4px 12px rgba(129, 230, 217, 0.2);
  flex-direction: column;
}

.welcome-icon {
  font-size: 20px;
}

.message-wrapper {
  display: flex;
  gap: 12px;
  max-width: 80%;
}

.message.user .message-wrapper {
  margin-left: auto;
  flex-direction: row-reverse;
}

.message-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  flex-shrink: 0;
  margin-top: 4px;
}

.message.user .message-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

.message.assistant .message-avatar {
  background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(72, 187, 120, 0.3);
}

.message-content {
  flex: 1;
  min-width: 0;
}

.message-text {
  padding: 16px 20px;
  border-radius: 18px;
  line-height: 1.6;
  word-wrap: break-word;
  font-size: 14px;
}

.message.user .message-text {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-bottom-right-radius: 6px;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
}

.message.assistant .message-text {
  background: #f8f9fa;
  color: #2d3748;
  border: 1px solid #e2e8f0;
  border-bottom-left-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.thinking-process {
  background: linear-gradient(135deg, #fffbf0 0%, #fef5e7 100%);
  border: 1px solid #f6e05e;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;
  font-family: "Courier New", monospace;
  font-size: 13px;
  color: #744210;
}

.thinking-header {
  font-weight: 600;
  color: #d69e2e;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.chat-input {
  background: white;
  border-top: 1px solid #e2e8f0;
  flex-shrink: 0;
}

.input-container {
  padding: 20px;
  background: linear-gradient(90deg, #f7fafc 0%, #ffffff 100%);
}

.input-group {
  display: flex;
  gap: 12px;
  align-items: flex-end;
  background: white;
  border-radius: 12px;
  border: 2px solid #e2e8f0;
  padding: 4px;
  transition: all 0.2s ease;
}

.input-group:focus-within {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.input-textarea {
  flex: 1;
  resize: none;
  border: none;
  padding: 12px 16px;
  min-height: 44px;
  max-height: 120px;
  font-size: 14px;
  line-height: 1.5;
  font-family: inherit;
  background: transparent;
  border-radius: 8px;
}

.input-textarea:focus {
  outline: none;
}

.input-textarea::placeholder {
  color: #a0aec0;
}

.send-button {
  width: 44px;
  height: 44px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
  box-shadow: 0 2px 4px rgba(102, 126, 234, 0.2);
  flex-shrink: 0;
}

.send-button:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.send-button:disabled {
  background: #cbd5e0;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.loading-indicator {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #718096;
  font-style: italic;
}

.loading-dots {
  display: flex;
  gap: 4px;
}

.loading-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #667eea;
  animation: pulse 1.5s ease-in-out infinite;
}

.loading-dot:nth-child(2) {
  animation-delay: 0.3s;
}
.loading-dot:nth-child(3) {
  animation-delay: 0.6s;
}

@keyframes pulse {
  0%,
  80%,
  100% {
    opacity: 0.3;
  }
  40% {
    opacity: 1;
  }
}

/* 滚动条美化 */
.conversation-list::-webkit-scrollbar,
.chat-messages::-webkit-scrollbar {
  width: 6px;
}

.conversation-list::-webkit-scrollbar-track,
.chat-messages::-webkit-scrollbar-track {
  background: #f7fafc;
}

.conversation-list::-webkit-scrollbar-thumb,
.chat-messages::-webkit-scrollbar-thumb {
  background: #cbd5e0;
  border-radius: 3px;
}

.conversation-list::-webkit-scrollbar-thumb:hover,
.chat-messages::-webkit-scrollbar-thumb:hover {
  background: #a0aec0;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .agent-chat {
    height: calc(100vh - 40px);
    margin: 4px;
  }

  .chat-sidebar {
    width: 100%;
    height: 200px;
  }

  .message-wrapper {
    max-width: 90%;
  }

  .input-container {
    padding: 16px;
  }
}
</style>

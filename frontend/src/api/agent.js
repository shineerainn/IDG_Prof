import axios from "axios";

// 创建axios实例
const api = axios.create({
  baseURL: "http://localhost:8080/agent",
  timeout: 30000,
  headers: {
    "Content-Type": "application/json",
  },
});

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    // 获取用户信息并添加到请求头
    const userInfo = localStorage.getItem("userInfo");
    if (userInfo) {
      try {
        const user = JSON.parse(userInfo);
        if (user.userId) {
          // 在请求头中添加用户ID
          config.headers["X-User-ID"] = user.userId;
        }
      } catch (e) {
        console.warn("解析用户信息失败:", e);
      }
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    return response.data;
  },
  (error) => {
    console.error("API请求错误:", error);

    // 处理权限相关错误
    if (error.response) {
      const status = error.response.status;
      const message = error.response.data?.message || error.message;

      if (status === 401) {
        // 未认证，清除本地用户信息
        localStorage.removeItem("userInfo");
        localStorage.removeItem("tempUserId");
        console.warn("用户未认证，已清除本地用户信息");
      } else if (status === 403) {
        // 权限不足
        console.warn("权限不足:", message);
        // 可以在这里添加用户提示，比如显示错误信息
      }
    }

    return Promise.reject(error);
  },
);

export const agentApi = {
  // 新增：认证相关API
  auth: {
    // 登录（设置用户session）
    login(userId) {
      return api.post("/auth/login", null, { params: { userId } });
    },

    // 登出
    logout() {
      return api.post("/auth/logout");
    },

    // 获取当前用户
    getCurrentUser() {
      return api.get("/auth/current-user");
    },
  },

  // 获取工具列表
  getTools(category) {
    return api.get("/tools", { params: { category } });
  },

  // 获取工具详情
  getToolById(id) {
    return api.get(`/tools/${id}`);
  },

  // 获取对话列表 (现在会自动验证用户权限)
  getConversations(userId, toolId) {
    return api.get("/conversations", { params: { userId, toolId } });
  },

  // 创建对话
  createConversation(data) {
    return api.post("/conversations", data);
  },

  // 获取对话详情
  getConversationById(id) {
    return api.get(`/conversations/${id}`);
  },

  // 更新对话标题
  updateConversationTitle(id, title) {
    return api.put(`/conversations/${id}/title`, title);
  },

  // 删除对话
  deleteConversation(id) {
    return api.delete(`/conversations/${id}`);
  },

  // 获取消息列表
  getMessages(conversationId) {
    return api.get(`/conversations/${conversationId}/messages`);
  },

  // 发送消息
  sendMessage(conversationId, content, modelName) {
    return api.post(`/conversations/${conversationId}/messages`, {
      content,
      modelName,
    });
  },
};

// 用户工具函数
export const userUtils = {
  // 获取当前用户ID
  getCurrentUserId() {
    const userInfo = localStorage.getItem("userInfo");
    if (userInfo) {
      try {
        const user = JSON.parse(userInfo);
        return user.userId;
      } catch (e) {
        console.warn("解析用户信息失败:", e);
      }
    }
    return null;
  },

  // 设置用户信息
  setUserInfo(userInfo) {
    localStorage.setItem("userInfo", JSON.stringify(userInfo));
  },

  // 清除用户信息
  clearUserInfo() {
    localStorage.removeItem("userInfo");
    localStorage.removeItem("tempUserId");
  },

  // 检查是否已登录
  isLoggedIn() {
    return this.getCurrentUserId() !== null;
  },
};

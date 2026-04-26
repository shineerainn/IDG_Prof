import { get, post } from "@/utils/request";
import axios from "axios";

// 原有的API
export const getUserPwd = (params) => get("/user/getPwd", params);
export const getUserRole = (params) => get("/user/getRole", params);

// 新增的认证相关API
export const login = (userId) =>
  post("/agent/auth/login", null, { params: { userId } });
export const logout = () => post("/agent/auth/logout");
export const getCurrentUser = () => get("/agent/auth/current-user");

// 添加请求拦截器，自动在请求头中添加用户ID
axios.interceptors.request.use(
  (config) => {
    // 获取用户信息
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

// 添加响应拦截器，处理权限相关错误
axios.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
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
      }
    }

    return Promise.reject(error);
  },
);

// 用户管理工具函数
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

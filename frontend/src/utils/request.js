// @/utils/request.js
import axios from "axios";
import { ElMessage } from "element-ui";

axios.defaults.withCredentials = true;

// 创建 axios 实例
const service = axios.create({
  baseURL: "/api", // 后端请求地址
  timeout: 10000, // 超时时间
  headers: { "Content-Type": "application/json;charset=utf-8" },
});

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    // 处理二进制数据时直接返回
    if (response.config.responseType === "blob") {
      return response;
    }

    const { code, msg } = response.data;
    // 根据业务状态码判断请求状态
    if (code === 200) {
      return response.data;
    } else {
      // 处理业务错误
      ElMessage.error(msg || "系统错误");
      return Promise.reject(new Error(msg || "Error"));
    }
  },
  (error) => {
    // 处理 HTTP 网络错误
    let message = "";
    const status = error.response?.status;
    switch (status) {
      case 401:
        message = "认证失败，请重新登录";
        // 这里可以触发退出登录操作
        break;
      case 403:
        message = "当前操作没有权限";
        break;
      case 404:
        message = "资源不存在";
        break;
      case 500:
        message = "服务器内部错误";
        break;
      default:
        message = "网络连接错误";
    }
    ElMessage.error(message);
    return Promise.reject(error);
  },
);

// 封装通用请求函数
export function request(config) {
  return service(config);
}

// 导出常用的请求方法
export const get = (url, params) => {
  return request({ method: "get", url, params });
};

export const post = (url, data) => {
  return request({ method: "post", url, data });
};

export const put = (url, data) => {
  return request({ method: "put", url, data });
};

export const del = (url, params) => {
  return request({ method: "delete", url, params });
};

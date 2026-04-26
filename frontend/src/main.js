import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

// 导入axios用于API请求
import axios from "axios";

// 导入全局样式
import "@/assets/css/global.css";

// 配置axios
Vue.prototype.$http = axios;

// 创建axios实例用于智能体API
const agentApi = axios.create({
  baseURL: "http://localhost:8080/agent",
  timeout: 30000,
  headers: {
    "Content-Type": "application/json",
  },
});

// 请求拦截器
agentApi.interceptors.request.use(
  (config) => {
    // 可以在这里添加loading状态
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

// 响应拦截器
agentApi.interceptors.response.use(
  (response) => {
    return response.data;
  },
  (error) => {
    // 统一错误处理
    if (error.response) {
      switch (error.response.status) {
        case 404:
          Vue.prototype.$message.error("请求的资源不存在");
          break;
        case 500:
          Vue.prototype.$message.error("服务器内部错误");
          break;
        default:
          Vue.prototype.$message.error("网络错误: " + error.message);
      }
    } else {
      Vue.prototype.$message.error("网络连接失败");
    }
    return Promise.reject(error);
  },
);

// 将agentApi挂载到Vue原型上，方便组件中使用
Vue.prototype.$agentApi = agentApi;

// 全局配置
Vue.config.productionTip = false;

// 使用ElementUI
Vue.use(ElementUI, {
  size: "medium", // 设置组件默认尺寸
});

// 全局过滤器 - 时间格式化
Vue.filter("formatTime", function (timestamp) {
  if (!timestamp) return "";

  const date = new Date(timestamp);
  const now = new Date();
  const diff = now - date;

  if (diff < 60000) return "刚刚";
  if (diff < 3600000) return Math.floor(diff / 60000) + "分钟前";
  if (diff < 86400000) return Math.floor(diff / 3600000) + "小时前";
  if (diff < 2592000000) return Math.floor(diff / 86400000) + "天前";

  return date.toLocaleDateString("zh-CN", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  });
});

// 全局过滤器 - 截断文本
Vue.filter("truncate", function (text, maxLength = 50) {
  if (!text) return "";
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + "...";
});

// 全局指令 - 自动聚焦
Vue.directive("focus", {
  inserted: function (el) {
    el.focus();
  },
});

// 全局方法 - 防抖
Vue.prototype.$debounce = function (func, delay = 300) {
  let timeoutId;
  return function (...args) {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => func.apply(this, args), delay);
  };
};

// 全局常量配置
Vue.prototype.$config = {
  // API基础URL
  API_BASE_URL: "http://localhost:8080",

  // 智能体相关配置
  AGENT: {
    DEFAULT_MODEL: "deepseek-r1:1.5b",
    MAX_MESSAGE_LENGTH: 1000,
    CONVERSATION_PAGE_SIZE: 20,
  },

  // 工具类别映射
  TOOL_CATEGORIES: {
    education: "教育",
    analytics: "数据分析",
    general: "通用",
    research: "研究",
  },

  // 工具图标映射
  TOOL_ICONS: {
    education: "fas fa-graduation-cap",
    analytics: "fas fa-chart-line",
    general: "fas fa-robot",
    research: "fas fa-flask",
  },
};

// 创建Vue实例
new Vue({
  router,
  store,
  render: (h) => h(App),

  // 全局错误处理
  errorCaptured(err, vm, info) {
    console.error("Vue Error:", err);
    console.error("Component:", vm);
    console.error("Info:", info);

    // 可以发送错误到监控服务
    this.$message.error("系统发生错误，请稍后重试");

    return false;
  },
}).$mount("#app");

// 全局未处理的Promise异常
window.addEventListener("unhandledrejection", (event) => {
  console.error("Unhandled promise rejection:", event.reason);
  Vue.prototype.$message.error("系统异常，请稍后重试");
});

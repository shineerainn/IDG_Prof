import { get, post } from "@/utils/request";

// 发起研究生画像建模请求 请求参数为：userId, 宽表布尔属性
export const generateSupervisorProfile = (params) =>
  post("/profile/supv/build", params);

// 获取研究生画像建模列表 请求参数为：userId
export const fetchSupervisorProfile = (params) =>
  get("/profile/supv/getList", params);

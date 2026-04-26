import { get, post } from "@/utils/request";

// 发起研究生画像建模请求 请求参数为：userId, 宽表布尔属性
export const generateGraduateProfile = (params) =>
  post("/profile/pg/build", params);

// 获取研究生画像建模列表 请求参数为：userId
export const fetchGraduateProfile = (params) =>
  get("/profile/pg/getList", params);

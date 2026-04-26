import { get, post } from "@/utils/request";

// 发起研究生预警建模请求 请求参数为：userId, 宽表布尔属性
export const sendGraduateDetection = (params) =>
  post("/detection/build", params);

// 获取研究生预警建模列表 请求参数为：userId
export const getGraduateDetectionList = (params) =>
  get("/detection/getList", params);

// 获取研究生预警建模分布 请求参数为：detectionId
export const getGraduateDetectionDistribution = (params) =>
  get("/detection/getDistribution", params);

// 获取研究生预警模型学生列表 请求参数为：detectionId
export const getGraduateDetectionStudentList = (params) =>
  get("/detection/getInfoList", params);

// 获取研究生预警模型学生详情 请求参数为：detectionId, studentId
export const getGraduateDetectionStudentDetail = (params) =>
  get("/detection/getInfo", params);

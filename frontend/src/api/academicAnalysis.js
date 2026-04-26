import { get, post } from "@/utils/request";

// 获取所有学生数据
export const getAllStudents = (params = {}) => {
  console.log("调用获取所有学生API:", "/academic/getAllPg", params);
  return get("/academic/getAllPg", params);
};

// 获取学生学业信息详情
export const getStudentAcademic = (params) => {
  console.log("调用获取学生学业信息API:", "/academic/getPg", params);
  return get("/academic/getPg", params);
};

// 计算学生分数
export const calculateStudentScores = (params) => {
  console.log("调用计算学生分数API:", "/academic/pg/calculateScores", params);
  return post("/academic/pg/calculateScores", params);
};

// 获取学生雷达图数据
export const getStudentRadarChartData = (studentId) => {
  console.log("调用获取学生雷达图数据API:", "/academic/pg/radarChart", {
    studentId,
  });
  return get("/academic/pg/radarChart", { studentId });
};

// 发起学业画像建模请求
export const generateAcademicProfile = (params) => {
  console.log("调用学业画像建模API:", "/academic/pg/build", params);
  return post("/academic/pg/build", params);
};

// 获取学业画像建模列表
export const fetchAcademicProfile = (params) => {
  console.log("调用获取学业画像列表API:", "/academic/pg/getList", params);
  return get("/academic/pg/getList", params);
};

// 获取学业画像详情
export const getAcademicProfileDetail = (params) => {
  console.log("调用获取学业画像详情API:", "/academic/pg/getDetail", params);
  return get("/academic/pg/getDetail", params);
};

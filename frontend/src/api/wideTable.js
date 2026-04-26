import { get } from "@/utils/request";
export const getPostgraduateList = (params) =>
  get("/wideTable/getAllPg", params);
export const getSupervisorList = (params) =>
  get("/wideTable/getAllSupv", params);

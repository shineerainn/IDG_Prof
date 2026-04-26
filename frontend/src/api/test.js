import { get } from "@/utils/request";
export const getTest = (params) => get("/tests/test", params);

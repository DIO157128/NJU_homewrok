import request from "@/network/request";
const testAPI = require("@/apis"); //此处务必使用require导入（因为是module.exports导出的...）


export const saleDetail = config => request._get(testAPI.SALE_DETAIL, config);
export const businessSituation = config => request._get(testAPI.BUSINESS_SITUATION, config);
export const businessHistory = config => request._get(testAPI.BUSINESS_HISTORY, config);

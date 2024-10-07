import request from "@/network/request";
const testAPI = require("@/apis"); //此处务必使用require导入（因为是module.exports导出的...）

export const generateAllSalary =config => request._get(testAPI.SALARY_GENERATE,config)
export const getAllSalary =config => request._get(testAPI.SALARY_SHEET_SHOW,config)
export const generateAllAnnual =config => request._get(testAPI.ANNUAL_GENERATE,config)

export const firstApproval = config => request._get(testAPI.SALARY_FIRST_APPROVAL, config);
export const secondApproval = config => request._get(testAPI.SALARY_SECOND_APPROVAL, config);



import request from "@/network/request"

const testAPI = require("@/apis")

export const getAllPay = config => request._get(testAPI.PAY_ALL, config);
export const createPay = config => request._post(testAPI.PAY_CREATE, config);
export const approvePay = config => request._get(testAPI.PAY_APPROVAL, config);

export const getAllReceive = config => request._get(testAPI.RECEIVE_ALL, config);
export const createReceive = config => request._post(testAPI.RECEIVE_CREATE, config);
export const approveReceive = config => request._get(testAPI.RECEIVE_APPROVAL, config);

// 公司账户管理
export const companyAccountCreate = config => request._post(testAPI.COMPANY_ACCOUNT_CREATE, config);
export const companyAccountUpdate = config => request._post(testAPI.COMPANY_ACCOUNT_UPDATE, config);
export const companyAccountDelete = config => request._get(testAPI.COMPANY_ACCOUNT_DELETE, config);
export const companyAccountQueryAll = config => request._get(testAPI.COMPANY_ACCOUNT_QUERY_ALL, config);
export const companyAccountQueryName = config => request._get(testAPI.COMPANY_ACCOUNT_QUERY_NAME, config);

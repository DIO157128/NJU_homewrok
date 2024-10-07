import request from "@/network/request";

const testAPI = require("@/apis"); //此处务必使用require导入（因为是module.exports导出的...）

export const getStaffInfo = config => request._get(testAPI.STAFF_INFO, config);
export const StaffDelete = config => request._get(testAPI.STAFF_DELETE, config);
export const StaffUpdate = config => request._post(testAPI.STAFF_UPDATE, config);
export const StaffCreate = config => request._post(testAPI.STAFF_CREATE, config);

export const getPostInfo = config => request._get(testAPI.POST_INFO, config);
export const PostDelete = config => request._get(testAPI.POST_DELETE, config);
export const PostUpdate = config => request._post(testAPI.POST_UPDATE, config);
export const PostCreate = config => request._post(testAPI.POST_CREATE, config);

export const punchin = config => request._get(testAPI.PUNCH_IN, config);
export const getPunchInInfo =config => request._get(testAPI.PUNCH_IN_QUERY_ALL,config);
export const getPunchContent =config =>request._get(testAPI.PUNCH_IN_QUERY_BY_STAFF_ID,config);


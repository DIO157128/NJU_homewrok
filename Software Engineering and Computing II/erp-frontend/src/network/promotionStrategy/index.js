import request from "@/network/request";
const testAPI = require("@/apis"); //此处务必使用require导入（因为是module.exports导出的...）

export const promotionStrategyOnLevelCreate=config =>request._post(testAPI.PROMOTION_STRATEGY_ON_LEVEL_CREATE,config)
export const promotionStrategyOnLevelGetAll=config =>request._get(testAPI.PROMOTION_STRATEGY_ON_LEVEL_GETALL,config)
export const promotionStrategyOnLevelUpdate=config =>request._post(testAPI.PROMOTION_STRATEGY_ON_LEVEL_UPDATE,config)
export const promotionStrategyOnLevelDelete=config =>request._get(testAPI.PROMOTION_STRATEGY_ON_LEVEL_DELETE,config)

export const promotionStrategyOnAmountCreate=config =>request._post(testAPI.PROMOTION_STRATEGY_ON_AMOUNT_CREATE,config)
export const promotionStrategyOnAmountGetAll=config =>request._get(testAPI.PROMOTION_STRATEGY_ON_AMOUNT_GETALL,config)
export const promotionStrategyOnAmountUpdate=config =>request._post(testAPI.PROMOTION_STRATEGY_ON_AMOUNT_UPDATE,config)
export const promotionStrategyOnAmountDelete=config =>request._get(testAPI.PROMOTION_STRATEGY_ON_AMOUNT_DELETE,config)

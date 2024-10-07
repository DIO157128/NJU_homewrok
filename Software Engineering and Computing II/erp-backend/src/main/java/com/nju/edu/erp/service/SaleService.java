package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.Format;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.CustomerPurchaseAmountPO;
import com.nju.edu.erp.model.vo.PromotionStrategyOnAmountVO;
import com.nju.edu.erp.model.vo.PromotionStrategyOnLevelVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface SaleService {

    /**
     * 指定销售单
     * @param userVO
     * @param saleSheetVO
     */
    void makeSaleSheet(UserVO userVO, SaleSheetVO saleSheetVO);

    /**
     * 根据单据状态获取销售单
     * @param state
     * @return
     */
    List<SaleSheetVO> getSaleSheetByState(SaleSheetState state);

    /**
     * 审批单据
     * @param saleSheetId
     * @param state
     */
    void approval(String saleSheetId, SaleSheetState state);

    /**
     * 获取某个销售人员某段时间内消费总金额最大的客户(不考虑退货情况,销售单不需要审批通过,如果这样的客户有多个，仅保留一个)
     * @param salesman 销售人员的名字
     * @param beginDateStr 开始时间字符串
     * @param endDateStr 结束时间字符串
     * @return
     */
    CustomerPurchaseAmountPO getMaxAmountCustomerOfSalesmanByTime(String salesman,String beginDateStr,String endDateStr);

    /**
     * 根据销售单Id搜索销售单信息
     * @param saleSheetId 销售单Id
     * @return 销售单全部信息
     */
    SaleSheetVO getSaleSheetById(String saleSheetId);


    /**
     * 制定按用户级别的促销策略
     *
     * @return
     */
    void makePromotionStrategyOnLevel(PromotionStrategyOnLevelVO promotionStrategyOnLevelVO);

    /**
     * 获取所有按用户级别的促销策略
     */
    List<PromotionStrategyOnLevelVO> getAllPromotionStrategyOnLevel();

    /**
     * 更新按用户级别的促销策略
     */
    PromotionStrategyOnLevelVO updatePromotionStrategyOnLevel(PromotionStrategyOnLevelVO promotionStrategyOnLevelVO);

    /**
     * 删除按用户级别的促销策略
     */
    void deletePromotionStrategyOnLevel(Integer id);



    /**
     * 制定按总价的促销策略
     */
    void makePromotionStrategyOnAmount(PromotionStrategyOnAmountVO promotionStrategyOnAmountVO);

    /**
     * 获取所有按总价制定的促销策略
     */
    List<PromotionStrategyOnAmountVO> getAllPromotionStrategyOnAmount();


    /**
     * 更新按总价的促销策略
     */
    PromotionStrategyOnAmountVO updatePromotionStrategyOnAmount(PromotionStrategyOnAmountVO promotionStrategyOnAmountVO);

    /**
     * 删除按总价的促销策略
     */
    void deletePromotionStrategyOnAmount(Integer id);

    /**
     * 找到交易额
     * @param name
     * @param t1
     * @param t2
     * @return
     */
    double findVolumeOfTrade(String name,Date t1,Date t2);

    /**
     * 找到交易次数
     * @param name
     * @param t1
     * @param t2
     * @return
     */
    int findTransXByNameAndPeriod(String name,Date t1,Date t2);


}

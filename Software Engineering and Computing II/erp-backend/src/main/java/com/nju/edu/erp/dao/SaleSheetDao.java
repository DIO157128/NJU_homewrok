package com.nju.edu.erp.dao;


import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.sale.SaleSheetContentPO;
import com.nju.edu.erp.model.po.sale.SaleSheetPO;
import com.nju.edu.erp.model.po.CustomerPurchaseAmountPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface SaleSheetDao {

    /**
     * 获取最近一条销售单
     * @return
     */
    SaleSheetPO getLatestSheet();

    /**
     * 存入一条销售单记录
     * @param toSave 一条销售单记录
     * @return 影响的行数
     */
    int saveSheet(SaleSheetPO toSave);

    /**
     * 把销售单上的具体内容存入数据库
     * @param saleSheetContent 入销售单上的具体内容
     */
    int saveBatchSheetContent(List<SaleSheetContentPO> saleSheetContent);

    /**
     * 查找所有销售单
     */
    List<SaleSheetPO> findAllSheet();

    /**
     * 查找指定id的销售单
     * @param id
     * @return
     */
    SaleSheetPO findSheetById(String id);

    /**
     *  获取 Voucher的值
     * @param id
     * @return
     */
    Integer getVoucherAmount(String id);

    /**
     * 查找指定销售单下具体的商品内容
     * @param sheetId
     */
    List<SaleSheetContentPO> findContentBySheetId(String sheetId);

    /**
     *  根据state返回销售单
     * @return 销售单
     */
    List<SaleSheetPO> findAllSheetByState(SaleSheetState state);

    /**
     * 更新指定销售单的状态
     * @param sheetId
     * @param state
     * @return
     */
    int updateSheetState(String sheetId, SaleSheetState state);

    /**
     * 根据当前状态更新销售单状态
     * @param sheetId
     * @param prev
     * @param state
     * @return
     */
    int updateSheetStateOnPrev(String sheetId, SaleSheetState prev, SaleSheetState state);


    /**
     *      找到某个销售人员（销售经理）的经手单据数目，计算提成,d1,d2一般传本月份的第一天和最后一天即可
     * @param salesman
     * @param d1
     * @param d2
     * @return
     */
    int findTransXByNameAndPeriod(String salesman,Date d1,Date d2);


    /**
     *  计算年终奖用，查找该销售人员手上的流水
     * @param salesman
     * @param d1
     * @param d2
     * @return
     */
    double findVolumeOfTrade(String salesman,Date d1,Date d2);


    /**
     * 获取某个销售人员某段时间内消费总金额最大的客户(不考虑退货情况,销售单不需要审批通过,如果这样的客户有多个，仅保留一个)
     * @param salesman 销售人员的名字
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    CustomerPurchaseAmountPO getMaxAmountCustomerOfSalesmanByTime(String salesman, Date beginTime,Date endTime);
}

package com.nju.edu.erp.dao;
import com.nju.edu.erp.enums.sheetState.PaySheetState;
import com.nju.edu.erp.model.po.pay.PaySheetContentPO;
import com.nju.edu.erp.model.po.pay.PaySheetPO;
import com.nju.edu.erp.model.po.sale.SaleSheetPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface PaySheetDao {
    /**
     * 保存付款单
     * @param toSave
     * @return
     */
    int saveSheet(PaySheetPO toSave);

    /**
     * 保存付款条目列表
     * @param paySheetContent
     * @return
     */
    int saveBatchSheetContent(List<PaySheetContentPO> paySheetContent);

    /**
     * 查找所有的付款单
     * @return
     */
    List<PaySheetPO> findAllSheet();

    /**
     * 通过单据号查找付款单
     * @param id
     * @return
     */
    PaySheetPO findSheetById(String id);

    /**
     * 通过单据号查找条目列表
     * @param sheetId
     * @return
     */
    List<PaySheetContentPO> findContentBySheetId(String sheetId);

    /**
     * 通过单据状态查找单据
     * @param state
     * @return
     */
    List<PaySheetPO> findAllSheetByState(PaySheetState state);

    /**
     * 更新单据状态
     * @param sheetId
     * @param state
     * @return
     */
    int updateSheetState(String sheetId, PaySheetState state);

    /**
     * 根据当前状态更新销售单状态
     * @param sheetId
     * @param prev
     * @param state
     * @return
     */
    int updateSheetStateOnPrev(String sheetId, PaySheetState prev, PaySheetState state);

    /**
     * 获取最近一条付款单
     * @return
     */
    PaySheetPO getLatestSheet();

    /**
     * 根据给定日期寻找这段时间内的单据
     * @param fromDate
     * @param toDate
     * @return
     */
    List<PaySheetPO> findAllSheetByDate(Date fromDate, Date toDate);
}

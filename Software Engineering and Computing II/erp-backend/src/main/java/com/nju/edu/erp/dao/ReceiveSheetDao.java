package com.nju.edu.erp.dao;


import com.nju.edu.erp.enums.sheetState.ReceiveSheetState;
import com.nju.edu.erp.model.po.pay.PaySheetPO;
import com.nju.edu.erp.model.po.receive.ReceiveSheetContentPO;
import com.nju.edu.erp.model.po.receive.ReceiveSheetPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface ReceiveSheetDao {
    /**
     * 保存付款单
     * @param toSave
     * @return
     */
    int saveSheet(ReceiveSheetPO toSave);

    /**
     * 保存付款条目列表
     * @param receiveSheetContent
     * @return
     */
    int saveBatchSheetContent(List<ReceiveSheetContentPO> receiveSheetContent);

    /**
     * 查找所有的付款单
     * @return
     */
    List<ReceiveSheetPO> findAllSheet();

    /**
     * 通过单据号查找付款单
     * @param id
     * @return
     */
    ReceiveSheetPO findSheetById(String id);

    /**
     * 通过单据号查找条目列表
     * @param sheetId
     * @return
     */
    List<ReceiveSheetContentPO> findContentBySheetId(String sheetId);

    /**
     * 通过单据状态查找单据
     * @param state
     * @return
     */
    List<ReceiveSheetPO> findAllSheetByState(ReceiveSheetState state);

    /**
     * 更新单据状态
     * @param sheetId
     * @param state
     * @return
     */
    int updateSheetState(String sheetId, ReceiveSheetState state);

    /**
     * 根据当前状态更新销售单状态
     * @param sheetId
     * @param prev
     * @param state
     * @return
     */
    int updateSheetStateOnPrev(String sheetId, ReceiveSheetState prev, ReceiveSheetState state);

    /**
     * 获取最近一条收款单
     * @return
     */
    ReceiveSheetPO getLatestSheet();

    /**
     * 根据给定日期寻找这段时间内的单据
     * @param fromDate
     * @param toDate
     * @return
     */
    List<ReceiveSheetPO> findAllSheetByDate(Date fromDate, Date toDate);

}

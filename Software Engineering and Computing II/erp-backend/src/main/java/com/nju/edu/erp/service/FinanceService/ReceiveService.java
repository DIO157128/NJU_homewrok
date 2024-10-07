package com.nju.edu.erp.service.FinanceService;

import com.nju.edu.erp.enums.sheetState.ReceiveSheetState;
import com.nju.edu.erp.enums.sheetState.ReceiveSheetState;
import com.nju.edu.erp.model.po.receive.ReceiveSheetPO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetVO;
import com.nju.edu.erp.service.SheetService;

import java.util.Date;
import java.util.List;

public interface ReceiveService extends SheetService {
    /**
     * 指定收款单
     * @param userVO
     * @param receiveSheetVO
     */
    void makeReceiveSheet(UserVO userVO, ReceiveSheetVO receiveSheetVO);

    /**
     * 根据单据状态获取收款单
     * @param state
     * @return
     */
    List<ReceiveSheetVO> getReceiveSheetByState(ReceiveSheetState state);

    /**
     * 审批单据
     * @param receiveSheetId
     * @param state
     */
    void approval(String receiveSheetId, ReceiveSheetState state);

    /**
     * 根据收款单Id搜索收款单信息
     * @param receiveSheetId
     * @return
     */
    ReceiveSheetVO getReceiveSheetById(String receiveSheetId);

    /**
     * 根据给定日期寻找这段时间内的单据
     * @param fromDate
     * @param toDate
     * @return
     */
    List<ReceiveSheetPO> findAllSheetByDate(Date fromDate, Date toDate);
}

package com.nju.edu.erp.service.FinanceService;

import com.nju.edu.erp.enums.sheetState.PaySheetState;
import com.nju.edu.erp.model.po.pay.PaySheetPO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.pay.PaySheetVO;
import com.nju.edu.erp.service.SheetService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


public interface PayService extends SheetService {
    /**
     * 指定付款单
     * @param userVO
     * @param paySheetVO
     */
    void makePaySheet(UserVO userVO, PaySheetVO paySheetVO);

    /**
     * 根据单据状态获取付款单
     * @param state
     * @return
     */
    List<PaySheetVO> getPaySheetByState(PaySheetState state);

    /**
     * 审批单据
     * @param paySheetId
     * @param state
     */
    void approval(String paySheetId, PaySheetState state);

    /**
     * 根据付款单Id搜索付款单信息
     * @param paySheetId
     * @return
     */
    PaySheetVO getPaySheetById(String paySheetId);


    /**
     * 根据日期查找付款单
     * @param fromDate
     * @param toDate
     * @return
     */
    List<PaySheetPO> findAllSheetByDate(Date fromDate, Date toDate);

}

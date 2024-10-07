package com.nju.edu.erp.service.SalaryService;

import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.po.salary.SalarySheetPO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.salary.SalarySheetVO;
import com.nju.edu.erp.service.SheetService;

import java.util.Date;
import java.util.List;


public interface SalaryService extends SheetService {
    /**
     * 指定工资单
     * @param userVO
     * @param salarySheetVO
     */
    void makeSalarySheet(UserVO userVO, SalarySheetVO salarySheetVO,SalarySheetState state);

    /**
     *  生成工资单
     *  无需参数
     * @return
     */
    List<SalarySheetVO> generateSalary();

    /**
     *  生成年终奖
     * @return
     */
    List<SalarySheetVO> generateAnnual();

    /**
     * 根据单据状态获取工资单
     * @param state
     * @return
     */
    List<SalarySheetVO> getSalarySheetByState(SalarySheetState state);

    /**
     * 审批单据
     * @param salarySheetId
     * @param state
     */
    void approval(String salarySheetId, SalarySheetState state);

    /**
     * 根据工资单Id搜索工资单信息
     * @param salarySheetId
     * @return
     */
    SalarySheetVO getSalarySheetById(String salarySheetId);

    /**
     * 根据给定日期寻找这段时间内的单据
     * @param fromDate
     * @param toDate
     * @return
     */
    List<SalarySheetPO> findAllSheetByDate(Date fromDate, Date toDate);

}

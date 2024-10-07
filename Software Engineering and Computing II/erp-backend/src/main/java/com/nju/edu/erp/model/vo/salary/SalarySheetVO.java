package com.nju.edu.erp.model.vo.salary;

import java.math.BigDecimal;

import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalarySheetVO {
    /**
     * 工资单据号
     */
    private String id ;
    /**
     * 员工id
     */
    private Integer staffId;
    /**
     * 员工姓名
     */
    private String staffName;
    /**
     * 银行账户
     */
    private String cardAccount;
    /**
     * 应付工资
     */
    private BigDecimal dueSalary;
    /**
     * 扣税
     */
    private BigDecimal tax;
    /**
     * 实际工资
     */
    private BigDecimal actualSalary;
    /**
     * 审批状态
     */
    private SalarySheetState state;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 工资单类型，1是基本工资，2是年终奖
     */
    private Integer type;
}

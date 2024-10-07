package com.nju.edu.erp.model.po.pay;
import java.math.BigDecimal;

import com.nju.edu.erp.enums.sheetState.PaySheetState;
import com.nju.edu.erp.enums.sheetState.PurchaseSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaySheetPO {
    /**
     * 付款单据号
     */
    private String id;
    /**
     * 银行账户
     */
    private String bankAccount;
    /**
     * 单据状态
     */
    private PaySheetState state;
    /**
     * 操作员
     */
    private String operator;
    /**
     * 应付金额
     */
    private BigDecimal allAmount;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 客户id
     */
    private Integer payerId;
}

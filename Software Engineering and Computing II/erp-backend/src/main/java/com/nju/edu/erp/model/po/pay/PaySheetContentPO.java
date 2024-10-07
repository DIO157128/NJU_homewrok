package com.nju.edu.erp.model.po.pay;
import java.math.BigDecimal;

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
public class PaySheetContentPO {
    /**
     * 条目清单编号
     */
    private Integer id;
    /**
     * 相关付款单据号
     */
    private String paySheetId;
    /**
     * 条目名称
     */
    private String entryName;
    /**
     * 备注
     */
    private String remark;
    /**
     * 转账金额
     */
    private BigDecimal transferAmount;
}

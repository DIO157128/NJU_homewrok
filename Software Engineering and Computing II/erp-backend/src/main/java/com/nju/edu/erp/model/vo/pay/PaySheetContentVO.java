package com.nju.edu.erp.model.vo.pay;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaySheetContentVO {
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

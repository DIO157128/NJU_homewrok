package com.nju.edu.erp.model.vo.receive;

import java.math.BigDecimal;

import com.nju.edu.erp.enums.sheetState.PurchaseSheetState;
import com.nju.edu.erp.enums.sheetState.ReceiveSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiveSheetVO {
    /**
     * 收款单据编号
     */
    private String id;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 客户类型
     */
    private String customerType;
    /**
     * 单据状态
     */
    private ReceiveSheetState state;
    /**
     * 操作员
     */
    private String operator;
    /**
     * 收款金额
     */
    private BigDecimal allAmount;
    /**
     * 折扣金额
     */
    private BigDecimal discount;
    /**
     * 转账列表
     */
    private List<ReceiveSheetContentVO> receiveSheetContent;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 实际金额
     */
    private BigDecimal actualAmount;
}


package com.nju.edu.erp.model.vo.pay;
import com.nju.edu.erp.enums.sheetState.PaySheetState;
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
public class PaySheetVO {
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
     * 客户名
     */
    private Integer payerId;
    /**
     * 条目列表
     */
    private List<PaySheetContentVO> paySheetContent;

}

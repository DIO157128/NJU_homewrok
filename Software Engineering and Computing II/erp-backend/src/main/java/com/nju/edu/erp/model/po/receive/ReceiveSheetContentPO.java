package com.nju.edu.erp.model.po.receive;

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
public class ReceiveSheetContentPO {
    /**
     * 转账列表编号
     */
    private Integer id;
    /**
     * 相关收款单id
     */
    String receiveSheetId;
    /**
     * 银行账户
     */
    String bankAccount;
    /**
     * 备注
     */
    String remark;
    /**
     * 转账金额
     */
    BigDecimal transferAmount;

}

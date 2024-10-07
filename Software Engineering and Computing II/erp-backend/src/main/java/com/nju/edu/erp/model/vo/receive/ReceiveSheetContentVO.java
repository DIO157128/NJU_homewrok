package com.nju.edu.erp.model.vo.receive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiveSheetContentVO {
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

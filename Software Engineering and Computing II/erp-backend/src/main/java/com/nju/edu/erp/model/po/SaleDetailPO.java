package com.nju.edu.erp.model.po;

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
public class SaleDetailPO {
    /**
     * 销售/退货时间
     */
    private Date createTime;
    /**
     * 商品id
     */
    private String pid;
    /**
     * 客户id
     */
    private String sellerId;
    /**
     * 数量
     */
    private int quantity;
    /**
     * 单价
     */
    private BigDecimal unit_price;
    /**
     * 总价
     */
    private BigDecimal total_price;
}

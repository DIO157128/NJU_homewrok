package com.nju.edu.erp.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessSituationVO {
    /**
     * 销售原收入
     */
    private BigDecimal saleRevenueRaw;
    /**
     * 销售折扣
     */
    private BigDecimal saleRevenueDiscount;
    /**
     * 销售实际收入
     */
    private BigDecimal saleRevenueReal;
    /**
     * 销售成本
     */
    private BigDecimal sellingCost;
    /**
     * 人力成本
     */
    private BigDecimal labourCost;
    /**
     * 利润
     */
    private BigDecimal profit;

}

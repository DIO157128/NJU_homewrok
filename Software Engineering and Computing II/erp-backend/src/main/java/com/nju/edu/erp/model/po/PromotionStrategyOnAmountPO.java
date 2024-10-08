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
public class PromotionStrategyOnAmountPO {
    /**
     *编号
     */
    private Integer id;


    /**
     * 总价区间最低价
     */
    private Integer min_amount;

    /**
     * 总价区间最高价
     */
    private Integer max_amount;

    /**
     * 折扣
     */
    private BigDecimal discount;

    /**
     *
     * 代金券
     *
     */
    private BigDecimal voucher;

    /**
     * 开始时间
     */
    private Date start_time;

    /**
     * 停止时间
     */
    private Date end_time;

    /**
     * 创建时间
     */
    private Date create_time;
}

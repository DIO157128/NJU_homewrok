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
public class PromotionStrategyOnLevelPO {
    /**
     * id
     */
    private Integer id;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     *
     * 客户级别
     *
     */
    private Integer level;

    /**
     *
     * 折扣
     *
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

}

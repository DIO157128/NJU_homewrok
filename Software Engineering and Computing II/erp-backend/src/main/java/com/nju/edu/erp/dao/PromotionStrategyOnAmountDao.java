package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.PromotionStrategyOnAmountPO;
import com.nju.edu.erp.model.po.PromotionStrategyOnLevelPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface PromotionStrategyOnAmountDao {
    /**
     * 增加一个按总价的促销策略
     */
    int setPromotionStrategyOnAmount(PromotionStrategyOnAmountPO promotionStrategyOnAmountPO);

    /**
     * 根据总价获得代金券
     */
    BigDecimal getVoucherByAmount(BigDecimal Amount);

    /**
     * 根据总价获得折扣
     */
    BigDecimal getDiscountByAmount(BigDecimal Amount);

    /**
     * 获取所有按总价的促销策略
     */
    List<PromotionStrategyOnAmountPO> getAllPromotionStrategyOnAmount();

    /**
     * 根据id更新总价促销策略
     */
    int updateById(PromotionStrategyOnAmountPO promotionStrategyOnAmountPO);

    /**
     * 根据id获得总价促销策略
     */
    PromotionStrategyOnAmountPO findById(Integer id);

    /**
     * 根据id删除总价促销策略
     */
    int deleteById(Integer id);

    /**
     * 获得所有该总价的促销策略
     */
    List<PromotionStrategyOnAmountPO> getStrategyOnOneAmount(BigDecimal amount);

}

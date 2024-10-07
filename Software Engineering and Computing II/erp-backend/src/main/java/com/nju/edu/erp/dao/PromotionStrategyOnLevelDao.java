package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.PromotionStrategyOnLevelPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface PromotionStrategyOnLevelDao {
    /**
     *
     * 在数据库中增加按用户级别的促销策略
     *
     */
    int setPromotionStrategyOnLevel(PromotionStrategyOnLevelPO promotionStrategyOnLevelPO);


    /**
     *
     *根据等级获得折扣
     *
     */
    BigDecimal getDiscountByLevel(Integer level);


    /**
     *
     * 根据等级获得代金券金额
     *
     */
    BigDecimal getVoucherByLevel(Integer level);

    /**
     * 根据id获得策略开始时间
     */
    Date getStartTime(Integer id);

    /**
     * 根据id获得策略终止时间
     */
    Date getEndTime(Integer id);

    /**
     *
     * 获取所有按等级的促销策略
     *
     */
    List<PromotionStrategyOnLevelPO> getAllPromotionStrategyOnLevel();

    /**
     * 根据id更新等级促销策略
     */
    int updateById(PromotionStrategyOnLevelPO promotionStrategyOnLevelPO);

    /**
     * 根据id获得等级促销策略
     */
    PromotionStrategyOnLevelPO findById(Integer id);

    /**
     * 根据id删除等级促销策略
     */
    int deleteById(Integer id);

    /**
     * 获得所有该等级的促销策略
     */
    List<PromotionStrategyOnLevelPO> getStrategyOnOneLevel(Integer level);
}

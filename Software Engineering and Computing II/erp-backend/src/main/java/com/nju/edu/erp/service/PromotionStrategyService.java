package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.PromotionStrategyOnLevelVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public interface PromotionStrategyService {
    /**
     * 获得折扣
     */
    BigDecimal getDiscount();

    /**
     * 获得代金券金额
     */
    BigDecimal getVoucher();

}

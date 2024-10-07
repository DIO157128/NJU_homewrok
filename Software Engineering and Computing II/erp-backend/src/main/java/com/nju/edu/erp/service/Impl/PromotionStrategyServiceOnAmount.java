package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.PromotionStrategyOnAmountDao;
import com.nju.edu.erp.service.PromotionStrategyService;
import com.nju.edu.erp.model.po.PromotionStrategyOnAmountPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PromotionStrategyServiceOnAmount implements PromotionStrategyService {
    private PromotionStrategyOnAmountDao promotionStrategyOnAmountDao;

    BigDecimal amount;

    @Autowired
    public PromotionStrategyServiceOnAmount(PromotionStrategyOnAmountDao promotionStrategyOnAmountDao) {
        this.promotionStrategyOnAmountDao = promotionStrategyOnAmountDao;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    @Override
    public BigDecimal getDiscount() {
        List<PromotionStrategyOnAmountPO> promotionStrategyOnAmountPOS = promotionStrategyOnAmountDao.getStrategyOnOneAmount(amount);

        BigDecimal discount = new BigDecimal(1);
        Date currentDate = new Date();
        for (PromotionStrategyOnAmountPO promotionStrategyOnAmountPO : promotionStrategyOnAmountPOS) {
            if (currentDate.after(promotionStrategyOnAmountPO.getStart_time()) && currentDate.before(promotionStrategyOnAmountPO.getEnd_time())) {
                discount = promotionStrategyOnAmountPO.getDiscount();
                break;
            }
        }
        return discount;
    }

    @Override
    public BigDecimal getVoucher() {
        List<PromotionStrategyOnAmountPO> promotionStrategyOnAmountPOS = promotionStrategyOnAmountDao.getStrategyOnOneAmount(amount);

        BigDecimal voucher = new BigDecimal(0);
        Date currentDate = new Date();
        for (PromotionStrategyOnAmountPO promotionStrategyOnAmountPO : promotionStrategyOnAmountPOS) {
            if (currentDate.after(promotionStrategyOnAmountPO.getStart_time()) && currentDate.before(promotionStrategyOnAmountPO.getEnd_time())) {
                voucher = promotionStrategyOnAmountPO.getVoucher();
                break;
            }
        }
        return voucher;
    }
}



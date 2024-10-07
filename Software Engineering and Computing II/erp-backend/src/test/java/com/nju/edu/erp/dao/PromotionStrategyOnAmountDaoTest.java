package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.PromotionStrategyOnAmountPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PromotionStrategyOnAmountDaoTest {
    @Autowired
    PromotionStrategyOnAmountDao promotionStrategyOnAmountDao;

    @Test
    @Transactional
    @Rollback(value = true)
    void setPromotionStrategyOnAmountTest() {
        PromotionStrategyOnAmountPO tosave=PromotionStrategyOnAmountPO.builder()
                .id(7)
                .min_amount(500)
                .max_amount(600)
                .discount(BigDecimal.valueOf(0.8))
                .voucher(BigDecimal.valueOf(100))
                .start_time(null)
                .end_time(null)
                .create_time(null)
                .build();
        int res=promotionStrategyOnAmountDao.setPromotionStrategyOnAmount(tosave);
        assertEquals(1,res);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void getVoucherByAmountTest() {
        BigDecimal actual=promotionStrategyOnAmountDao.getVoucherByAmount(BigDecimal.valueOf(100));
        assertEquals(0,actual.compareTo(BigDecimal.valueOf(0)));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void getDiscountByAmountTest() {
        BigDecimal actual=promotionStrategyOnAmountDao.getDiscountByAmount(BigDecimal.valueOf(100));
        assertEquals(0,actual.compareTo(BigDecimal.valueOf(1)));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void getAllPromotionStrategyOnAmountTest() {
        List<PromotionStrategyOnAmountPO> actual =promotionStrategyOnAmountDao.getAllPromotionStrategyOnAmount();
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void updateByIdTest() {
        PromotionStrategyOnAmountPO tosave=PromotionStrategyOnAmountPO.builder()
                .id(1)
                .min_amount(500)
                .max_amount(600)
                .discount(BigDecimal.valueOf(0.8))
                .voucher(BigDecimal.valueOf(100))
                .start_time(null)
                .end_time(null)
                .create_time(null)
                .build();
        int res=promotionStrategyOnAmountDao.updateById(tosave);
        assertEquals(1,res);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findByIdTest() {
        PromotionStrategyOnAmountPO actual=promotionStrategyOnAmountDao.findById(1);
        assertNotNull(actual);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void deleteByIdTest() {
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void getStrategyOnOneAmountTest() {
        List<PromotionStrategyOnAmountPO> actual=promotionStrategyOnAmountDao.getStrategyOnOneAmount(BigDecimal.valueOf(50));
        assertNotNull(actual);
    }
}

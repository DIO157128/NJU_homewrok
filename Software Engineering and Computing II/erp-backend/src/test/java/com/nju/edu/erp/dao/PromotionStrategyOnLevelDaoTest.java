package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.PromotionStrategyOnLevelPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PromotionStrategyOnLevelDaoTest {
    @Autowired
    PromotionStrategyOnLevelDao promotionStrategyOnLevelDao;

    @Test
    @Transactional
    @Rollback(value = true)
    void setPromotionStrategyOnLevelTest() {
        PromotionStrategyOnLevelPO tosave=PromotionStrategyOnLevelPO.builder()
                .level(1)
                .discount(BigDecimal.valueOf(1))
                .voucher(BigDecimal.valueOf(10))
                .start_time(null)
                .end_time(null)
                .create_time(null)
                .build();
        int res=promotionStrategyOnLevelDao.setPromotionStrategyOnLevel(tosave);
        assertEquals(1,res);

    }

    @Test
    @Transactional
    @Rollback(value = true)
    void getDiscountByLevelTest() {
        BigDecimal actual=promotionStrategyOnLevelDao.getDiscountByLevel(1);
        assertEquals(0,actual.compareTo(BigDecimal.valueOf(1)));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void getVoucherByLevelTest() {
        BigDecimal actual=promotionStrategyOnLevelDao.getVoucherByLevel(1);
        assertEquals(0,actual.compareTo(BigDecimal.valueOf(0)));
    }


    @Test
    @Transactional
    @Rollback(value = true)
    void getAllPromotionStrategyOnLevelTest() {
        List<PromotionStrategyOnLevelPO> actual=promotionStrategyOnLevelDao.getAllPromotionStrategyOnLevel();
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void updateByIdTest() {
        PromotionStrategyOnLevelPO tosave=PromotionStrategyOnLevelPO.builder()
                .id(3)
                .level(1)
                .discount(BigDecimal.valueOf(1))
                .voucher(BigDecimal.valueOf(10))
                .start_time(null)
                .end_time(null)
                .create_time(null)
                .build();
        int res=promotionStrategyOnLevelDao.updateById(tosave);
        assertEquals(1,res);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findByIdTest() {
        PromotionStrategyOnLevelPO actual=promotionStrategyOnLevelDao.findById(3);
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
    void getStrategyOnOneLevelTest() {
        List<PromotionStrategyOnLevelPO> actual=promotionStrategyOnLevelDao.getStrategyOnOneLevel(1);
        assertNotNull(actual);
    }
}

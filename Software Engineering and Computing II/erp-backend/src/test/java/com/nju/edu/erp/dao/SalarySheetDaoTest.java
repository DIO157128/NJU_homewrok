package com.nju.edu.erp.dao;


import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.po.salary.SalarySheetPO;
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
class SalarySheetDaoTest {
    @Autowired
    SalarySheetDao salarySheetDao;


    @Test
    @Transactional
    @Rollback(value = true)
    void saveSheetTest() {
        SalarySheetPO tosave = SalarySheetPO.builder()
                .id("GZD-20220629-00023")
                .staffId(3)
                .staffName("kucun")
                .cardAccount("00003")
                .dueSalary(BigDecimal.valueOf(300))
                .tax(BigDecimal.valueOf(1))
                .actualSalary(BigDecimal.valueOf(499))
                .state(SalarySheetState.PENDING_LEVEL_1)
                .type(1)
                .createDate(new Date(122, 5, 29))
                .build();
        int res = salarySheetDao.saveSheet(tosave);
        assertEquals(1, res);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findAllSheetTest() {
        List<SalarySheetPO> actual = salarySheetDao.findAllSheet();

    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findSheetByIdTest() {
        SalarySheetPO actual = salarySheetDao.findSheetById("GZD-20220629-00000");
        assertNotNull(actual);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findAllSheetByStateTest() {
        List<SalarySheetPO> actual =salarySheetDao.findAllSheetByState(SalarySheetState.SUCCESS);
        assertNotEquals(0,actual.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void updateSheetStateTest() {
        int res = salarySheetDao.updateSheetState("GZD-20220630-00000",SalarySheetState.SUCCESS);
        assertEquals(1,res);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void updateSheetStateOnPrevTest() {
        int res = salarySheetDao.updateSheetStateOnPrev("GZD-20220630-00000",SalarySheetState.FAILURE,SalarySheetState.SUCCESS);
        assertEquals(0,res);
    }
}

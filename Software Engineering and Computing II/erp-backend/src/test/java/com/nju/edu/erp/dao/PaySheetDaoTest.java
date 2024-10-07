package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.PaySheetState;
import com.nju.edu.erp.model.po.pay.PaySheetContentPO;
import com.nju.edu.erp.model.po.pay.PaySheetPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaySheetDaoTest {
    @Autowired PaySheetDao paySheetDao;

    @Test
    @Transactional
    @Rollback(value = true)
    void saveSheetTest() {
        PaySheetPO paySheetPO=PaySheetPO.builder()
                .id("FKD-20220629-00023")
                .bankAccount("13245646")
                .state(PaySheetState.PENDING)
                .operator("caiwurenyuan")
                .allAmount(BigDecimal.valueOf(134546))
                .createDate(new Date(2022,6,29))
                .build();
        int res = paySheetDao.saveSheet(paySheetPO);
        assertEquals(1,res);

    }

    @Test
    @Transactional
    @Rollback(value = true)
    void saveBatchSheetContentTest() {
        List<PaySheetContentPO> paySheetContentPOS =new ArrayList<>();
        paySheetContentPOS.add(PaySheetContentPO.builder()
                .paySheetId("FKD-20220629-00023")
                .remark("123")
                .entryName("12")
                .transferAmount(BigDecimal.valueOf(1000))
                .build());
        paySheetContentPOS.add(PaySheetContentPO.builder()
                .paySheetId("FKD-20220629-00023")
                .remark("1234")
                .entryName("124")
                .transferAmount(BigDecimal.valueOf(10004))
                .build());

        int res = paySheetDao.saveBatchSheetContent(paySheetContentPOS);
        assertEquals(2,res);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findAllSheetTest() {
        List<PaySheetPO> expect = new ArrayList<>();
        expect.add(PaySheetPO.builder()
                .id("FKD-20220629-00000")
                .bankAccount("13245646")
                .state(PaySheetState.SUCCESS)
                .operator("caiwurenyuan")
                .allAmount(BigDecimal.valueOf(134546))
                .createDate(new Date(122,5,29))
                .build());
        expect.add(PaySheetPO.builder()
                .id("FKD-20220629-00001")
                .bankAccount("54646")
                .state(PaySheetState.FAILURE)
                .operator("caiwurenyuan")
                .allAmount(BigDecimal.valueOf(21321))
                .createDate(new Date(122,5,29))
                .build());
        expect.add(PaySheetPO.builder()
                .id("FKD-20220630-00000")
                .bankAccount("123465")
                .state(PaySheetState.PENDING)
                .operator("caiwurenyuan")
                .allAmount(BigDecimal.valueOf(11111))
                .createDate(new Date(122,5,30))
                .build());
        List<PaySheetPO> actual =paySheetDao.findAllSheet();
//        assertEquals(expect.size(),actual.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findSheetByIdTest() {
        PaySheetPO expect = PaySheetPO.builder()
                .id("FKD-20220629-00000")
                .bankAccount("13245646")
                .state(PaySheetState.SUCCESS)
                .operator("caiwurenyuan")
                .allAmount(new BigDecimal(134546).setScale(2,BigDecimal.ROUND_HALF_UP))
                .createDate(new Date(122,5,29))
                .build();
        PaySheetPO actual = paySheetDao.findSheetById("FKD-20220629-00000");
        assertNotNull(actual);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findContentBySheetIdTest() {
        List<PaySheetContentPO> actual = paySheetDao.findContentBySheetId("FKD-20220629-00000");
        assertEquals(2,actual.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findAllSheetByStateTest() {
        List<PaySheetPO> actual =paySheetDao.findAllSheetByState(PaySheetState.SUCCESS);
        assertEquals(1,actual.size());

    }

    @Test
    @Transactional
    @Rollback(value = true)
    void updateSheetStateTest() {
        int res = paySheetDao.updateSheetState("FKD-20220630-00000",PaySheetState.FAILURE);
        assertEquals(1,res);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void updateSheetStateOnPrevTest() {
        int res = paySheetDao.updateSheetStateOnPrev("FKD-20220630-00000",PaySheetState.PENDING,PaySheetState.FAILURE);
        assertEquals(1,res);
    }
}

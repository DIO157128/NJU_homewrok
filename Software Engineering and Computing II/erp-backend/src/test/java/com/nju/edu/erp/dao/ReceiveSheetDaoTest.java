package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.ReceiveSheetState;
import com.nju.edu.erp.enums.sheetState.ReceiveSheetState;
import com.nju.edu.erp.model.po.receive.ReceiveSheetContentPO;
import com.nju.edu.erp.model.po.receive.ReceiveSheetPO;
import com.nju.edu.erp.model.po.receive.ReceiveSheetContentPO;
import com.nju.edu.erp.model.po.receive.ReceiveSheetPO;
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
class ReceiveSheetDaoTest {
    @Autowired
    ReceiveSheetDao receiveSheetDao;

    @Test
    @Transactional
    @Rollback(value = true)
    void saveSheetTest() {
        ReceiveSheetPO receiveSheetPO = ReceiveSheetPO.builder()
                .id("SKD-20220629-00023")
                .customerName("lxs2")
                .customerType("销售商")
                .state(ReceiveSheetState.PENDING)
                .discount(BigDecimal.valueOf(32))
                .operator("caiwurenyuan")
                .allAmount(BigDecimal.valueOf(134546))
                .createDate(new Date(122, 5, 29))
                .build();
        int res = receiveSheetDao.saveSheet(receiveSheetPO);
        assertEquals(1, res);

    }

    @Test
    @Transactional
    @Rollback(value = true)
    void saveBatchSheetContentTest() {
        List<ReceiveSheetContentPO> ReceiveSheetContentPOS = new ArrayList<>();
        ReceiveSheetContentPOS.add(ReceiveSheetContentPO.builder()
                .receiveSheetId("FKD-20220629-00023")
                .remark("123")
                .bankAccount("123123")
                .transferAmount(BigDecimal.valueOf(1000))
                .build());
        ReceiveSheetContentPOS.add(ReceiveSheetContentPO.builder()
                .receiveSheetId("FKD-20220629-00023")
                .remark("1234")
                .bankAccount("123123")
                .transferAmount(BigDecimal.valueOf(10004))
                .build());

        int res = receiveSheetDao.saveBatchSheetContent(ReceiveSheetContentPOS);
        assertEquals(2, res);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findAllSheetTest() {
        List<ReceiveSheetPO> actual = receiveSheetDao.findAllSheet();
        assertNotEquals(0, actual.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findSheetByIdTest() {
        ReceiveSheetPO expect = ReceiveSheetPO.builder()
                .id("FKD-20220629-00000")
                .customerName("lxs2")
                .customerType("销售商")
                .state(ReceiveSheetState.SUCCESS)
                .operator("caiwurenyuan")
                .allAmount(new BigDecimal(134546).setScale(2, BigDecimal.ROUND_HALF_UP))
                .createDate(new Date(122, 5, 29))
                .build();
        ReceiveSheetPO actual = receiveSheetDao.findSheetById("SKD-20220629-00000");
        assertNotNull(actual);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findContentBySheetIdTest() {
        List<ReceiveSheetContentPO> actual = receiveSheetDao.findContentBySheetId("SKD-20220629-00000");
        assertEquals(2, actual.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findAllSheetByStateTest() {
        List<ReceiveSheetPO> actual = receiveSheetDao.findAllSheetByState(ReceiveSheetState.SUCCESS);
        assertEquals(1, actual.size());

    }

    @Test
    @Transactional
    @Rollback(value = true)
    void updateSheetStateTest() {
        int res = receiveSheetDao.updateSheetState("SKD-20220629-00000", ReceiveSheetState.FAILURE);
        assertEquals(1, res);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void updateSheetStateOnPrevTest() {
        int res = receiveSheetDao.updateSheetStateOnPrev("SKD-20220629-00000", ReceiveSheetState.SUCCESS, ReceiveSheetState.FAILURE);
        assertEquals(1, res);
    }
}

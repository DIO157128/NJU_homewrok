package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.CompanyAccountDao;
import com.nju.edu.erp.dao.CustomerDao;
import com.nju.edu.erp.dao.ReceiveSheetDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.ReceiveSheetState;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.receive.ReceiveSheetContentPO;
import com.nju.edu.erp.model.po.receive.ReceiveSheetPO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetContentVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetVO;
import com.nju.edu.erp.service.FinanceService.ReceiveService;
import com.nju.edu.erp.utils.IdGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SpringBootTest
class ReceiveServiceTest {
    @Autowired
    ReceiveSheetDao receiveSheetDao;

    @Autowired
    ReceiveService receiveService;

    @Autowired
    CustomerDao customerDao;

    @Autowired
    CompanyAccountDao companyAccountDao;

    @Test
    @Transactional
    @Rollback(value = true)
    void makeReceiveSheetTest() {
        UserVO userVO = UserVO.builder()
                .name("caiwurenyuan")
                .role(Role.FINANCIAL_STAFF)
                .build();
        List<ReceiveSheetContentVO> receiveSheetContentVOS = new ArrayList<>();
        receiveSheetContentVOS.add(ReceiveSheetContentVO.builder()
                .bankAccount("123")
                .remark("Test1-sheet")
                .transferAmount(BigDecimal.valueOf(100))
                .build());
        receiveSheetContentVOS.add(ReceiveSheetContentVO.builder()
                .bankAccount("123")
                .remark("Test1-sheet")
                .transferAmount(BigDecimal.valueOf(200))
                .build());
        ReceiveSheetVO receiveSheetVO = ReceiveSheetVO.builder()
                .customerName("lxs")
                .customerType("销售商")
                .discount(BigDecimal.valueOf(10))
                .receiveSheetContent(receiveSheetContentVOS)
                .build();
        ReceiveSheetPO prevSheet = receiveSheetDao.getLatestSheet();
        String realSheetId = IdGenerator.generateSheetId(prevSheet == null ? null : prevSheet.getId(), "SKD");
        receiveService.makeReceiveSheet(userVO, receiveSheetVO);
        ReceiveSheetPO latestSheet = receiveSheetDao.getLatestSheet();
        Assertions.assertNotNull(latestSheet);
        Assertions.assertEquals(realSheetId, latestSheet.getId());
        Assertions.assertEquals(0, latestSheet.getAllAmount().compareTo(BigDecimal.valueOf(300.00)));
        Assertions.assertEquals(0, latestSheet.getActualAmount().compareTo(BigDecimal.valueOf(290.00)));
        Assertions.assertEquals(ReceiveSheetState.PENDING, latestSheet.getState());

        String sheetId = latestSheet.getId();
        Assertions.assertNotNull(sheetId);
        List<ReceiveSheetContentPO> content = receiveSheetDao.findContentBySheetId(sheetId);
        content.sort(Comparator.comparing(ReceiveSheetContentPO::getId));
        Assertions.assertEquals(2, content.size());

    }


    @Test
    @Transactional
    @Rollback(value = true)
    void approval_exception_1Test() {//不能把已经审批通过的Fail掉
        try {
            receiveService.approval("SKD-20220629-00000", ReceiveSheetState.FAILURE);
        } catch (Exception ignore){
        } finally {
            ReceiveSheetPO sheet = receiveSheetDao.findSheetById("SKD-20220629-00000");
            Assertions.assertEquals(ReceiveSheetState.SUCCESS,sheet.getState());
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void approval_exception_2Test() {//不能把已经审批失败的Success掉
        try {
            receiveService.approval("SKD-20220701-00001", ReceiveSheetState.SUCCESS);
        } catch (Exception ignore){
        } finally {
            ReceiveSheetPO sheet = receiveSheetDao.findSheetById("SKD-20220701-00001");
            Assertions.assertEquals(ReceiveSheetState.FAILURE,sheet.getState());
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void approval_successTest() {
        receiveService.approval("SKD-20220701-00000", ReceiveSheetState.SUCCESS);
        ReceiveSheetPO sheet = receiveSheetDao.findSheetById("SKD-20220701-00000");
        Assertions.assertEquals(ReceiveSheetState.SUCCESS,sheet.getState());
        CustomerPO customerPO = customerDao.findOneByName(sheet.getCustomerName());
        Assertions.assertEquals(0,BigDecimal.valueOf(4431301).compareTo(customerPO.getReceivable()));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void approval_failTest() {
        receiveService.approval("SKD-20220701-00000", ReceiveSheetState.FAILURE);
        ReceiveSheetPO sheet = receiveSheetDao.findSheetById("SKD-20220701-00000");
        Assertions.assertEquals(ReceiveSheetState.FAILURE,sheet.getState());

    }

    @Test
    @Transactional
    @Rollback(value = true)
    void getReceiveSheetByStateTest() {
        List<ReceiveSheetVO> receiveSheetByState = receiveService.getReceiveSheetByState(ReceiveSheetState.SUCCESS);
        Assertions.assertNotNull(receiveSheetByState);
        Assertions.assertEquals(1, receiveSheetByState.size());
        ReceiveSheetVO sheet1 = receiveSheetByState.get(0);
        Assertions.assertNotNull(sheet1);
        Assertions.assertEquals("SKD-20220629-00000", sheet1.getId());

        List<ReceiveSheetContentVO> sheet1Content = sheet1.getReceiveSheetContent();
        Assertions.assertNotNull(sheet1Content);
        Assertions.assertEquals(2, sheet1Content.size());

    }

}

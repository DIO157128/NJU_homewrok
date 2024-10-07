package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.CompanyAccountDao;
import com.nju.edu.erp.dao.CustomerDao;
import com.nju.edu.erp.dao.PaySheetDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.PaySheetState;
import com.nju.edu.erp.model.po.CompanyAccountPO;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.pay.PaySheetContentPO;
import com.nju.edu.erp.model.po.pay.PaySheetPO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.pay.PaySheetContentVO;
import com.nju.edu.erp.model.vo.pay.PaySheetVO;
import com.nju.edu.erp.service.FinanceService.PayService;
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
class PayServiceTest {

    @Autowired
    PaySheetDao paySheetDao;

    @Autowired
    PayService payService;

    @Autowired
    CustomerDao customerDao;

    @Autowired
    CompanyAccountDao companyAccountDao;

    @Test
    @Transactional
    @Rollback(value = true)
    void makePaySheetTest() {
        UserVO userVO = UserVO.builder()
                .name("caiwurenyuan")
                .role(Role.FINANCIAL_STAFF)
                .build();
        List<PaySheetContentVO> paySheetContentVOS = new ArrayList<>();
        paySheetContentVOS.add(PaySheetContentVO.builder()
                .entryName("Type1")
                .remark("Test1-sheet")
                .transferAmount(BigDecimal.valueOf(100))
                .build());
        paySheetContentVOS.add(PaySheetContentVO.builder()
                .entryName("Type2")
                .remark("Test1-sheet")
                .transferAmount(BigDecimal.valueOf(200))
                .build());
        PaySheetVO paySheetVO = PaySheetVO.builder()
                .bankAccount("123456")
                .paySheetContent(paySheetContentVOS)
                .build();
        PaySheetPO prevSheet = paySheetDao.getLatestSheet();
        String realSheetId = IdGenerator.generateSheetId(prevSheet == null ? null : prevSheet.getId(), "FKD");
        payService.makePaySheet(userVO, paySheetVO);
        PaySheetPO latestSheet = paySheetDao.getLatestSheet();
        Assertions.assertNotNull(latestSheet);
        Assertions.assertEquals(realSheetId, latestSheet.getId());
        Assertions.assertEquals(0, latestSheet.getAllAmount().compareTo(BigDecimal.valueOf(300.00)));
        Assertions.assertEquals(PaySheetState.PENDING, latestSheet.getState());

        String sheetId = latestSheet.getId();
        Assertions.assertNotNull(sheetId);
        List<PaySheetContentPO> content = paySheetDao.findContentBySheetId(sheetId);
        content.sort(Comparator.comparing(PaySheetContentPO::getId));
        Assertions.assertEquals(2, content.size());

    }

    @Test
    @Transactional
    @Rollback(value = true)
    void getPaySheetByStateTest() {
        List<PaySheetVO> paySheetByState = payService.getPaySheetByState(PaySheetState.SUCCESS);
        Assertions.assertNotNull(paySheetByState);
        Assertions.assertEquals(1, paySheetByState.size());
        PaySheetVO sheet1 = paySheetByState.get(0);
        Assertions.assertNotNull(sheet1);
        Assertions.assertEquals("FKD-20220629-00000", sheet1.getId());

        List<PaySheetContentVO> sheet1Content = sheet1.getPaySheetContent();
        Assertions.assertNotNull(sheet1Content);
        Assertions.assertEquals(2, sheet1Content.size());

    }

    @Test
    @Transactional
    @Rollback(value = true)
    void approval_exception_1Test() {//不能把已经审批通过的Fail掉
        try {
            payService.approval("FKD-20220629-00000", PaySheetState.FAILURE);
        } catch (Exception ignore){
        } finally {
            PaySheetPO sheet = paySheetDao.findSheetById("FKD-20220629-00000");
            Assertions.assertEquals(PaySheetState.SUCCESS,sheet.getState());
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void approval_exception_2Test() {//不能把已经审批失败的Success掉
        try {
            payService.approval("FKD-20220629-00001", PaySheetState.SUCCESS);
        } catch (Exception ignore){
        } finally {
            PaySheetPO sheet = paySheetDao.findSheetById("FKD-20220629-00001");
            Assertions.assertEquals(PaySheetState.FAILURE,sheet.getState());
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void approval_failedTest() {
        payService.approval("FKD-20220630-00000", PaySheetState.FAILURE);
        PaySheetPO sheet = paySheetDao.findSheetById("FKD-20220630-00000");
        Assertions.assertEquals(PaySheetState.FAILURE,sheet.getState());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void approval_successTest() {
        payService.approval("FKD-20220630-00000", PaySheetState.SUCCESS);
        PaySheetPO sheet = paySheetDao.findSheetById("FKD-20220630-00000");
        Assertions.assertEquals(PaySheetState.SUCCESS,sheet.getState());
        CustomerPO customerPO = customerDao.findOneById(sheet.getPayerId());
        CompanyAccountPO companyAccountPO = companyAccountDao.findOneByName(sheet.getBankAccount());
        Assertions.assertEquals(0,BigDecimal.valueOf(988889).compareTo(companyAccountPO.getBalance()));
    }
}

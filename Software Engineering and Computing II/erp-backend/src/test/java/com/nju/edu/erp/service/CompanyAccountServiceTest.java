package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.CompanyAccountVO;
import com.nju.edu.erp.service.FinanceService.CompanyAccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CompanyAccountServiceTest {


    @Autowired
    CompanyAccountService companyAccountService;

    @Test
    @Transactional
    @Rollback
    void addCompanyAccountTest() {
        CompanyAccountVO companyAccountVO = CompanyAccountVO.builder()
                .name("account_test_1")
                .balance(BigDecimal.valueOf(10000))
                .build();
        Assertions.assertNotEquals(0, companyAccountService.addCompanyAccount(companyAccountVO));
    }

    @Test
    @Transactional
    @Rollback
    void updateCompanyAccountTest() {
        CompanyAccountVO companyAccountVO = CompanyAccountVO.builder()
                .id(1)
                .name("account_test_2")
                .balance(BigDecimal.valueOf(10000))
                .build();
        Assertions.assertNotEquals(0, companyAccountService.updateCompanyAccount(companyAccountVO));
    }

    @Test
    @Transactional
    @Rollback
    void deleteCompanyAccountTest() {
        Assertions.assertNotEquals(0, companyAccountService.deleteCompanyAccount(1));
    }

    @Test
    @Transactional
    @Rollback
    void queryAllAccountsTest() {
        //expected result
        List<CompanyAccountVO> expect = new ArrayList<>();
        CompanyAccountVO companyAccountVO_1 = CompanyAccountVO.builder()
                .id(1)
                .name("account_1")
                .balance(new BigDecimal("1000000.00"))
                .build();
        expect.add(companyAccountVO_1);
        CompanyAccountVO companyAccountVO_2 = CompanyAccountVO.builder()
                .id(2)
                .name("account_2")
                .balance(new BigDecimal("1000000.00"))
                .build();
        expect.add(companyAccountVO_2);
        CompanyAccountVO companyAccountVO_3 = CompanyAccountVO.builder()
                .id(3)
                .name("account_3")
                .balance(new BigDecimal("1000000.00"))
                .build();
        expect.add(companyAccountVO_3);
        //actual result
        List<CompanyAccountVO> actual = companyAccountService.queryAllCompanyAccounts();
        Assertions.assertEquals(expect, actual);
    }


    @Test
    @Transactional
    @Rollback
    void findOneByNameTest() {
        CompanyAccountVO companyAccountVO = CompanyAccountVO.builder()
                .id(1)
                .name("account_1")
                .balance(new BigDecimal("1000000.00"))
                .build();
        Assertions.assertEquals(companyAccountVO, companyAccountService.findOneByName("account_1"));
    }
}

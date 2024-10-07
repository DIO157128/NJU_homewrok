package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.CompanyAccountPO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CompanyAccountDAOTest {
    @Autowired
    CompanyAccountDao companyAccountDAO;

    @Test
    @Transactional
    @Rollback
    void addCompanyAccountTest() {
        CompanyAccountPO companyAccountPO = CompanyAccountPO.builder()
                .name("account_test_1")
                .balance(BigDecimal.valueOf(10000))
                .build();
        Assertions.assertNotEquals(0, companyAccountDAO.addCompanyAccount(companyAccountPO));
    }

    @Test
    @Transactional
    @Rollback
    void updateCompanyAccountTest() {
        CompanyAccountPO companyAccountPO = CompanyAccountPO.builder()
                .id(1)
                .name("account_test_2")
                .balance(BigDecimal.valueOf(10000))
                .build();
        Assertions.assertNotEquals(0, companyAccountDAO.updateCompanyAccount(companyAccountPO));
    }

    @Test
    @Transactional
    @Rollback
    void deleteCompanyAccountTest() {
        Assertions.assertNotEquals(0, companyAccountDAO.deleteCompanyAccount(1));
    }

    @Test
    @Transactional
    @Rollback
    void queryAllAccountsTest() {
        //expected result
        List<CompanyAccountPO> expect = new ArrayList<>();
        CompanyAccountPO companyAccountPO_1 = CompanyAccountPO.builder()
                .id(1)
                .name("account_1")
                .balance(new BigDecimal("1000000.00"))
                .build();
        expect.add(companyAccountPO_1);
        CompanyAccountPO companyAccountPO_2 = CompanyAccountPO.builder()
                .id(2)
                .name("account_2")
                .balance(new BigDecimal("1000000.00"))
                .build();
        expect.add(companyAccountPO_2);
        CompanyAccountPO companyAccountPO_3 = CompanyAccountPO.builder()
                .id(3)
                .name("account_3")
                .balance(new BigDecimal("1000000.00"))
                .build();
        expect.add(companyAccountPO_3);

        List<CompanyAccountPO> actual = companyAccountDAO.queryAllAccounts();
        Assertions.assertEquals(expect, actual);
    }


    @Test
    @Transactional
    @Rollback
    void findOneByNameTest() {
        CompanyAccountPO companyAccountPO = CompanyAccountPO.builder()
                .id(1)
                .name("account_1")
                .balance(new BigDecimal("1000000.00"))
                .build();
        Assertions.assertEquals(companyAccountPO, companyAccountDAO.findOneByName("account_1"));
    }

    @Test
    @Transactional
    @Rollback
    void findOneByIdTest() {
        CompanyAccountPO companyAccountPO = CompanyAccountPO.builder()
                .id(1)
                .name("account_1")
                .balance(new BigDecimal("1000000.00"))
                .build();
        Assertions.assertEquals(companyAccountPO, companyAccountDAO.findOneById(1));
    }
}

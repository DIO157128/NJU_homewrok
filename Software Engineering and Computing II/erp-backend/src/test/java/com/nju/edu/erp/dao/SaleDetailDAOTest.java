package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.CompanyAccountPO;
import com.nju.edu.erp.model.po.SaleDetailPO;
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
class SaleDetailDAOTest {
    @Autowired
    SaleDetailDAO saleDetailDAO;

    @Test
    @Transactional
    @Rollback
    void getSaleDetailTest(){
        List<SaleDetailPO> POs = saleDetailDAO.getSailDetail();
        Assertions.assertNotNull(POs);
    }
}

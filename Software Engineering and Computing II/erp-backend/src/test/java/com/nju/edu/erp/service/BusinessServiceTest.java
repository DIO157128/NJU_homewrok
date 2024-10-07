package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.BusinessHistoryVO;
import com.nju.edu.erp.model.vo.BusinessSituationVO;
import com.nju.edu.erp.model.vo.SaleDetailVO;
import com.nju.edu.erp.service.BusinessService.BusinessService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class BusinessServiceTest {


    @Autowired
    BusinessService businessService;

    @Test
    @Transactional
    @Rollback
    void getSaleDetailTest(){
        List<SaleDetailVO> VOs = businessService.getSaleDetail();
        Assertions.assertNotNull(VOs);
        for(SaleDetailVO vo:VOs){
            System.out.println(vo);
        }
    }

    @Test
    @Transactional
    @Rollback
    void getBusinessHistory(){
        BusinessHistoryVO businessHistoryVO = businessService.getBusinessHistory();
        Assertions.assertNotNull(businessHistoryVO);
        Assertions.assertNotNull(businessHistoryVO.getSaleList());
        Assertions.assertNotNull(businessHistoryVO.getSaleReturnList());
        Assertions.assertNotNull(businessHistoryVO.getPurchaseList());
        Assertions.assertNotNull(businessHistoryVO.getPurchaseReturnList());
        Assertions.assertNotNull(businessHistoryVO.getPayList());
        Assertions.assertNotNull(businessHistoryVO.getReceiveList());
        Assertions.assertNotNull(businessHistoryVO.getSalaryList());
    }

    @Test
    @Transactional
    @Rollback
    void getBusinessSituationTest(){
        BusinessSituationVO businessSituationVO = businessService.getBusinessSituation(new Date(122, Calendar.MAY,1,0,0,0),new Date(122, Calendar.SEPTEMBER,1,0,0,0));
        Assert.assertNotNull(businessSituationVO);
        Assert.assertEquals(0, BigDecimal.valueOf(134546).compareTo(businessSituationVO.getSellingCost()));
        Assert.assertEquals(0, BigDecimal.valueOf(12345).compareTo(businessSituationVO.getSaleRevenueRaw()));
        Assert.assertEquals(0, BigDecimal.valueOf(5).compareTo(businessSituationVO.getSaleRevenueDiscount()));
        Assert.assertEquals(0, BigDecimal.valueOf(12340).compareTo(businessSituationVO.getSaleRevenueReal()));
        Assert.assertEquals(0, BigDecimal.valueOf(1000).compareTo(businessSituationVO.getLabourCost()));
    }

}

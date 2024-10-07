package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SaleReturnsServiceImplTest { // 该测试为集成测试，需要用到数据库，请大家连给定的测试数据库进行测试

    @Autowired
    WarehouseService warehouseService;
    @Autowired
    SaleReturnsService saleReturnsService;

    @Autowired
    SaleReturnsSheetDao saleReturnsSheetDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    CustomerDao customerDao;

    @Autowired
    WarehouseOutputSheetDao warehouseOutputSheetDao;



//    @Test
//    @Transactional
//    @Rollback(value = true)
//    public void approval_2() { // 测试二级审批
        // 二级审批成功之后需要进行
        // 1. 修改单据状态
        // 2. 更新商品表
        // 3. 更新客户表
        // 4. 新建出库草稿
//        saleReturnsService.approval("XSTHD-20220528-00000", SaleReturnsSheetState.SUCCESS);
//        SaleReturnsSheetPO sheet = saleReturnsSheetDao.findOneById("XSTHD-20220524-00000");
//        Assertions.assertEquals(SaleSheetState.SUCCESS,sheet.getState());
//
//        Assertions.assertEquals(0, productDao.findById("0000000000400000").getRecentRp().compareTo(BigDecimal.valueOf(2800.00)));
//        Assertions.assertEquals(0, productDao.findById("0000000000400001").getRecentRp().compareTo(BigDecimal.valueOf(3800.00)));
//
//        Assertions.assertEquals(0, customerDao.findOneById(2).getReceivable().compareTo(BigDecimal.valueOf(4959100.00)));
//        List<WarehouseOutputSheetPO> draftSheets = warehouseOutputSheetDao.getDraftSheets(WarehouseOutputSheetState.DRAFT);
//        Assertions.assertNotNull(draftSheets);
//        Assertions.assertEquals(1, draftSheets.size());
//        WarehouseOutputSheetPO draftSheet = draftSheets.get(0);
//        Assertions.assertNotNull(draftSheet);
//        Assertions.assertEquals("XSD-20220524-00003",draftSheet.getSaleSheetId());
//    }
}

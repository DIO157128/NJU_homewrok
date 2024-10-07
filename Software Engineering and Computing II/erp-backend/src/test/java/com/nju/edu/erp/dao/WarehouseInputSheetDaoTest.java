package com.nju.edu.erp.dao;

import com.nju.edu.erp.service.WarehouseService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;


@SpringBootTest
public class WarehouseInputSheetDaoTest {

    @Autowired
    WarehouseInputSheetDao warehouseInputSheetDao;

    @Test
    public void getLatestTest() {

    }

    @Test
    public void saveTest() {
    }

    @Test
    public void saveBatchTest() {
    }

    @Test
    public void getDraftSheetsTest() {
    }

    @Test
    public void getAllSheetsTest() {
    }

    @Test
    public void getSheetTest() {
    }

    @Test
    public void updateByIdTest() {
    }

    @Test
    public void getAllContentByIdTest() {
    }

    @Test
    public void getWarehouseIODetailByTimeTest() {
    }

    //@Test
    //@Transactional
    //@Rollback
    //public void getWarehouseInputProductQuantityByTime() {
        //new Date("month dd,yyyy hh:mm:ss");
      //  Integer sum = warehouseInputSheetDao.getWarehouseInputProductQuantityByTime(new Date(2022, Calendar.JUNE, 20, 12, 0, 0), new Date(2022, Calendar.AUGUST, 20, 12, 0, 0));
        //assert (sum==7532);
    //}
}

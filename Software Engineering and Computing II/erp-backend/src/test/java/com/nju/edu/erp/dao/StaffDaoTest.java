package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.StaffPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class StaffDaoTest {
    @Autowired
    StaffDao staffDao;


    @Test
    @Transactional
    @Rollback(value = true)
    void createStaffTest() {
        StaffPO staffPO = StaffPO.builder().id(9).birthday(new Date()).name("test_staff").gender(1).phoneNum("18929493243").postId(8).cardAccountId("00022").build();
        int line = staffDao.createStaff(staffPO);
        assertEquals(1, line);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void updateStaff_not_foundTest() {
        StaffPO staffPO = StaffPO.builder().id(9).birthday(new Date()).name("test_staff").gender(1).phoneNum("18929493243").postId(8).cardAccountId("00022").build();
        int line = staffDao.updateStaff(staffPO);
        assertEquals(0, line);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void updateStaffTest() {
        StaffPO staffPO = StaffPO.builder().id(8).birthday(new Date()).name("test_staff").gender(1).phoneNum("18929493243").postId(8).cardAccountId("00022").build();
        int line = staffDao.updateStaff(staffPO);
        assertEquals(1, line);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findByStaffIdTest() {
        StaffPO po = staffDao.findByStaffId(3);
        assertNotNull(po);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void queryAllTest() {
        List<StaffPO> list = staffDao.queryAll();
        assertNotNull(list);
        assertEquals(8, list.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findBasicSalaryTest() {
        int basicSalary = staffDao.findBasicSalary(5);
        assertEquals(10000, basicSalary);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findCalSalaryMethodTest() {
        String calSalaryMethod = staffDao.findCalSalaryMethod(5);
        assertEquals(calSalaryMethod, "YEARLY_SALARY");
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void checkIfAnEmptyPostTest() {
        int num = staffDao.checkIfAnEmptyPost(200);
        assertEquals(0, num);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void checkIfAnEmptyPost_not_emptyTest() {
        int num = staffDao.checkIfAnEmptyPost(5);
        assertEquals(1, num);            //有与之关联的员工
    }


    @Test
    @Transactional
    @Rollback(value = true)
    void deleteStaffTest() {
        staffDao.deleteStaff(3);
        StaffPO po = staffDao.findByStaffId(3);
        assert (po == null);
    }

}

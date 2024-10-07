package com.nju.edu.erp.service;


import com.nju.edu.erp.enums.Format;
import com.nju.edu.erp.model.vo.PostVO;
import com.nju.edu.erp.model.vo.StaffVO;

import com.nju.edu.erp.service.StaffService.StaffService;
import com.nju.edu.erp.utils.TimeFormatConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@SpringBootTest
public class StaffServiceTest {

    @Autowired
    StaffService staffService;



    /**
     * 增加员工通过
     */
    @Test
    @Transactional
    @Rollback
    public void register_passTest(){
        StaffVO staffVO=StaffVO.builder().name("byd").gender(1).birthday("2002-02-01 00:00:00").phoneNum("13719382748").postId(8).cardAccountId("00009").build();
        Format f = staffService.addStaff(staffVO);
        assert (f==Format.PASS);
    }

    /**
     * 手机格式有误
     */
    @Test
    @Transactional
    @Rollback
    public void register_phoneTest(){
        StaffVO staffVO=StaffVO.builder().name("byd").gender(1).birthday("2002-02-01 00:00:00").phoneNum("1371938274").postId(8).cardAccountId("00009").build();
        Format f = staffService.addStaff(staffVO);
        assert (f==Format.PHONE);
    }

    /**
     * 性别字段有误
     */
    @Test
    @Transactional
    @Rollback
    public void register_genderTest(){
        StaffVO staffVO=StaffVO.builder().name("byd").gender(3).birthday("2002-02-01 00:00:00").phoneNum("1371938274").postId(8).cardAccountId("00009").build();
        Format f = staffService.addStaff(staffVO);
        assert (f==Format.PHONE);
    }


    /**
     * 没有对应的岗位
     */
    @Test
    @Transactional
    @Rollback
    public void register_postTest(){
        StaffVO staffVO=StaffVO.builder().name("byd").gender(1).birthday("2002-02-01 00:00:00").phoneNum("13719382784").postId(100).cardAccountId("00009").build();
        Format f = staffService.addStaff(staffVO);
        assert (f==Format.POST);
    }

    /**
     * 增加员工通过
     */
    @Test
    @Transactional
    @Rollback
    public void update_passTest(){
        StaffVO staffVO=StaffVO.builder().id(1).name("byd").gender(1).birthday("2002-02-01 00:00:00").phoneNum("13719382748").postId(8).cardAccountId("00009").build();
        Format f = staffService.updateStaff(staffVO);
        assert (f==Format.PASS);
    }

    /**
     * 手机格式有误
     */
    @Test
    @Transactional
    @Rollback
    public void update_phoneTest(){
        StaffVO staffVO=StaffVO.builder().id(1).name("byd").gender(1).birthday("2002-02-01 00:00:00").phoneNum("1371938274").postId(8).cardAccountId("00009").build();
        Format f = staffService.updateStaff(staffVO);
        assert (f==Format.PHONE);
    }

    /**
     * 性别字段有误
     */
    @Test
    @Transactional
    @Rollback
    public void update_genderTest(){
        StaffVO staffVO=StaffVO.builder().id(1).name("byd").gender(3).birthday("2002-02-01 00:00:00").phoneNum("13714938274").postId(8).cardAccountId("00009").build();
        Format f = staffService.updateStaff(staffVO);
        assert (f==Format.GENDER);
    }


    /**
     * 没有对应的岗位
     */
    @Test
    @Transactional
    @Rollback
    public void update_postTest(){
        StaffVO staffVO=StaffVO.builder().id(1).name("byd").gender(1).birthday("2002-02-01 00:00:00").phoneNum("13719382784").postId(100).cardAccountId("00009").build();
        Format f = staffService.updateStaff(staffVO);
        assert (f==Format.POST);
    }

    /**
     * 没有对应的员工，无法进行更新
     */
    @Test
    @Transactional
    @Rollback
    public void update_no_correspondingTest(){
        StaffVO staffVO=StaffVO.builder().id(100).name("byd").gender(1).birthday("2002-02-01 00:00:00").phoneNum("13719382784").postId(1).cardAccountId("00009").build();
        Format f = staffService.updateStaff(staffVO);
        assert (f==Format.NO_CORRESPONDING);
    }


    @Test
    @Transactional
    @Rollback
    public void addPost_passTest(){
        PostVO postVO= PostVO.builder().postName("GM").postSalary(16500).basicSalary(16500).calSalaryMethod("YEARLY_SALARY").level(1).taxDeductionRemark("无").paySalaryMethod("CARD").build();
        Format f = staffService.addPost(postVO);
        assert (f==Format.PASS);
    }

    @Test
    @Transactional
    @Rollback
    public void addPost_postNameTest(){
        PostVO postVO= PostVO.builder().postName("GG").postSalary(16500).basicSalary(16500).calSalaryMethod("YEARLY_SALARY").level(1).taxDeductionRemark("无").paySalaryMethod("CARD").build();
        Format f = staffService.addPost(postVO);
        assert (f==Format.POST_NAME);
    }


    @Test
    @Transactional
    @Rollback
    public void addPost_salary_1Test(){
        PostVO postVO= PostVO.builder().postName("GM").postSalary(-16500).basicSalary(16500).calSalaryMethod("YEARLY_SALARY").level(1).taxDeductionRemark("无").paySalaryMethod("CARD").build();
        Format f = staffService.addPost(postVO);
        assert (f==Format.SALARY);
    }

    @Test
    @Transactional
    @Rollback
    public void addPost_salary_2Test(){
        PostVO postVO= PostVO.builder().postName("GM").postSalary(16500).basicSalary(-16500).calSalaryMethod("YEARLY_SALARY").level(1).taxDeductionRemark("无").paySalaryMethod("CARD").build();
        Format f = staffService.addPost(postVO);
        assert (f==Format.SALARY);
    }

    @Test
    @Transactional
    @Rollback
    public void addPost_calSalaryMethodTest(){
        PostVO postVO= PostVO.builder().postName("GM").postSalary(16500).basicSalary(16500).calSalaryMethod("YEARLY").level(1).taxDeductionRemark("无").paySalaryMethod("CARD").build();
        Format f = staffService.addPost(postVO);
        assert (f==Format.CAL_SAL_METHOD);
    }

    @Test
    @Transactional
    @Rollback
    public void addPost_levelTest(){
        PostVO postVO= PostVO.builder().postName("GM").postSalary(16500).basicSalary(16500).calSalaryMethod("YEARLY_SALARY").level(0).taxDeductionRemark("无").paySalaryMethod("CARD").build();
        Format f = staffService.addPost(postVO);
        assert (f==Format.LEVEL);
    }

    @Test
    @Transactional
    @Rollback
    public void updatePost_passTest(){
        PostVO postVO= PostVO.builder().postName("GM").id(1).postSalary(16500).basicSalary(16500).calSalaryMethod("YEARLY_SALARY").level(1).taxDeductionRemark("无").paySalaryMethod("CARD").build();
        Format f = staffService.updatePost(postVO);
        assert (f==Format.PASS);
    }

    @Test
    @Transactional
    @Rollback
    public void updatePost_salary_1Test(){
        PostVO postVO= PostVO.builder().postName("GM").id(1).postSalary(-16500).basicSalary(16500).calSalaryMethod("YEARLY_SALARY").level(1).taxDeductionRemark("无").paySalaryMethod("CARD").build();
        Format f = staffService.updatePost(postVO);
        assert (f==Format.SALARY);
    }

    @Test
    @Transactional
    @Rollback
    public void updatePost_salary_2Test(){
        PostVO postVO= PostVO.builder().postName("GM").id(1).postSalary(16500).basicSalary(-16500).calSalaryMethod("YEARLY_SALARY").level(1).taxDeductionRemark("无").paySalaryMethod("CARD").build();
        Format f = staffService.updatePost(postVO);
        assert (f==Format.SALARY);
    }

    @Test
    @Transactional
    @Rollback
    public void updatePost_calSalaryMethodTest(){
        PostVO postVO= PostVO.builder().postName("GM").id(1).postSalary(16500).basicSalary(16500).calSalaryMethod("YEARLY").level(1).taxDeductionRemark("无").paySalaryMethod("CARD").build();
        Format f = staffService.updatePost(postVO);
        assert (f==Format.CAL_SAL_METHOD);
    }

    @Test
    @Transactional
    @Rollback
    public void updatePost_levelTest(){
        PostVO postVO= PostVO.builder().postName("GM").id(1).postSalary(16500).basicSalary(16500).calSalaryMethod("YEARLY_SALARY").level(-1).taxDeductionRemark("无").paySalaryMethod("CARD").build();
        Format f = staffService.updatePost(postVO);
        assert (f==Format.LEVEL);
    }

    @Test
    @Transactional
    @Rollback
    public void updatePost_no_correspondingTest(){
        PostVO postVO= PostVO.builder().postName("GM").id(100).postSalary(16500).basicSalary(16500).calSalaryMethod("YEARLY_SALARY").level(1).taxDeductionRemark("无").paySalaryMethod("CARD").build();
        Format f = staffService.updatePost(postVO);
        assert (f==Format.NO_CORRESPONDING);
    }

    @Test
    @Transactional
    @Rollback
    public void queryAllTest(){
        List<StaffVO> staffVOS = staffService.queryAll();
        Assertions.assertNotNull(staffVOS);
        assert (staffVOS.size()==8);
    }






}

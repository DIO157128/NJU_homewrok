package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.po.receive.ReceiveSheetPO;
import com.nju.edu.erp.model.po.salary.SalarySheetPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface SalarySheetDao {
    /**
     * 保存工资单
     * @param toSave
     * @return
     */
    int saveSheet(SalarySheetPO toSave);

    /**
     * 查找是否已经被创建了，因为HR一个月内可能多次触发制定工资单的按钮，所以避免重复写入
     * @param staffId
     * @param d1
     * @param d2
     * @return
     */
    int findIfSalaryHasBeenMade(int staffId, Date d1,Date d2);

    /**
     * 查找总经理的工资是否被创建
     * @param staffId
     * @param year
     * @return
     */
    int findIfSalaryHasBeenMadeGM(int staffId,int year);


    /**
     * 查看年终奖是否已经被创建
     * @param staffId
     * @param year
     * @return
     */
    int findIfAnnualHasBeenMade(int staffId,int year);


    /**
     * 查找所有的工资单
     * @return
     */
    List<SalarySheetPO> findAllSheet();

    /**
     * 通过单据号查找工资单
     * @param id
     * @return
     */
    SalarySheetPO findSheetById(String id);


    /**
     * 通过单据状态查找单据
     * @param state
     * @return
     */
    List<SalarySheetPO> findAllSheetByState(SalarySheetState state);

    /**
     * 更新单据状态
     * @param sheetId
     * @param state
     * @return
     */
    int updateSheetState(String sheetId, SalarySheetState state);

    /**
     * 根据当前状态更新销售单状态
     * @param sheetId
     * @param prev
     * @param state
     * @return
     */
    int updateSheetStateOnPrev(String sheetId, SalarySheetState prev, SalarySheetState state);

    /**
     * 获取最近一条工资单
     * @return
     */
    SalarySheetPO getLatestSheet();

    /**
     * 根据给定日期寻找这段时间内的单据
     * @param fromDate
     * @param toDate
     * @return
     */
    List<SalarySheetPO> findAllSheetByDate(Date fromDate,Date toDate);

    /**
     *  查找今年的年终奖记录
     * @param staffId
     * @param year
     * @return
     */
    SalarySheetPO findAnnual(int staffId,int year);


    /**
     * 测试使用,删除全部数据再创建
     */
    void deleteAll();


    /**
     * 测试用
     * @return
     */
    String findMaxId();
}

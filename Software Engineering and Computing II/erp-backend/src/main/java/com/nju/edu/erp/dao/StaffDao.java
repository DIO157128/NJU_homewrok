package com.nju.edu.erp.dao;


import com.nju.edu.erp.model.po.PostPO;
import com.nju.edu.erp.model.po.StaffPO;
import com.nju.edu.erp.model.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface StaffDao {

    /**
     *  登记员工信息
     * @param staffPO
     * @return
     */
    int createStaff(StaffPO staffPO);

    /**
     * 更新员工信息
     * @param staffPO
     * @return
     */
    int updateStaff(StaffPO staffPO);

    /**
     * 按员工名称查找
     * @param staffName
     * @return
     */
    StaffPO findByStaffName(String staffName);

    /**
     * 按员工id查找
     * @param id
     * @return
     */
    StaffPO findByStaffId(int id);

    /**
     * 全搜索
     * @return
     */
    List<StaffPO> queryAll();

    /**
     *  除了总经理都是月薪
     * @return
     */
    List<StaffPO> queryAllExceptGM();

    /**
     * 寻找岗位
     * @param id
     * @return
     */
    String findPost(int id);

    /**
     *  查询基本工资
     * @param id
     * @return
     */
    int findBasicSalary(int id);

    /**
     * 寻找CalSalaryMethod字段
     * @return
     */
    String findCalSalaryMethod(int id);


    /**
     * 检查是否是没有 staff 与之关联的岗位
     * @param
     * @return
     */
    int checkIfAnEmptyPost(int postId);


    /**
     * 删除员工
     * @param staffId
     * @return
     */
    void deleteStaff(int staffId);

    /**
     * 测试用
     * @return
     */
    int getMaxId();
}

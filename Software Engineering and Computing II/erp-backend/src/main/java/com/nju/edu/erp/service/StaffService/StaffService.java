package com.nju.edu.erp.service.StaffService;

import com.nju.edu.erp.enums.Format;
import com.nju.edu.erp.model.po.StaffPO;
import com.nju.edu.erp.model.vo.PostVO;
import com.nju.edu.erp.model.vo.StaffVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StaffService {


    /**
     * 登记员工信息
     * @param staffVO
     */
    Format addStaff(StaffVO staffVO);


    /**
     * 查询所有员工信息
     * @return
     */
    List<StaffVO> queryAll();

    /**
     * 查询所有员工信息，别的模块使用
     * @return
     */
    List<StaffPO> queryAllPO();

    /**
     * 查询所有岗位信息
     * @return
     */
    List<PostVO> queryAllPost();

    /**
     * 删除员工
     * @param staffId
     * @return
     */
    boolean deleteStaff(int staffId);

    /**
     * 根据postId删除post
     * @param postId
     * @return
     */
    boolean deletePost(int postId);


    /**
     * 更新岗位
     */
    Format updatePost(PostVO postVO);

    /**
     * 更新员工
     * @param staffVO
     */
    Format updateStaff(StaffVO staffVO);

    /**
     * 增加岗位
     * @param postVO
     */
    Format addPost(PostVO postVO);

    /**
     * 根据staffId找到对应的PO
     * @param staffId
     * @return
     */
    StaffPO findByStaffId(int staffId);

    /**
     * 获取 calSalaryMethod
     * @param id
     * @return
     */
    String findCalSalaryMethod(int id);

    /**
     *
     * @param id
     * @return
     */
    int findBasicSalary(int id);

}

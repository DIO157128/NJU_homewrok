package com.nju.edu.erp.service.Impl.StaffServiceImpl;

import com.nju.edu.erp.dao.PostDao;
import com.nju.edu.erp.dao.StaffDao;
import com.nju.edu.erp.enums.Format;
import com.nju.edu.erp.model.po.PostPO;
import com.nju.edu.erp.model.po.StaffPO;
import com.nju.edu.erp.model.vo.PostVO;
import com.nju.edu.erp.model.vo.StaffVO;
import com.nju.edu.erp.service.StaffService.StaffService;
import com.nju.edu.erp.utils.CheckFormat;
import com.nju.edu.erp.utils.TimeFormatConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class StaffServiceImpl implements StaffService {

    private final StaffDao staffDao;

    private final PostDao postDao;


    @Autowired
    public StaffServiceImpl(StaffDao staffDao, PostDao postDao) {
        this.staffDao = staffDao;
        this.postDao = postDao;
    }


    @Override
    public Format addStaff(StaffVO staffVO) {
        StaffPO staffPO = new StaffPO();
        BeanUtils.copyProperties(staffVO, staffPO);
        staffPO.setBirthday(TimeFormatConverter.timeTransfer(staffVO.getBirthday()));
        if (!CheckFormat.checkIfPhone(staffPO.getPhoneNum()))
            return Format.PHONE;           //手机必须满足格式,注意这个  !,如果格式正确返回的是true
        if (!CheckFormat.checkGender(staffPO.getGender())) return Format.GENDER;              //检查性别字段
        if (staffVO.getBirthday() == null) staffPO.setBirthday(new Date());

        int ifHasPost = postDao.findIfHasPost(staffPO.getPostId());                   //没有对应的岗位则不允许添加员工
        if (ifHasPost == 0) return Format.POST;
        staffDao.createStaff(staffPO);
        return Format.PASS;
    }

    @Override
    public List<StaffVO> queryAll() {
        List<StaffPO> staffPOS = staffDao.queryAll();

        List<StaffVO> staffVOS = new ArrayList<>();
        for (StaffPO staffPO : staffPOS) {
            StaffVO staffVO = new StaffVO();
            BeanUtils.copyProperties(staffPO, staffVO);
            staffVO.setBirthday(TimeFormatConverter.timeTrans(staffPO.getBirthday()));
            staffVOS.add(staffVO);
        }
        return staffVOS;
    }

    @Override
    public List<StaffPO> queryAllPO() {
        return staffDao.queryAll();
    }

    @Override
    public List<PostVO> queryAllPost() {
        List<PostPO> postPOS = postDao.queryAllPost();
        List<PostVO> postVOS = new ArrayList<>();
        for (PostPO postPO : postPOS) {
            PostVO postVO = new PostVO();
            BeanUtils.copyProperties(postPO, postVO);
            postVOS.add(postVO);
        }
        return postVOS;
    }

    @Override
    public boolean deleteStaff(int staffId) {
        staffDao.deleteStaff(staffId);
        return true;
    }

    /**
     * 删除岗位，先要检查一下是否有与之关联的staff(员工)，如果有的话就不能删除
     *
     * @param postId
     * @return
     */
    @Override
    public boolean deletePost(int postId) {
        int sum = staffDao.checkIfAnEmptyPost(postId);
        if (sum != 0) return false;                 //不能删
        postDao.deletePost(postId);
        return true;
    }

    @Override
    public Format updatePost(PostVO postVO) {
        PostPO postPO = new PostPO();
        BeanUtils.copyProperties(postVO, postPO);
        if (!CheckFormat.checkSalary(postPO.getBasicSalary())) return Format.SALARY;
        if (!CheckFormat.checkSalary(postPO.getPostSalary())) return Format.SALARY;
        if (!CheckFormat.checkLevel(postPO.getLevel())) return Format.LEVEL;
        if (!CheckFormat.checkPostName(postPO.getPostName())) return Format.POST_NAME;
        if (!CheckFormat.checkCalSalaryMethod(postPO.getCalSalaryMethod())) return Format.CAL_SAL_METHOD;

        if(postDao.findByPostId(postPO.getId())==null) return Format.NO_CORRESPONDING;
        postDao.updatePost(postPO);
        return Format.PASS;
    }

    @Override
    public Format updateStaff(StaffVO staffVO) {
        StaffPO staffPO = new StaffPO();
        BeanUtils.copyProperties(staffVO, staffPO);
        if (!CheckFormat.checkIfPhone(staffPO.getPhoneNum())) return Format.PHONE;             //手机必须满足格式
        if (!CheckFormat.checkGender(staffPO.getGender())) return Format.GENDER;               //检查性别字段
        if (staffVO.getBirthday() == null) staffPO.setBirthday(new Date());
        else staffPO.setBirthday(TimeFormatConverter.timeTransfer(staffVO.getBirthday()));

        int ifHasPost = postDao.findIfHasPost(staffPO.getPostId());                             //没有对应的岗位则不允许添加员工
        if (ifHasPost == 0) return Format.POST;
        if (staffDao.findByStaffId(staffPO.getId()) == null) return Format.NO_CORRESPONDING;       //没有找到对应的员工
        staffDao.updateStaff(staffPO);
        return Format.PASS;
    }

    @Override
    public Format addPost(PostVO postVO) {
        PostPO postPO = new PostPO();
        BeanUtils.copyProperties(postVO, postPO);
        if (!CheckFormat.checkSalary(postPO.getBasicSalary())) return Format.SALARY;
        if (!CheckFormat.checkSalary(postPO.getPostSalary())) return Format.SALARY;
        if (!CheckFormat.checkLevel(postPO.getLevel())) return Format.LEVEL;
        if (!CheckFormat.checkPostName(postPO.getPostName())) return Format.POST_NAME;
        if (!CheckFormat.checkCalSalaryMethod(postPO.getCalSalaryMethod())) return Format.CAL_SAL_METHOD;
        postDao.addPost(postPO);
        return Format.PASS;
    }

    @Override
    public StaffPO findByStaffId(int staffId) {
        return staffDao.findByStaffId(staffId);
    }

    @Override
    public String findCalSalaryMethod(int id) {
        return staffDao.findCalSalaryMethod(id);
    }

    @Override
    public int findBasicSalary(int id) {
        return staffDao.findBasicSalary(id);
    }


}

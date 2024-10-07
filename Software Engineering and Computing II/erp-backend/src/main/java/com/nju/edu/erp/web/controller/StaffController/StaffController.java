package com.nju.edu.erp.web.controller.StaffController;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Format;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.PostVO;
import com.nju.edu.erp.model.vo.StaffVO;
import com.nju.edu.erp.service.StaffService.StaffService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/staff")
public class StaffController {

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping("/staff-create")
    @Authorized(roles = {Role.ADMIN, Role.HR, Role.GM})
    public Response createStaff(@RequestBody StaffVO staffVO) {
        Format f = staffService.addStaff(staffVO);
        if (f == Format.PASS) return Response.buildSuccess();
        else if (f == Format.GENDER) return Response.buildFailed("B0001", "性别字段有误");
        else if (f == Format.PHONE) return Response.buildFailed("B0002", "手机格式有误");
        else return Response.buildFailed("B0003", "没有对应的岗位信息，请先填写对应的岗位信息");
    }

    @PostMapping("/staff-update")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.HR})
    public Response updateStaff(@RequestBody StaffVO staffVO) {
        Format f = staffService.updateStaff(staffVO);
        if (f == Format.PASS) return Response.buildSuccess();
        else if (f == Format.GENDER) return Response.buildFailed("B0001", "性别字段有误");
        else if (f == Format.PHONE) return Response.buildFailed("B0002", "手机格式有误");
        else if (f == Format.NO_CORRESPONDING) return Response.buildFailed("B0003", "没有对应员工的信息");
        else return Response.buildFailed("B0004", "没有对应的岗位信息，请先填写对应的岗位信息");
    }

    @GetMapping("/query-all-staff")
    @Authorized(roles = {Role.ADMIN, Role.HR, Role.GM})
    public Response queryAllStaff() {
        List<StaffVO> staffVOS = staffService.queryAll();
        return Response.buildSuccess(staffVOS);
    }

    @GetMapping("/staff-delete")
    @Authorized(roles = {Role.ADMIN, Role.HR})
    public Response deleteStaff(int id) {
        boolean success = staffService.deleteStaff(id);
        if (success) return Response.buildSuccess();
        return Response.buildFailed("B0001", "");
    }


    @GetMapping("/query-all-post")
    @Authorized(roles = {Role.ADMIN, Role.HR})
    public Response queryAllPost() {
        List<PostVO> postVOS = staffService.queryAllPost();
        return Response.buildSuccess(postVOS);
    }

    @GetMapping("/post-delete")
    @Authorized(roles = {Role.ADMIN, Role.HR})
    public Response deletePost(int id) {
        boolean success = staffService.deletePost(id);
        if (success) return Response.buildSuccess();
        return Response.buildFailed("C0006", "存在关联的员工信息，不能删除该岗位");
    }

    @PostMapping("/post-update")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.HR})
    public Response updatePost(@RequestBody PostVO postVO) {
        Format f = staffService.updatePost(postVO);
        if(f==Format.CAL_SAL_METHOD) return Response.buildFailed("C0001","薪资计算方式有误");
        else if(f==Format.SALARY) return Response.buildFailed("C0002","薪资有误");
        else if(f==Format.POST_NAME) return Response.buildFailed("C0003","岗位名称有误");
        else if(f==Format.LEVEL) return Response.buildFailed("C0004","等级有误");
        else return Response.buildSuccess();
    }

    @PostMapping("/post-create")
    @Authorized(roles = {Role.ADMIN, Role.HR, Role.GM})
    public Response addPost(@RequestBody PostVO postVO) {
        Format f = staffService.addPost(postVO);
        if(f==Format.CAL_SAL_METHOD) return Response.buildFailed("C0001","薪资计算方式有误");
        else if(f==Format.SALARY) return Response.buildFailed("C0002","薪资有误");
        else if(f==Format.POST_NAME) return Response.buildFailed("C0003","岗位名称有误");
        else if(f==Format.LEVEL) return Response.buildFailed("C0004","等级有误");
        else return Response.buildSuccess();
    }

}

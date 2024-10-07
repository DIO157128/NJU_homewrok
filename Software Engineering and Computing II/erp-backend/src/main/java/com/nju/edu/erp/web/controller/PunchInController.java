package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.service.StaffService.PunchInService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/punch-in")
public class PunchInController {

    private final PunchInService punchInService;

    @Autowired
    public PunchInController(PunchInService punchInService) {
        this.punchInService = punchInService;
    }

    @GetMapping("/punch-in")
    @Authorized(roles = {Role.ADMIN, Role.HR,Role.SALE_MANAGER,Role.FINANCIAL_STAFF,Role.SALE_STAFF, Role.INVENTORY_MANAGER})
    public Response punchIn(UserVO user) {
        boolean success = punchInService.punchIn(user);
        if(success) return Response.buildSuccess();
        else return Response.buildFailed("重复打卡","重复打卡");
    }

    @GetMapping("/query-one")
    @Authorized(roles = {Role.ADMIN, Role.HR})
    public Response queryAllRecordByStaff(@RequestParam int staffId,
                                   @RequestParam String beginDateStr,
                                   @RequestParam String endDateStr) {
        return Response.buildSuccess(punchInService.findPunchInRecordContentByStaffAndPeriod(staffId,beginDateStr,endDateStr));
    }

    @GetMapping("/query-all")
    @Authorized(roles = {Role.ADMIN, Role.HR})
    public Response queryAllRecord(@RequestParam String beginDateStr, @RequestParam String endDateStr) {
        return Response.buildSuccess(punchInService.findAllPunchInRecordByPeriod(beginDateStr, endDateStr));
    }


}

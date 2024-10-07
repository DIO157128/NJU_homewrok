package com.nju.edu.erp.web.controller.FinanceController;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.CompanyAccountVO;
import com.nju.edu.erp.service.FinanceService.CompanyAccountService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;

@RestController
@RequestMapping(path = "/company-account")
public class CompanyAccountController {

    private final CompanyAccountService companyAccountService;

    @Autowired
    public CompanyAccountController(CompanyAccountService companyAccountService) {
        this.companyAccountService = companyAccountService;
    }

    @PostMapping("/create")
    @Authorized(roles = {Role.ADMIN, Role.FINANCIAL_STAFF, Role.GM})
    public Response addCompanyAccount(@RequestBody CompanyAccountVO companyAccountVO) {
        if (Objects.equals(companyAccountVO.getName(), "")) {
            return Response.buildFailed("300001", "账户名不能为空");
        } else if (companyAccountVO.getBalance() == null) {
            return Response.buildFailed("300002", "余额不能为空");
        } else if (companyAccountVO.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            return Response.buildFailed("300003", "余额不能为负数");
        } else {
            companyAccountService.addCompanyAccount(companyAccountVO);
            return Response.buildSuccess();
        }

    }


    @GetMapping("/queryAll")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.FINANCIAL_STAFF})
    public Response queryAllCompanyAccounts() {
        return Response.buildSuccess(companyAccountService.queryAllCompanyAccounts());
    }

    @PostMapping("/update")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.FINANCIAL_STAFF})
    public Response updateCompanyAccount(@RequestBody CompanyAccountVO companyAccountVO) {
        if(Objects.equals(companyAccountVO.getName(), "")){
            return Response.buildFailed("000001", "账户名不能为空");
        }else{
            companyAccountService.updateCompanyAccount(companyAccountVO);
            return Response.buildSuccess();
        }

    }

    @GetMapping("/delete")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.FINANCIAL_STAFF})
    public Response deleteCompanyAccount(@RequestParam Integer id) {
        companyAccountService.deleteCompanyAccount(id);
        return Response.buildSuccess();
    }

    @GetMapping("/queryByName")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.FINANCIAL_STAFF})
    public Response findCompanyAccountByName(@RequestParam String name) {
        return Response.buildSuccess(companyAccountService.findOneByName(name));
    }

}

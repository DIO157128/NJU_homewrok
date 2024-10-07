package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.vo.CreateProductVO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Console;


@RestController
@RequestMapping(path = "/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.SALE_STAFF, Role.SALE_MANAGER})
    public Response createCustomer(@RequestBody CustomerVO customerVO) {
        customerService.addCustomer(customerVO);
        return Response.buildSuccess();
    }

    @GetMapping("/delete")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.SALE_STAFF, Role.SALE_MANAGER})
    public Response deleteCustomer(@RequestParam String id) {
        customerService.deleteCustomer(Integer.valueOf(id));
        return Response.buildSuccess();
    }

    @PostMapping("/update")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.SALE_STAFF, Role.SALE_MANAGER})
    public Response updateCustomer(@RequestBody CustomerVO customerVO) {
        customerService.updateCustomer(customerVO);
        return Response.buildSuccess();
    }

    @GetMapping("/query-all")
    public Response findAllCustomers() {
        return Response.buildSuccess(customerService.queryAllCustomers());
    }

    @GetMapping("/query-by-type")
    public Response findByType(@RequestParam CustomerType type) {
        return Response.buildSuccess(customerService.getCustomersByType(type));
    }

    @GetMapping("/query-by-id")
    public Response findById(@RequestParam Integer id) {
        return Response.buildSuccess(customerService.findCustomerById(id));
    }


}

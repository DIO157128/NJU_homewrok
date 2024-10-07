package com.nju.edu.erp.web.controller.BusinessController;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.BusinessSituationVO;
import com.nju.edu.erp.model.vo.SaleDetailVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseCountingVO;
import com.nju.edu.erp.service.BusinessService.BusinessService;
import com.nju.edu.erp.utils.DownExcel;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/business")
public class BusinessController {

    private final BusinessService businessService;

    @Autowired
    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping("/sale-detail")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.FINANCIAL_STAFF})
    public Response getSaleDetail() {
        return Response.buildSuccess(businessService.getSaleDetail());
    }

    /**
     * 导出excel
     */
    @GetMapping("/sale-detail/excel")
    public void getExcelSaleDetail(HttpServletResponse response) throws IllegalAccessException, IOException,
            InstantiationException{
        List<SaleDetailVO> list = businessService.getSaleDetail();
        DownExcel.download(response,SaleDetailVO.class,list,"销售明细");
    }

    @GetMapping("/business-situation")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.FINANCIAL_STAFF})
    public Response getBusinessSituation(@RequestParam("fromDate")Date fromDate,
                                         @RequestParam("toDate") Date toDate) {
        return Response.buildSuccess(businessService.getBusinessSituation(fromDate,toDate));
    }

    @GetMapping("/business-history")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.FINANCIAL_STAFF})
    public Response getBusinessHistory() {
        return Response.buildSuccess(businessService.getBusinessHistory());
    }

}

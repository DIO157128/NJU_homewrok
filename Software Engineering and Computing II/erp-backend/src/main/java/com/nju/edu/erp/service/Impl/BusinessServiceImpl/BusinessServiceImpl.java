package com.nju.edu.erp.service.Impl.BusinessServiceImpl;

import com.nju.edu.erp.dao.*;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.SaleDetailPO;
import com.nju.edu.erp.model.po.pay.PaySheetPO;
import com.nju.edu.erp.model.po.receive.ReceiveSheetPO;
import com.nju.edu.erp.model.po.salary.SalarySheetPO;
import com.nju.edu.erp.model.vo.BusinessHistoryVO;
import com.nju.edu.erp.model.vo.BusinessSituationVO;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import com.nju.edu.erp.model.vo.SaleDetailVO;
import com.nju.edu.erp.service.*;
import com.nju.edu.erp.service.BusinessService.BusinessService;
import com.nju.edu.erp.service.FinanceService.PayService;
import com.nju.edu.erp.service.FinanceService.ReceiveService;
import com.nju.edu.erp.service.SalaryService.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BusinessServiceImpl implements BusinessService {


    private final SaleDetailDAO saleDetailDAO;
    private final ProductService productService;
    private final CustomerService customerService;
    private final SaleService saleService;
    private final SaleReturnsService saleReturnsService;
    private final PurchaseService purchaseService;
    private final PurchaseReturnsService purchaseReturnsService;
    private final PayService payService;
    private final ReceiveService receiveService;
    private final SalaryService salaryService;

    @Autowired
    public BusinessServiceImpl(SaleDetailDAO saleDetailDAO, ProductService productService, CustomerService customerService,
                               SaleService saleService, SaleReturnsService saleReturnsService, PurchaseService purchaseService,
                               PurchaseReturnsService purchaseReturnsService, PayService payService, ReceiveService receiveService,
                               SalaryService salaryService) {
        this.saleDetailDAO = saleDetailDAO;
        this.productService = productService;
        this.customerService = customerService;
        this.saleService = saleService;
        this.saleReturnsService = saleReturnsService;
        this.purchaseService = purchaseService;
        this.purchaseReturnsService = purchaseReturnsService;
        this.payService = payService;
        this.receiveService = receiveService;
        this.salaryService = salaryService;
    }

    @Override
    public List<SaleDetailVO> getSaleDetail() {
        List<SaleDetailPO> POs = saleDetailDAO.getSailDetail();
        List<SaleDetailVO> VOs = new ArrayList<>();
        for (SaleDetailPO po : POs) {
            SaleDetailVO vo = new SaleDetailVO();
            // 复制创建时间 单价 总价
            vo.setCreateTime(po.getCreateTime());
            vo.setTotal_price(po.getTotal_price());
            vo.setUnit_price(po.getUnit_price());
            vo.setQuantity(po.getQuantity());
            //获得商品名称，型号
            ProductInfoVO productInfoVO = productService.getOneProductByPid(po.getPid());
            vo.setPName(productInfoVO.getName());
            vo.setCategoryType(productInfoVO.getType());
            //获得顾客姓名
            CustomerPO customerPO = customerService.findCustomerById(Integer.valueOf(po.getSellerId()));
            vo.setSellerName(customerPO.getName());
            //加入结果中
            VOs.add(vo);
        }
        return VOs;
    }

    @Override
    public BusinessSituationVO getBusinessSituation(Date fromDate, Date toDate) {
        List<PaySheetPO> paySheetPOS = payService.findAllSheetByDate(fromDate, toDate);
        List<ReceiveSheetPO> receiveSheetPOS = receiveService.findAllSheetByDate(fromDate, toDate);
        List<SalarySheetPO> salarySheetPOS = salaryService.findAllSheetByDate(fromDate, toDate);
        BigDecimal saleRevenueRaw = BigDecimal.ZERO;
        BigDecimal saleRevenueDiscount = BigDecimal.ZERO;
        BigDecimal saleRevenueReal = BigDecimal.ZERO;
        BigDecimal sellingCost = BigDecimal.ZERO;
        BigDecimal labourCost = BigDecimal.ZERO;
        BigDecimal profit = BigDecimal.ZERO;
        for (PaySheetPO paySheetPO : paySheetPOS) {
            sellingCost = sellingCost.add(paySheetPO.getAllAmount());
        }
        for (ReceiveSheetPO receiveSheetPO : receiveSheetPOS) {
            saleRevenueRaw = saleRevenueRaw.add(receiveSheetPO.getAllAmount());
            saleRevenueDiscount = saleRevenueDiscount.add(receiveSheetPO.getDiscount());
            saleRevenueReal = saleRevenueReal.add(receiveSheetPO.getActualAmount());
        }
        for (SalarySheetPO salarySheetPO : salarySheetPOS) {
            labourCost = labourCost.add(salarySheetPO.getDueSalary());
        }
        profit = profit.add(saleRevenueReal);
        profit = profit.subtract(labourCost);
        profit = profit.subtract(sellingCost);
        BusinessSituationVO businessSituationVO = new BusinessSituationVO();
        businessSituationVO.setSaleRevenueRaw(saleRevenueRaw);
        businessSituationVO.setSaleRevenueDiscount(saleRevenueDiscount);
        businessSituationVO.setSaleRevenueReal(saleRevenueReal);
        businessSituationVO.setLabourCost(labourCost);
        businessSituationVO.setSellingCost(sellingCost);
        businessSituationVO.setProfit(profit);
        return businessSituationVO;
    }

    @Override
    public BusinessHistoryVO getBusinessHistory() {
        BusinessHistoryVO businessHistoryVO = new BusinessHistoryVO();
        //get all list
        businessHistoryVO.setSaleList(saleService.getSaleSheetByState(null));
        businessHistoryVO.setSaleReturnList(saleReturnsService.getSaleReturnsSheetByState(null));
        businessHistoryVO.setPurchaseList(purchaseService.getPurchaseSheetByState(null));
        businessHistoryVO.setPurchaseReturnList(purchaseReturnsService.getPurchaseReturnsSheetByState(null));
        businessHistoryVO.setPayList(payService.getPaySheetByState(null));
        businessHistoryVO.setReceiveList(receiveService.getReceiveSheetByState(null));
        businessHistoryVO.setSalaryList(salaryService.getSalarySheetByState(null));
        //return result
        return businessHistoryVO;
    }
}

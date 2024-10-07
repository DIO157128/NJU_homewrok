package com.nju.edu.erp.web.controller.SaleController;


import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.PurchaseSheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.CustomerPurchaseAmountPO;
import com.nju.edu.erp.model.po.PromotionStrategyOnAmountPO;
import com.nju.edu.erp.model.po.PromotionStrategyOnLevelPO;
import com.nju.edu.erp.model.vo.PromotionStrategyOnAmountVO;
import com.nju.edu.erp.model.vo.PromotionStrategyOnLevelVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.service.SaleService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/sale")
public class SaleController {

    private final SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    /**
     * 销售人员制定销售单
     */
    @Authorized (roles = {Role.SALE_STAFF, Role.SALE_MANAGER, Role.GM, Role.ADMIN})
    @PostMapping(value = "/sheet-make")
    public Response makePurchaseOrder(UserVO userVO, @RequestBody SaleSheetVO saleSheetVO)  {
        saleService.makeSaleSheet(userVO, saleSheetVO);
        return Response.buildSuccess();
    }

    /**
     * 根据状态查看销售单
     */
    @GetMapping(value = "/sheet-show")
    public Response showSheetByState(@RequestParam(value = "state", required = false) SaleSheetState state)  {
        return Response.buildSuccess(saleService.getSaleSheetByState(state));
    }

    /**
     * 销售经理审批
     * @param saleSheetId 进货单id
     * @param state 修改后的状态("审批失败"/"待二级审批")
     */
    @GetMapping(value = "/first-approval")
    @Authorized (roles = {Role.SALE_MANAGER, Role.ADMIN})
    public Response firstApproval(@RequestParam("saleSheetId") String saleSheetId,
                                  @RequestParam("state") SaleSheetState state)  {
        if(state.equals(SaleSheetState.FAILURE) || state.equals(SaleSheetState.PENDING_LEVEL_2)) {
            saleService.approval(saleSheetId, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
        }
    }

    /**
     * 总经理审批
     * @param saleSheetId 进货单id
     * @param state 修改后的状态("审批失败"/"审批完成")
     */
    @Authorized (roles = {Role.GM, Role.ADMIN})
    @GetMapping(value = "/second-approval")
    public Response secondApproval(@RequestParam("saleSheetId") String saleSheetId,
                                   @RequestParam("state") SaleSheetState state)  {
        if(state.equals(SaleSheetState.FAILURE) || state.equals(SaleSheetState.SUCCESS)) {
            saleService.approval(saleSheetId, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
        }
    }

    /**
     * 获取某个销售人员某段时间内消费总金额最大的客户(不考虑退货情况,销售单不需要审批通过,如果这样的客户有多个,仅保留一个)
     * @param salesman 销售人员的名字
     * @param beginDateStr 开始时间字符串 格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @param endDateStr 结束时间字符串 格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @return
     */
    @GetMapping("/maxAmountCustomer")
    @Authorized(roles = {Role.SALE_MANAGER,Role.GM, Role.ADMIN})
    public Response getMaxAmountCustomerOfSalesmanByTime(@RequestParam String salesman, @RequestParam String beginDateStr, @RequestParam String endDateStr){
        CustomerPurchaseAmountPO ans=saleService.getMaxAmountCustomerOfSalesmanByTime(salesman,beginDateStr,endDateStr);
        return Response.buildSuccess(ans);
    }

    /**
     * 根据销售单Id搜索销售单信息
     * @param id 销售单Id
     * @return 销售单全部信息
     */
    @GetMapping(value = "/find-sheet")
    public Response findBySheetId(@RequestParam(value = "id") String id)  {
        return Response.buildSuccess(saleService.getSaleSheetById(id));
    }

    /**
     * 制定关于等级的促销策略
     * @param promotionStrategyOnLevelVO
     * @return
     */
    @PostMapping(path = "/PromotionStrategy/OnLevel/create")
    @Authorized(roles = {Role.ADMIN,Role.GM})
    public Response makePromotionStrategyOnLevel(@RequestBody PromotionStrategyOnLevelVO promotionStrategyOnLevelVO){
        promotionStrategyOnLevelVO.setCreate_time(new Date());
        if(promotionStrategyOnLevelVO.getDiscount().compareTo(BigDecimal.ONE)==1){
            return Response.buildFailed("000000","折扣不能大于1");
        }else if(promotionStrategyOnLevelVO.getVoucher().compareTo(BigDecimal.ZERO)==-1){
            return Response.buildFailed("000001","代金券金额不能为负数");
        }else if(promotionStrategyOnLevelVO.getStart_time()==null){
            return Response.buildFailed("000003","开始时间不能为空");
        }else if(promotionStrategyOnLevelVO.getEnd_time()==null){
            return Response.buildFailed("000004","结束时间不能为空");
        }else if(promotionStrategyOnLevelVO.getStart_time().after(promotionStrategyOnLevelVO.getEnd_time())){
            return Response.buildFailed("000005","开始时间不能晚于结束时间");
        }else{
            saleService.makePromotionStrategyOnLevel(promotionStrategyOnLevelVO);
            return Response.buildSuccess();
        }
    }

    /**
     * 获得所有的关于等级的促销策略
     * @return
     */
    @GetMapping(path = "/PromotionStrategy/OnLevel/getall")
    @Authorized(roles = {Role.ADMIN,Role.GM})
    public Response getAllPromotionStrategyOnLevel(){
        List<PromotionStrategyOnLevelVO> vo=saleService.getAllPromotionStrategyOnLevel();
        return Response.buildSuccess(vo);
    }

    /**
     * 更新关于等级的促销策略
     * @param promotionStrategyOnLevelVO
     * @return
     */
    @PostMapping(path = "/PromotionStrategy/OnLevel/update")
    @Authorized(roles = {Role.ADMIN,Role.GM})
    public Response updatePromotionStrategyOnLevel(@RequestBody PromotionStrategyOnLevelVO promotionStrategyOnLevelVO){
        promotionStrategyOnLevelVO.setCreate_time(new Date());
        if(promotionStrategyOnLevelVO.getDiscount().compareTo(BigDecimal.ONE)==1){
            return Response.buildFailed("000000","折扣不能大于1");
        }else if(promotionStrategyOnLevelVO.getVoucher().compareTo(BigDecimal.ZERO)==-1){
            return Response.buildFailed("000001","金额不能为负数");
        }else if(promotionStrategyOnLevelVO.getStart_time()==null){
            return Response.buildFailed("000003","开始时间不能为空");
        }else if(promotionStrategyOnLevelVO.getEnd_time()==null){
            return Response.buildFailed("000004","结束时间不能为空");
        }else if(promotionStrategyOnLevelVO.getStart_time().after(promotionStrategyOnLevelVO.getEnd_time())){
            return Response.buildFailed("000005","开始时间不能晚于结束时间");
        }else {
            saleService.updatePromotionStrategyOnLevel(promotionStrategyOnLevelVO);
            return Response.buildSuccess();
        }
    }

    /**
     * 删除关于等级的促销策略
     * @param id
     * @return
     */
    @GetMapping(path = "/PromotionStrategy/OnLevel/delete")
    @Authorized(roles = {Role.ADMIN,Role.GM})
    public Response deletePromotionStrategyOnLevel(@RequestParam Integer id){
        saleService.deletePromotionStrategyOnLevel(id);
        return Response.buildSuccess();
    }

    /**
     * 制定关于总价的促销策略
     * @param promotionStrategyOnAmountVO
     * @return
     */
    @PostMapping(path = "/PromotionStrategy/OnAmount/create")
    @Authorized(roles = {Role.ADMIN,Role.GM})
    public Response makePromotionStrategyOnAmount(@RequestBody PromotionStrategyOnAmountVO promotionStrategyOnAmountVO){
        promotionStrategyOnAmountVO.setCreate_time(new Date());
        if(promotionStrategyOnAmountVO.getVoucher().compareTo(BigDecimal.ZERO)<0){
            return Response.buildFailed("000001","金额不能为负数");
        }else if(promotionStrategyOnAmountVO.getMax_amount()<0){
            return Response.buildFailed("000001","金额不能为负数");
        }else if(promotionStrategyOnAmountVO.getDiscount().compareTo(BigDecimal.ONE)>0){
            return Response.buildFailed("000000","折扣不能大于1");
        }else if(promotionStrategyOnAmountVO.getMin_amount()> promotionStrategyOnAmountVO.getMax_amount()){
            return Response.buildFailed("000002","最小值不能大于最大值");
        }else if(promotionStrategyOnAmountVO.getMin_amount()<0){
            return Response.buildFailed("000001","金额不能为负数");
        }else if(promotionStrategyOnAmountVO.getStart_time()==null){
            return Response.buildFailed("000003","结束时间不能为空");
        }else if(promotionStrategyOnAmountVO.getEnd_time()==null){
            return Response.buildFailed("000004","结束时间不能为空");
        }else if(promotionStrategyOnAmountVO.getStart_time().after(promotionStrategyOnAmountVO.getEnd_time())){
            return Response.buildFailed("000005","开始时间不能晚于结束时间");
        } else {
            saleService.makePromotionStrategyOnAmount(promotionStrategyOnAmountVO);
            return Response.buildSuccess();
        }

    }

    /**
     * 获得所有的关于总价的促销策略
     * @return
     */
    @GetMapping(path = "/PromotionStrategy/OnAmount/getall")
    @Authorized(roles = {Role.ADMIN,Role.GM})
    public Response getAllPromotionStrategyOnAmount(){
        return Response.buildSuccess(saleService.getAllPromotionStrategyOnAmount());
    }

    /**
     * 更新关于总价的促销策略
     * @param promotionStrategyOnAmountVO
     * @return
     */
    @PostMapping(path = "/PromotionStrategy/OnAmount/update")
    @Authorized(roles = {Role.ADMIN,Role.GM})
    public Response updatePromotionStrategyOnAmount(@RequestBody PromotionStrategyOnAmountVO promotionStrategyOnAmountVO){
        promotionStrategyOnAmountVO.setCreate_time(new Date());
        if(promotionStrategyOnAmountVO.getDiscount().compareTo(BigDecimal.ONE)>0){
            return Response.buildFailed("000000","折扣不能大于1");
        }else if(promotionStrategyOnAmountVO.getVoucher().compareTo(BigDecimal.ZERO)<0){
            return Response.buildFailed("000001","金额不能为负数");
        }else if(promotionStrategyOnAmountVO.getMax_amount()<0){
            return Response.buildFailed("000001","金额不能为负数");
        }else if(promotionStrategyOnAmountVO.getMin_amount()<0){
            return Response.buildFailed("000001","金额不能为负数");
        }else if(promotionStrategyOnAmountVO.getMin_amount()> promotionStrategyOnAmountVO.getMax_amount()){
            return Response.buildFailed("000002","最小值不能大于最大值");
        }else if(promotionStrategyOnAmountVO.getStart_time()==null){
            return Response.buildFailed("000003","结束时间不能为空");
        }else if(promotionStrategyOnAmountVO.getEnd_time()==null){
            return Response.buildFailed("000004","结束时间不能为空");
        }else if(promotionStrategyOnAmountVO.getStart_time().after(promotionStrategyOnAmountVO.getEnd_time())){
            return Response.buildFailed("000005","开始时间不能晚于结束时间");
        } else {
            return Response.buildSuccess(saleService.updatePromotionStrategyOnAmount(promotionStrategyOnAmountVO));
        }
    }

    /**
     * 删除关于总价的促销策略
     * @param id
     * @return
     */
    @GetMapping(path = "/PromotionStrategy/OnAmount/delete")
    @Authorized(roles = {Role.ADMIN,Role.GM})
    public Response deletePromotionStrategyOnAmount(@RequestParam Integer id){
        saleService.deletePromotionStrategyOnAmount(id);
        return Response.buildSuccess();
    }


}

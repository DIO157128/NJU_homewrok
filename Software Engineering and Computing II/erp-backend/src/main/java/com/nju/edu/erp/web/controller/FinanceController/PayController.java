package com.nju.edu.erp.web.controller.FinanceController;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.PaySheetState;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.pay.PaySheetVO;
import com.nju.edu.erp.service.FinanceService.PayService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;

@RestController
@RequestMapping(path = "/pay")
public class PayController {
    private final PayService payService;

    @Autowired
    public PayController(PayService payService) {
        this.payService = payService;
    }

    /**
     * 财务人员制定付款单
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.GM, Role.ADMIN})
    @PostMapping(value = "/sheet-make")
    public Response makePayOrder(UserVO userVO, @RequestBody PaySheetVO paySheetVO) {
        if(paySheetVO.getAllAmount()==null){
            return Response.buildFailed("000000", "应付金额不能为空");
        }else if (paySheetVO.getAllAmount().compareTo(BigDecimal.ZERO) < 0) {
            return Response.buildFailed("000000", "应付金额不能为负数");
        } else if (Objects.equals(paySheetVO.getBankAccount(), "")) {
            return Response.buildFailed("000001", "账户不能为空");
        } else if (paySheetVO.getPayerId() == null) {
            return Response.buildFailed("000002", "客户不能为空");
        } else {
            payService.makePaySheet(userVO, paySheetVO);
            return Response.buildSuccess();
        }

    }

    /**
     * 根据状态查看付款单
     */
    @GetMapping(value = "/sheet-show")
    public Response showSheetByState(@RequestParam(value = "state", required = false) PaySheetState state) {
        return Response.buildSuccess(payService.getPaySheetByState(state));
    }

    /**
     * 总经理审批
     */
    @GetMapping(value = "/approval")
    @Authorized(roles = {Role.GM, Role.ADMIN})
    public Response approval(@RequestParam("paySheetId") String paySheetId,
                             @RequestParam("state") PaySheetState state) {
        if (state.equals(PaySheetState.FAILURE) || state.equals(PaySheetState.SUCCESS)) {
            try {
                payService.approval(paySheetId, state);
            } catch (RuntimeException e) {
                return Response.buildFailed("000000", "账户余额不足");
            }
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000", "操作失败"); // code可能得改一个其他的
        }
    }


    /**
     * 根据付款单Id搜索付款单信息
     *
     * @param id 付款单Id
     * @return 付款单全部信息
     */
    @GetMapping(value = "/find-sheet")
    public Response findBySheetId(@RequestParam(value = "id") String id) {
        return Response.buildSuccess(payService.getPaySheetById(id));
    }

}

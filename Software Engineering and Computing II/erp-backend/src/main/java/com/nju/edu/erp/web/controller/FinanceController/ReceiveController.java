package com.nju.edu.erp.web.controller.FinanceController;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.ReceiveSheetState;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetVO;
import com.nju.edu.erp.service.FinanceService.ReceiveService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;

@RestController
@RequestMapping(path = "/receive")
public class ReceiveController {
    private final ReceiveService receiveService;

    @Autowired
    public ReceiveController(ReceiveService receiveService) {
        this.receiveService = receiveService;
    }

    /**
     * 财务人员制定收款单
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.GM, Role.ADMIN})
    @PostMapping(value = "/sheet-make")
    public Response makeReceiveOrder(UserVO userVO, @RequestBody ReceiveSheetVO receiveSheetVO) {
        if (receiveSheetVO.getActualAmount() == null
                || receiveSheetVO.getAllAmount() == null
                || receiveSheetVO.getDiscount() == null) {
            return Response.buildFailed("000000", "金额不能为空");
        } else if (receiveSheetVO.getActualAmount().compareTo(BigDecimal.ZERO) < 0
                || receiveSheetVO.getAllAmount().compareTo(BigDecimal.ZERO) < 0
                || receiveSheetVO.getDiscount().compareTo(BigDecimal.ZERO) < 0) {
            return Response.buildFailed("000001", "金额不能为负数");
        } else if (Objects.equals(receiveSheetVO.getCustomerName(), "")
                || Objects.equals(receiveSheetVO.getCustomerType(), "")) {
            return Response.buildFailed("000002", "客户不能为空");
        } else {
            receiveService.makeReceiveSheet(userVO, receiveSheetVO);
            return Response.buildSuccess();
        }
    }

    /**
     * 根据状态查看收款单
     */
    @GetMapping(value = "/sheet-show")
    public Response showSheetByState(@RequestParam(value = "state", required = false) ReceiveSheetState state) {
        return Response.buildSuccess(receiveService.getReceiveSheetByState(state));
    }

    /**
     * 总经理审批
     */
    @GetMapping(value = "/approval")
    @Authorized(roles = {Role.GM, Role.ADMIN})
    public Response approval(@RequestParam("receiveSheetId") String receiveSheetId,
                             @RequestParam("state") ReceiveSheetState state) {
        if (state.equals(ReceiveSheetState.FAILURE) || state.equals(ReceiveSheetState.SUCCESS)) {
            receiveService.approval(receiveSheetId, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000", "操作失败"); // code可能得改一个其他的
        }
    }


    /**
     * 根据收款单Id搜索收款单信息
     *
     * @param id 收款单Id
     * @return 收款单全部信息
     */
    @GetMapping(value = "/find-sheet")
    public Response findBySheetId(@RequestParam(value = "id") String id) {
        return Response.buildSuccess(receiveService.getReceiveSheetById(id));
    }
}

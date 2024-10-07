package com.nju.edu.erp.web.controller.SalaryController;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.salary.SalarySheetVO;
import com.nju.edu.erp.service.SalaryService.SalaryService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/salary")
public class SalaryController {
    private final SalaryService salaryService;

    @Autowired
    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    /**
     *      人力资源人员制定工资单,仅知道谁制定的单据即可
     */
    @Authorized(roles = {Role.HR, Role.GM, Role.ADMIN})
    @GetMapping(value = "/sheet-make")
    public Response makeSalary(UserVO userVO)  {
        List<SalarySheetVO> salarySheetVOS = salaryService.generateSalary();                            //生成工资条目
        if(salarySheetVOS.size()==0) return Response.buildFailed("D0001","已生成本月工资");     //如果是12月，则可能确实没生成本月工资，因为总经理尚未生成年终奖
        for (SalarySheetVO salarySheetVO : salarySheetVOS) {
            salaryService.makeSalarySheet(userVO, salarySheetVO,SalarySheetState.PENDING_LEVEL_1);       //存入工资单
        }
        return Response.buildSuccess();
    }

    @Authorized(roles = {Role.HR, Role.GM, Role.ADMIN})
    @GetMapping(value = "/annual")
    public Response makeAnnual(UserVO userVO)  {
        List<SalarySheetVO> salarySheetVOS = salaryService.generateAnnual();                            //生成年终奖
        if(salarySheetVOS.size()==0) return Response.buildFailed("D0002","已生成年终奖");
        for (SalarySheetVO salarySheetVO : salarySheetVOS) {
            salaryService.makeSalarySheet(userVO, salarySheetVO,SalarySheetState.SUCCESS);               //存入工资单
        }
        return Response.buildSuccess();
    }



    /**
     * 根据状态查看工资单
     */
    @GetMapping(value = "/sheet-show")
    public Response showSheetByState(@RequestParam(value = "state", required = false) SalarySheetState state)  {
        return Response.buildSuccess(salaryService.getSalarySheetByState(state));
    }


    /**
     * HR审批
     * @param salarySheetId 工作单id
     * @param state 修改后的状态("审批失败"/"待二级审批")
     */
    @GetMapping(value = "/first-approval")
    @Authorized (roles = {Role.HR, Role.ADMIN})
    public Response firstApproval(@RequestParam("salarySheetId") String salarySheetId,
                                  @RequestParam("state") SalarySheetState state)  {
        if(state.equals(SalarySheetState.FAILURE) || state.equals(SalarySheetState.PENDING_LEVEL_2)) {
            salaryService.approval(salarySheetId, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
        }
    }

    /**
     * 总经理审批
     * @param salarySheetId 工资单id
     * @param state 修改后的状态("审批失败"/"审批完成")
     */
    @Authorized (roles = {Role.GM, Role.ADMIN})
    @GetMapping(value = "/second-approval")
    public Response secondApproval(@RequestParam("salarySheetId") String salarySheetId,
                                   @RequestParam("state") SalarySheetState state)  {
        if(state.equals(SalarySheetState.FAILURE) || state.equals(SalarySheetState.SUCCESS)) {
            salaryService.approval(salarySheetId, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
        }
    }

    /**
     * 根据工资单Id搜索工资单信息
     * @param id 工资单Id
     * @return 工资单全部信息
     */
    @GetMapping(value = "/find-sheet")
    public Response findBySheetId(@RequestParam(value = "id") String id)  {
        return Response.buildSuccess(salaryService.getSalarySheetById(id));
    }
}

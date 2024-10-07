package com.nju.edu.erp.service.SalaryStrategy;

import com.nju.edu.erp.model.po.StaffPO;
import com.nju.edu.erp.service.SaleService;
import com.nju.edu.erp.service.StaffService.PunchInService;
import com.nju.edu.erp.service.StaffService.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CalStrategyContext {

    private final StaffService staffService;

    private final PunchInService punchInService;

    private final SaleService saleService;

    @Autowired
    public CalStrategyContext(StaffService staffService, PunchInService punchInService, SaleService saleService) {
        this.staffService = staffService;
        this.punchInService = punchInService;
        this.saleService = saleService;
    }

    /**
     * 工资
     * @param staffPO
     * @return
     */
    public List<BigDecimal> generateSalary(StaffPO staffPO){
        CalStrategy strategy;
        String calSalaryMethod = staffService.findCalSalaryMethod(staffPO.getId());
        if(calSalaryMethod.equals("MONTHLY_SALARY")) strategy=new MONTHLY_SAL(staffService,punchInService);
        else if(calSalaryMethod.equals("PERCENTAGE")) strategy=new PERCENTAGE_SAL(staffService,punchInService,saleService);
        else strategy=new YEARLY_SAL(staffService);
        return strategy.getPay(staffPO);
    }

    /**
     * 年终奖
     * @param staffPO
     * @return
     */
    public List<BigDecimal> generateAnnual(StaffPO staffPO){
        CalStrategy strategy;
        String calSalaryMethod = staffService.findCalSalaryMethod(staffPO.getId());
        if(calSalaryMethod.equals("MONTHLY_SALARY")) strategy=new MONTHLY_ANN(staffService,punchInService);
        else if(calSalaryMethod.equals("PERCENTAGE")) strategy=new PERCENTAGE_ANN(staffService,punchInService,saleService);
        else strategy=new YEARLY_ANN(staffService);
        return strategy.getPay(staffPO);
    }
}

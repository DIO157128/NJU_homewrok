package com.nju.edu.erp.service.SalaryStrategy;

import com.nju.edu.erp.model.po.StaffPO;
import com.nju.edu.erp.service.StaffService.PunchInService;
import com.nju.edu.erp.service.StaffService.StaffService;
import com.nju.edu.erp.utils.TimeUtils;
import com.nju.edu.erp.utils.TaxComputing;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MONTHLY_SAL implements CalStrategy {

    private final StaffService staffService;

    private final PunchInService punchInService;

    private final double DEDUCT =  TimeUtils.getDaysNumOfThisMonth();            //扣除工资比例

    @Autowired
    public MONTHLY_SAL(StaffService staffService, PunchInService punchInService) {
        this.staffService = staffService;
        this.punchInService = punchInService;
    }


    @Override
    public List<BigDecimal> getPay(StaffPO staffPO) {

        List<Date> dates = TimeUtils.getFirstAndLastDayOfMonth();
        List<BigDecimal> ret=new ArrayList<>();
        BigDecimal dueSalary;
        BigDecimal tax;
        BigDecimal actualSalary;

        //1. 基本工资
        int basicSalary = staffService.findBasicSalary(staffPO.getId());

        //2. 跳过提成
        //3. 扣除缺勤
        int punchInTimes = punchInService.countByIdAndPeriod(staffPO.getId(), dates.get(0), dates.get(1));
        int absent = TimeUtils.getDaysNumOfThisMonth()-punchInTimes;
        dueSalary = BigDecimal.valueOf(basicSalary* (1 - absent / DEDUCT));         //除以天数

        //4. 计算税款
        double _tax = TaxComputing.compute(dueSalary.doubleValue());
        tax = BigDecimal.valueOf(_tax);

        //5. 税后工资
        actualSalary=dueSalary.subtract(tax);

        ret.add(dueSalary);
        ret.add(tax);
        ret.add(actualSalary);
        return ret;
    }
}

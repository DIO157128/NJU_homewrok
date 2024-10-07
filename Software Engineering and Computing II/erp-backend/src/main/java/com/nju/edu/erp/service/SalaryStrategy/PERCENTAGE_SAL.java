package com.nju.edu.erp.service.SalaryStrategy;

import com.nju.edu.erp.model.po.StaffPO;
import com.nju.edu.erp.service.SaleService;
import com.nju.edu.erp.service.StaffService.PunchInService;
import com.nju.edu.erp.service.StaffService.StaffService;
import com.nju.edu.erp.utils.TimeUtils;
import com.nju.edu.erp.utils.TaxComputing;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PERCENTAGE_SAL implements CalStrategy {

    private final StaffService staffService;

    private final PunchInService punchInService;

    private final SaleService saleService;

    private final double DEDUCT =  TimeUtils.getDaysNumOfThisMonth();            //扣除工资比例

    private final int EachTranX = 200;              //每次交易的bonus

    @Autowired
    public PERCENTAGE_SAL(StaffService staffService, PunchInService punchInService, SaleService saleService) {

        this.staffService = staffService;
        this.punchInService = punchInService;
        this.saleService = saleService;
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

        //2. 提成
        int tranX = saleService.findTransXByNameAndPeriod(staffPO.getName(), dates.get(0), dates.get(1));  //已测试此方法
        double Addition = tranX * EachTranX;

        //3. 扣除缺勤
        int punchInTimes = punchInService.countByIdAndPeriod(staffPO.getId(), dates.get(0), dates.get(1));
        int absent = TimeUtils.getDaysNumOfThisMonth()-punchInTimes;
        dueSalary = BigDecimal.valueOf((Addition+basicSalary)* (1 - absent / DEDUCT));

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

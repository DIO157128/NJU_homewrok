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
import java.util.Vector;

public class PERCENTAGE_ANN implements CalStrategy {

    private final StaffService staffService;

    private final PunchInService punchInService;

    private final SaleService saleService;

    private final double DAYS =  365;            //扣除工资比例

    private final double EachTranX = 0.00001;    //交易额达到一万即奖励一元

    @Autowired
    public PERCENTAGE_ANN(StaffService staffService, PunchInService punchInService, SaleService saleService) {

        this.staffService = staffService;
        this.punchInService = punchInService;
        this.saleService = saleService;
    }

    @Override
    public List<BigDecimal> getPay(StaffPO staffPO) {
        List<Date> dates = TimeUtils.getFirstAndLastDayOfYear();
        List<BigDecimal> ret=new ArrayList<>();
        BigDecimal dueSalary;
        BigDecimal tax;
        BigDecimal actualSalary;

        //1. 基本工资
        int basicSalary = staffService.findBasicSalary(staffPO.getId());

        //2. 年终奖提成
        System.out.println(staffPO.getName());
        double tranX = saleService.findVolumeOfTrade(staffPO.getName(), dates.get(0), dates.get(1));  //已测试此方法
        double Addition = tranX * EachTranX;

        //3. 奖励签到
        int punchInTimes = punchInService.countByIdAndPeriod(staffPO.getId(), dates.get(0), dates.get(1));
        dueSalary = BigDecimal.valueOf((Addition+basicSalary)* (1 + punchInTimes / DAYS));

        //4. 计算税款
        Vector<Double> vec = TaxComputing.computeNoTrap(dueSalary.doubleValue());
        dueSalary=BigDecimal.valueOf(vec.get(0));               //  如果是上一个节点税后工资更高，则将税前工资也要转为上一个节点的税前工资
        tax = BigDecimal.valueOf(vec.get(1));

        //5. 税后工资
        actualSalary=dueSalary.subtract(tax);

        ret.add(dueSalary);
        ret.add(tax);
        ret.add(actualSalary);
        return ret;
    }
}

package com.nju.edu.erp.service.SalaryStrategy;

import com.nju.edu.erp.model.po.StaffPO;
import com.nju.edu.erp.service.StaffService.StaffService;
import com.nju.edu.erp.utils.TaxComputing;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class YEARLY_SAL implements CalStrategy {

    private final StaffService staffService;

    private final long NUM_OF_MONTHS=12;

    @Autowired
    public YEARLY_SAL(StaffService staffService) {
        this.staffService = staffService;
    }


    @Override
    public List<BigDecimal> getPay(StaffPO staffPO) {

        List<BigDecimal> ret=new ArrayList<>();
        BigDecimal dueSalary;
        BigDecimal tax;
        BigDecimal actualSalary;

        //1. 基本工资，这是全年工资
        int basicSalary = staffService.findBasicSalary(staffPO.getId());
        dueSalary = BigDecimal.valueOf(basicSalary*NUM_OF_MONTHS);

        //2. 跳过提成
        //3. 跳过缺勤

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

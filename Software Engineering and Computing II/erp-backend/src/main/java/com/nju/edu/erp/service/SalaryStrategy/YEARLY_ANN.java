package com.nju.edu.erp.service.SalaryStrategy;

import com.nju.edu.erp.model.po.StaffPO;
import com.nju.edu.erp.service.StaffService.StaffService;
import com.nju.edu.erp.utils.TaxComputing;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class YEARLY_ANN implements CalStrategy {

    private final StaffService staffService;

    private final long NUM_OF_ANN = 2;      //直接给两个月的年终奖

    @Autowired
    public YEARLY_ANN(StaffService staffService) {
        this.staffService = staffService;
    }


    @Override
    public List<BigDecimal> getPay(StaffPO staffPO) {

        List<BigDecimal> ret = new ArrayList<>();
        BigDecimal dueSalary;
        BigDecimal tax;
        BigDecimal actualSalary;

        //1. 基本工资，这是全年工资
        int basicSalary = staffService.findBasicSalary(staffPO.getId());
        dueSalary = BigDecimal.valueOf(basicSalary * NUM_OF_ANN);


        //4. 计算税款
        Vector<Double> vec = TaxComputing.computeNoTrap(dueSalary.doubleValue());
        dueSalary=BigDecimal.valueOf(vec.get(0));               //  如果是上一个节点税后工资更高，则将税前工资也要转为上一个节点的税前工资
        tax = BigDecimal.valueOf(vec.get(1));

        //5. 税后工资
        actualSalary = dueSalary.subtract(tax);

        ret.add(dueSalary);
        ret.add(tax);
        ret.add(actualSalary);
        return ret;
    }
}

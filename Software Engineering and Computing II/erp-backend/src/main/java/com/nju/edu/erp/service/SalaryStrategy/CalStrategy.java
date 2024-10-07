package com.nju.edu.erp.service.SalaryStrategy;

import com.nju.edu.erp.model.po.StaffPO;

import java.math.BigDecimal;
import java.util.List;

public interface CalStrategy {
    /**
     * 返回的第一个为基本工资，第二个为个税，第三个为税后工资
     * @param
     * @return
     */
    public List<BigDecimal> getPay(StaffPO staffPO);
}

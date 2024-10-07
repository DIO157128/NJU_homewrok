package com.nju.edu.erp.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostVO {


    /**
     * 岗位id
     */
    private int id;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 基本工资
     */
    private int basicSalary;

    /**
     * 岗位工资
     */
    private int postSalary;

    /**
     * 等级
     */
    private int level;

    /**
     * 薪资计算方式
     */
    private String calSalaryMethod;

    /**
     * 薪资发放方式
     */
    private String paySalaryMethod;

    /**
     * 扣税信息
     */
    private String taxDeductionRemark;


}

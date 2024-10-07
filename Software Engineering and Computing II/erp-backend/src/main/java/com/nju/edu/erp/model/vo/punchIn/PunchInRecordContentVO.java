package com.nju.edu.erp.model.vo.punchIn;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PunchInRecordContentVO {

    //如果显示具体哪次的话应该是PunchInRecordContentVO

    /**
     * 员工id
     */
    private int staffId;


    /**
     *  员工姓名
     */
    private String staffName;


    /**
     * 次数
     */
    private Date punchInTime;



}

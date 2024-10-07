package com.nju.edu.erp.model.po.punchIn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PunchInRecordContentPO {

    //打卡记录表


    /**
     * 员工id
     */
    private int staffId;


    /**
     * 员工姓名
     */
    private String staffName;


    /**
     * 打卡的时间
     */
    private Date punchInTime;


}

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
public class PunchInRecordPO {

    //打卡条目记录表


    /**
     * 员工id
     */
    private int staffId;


    /**
     * 员工姓名
     */
    private String staffName;


    /**
     * 打卡次数
     */
    private int times;


}

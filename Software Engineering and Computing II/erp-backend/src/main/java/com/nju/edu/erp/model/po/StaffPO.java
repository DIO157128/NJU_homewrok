package com.nju.edu.erp.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffPO {

    /**
     * id
     */
    private int id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private int gender;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 电话号码
     */
    private String phoneNum;

    /**
     * 岗位id
     */
    private int postId;

    /**
     * 卡账号id
     */
    private String cardAccountId;


}
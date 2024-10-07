package com.nju.edu.erp.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckFormat {

    /**
     * 检测是否合法的电话号码
     *
     * @param phoneNum
     * @return
     */
    public static boolean checkIfPhone(String phoneNum) {

        String regix = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
        Pattern pattern = Pattern.compile(regix);
        Matcher matcher = pattern.matcher(phoneNum);
        return matcher.matches();

    }

    public static boolean checkGender(int gender) {
        return gender == 0 || gender == 1;
    }

    public static boolean checkSalary(int salary){
        return salary>=0;
    }

    public static boolean checkLevel(int level){
        return level>=1&&level<=30;
    }

    public static boolean checkPostName(String postName){
        List<String> post=new ArrayList<>();
        post.add("INVENTORY_MANAGER");
        post.add("SALE_MANAGER");
        post.add("SALE_STAFF");
        post.add("GM");
        post.add("HR");
        return post.contains(postName);
    }

    public static boolean checkCalSalaryMethod(String method){
        List<String> methods=new ArrayList<>();
        methods.add("MONTHLY_SALARY");
        methods.add("YEARLY_SALARY");
        methods.add("PERCENTAGE");
        return methods.contains(method);
    }

}

package com.nju.edu.erp.utils;


import java.util.*;

public class TimeUtils {
    /**
     *  获取本月的第一天和最后一天
     * @return
     */
    public static List<Date> getFirstAndLastDayOfMonth(){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MONTH, 0);
        calendar1.set(Calendar.DAY_OF_MONTH, 1);
        Date t1 = calendar1.getTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MONTH, 1);
        calendar2.set(Calendar.DAY_OF_MONTH, 0);
        Date t2 = calendar2.getTime();
        List<Date> l=new ArrayList<>();
        l.add(t1);
        l.add(t2);
        return l;
    }

    public static int getYear(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }
    // 缺个获取当前年份天数的方法

    public static int getMonth(){
        Calendar calendar=Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 本年的第一天和最后一天
     * @return
     */
    public static List<Date> getFirstAndLastDayOfYear(){

        Calendar calendar1 = Calendar.getInstance();
        int yearVal = calendar1.get(Calendar.YEAR);
        calendar1.clear();
        calendar1.set(Calendar.YEAR, --yearVal);
        Date t1 = calendar1.getTime();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.YEAR, ++yearVal);
        calendar2.set(Calendar.DAY_OF_MONTH, 0);
        Date t2 = calendar2.getTime();

        List<Date> l=new ArrayList<>();
        l.add(t1);
        l.add(t2);

        return l;
    }

    /**
     * 获取本月的天数，用于计算缺勤次数
     * @return
     */
    public static int getDaysNumOfThisMonth(){
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        return calendar.getActualMaximum(Calendar.DATE);
    }

}

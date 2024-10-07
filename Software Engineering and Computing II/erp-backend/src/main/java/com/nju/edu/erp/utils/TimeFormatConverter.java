package com.nju.edu.erp.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatConverter {
    public static Date timeTransfer(String DateStr){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return format.parse(DateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String timeTrans(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public static boolean checkTimeError(String beginDateStr,String endDateStr){
        if(beginDateStr.compareTo(endDateStr)>0) return true;
        return false;
    }
}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   Utilities.java

package uk.ac.wlv.wkaclass;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utilities {

    private static final long THOUSAND = 1000L;
    /**
     * 不带参的初始化.
     */
    public Utilities() {
    }

    /**
     * 打印路径.
     */
    public static void printClasspath() {
        ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader) sysClassLoader).getURLs();
        for (int i = 0; i < urls.length; i++) {
            System.out.println(urls[i].getFile());
        }

    }

    /**
     * 添加路径.
     *
     * @param s
     * @throws Exception
     */
    public static void addToClassPath(String s)
            throws Exception {
        File f = new File(s);
        URL u = f.toURL();
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class urlClass = URLClassLoader.class;
        Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
        method.setAccessible(true);
        method.invoke(urlClassLoader, new Object[]{
                u
        });
    }

    /**
     * 输出警告和名称+换行.
     *
     * @param methodName
     */
    public static void printlnNameAndWarning(String methodName) {
        System.out.println((new StringBuilder("Starting ")).append(methodName).append(" ").append(now()).append(" ... ").toString());
    }

    /**
     * 输出警告和名称.
     *
     * @param methodName
     */
    public static void printNameAndWarning(String methodName) {
        System.out.print((new StringBuilder("Starting ")).append(methodName).append(" ").append(now()).append(" ... ").toString());
    }

    /**
     * 返回现在的时间.
     *
     * @return 符合format的时间
     */
    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    /**
     * 返回现在的时间.
     *
     * @return 时间
     */
    public static Date getNow() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    /**
     * 返回时间间隔.
     *
     * @param firstDate
     * @param secondDate
     * @return timeGapReport
     */
    public static String timeGap(Date firstDate, Date secondDate) {
        Calendar calFirst = Calendar.getInstance();
        Calendar calSecond = Calendar.getInstance();
        calFirst.setTime(firstDate);
        calSecond.setTime(secondDate);
        long firstMilis = calFirst.getTimeInMillis();
        long secondMilis = calSecond.getTimeInMillis();
        String timeGapReport = (new StringBuilder(String.valueOf(Long.toString((secondMilis - firstMilis) / THOUSAND)))).append(" secs").toString();
        return timeGapReport;
    }
}

package com.nju.edu.erp.utils;

import com.alibaba.excel.EasyExcel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DownExcel {
    public static void download(HttpServletResponse response,Class t,List list, String name) throws IOException,IllegalAccessException,InstantiationException{
        response.setContentType("application/vnd.ms-excel");//设置文本类型
        response.setCharacterEncoding("utf-8");//设置字符编码
        response.setHeader("Content-disposition","attachment;filename=demo.xlsx");//设置响应头
        EasyExcel.write(response.getOutputStream(),t).sheet(name).doWrite(list);//用IO流来写入数据
    }
}

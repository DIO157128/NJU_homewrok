package com.example.sentistrengthbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.wlv.sentistrength.SentiStrength;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

@Service
public class SentiService {
    SentiStrength sentiStrength;
    private static final String inputPath = "/home/sentiStrength_backend/resources/input/";
    private static final String sentiDataPath = "/home/sentiStrength_backend/resources/sentidata/";
    private static final String outputPath = "/home/sentiStrength_backend/resources/output/";
    private String fileName;

    public String runWithText(String args) {
        sentiStrength = new SentiStrength();
        args = "sentidata " + sentiDataPath + args;
        String[] argsList = args.split(" ");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        System.setOut(ps);
        sentiStrength.initialiseAndRun(argsList);
        return byteArrayOutputStream.toString();
    }

    public String runWithFile(String args) {
        sentiStrength = new SentiStrength();
        args = "sentidata " + sentiDataPath + " input " + inputPath + fileName + args + " outputfolder " + outputPath;
        String[] argsList = args.split(" ");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        System.setOut(ps);
        sentiStrength.initialiseAndRun(argsList);
        String path = byteArrayOutputStream.toString();
        // 输出文件路径
        path = path.substring(path.indexOf(":") + 2);
        path = path.trim();
        return getFile(path);
    }

    public void fileUploads(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {

        sentiStrength = new SentiStrength();
        // 获取上传的文件名称
        String fileName = file.getOriginalFilename();
        this.fileName = fileName;
        // 得到文件保存的位置以及新文件名
        File dest = new File(inputPath + fileName);
        try {
            file.transferTo(dest);
            System.out.println("Upload success! File saved in " + inputPath + fileName);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public String getFile(String path) {
        String jsonString = "";
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), "UTF-8"));// 读取文件
            String thisLine;
            while ((thisLine = in.readLine()) != null) {
                jsonString += (thisLine + "\n");
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("读取文件失败！");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException el) {
                    System.out.println(el);
                }
            }
        }
        return jsonString;
    }
}

package uk.ac.wlv.utilities;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class ReportAnalysis {
    /**
     * 对进行checkstyle报告进行分析.
     *
     * @param args
     */
    public static void main(String args[]) {
        try {
            //创建DOM文档对象
            DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dFactory.newDocumentBuilder();
            Document doc1;
            Document doc2;
            doc1 = builder.parse(new File("项目代码分析//分析报告//checkstyle//checkstyle_errors_1.xml"));
            doc2 = builder.parse(new File("项目代码分析//分析报告//checkstyle//checkstyle_errors_2.xml"));
            //获取包含类名的文本节点
            NodeList fileNodeList1 = doc1.getElementsByTagName("file");
            NodeList fileNodeList2 = doc2.getElementsByTagName("file");
            int openWarning = 0;
            int closedWarning = 0;
            int newWarning = 0;
            for (int i = 0; i < fileNodeList2.getLength(); i++) {
                Node file = fileNodeList2.item(i);
                NodeList errorList = file.getChildNodes();
                NodeList prevErrorList = fileNodeList1.item(i).getChildNodes();
                for (int j = 0; 2 * j + 1 < errorList.getLength(); j++) {
                    // 获得message和source
                    String message = errorList.item(2 * j + 1).getAttributes().getNamedItem("message").getNodeValue();
                    String source = errorList.item(2 * j + 1).getAttributes().getNamedItem("source").getNodeValue();
                    boolean same = false;
                    for (int k = 0; 2 * k + 1 < prevErrorList.getLength(); k++) {
                        if (message.equals(prevErrorList.item(2 * k + 1).getAttributes().getNamedItem("message").getNodeValue()) && source.equals(prevErrorList.item(2 * k + 1).getAttributes().getNamedItem("source").getNodeValue())) {
                            same = true;
                            break;
                        }
                    }
                    if (same) {
                        openWarning++;
                    } else {
                        newWarning++;
                    }
                }
            }
            System.out.println("open warinings:  " + openWarning);
            closedWarning = doc1.getElementsByTagName("error").getLength() - openWarning;
            System.out.println("closed warnings: " + closedWarning);
            System.out.println("new warnings:    " + newWarning);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

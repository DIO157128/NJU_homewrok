package uk.ac.wlv.utilities;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;

/**
 * 该类是读取属性和方法配置的.
 */

public class ArgsXMLUtil {
    private ArrayList<String> args;
    private ArrayList<String> methods;

    /**
     * 无参构造函数.
     */
    public ArgsXMLUtil() {
        args = new ArrayList<String>();
        methods = new ArrayList<String>();
    }

    /**
     * 获取args列表.
     *
     * @param filepath
     * @return args
     */
    public ArrayList<String> getArgs(String filepath) {
        args.clear();
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(filepath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document doc;
            doc = builder.parse(inputStream);



            NodeList list = doc.getElementsByTagName("arg");
            for (int i = 0; i < list.getLength(); i++) {
                Node temp = list.item(i).getFirstChild();
                String arg = temp.getNodeValue();

                args.add(arg);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return args;
    }

    /**
     * 获取方法名列表.
     *
     * @param filepath
     * @return methods
     */
    public ArrayList<String> getMethods(String filepath) {
        methods.clear();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document doc;
            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(filepath);
            doc = builder.parse(inputStream);

            NodeList list = doc.getElementsByTagName("method");
            for (int i = 0; i < list.getLength(); i++) {
                Node temp = list.item(i).getFirstChild();
                String method = temp.getNodeValue();

                methods.add(method);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return methods;
    }
}

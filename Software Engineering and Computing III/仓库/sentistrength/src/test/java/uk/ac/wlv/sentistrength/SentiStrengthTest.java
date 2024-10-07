package uk.ac.wlv.sentistrength;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.*;

import static org.junit.Assert.*;

/**
 * SentiStrength Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>04/07/2023</pre>
 */
public class SentiStrengthTest extends TestCase {
    public SentiStrengthTest(String name) {
        super(name);
    }

    String lineSeparator = System.lineSeparator();

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * 针对SentiStrength主类的测试1
     *
     * @throws Exception
     */
    public void testMain1() throws Exception {
        String[] args = new String[5];
        args[0] = "sentidata";
        args[1] = "src/main/java/SentStrength_Data/";
        args[2] = "text";
        args[3] = "I+love+you";
        args[4] = "trinary";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        SentiStrength classifier = new SentiStrength();
        classifier.initialiseAndRun(args);
        String consoleOutput = baos.toString();
        assertEquals("3 -1 1" + lineSeparator, consoleOutput);
        // 恢复System.out到控制台
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        // 打印捕获的控制台输出
        System.out.println("捕获到的控制台输出为: " + consoleOutput);

    }

    /**
     * 针对SentiStrength主类的测试2
     *
     * @throws Exception
     */
    public void testMain2() throws Exception {
        String[] args = new String[5];
        args[0] = "sentidata";
        args[1] = "src/main/java/SentStrength_Data/";
        args[2] = "text";
        args[3] = "I+hate+you";
        args[4] = "trinary";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        SentiStrength classifier = new SentiStrength();
        classifier.initialiseAndRun(args);
        String consoleOutput = baos.toString();
        assertEquals("1 -4 -1" + lineSeparator, consoleOutput);
        // 恢复System.out到控制台
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        // 打印捕获的控制台输出
        System.out.println("捕获到的控制台输出为: " + consoleOutput);

    }

    /**
     * 针对SentiStrength主类的测试3
     *
     * @throws Exception
     */
    public void testMain3() throws Exception {
        String[] args = new String[5];
        args[0] = "sentidata";
        args[1] = "src/main/java/SentStrength_Data/";
        args[2] = "text";
        args[3] = "I+hate+you!";
        args[4] = "trinary";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        SentiStrength classifier = new SentiStrength();
        classifier.initialiseAndRun(args);
        String consoleOutput = baos.toString();
        assertEquals("1 -5 -1" + lineSeparator, consoleOutput);
        // 恢复System.out到控制台
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        // 打印捕获的控制台输出
        System.out.println("捕获到的控制台输出为: " + consoleOutput);

    }

    /**
     * 针对SentiStrength主类的测试4
     *
     * @throws Exception
     */
    public void testMain4() throws Exception {
        String[] args = new String[5];
        args[0] = "sentidata";
        args[1] = "src/main/java/SentStrength_Data/";
        args[2] = "text";
        args[3] = "hate!";
        args[4] = "trinary";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        SentiStrength classifier = new SentiStrength();
        classifier.initialiseAndRun(args);
        String consoleOutput = baos.toString();
        assertEquals("1 -5 -1" + lineSeparator, consoleOutput);
        // 恢复System.out到控制台
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        // 打印捕获的控制台输出
        System.out.println("捕获到的控制台输出为: " + consoleOutput);

    }

    /**
     * 针对SentiStrength主类的测试5
     *
     * @throws Exception
     */
    public void testMain5() throws Exception {
        String[] args = new String[5];
        args[0] = "sentidata";
        args[1] = "src/main/java/SentStrength_Data/";
        args[2] = "input";
        args[3] = "src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/testFileClas.txt";
        args[4] = "trinary";
        String fileName = args[3];
        String outputFilename = "src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/testFileClas0_out.txt";
        SentiStrength classifier = new SentiStrength();
        classifier.initialiseAndRun(args);
        FileInputStream fin = new FileInputStream(fileName);
        InputStreamReader reader = new InputStreamReader(fin);
        BufferedReader buffReader = new BufferedReader(reader);
        String expectOutPut = "Overall\tText" + lineSeparator +
                "1\tI love you" + lineSeparator +
                "-1\tI hate you" + lineSeparator;
        String actualOutPut = readFileContent(outputFilename);
        deleteFile(outputFilename);
        Assert.assertEquals(expectOutPut, actualOutPut);

    }

    /**
     * 针对SentiStrength主类的测试6
     *
     * @throws Exception
     */
    public void testMain6() throws Exception {
        String[] args = new String[7];
        args[0] = "sentidata";
        args[1] = "src/main/java/SentStrength_Data/";
        args[2] = "input";
        args[3] = "src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/testFileClas.txt";
        args[4] = "trinary";
        args[5] = "resultsextension";
        args[6] = "_test.txt";
        String fileName = args[3];
        String outputFilename = "src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/testFileClas0_test.txt";
        SentiStrength classifier = new SentiStrength();
        classifier.initialiseAndRun(args);
        FileInputStream fin = new FileInputStream(fileName);
        InputStreamReader reader = new InputStreamReader(fin);
        BufferedReader buffReader = new BufferedReader(reader);
        String expectOutPut = "Overall\tText" + lineSeparator +
                "1\tI love you" + lineSeparator +
                "-1\tI hate you" + lineSeparator;
        String actualOutPut = readFileContent(outputFilename);
        deleteFile(outputFilename);
        Assert.assertEquals(expectOutPut, actualOutPut);

    }

    /**
     * 针对SentiStrength主类的测试7
     *
     * @throws Exception
     */
    public void testMain7() throws Exception {
        String[] args = new String[7];
        args[0] = "sentidata";
        args[1] = "src/main/java/SentStrength_Data/";
        args[2] = "input";
        args[3] = "src/test/java/uk/ac/wlv/sentistrength/testFiles/testFileClas.txt";
        args[4] = "trinary";
        args[5] = "outputFolder";
        args[6] = "src/test/java/uk/ac/wlv/sentistrength/testFolder/";
        String fileName = args[3];
        String outputFilename = "src/test/java/uk/ac/wlv/sentistrength/testFolder/testFileClas0_out.txt";
        SentiStrength classifier = new SentiStrength();
        classifier.initialiseAndRun(args);
        FileInputStream fin = new FileInputStream(fileName);
        InputStreamReader reader = new InputStreamReader(fin);

        String expectOutPut = "Overall\tText" + lineSeparator +
                "1\tI love you" + lineSeparator +
                "-1\tI hate you" + lineSeparator;
        String actualOutPut = readFileContent(outputFilename);
        deleteFile(outputFilename);
        Assert.assertEquals(expectOutPut, actualOutPut);

    }

    /**
     * 读取文件内容
     *
     * @param filePath 文件路径
     * @return 文件内容
     * @throws IOException
     */
    private static String readFileContent(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     */
    private static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.delete()) {
            System.out.println("File deleted: " + filePath);
        } else {
            System.out.println("Failed to delete file: " + filePath);
        }
    }

    public static Test suite() {
        return new TestSuite(SentiStrengthTest.class);
    }
}

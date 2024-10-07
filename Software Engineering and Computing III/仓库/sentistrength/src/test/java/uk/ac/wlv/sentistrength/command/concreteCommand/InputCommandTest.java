package uk.ac.wlv.sentistrength.command.concreteCommand;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import org.junit.Assert;
import uk.ac.wlv.sentistrength.Corpus;
import uk.ac.wlv.sentistrength.command.concreteCommand.inputStrategy.SaveWithIDStrategy;
import uk.ac.wlv.utilities.globalParameter.GlobalParameterHolder;
import uk.ac.wlv.utilities.globalParameter.commandOptions.InputCommandOptions;

import java.io.*;

/**
 * InputCommand Tester.
 *
 * @author <yubowen>
 * @version 1.0
 * @since <pre>04/19/2023</pre>
 */
public class InputCommandTest extends TestCase {
    public InputCommandTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    String lineSeparator = System.lineSeparator();

    /**
     * 读取文件内容
     *
     * @param filePath 文件路径
     * @throws IOException
     * @return文件内容
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

    /**
     * ClassifyAllLineStrategy的测试
     *
     * @throws Exception
     */
    public void testClassifyAllLineStrategy() throws Exception {
        Corpus c = new Corpus();
        c.options.bgTrinaryMode = true;
        GlobalParameterHolder globalParameterHolder = new GlobalParameterHolder(c);
        InputCommandOptions inputCommandOptions = globalParameterHolder.createInputCommandOptions();
        String fileName = "src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/testFileClas.txt";
        String outputFilename = "src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/testFileClas0_out.txt";
        inputCommandOptions.setsInputFile(fileName);
        InputCommand inputCommand = new InputCommand(inputCommandOptions);
        if (inputCommand.getCommandOptions().getC().initialise()) {
            inputCommand.action();
        }
        FileInputStream fin = new FileInputStream(fileName);
        InputStreamReader reader = new InputStreamReader(fin);
        BufferedReader buffReader = new BufferedReader(reader);
        String expectOutPut = "Overall\tText" + lineSeparator +
                "1\tI love you" + lineSeparator +
                "-1\tI hate you" + lineSeparator;
        String actualOutPut = readFileContent(outputFilename);
        deleteFile(outputFilename);
        Assert.assertEquals(expectOutPut, actualOutPut);

        buffReader.close();
    }

    /**
     * 修改文件拓展名的测试
     *
     * @throws Exception
     */
    public void testFileExtension() throws Exception {
        Corpus c = new Corpus();
        c.options.bgTrinaryMode = true;
        GlobalParameterHolder globalParameterHolder = new GlobalParameterHolder(c);
        InputCommandOptions inputCommandOptions = globalParameterHolder.createInputCommandOptions();
        String fileName = "src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/testFileClas.txt";
        String outputFilename = "src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/testFileClas0_test.txt";
        inputCommandOptions.setsInputFile(fileName);
        inputCommandOptions.setsResultsFileExtension("_test.txt");
        InputCommand inputCommand = new InputCommand(inputCommandOptions);
        if (inputCommand.getCommandOptions().getC().initialise()) {
            inputCommand.action();
        }
        FileInputStream fin = new FileInputStream(fileName);
        InputStreamReader reader = new InputStreamReader(fin);
        BufferedReader buffReader = new BufferedReader(reader);
        String expectOutPut = "Overall\tText" + lineSeparator +
                "1\tI love you" + lineSeparator +
                "-1\tI hate you" + lineSeparator;
        String actualOutPut = readFileContent(outputFilename);
        deleteFile(outputFilename);
        Assert.assertEquals(expectOutPut, actualOutPut);

        buffReader.close();
    }


    /**
     * OptimalStrategy 的测试1
     *
     * @throws Exception
     */
    public void testOptimalStrategy1() throws Exception {
        Corpus c = new Corpus();
        c.options.bgTrinaryMode = true;
        GlobalParameterHolder globalParameterHolder = new GlobalParameterHolder(c);
        globalParameterHolder.setbTrain(true);
        globalParameterHolder.setsOptimalTermStrengths("src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/optTester.txt");
        InputCommandOptions inputCommandOptions = globalParameterHolder.createInputCommandOptions();
        String fileName = "src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/testFileOpt.txt";
        String outputFilename = "src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/optTester.txt";
        inputCommandOptions.setsInputFile(fileName);
        InputCommand inputCommand = new InputCommand(inputCommandOptions);
        if (inputCommand.getCommandOptions().getC().initialise()) {
            inputCommand.action();
        }
        deleteFile(outputFilename);
    }

    /**
     * OptimalStrategy 的测试2
     *
     * @throws Exception
     */
    public void testOptimalStrategy2() throws Exception {
        Corpus c = new Corpus();
        c.options.bgTrinaryMode = true;
        GlobalParameterHolder globalParameterHolder = new GlobalParameterHolder(c);
        globalParameterHolder.setbTrain(true);
        globalParameterHolder.setsOptimalTermStrengths("src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/optTester.txt");
        InputCommandOptions inputCommandOptions = globalParameterHolder.createInputCommandOptions();
        String fileName = "src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/testFileOpt2.txt";
        String outputFilename = "src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/optTester.txt";
        inputCommandOptions.setsInputFile(fileName);
        InputCommand inputCommand = new InputCommand(inputCommandOptions);
        if (inputCommand.getCommandOptions().getC().initialise()) {
            inputCommand.action();
        }
        deleteFile(outputFilename);
    }

    /**
     * TermWeightStrategy的测试
     *
     * @throws Exception
     */
    public void testTermWeightStrategy() throws Exception {
        Corpus c = new Corpus();
        c.options.bgTrinaryMode = true;
        GlobalParameterHolder globalParameterHolder = new GlobalParameterHolder(c);
        globalParameterHolder.setbTrain(true);
        globalParameterHolder.setbReportNewTermWeightsForBadClassifications(true);
        InputCommandOptions inputCommandOptions = globalParameterHolder.createInputCommandOptions();
        String fileName = "src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/testFileTerm.txt";
        String outputFilename = "src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/testFileTerm_unusedTerms.txt";
        inputCommandOptions.setsInputFile(fileName);
        InputCommand inputCommand = new InputCommand(inputCommandOptions);
        if (inputCommand.getCommandOptions().getC().initialise()) {
            inputCommand.action();
        }
        deleteFile(outputFilename);
    }

    /**
     * SaveWithIDStrategy的测试
     *
     * @throws Exception
     */
    public void testSaveWithIDStrategy() throws Exception {
        Corpus c = new Corpus();
        c.options.bgTrinaryMode = true;
        GlobalParameterHolder globalParameterHolder = new GlobalParameterHolder(c);
        globalParameterHolder.setbTrain(true);
        globalParameterHolder.setiTextCol(2);
        globalParameterHolder.setiIdCol(2);
        InputCommandOptions inputCommandOptions = globalParameterHolder.createInputCommandOptions();
        String fileName = "src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/testFileSave.txt";
        String outputFilename = "src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/testFileSave_classID.txt";
        inputCommandOptions.setsInputFile(fileName);
        InputCommand inputCommand = new InputCommand(inputCommandOptions);
        if (inputCommand.getCommandOptions().getC().initialise()) {
            inputCommand.action();
        }
        String expectOutPut = "love\t1" + lineSeparator +
                "hate\t-1" + lineSeparator;
        String actualOutPut = readFileContent(outputFilename);
        deleteFile(outputFilename);
        Assert.assertEquals(expectOutPut, actualOutPut);

    }

    /**
     * AnnotationColStrategy的测试
     *
     * @throws Exception
     */
    public void testAnnotationColStrategy() throws Exception {
        Corpus c = new Corpus();
        c.options.bgTrinaryMode = true;
        GlobalParameterHolder globalParameterHolder = new GlobalParameterHolder(c);
        globalParameterHolder.setbTrain(true);
        globalParameterHolder.setiTextColForAnnotation(2);
        InputCommandOptions inputCommandOptions = globalParameterHolder.createInputCommandOptions();
        inputCommandOptions.setbOkToOverwrite(true);
        String fileName = "src/test/java/uk/ac/wlv/sentistrength/command/concreteCommand/testFiles/testFileAnno.txt";
        inputCommandOptions.setsInputFile(fileName);
        InputCommand inputCommand = new InputCommand(inputCommandOptions);
        if (inputCommand.getCommandOptions().getC().initialise()) {
            inputCommand.action();
        }
        String originalContent = "I\tlove\tyou" + lineSeparator +
                "I\thate\tyou" + lineSeparator;
        String expectOutput = "I\tlove\tyou\t1" + lineSeparator +
                "I\thate\tyou\t-1" + lineSeparator;
        String actualOutput = readFileContent(fileName);
        try {
            FileWriter writer = new FileWriter(fileName, false); // 第二个参数为false，表示覆盖模式
            writer.write(originalContent);
            writer.close();
            System.out.println("源文件内容已恢复");
        } catch (IOException e) {
            System.out.println("写入文件时出现异常。");
            e.printStackTrace();
        }
        Assert.assertEquals(expectOutput, actualOutput);
    }

    public static Test suite() {
        return new TestSuite(InputCommandTest.class);
    }
}

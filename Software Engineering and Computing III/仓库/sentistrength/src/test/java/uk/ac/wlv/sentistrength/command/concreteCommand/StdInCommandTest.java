package uk.ac.wlv.sentistrength.command.concreteCommand;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import uk.ac.wlv.sentistrength.Corpus;
import uk.ac.wlv.utilities.globalParameter.GlobalParameterHolder;
import uk.ac.wlv.utilities.globalParameter.commandOptions.StdInCommandOptions;


import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.io.PrintStream;


/**
 * StdInCommand Tester.
 *
 * @author <yubowen>
 * @version 1.0
 * @since <pre>04/19/2023</pre>
 */
public class StdInCommandTest extends TestCase {
    public StdInCommandTest(String name) {
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
     * StdInCommand的action函数的测试1
     *
     * @throws Exception
     */
    public void testAction1() throws Exception {
        Corpus c = new Corpus();
        c.options.bgTrinaryMode = true;
        GlobalParameterHolder globalParameterHolder = new GlobalParameterHolder(c);
        StdInCommandOptions stdInCommandOptions = globalParameterHolder.createStdInCommandOptions();

        StdInCommand stdInCommand = new StdInCommand(stdInCommandOptions);
        String input = "I love you\nI hate you\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        if (stdInCommand.getCommandOptions().getC().initialise()) {
            stdInCommand.action();
        }
        String expectedOutput = "3\t-1\t1\t" + lineSeparator;
        assertEquals(expectedOutput, outContent.toString());
    }

    /**
     * StdInCommand的action函数的测试2
     *
     * @throws Exception
     */
    public void testAction2() throws Exception {
        Corpus c = new Corpus();
        c.options.bgTrinaryMode = true;
        GlobalParameterHolder globalParameterHolder = new GlobalParameterHolder(c);
        StdInCommandOptions stdInCommandOptions = globalParameterHolder.createStdInCommandOptions();

        StdInCommand stdInCommand = new StdInCommand(stdInCommandOptions);
        String input = "I love you\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        if (stdInCommand.getCommandOptions().getC().initialise()) {
            stdInCommand.action();
        }
        String expectedOutput = "3\t-1\t1\t" + lineSeparator;
        assertEquals(expectedOutput, outContent.toString());
    }

    /**
     * StdInCommand的action函数的测试3
     *
     * @throws Exception
     */
    public void testAction3() throws Exception {
        Corpus c = new Corpus();
        c.options.bgTrinaryMode = true;
        GlobalParameterHolder globalParameterHolder = new GlobalParameterHolder(c);
        StdInCommandOptions stdInCommandOptions = globalParameterHolder.createStdInCommandOptions();

        StdInCommand stdInCommand = new StdInCommand(stdInCommandOptions);
        String input = "";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        if (stdInCommand.getCommandOptions().getC().initialise()) {
            stdInCommand.action();
        }
        String expectedOutput = "";
        assertEquals(expectedOutput, outContent.toString());
    }

    public static Test suite() {
        return new TestSuite(StdInCommandTest.class);
    }
}

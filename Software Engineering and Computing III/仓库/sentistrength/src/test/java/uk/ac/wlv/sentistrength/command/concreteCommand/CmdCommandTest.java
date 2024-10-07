package uk.ac.wlv.sentistrength.command.concreteCommand;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import uk.ac.wlv.sentistrength.Corpus;
import uk.ac.wlv.utilities.globalParameter.GlobalParameterHolder;
import uk.ac.wlv.utilities.globalParameter.commandOptions.CmdCommandOptions;
import uk.ac.wlv.utilities.globalParameter.commandOptions.StdInCommandOptions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * CmdCommand Tester.
 *
 * @author <yubowen>
 * @version 1.0
 * @since <pre>04/19/2023</pre>
 */
public class CmdCommandTest extends TestCase {
    public CmdCommandTest(String name) {
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
     * CmdCommand的action函数的测试
     *
     * @throws Exception
     */
    public void testAction() throws Exception {
        Corpus c = new Corpus();
        c.options.bgTrinaryMode = true;
        GlobalParameterHolder globalParameterHolder = new GlobalParameterHolder(c);
        CmdCommandOptions cmdCommandOptions = globalParameterHolder.createCmdCommandOptions();

        CmdCommand cmdCommand = new CmdCommand(cmdCommandOptions);
        String input = "I love you\nI hate you\n@end";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        if (cmdCommand.getCommandOptions().getC().initialise()) {
            cmdCommand.action();
        }
        String expectedOutput = "3 -1 1" + lineSeparator + "1 -4 -1" + lineSeparator;
        assertEquals(expectedOutput, outContent.toString());
    }


    public static Test suite() {
        return new TestSuite(CmdCommandTest.class);
    }
}

package uk.ac.wlv.sentistrength.command.concreteCommand;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import uk.ac.wlv.sentistrength.Corpus;
import uk.ac.wlv.utilities.globalParameter.GlobalParameterHolder;
import uk.ac.wlv.utilities.globalParameter.commandOptions.TextCommandOptions;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * TextCommand Tester.
 *
 * @author <yubowen>
 * @version 1.0
 * @since <pre>04/19/2023</pre>
 */
public class TextCommandTest extends TestCase {
    public TextCommandTest(String name) {
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
     * TextCommand的action函数的测试1
     *
     * @throws Exception
     */
    public void testAction1() throws Exception {
        Corpus c = new Corpus();
        c.options.bgTrinaryMode = true;
        GlobalParameterHolder globalParameterHolder = new GlobalParameterHolder(c);
        TextCommandOptions textCommandOptions = globalParameterHolder.createTextCommandOptions();
        textCommandOptions.setsTextToParse("I+love+you");
        TextCommand textCommand = new TextCommand(textCommandOptions);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        if (textCommand.getCommandOptions().getC().initialise()) {
            textCommand.action();
        }
        String consoleOutput = baos.toString();
        assertEquals("3 -1 1" + lineSeparator, consoleOutput);
    }

    /**
     * TextCommand的action函数的测试2
     *
     * @throws Exception
     */
    public void testAction2() throws Exception {
        Corpus c = new Corpus();
        c.options.bgTrinaryMode = true;
        GlobalParameterHolder globalParameterHolder = new GlobalParameterHolder(c);
        TextCommandOptions textCommandOptions = globalParameterHolder.createTextCommandOptions();
        textCommandOptions.setsTextToParse("I+hate+you");
        TextCommand textCommand = new TextCommand(textCommandOptions);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        if (textCommand.getCommandOptions().getC().initialise()) {
            textCommand.action();
        }
        String consoleOutput = baos.toString();
        assertEquals("1 -4 -1" + lineSeparator, consoleOutput);
    }

    public static Test suite() {
        return new TestSuite(TextCommandTest.class);
    }
}

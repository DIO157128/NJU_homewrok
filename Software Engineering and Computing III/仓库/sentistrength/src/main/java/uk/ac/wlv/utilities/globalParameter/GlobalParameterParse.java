package uk.ac.wlv.utilities.globalParameter;

import uk.ac.wlv.sentistrength.command.Command;
import uk.ac.wlv.sentistrength.command.concreteCommand.*;
import uk.ac.wlv.utilities.ArgsXMLUtil;
import uk.ac.wlv.utilities.globalParameter.commandOptions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class GlobalParameterParse {
    private GlobalParameterHolder globalParameterHolder;
    boolean[] bArgumentRecognised;

    private ArrayList<String> argsList;
    private ArrayList<String> methodsList;

    private CommandType type;
    private Command command = null;

    /**
     * 有参构造.
     *
     * @param globalParameterHolder
     * @param argsXMLUtil
     */
    public GlobalParameterParse(GlobalParameterHolder globalParameterHolder, ArgsXMLUtil argsXMLUtil) {
        this.globalParameterHolder = globalParameterHolder;
        this.argsList = argsXMLUtil.getArgs("argsForGlobalConfig.xml");
        this.methodsList = argsXMLUtil.getMethods("argsForGlobalParse.xml");
    }

    /**
     * 无参构造.
     */
    public GlobalParameterParse() {
    }

    /**
     * 参数解析类.
     *
     * @param args
     */
    public void parse(String[] args) {
        bArgumentRecognised = new boolean[args.length];
        Class<Object> thisClass = null;
        Method method = null;

        for (int i = 0; i < args.length; i++) {
            bArgumentRecognised[i] = false;
        }

        try {
            //生成类对象，通过java反射机制来invoke方法
            thisClass = (Class<Object>) Class.forName("uk.ac.wlv.utilities.globalParameter.GlobalParameterParse");
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }

        for (int i = 0; i < args.length; i++) {
            if (argsList.contains(args[i].toLowerCase())) {
                String methodName = methodsList.get(argsList.indexOf(args[i].toLowerCase()));
                try {
                    method = thisClass.getMethod(methodName, Integer.class, String[].class);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

                try {
                    method.invoke(this, i, args);
                } catch (IllegalAccessException | InvocationTargetException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                } catch (NumberFormatException var5) {
                    System.out.println("Error in argument for " + args[i] + ". Integer expected!");
                } catch (Exception var6) {
                    System.out.println("Error in argument for " + args[i] + ". Argument missing?");
                }
            }
        }

        setDataSourceOptions();
    }

    /**
     * 设置options.
     */
    private void setDataSourceOptions() {
        switch (this.type) {
            case CmdCommand:
                CmdCommandOptions cmdCommandOptions = globalParameterHolder.createCmdCommandOptions();
                command.setCommandOptions(cmdCommandOptions);
                break;
            case InputCommand:
                InputCommandOptions inputCommandOptions = globalParameterHolder.createInputCommandOptions();
                command.setCommandOptions(inputCommandOptions);
                break;
            case ListenCommand:
                ListenPortCommandOptions listenPortCommandOptions = globalParameterHolder.createListenPortCommandOptions();
                command.setCommandOptions(listenPortCommandOptions);
                break;
            case StdInCommand:
                StdInCommandOptions stdInCommand = globalParameterHolder.createStdInCommandOptions();
                command.setCommandOptions(stdInCommand);
                break;
            case TextCommand:
                TextCommandOptions textCommandOptions = globalParameterHolder.createTextCommandOptions();
                command.setCommandOptions(textCommandOptions);
                break;
            default:
                break;
        }
    }

    /**
     * pair参数设置.
     *
     * @param i
     */
    private void pairParameterArg(int i) {
        bArgumentRecognised[i] = true;
        bArgumentRecognised[i + 1] = true;
    }

    /**
     * one参数设置.
     *
     * @param i
     */
    private void oneParameterArg(int i) {
        bArgumentRecognised[i] = true;
    }

    /**
     * 解析input.
     *
     * @param i
     * @param args
     */
    public void parseInput(Integer i, String[] args) {
        if (this.command == null) {
            this.type = CommandType.InputCommand;
            this.command = new InputCommand();
        }
        globalParameterHolder.setsInputFile(args[i + 1]);
        pairParameterArg(i);
    }

    /**
     * 解析InputFolder.
     *
     * @param i
     * @param args
     */
    public void parseInputFolder(Integer i, String[] args) {
        if (this.command == null) {
            this.type = CommandType.InputCommand;
            this.command = new InputCommand();
        }
        globalParameterHolder.setsInputFolder(args[i + 1]);
        pairParameterArg(i);
    }

    /**
     * 解析OutputFolder.
     *
     * @param i
     * @param args
     */
    public void parseOutputFolder(Integer i, String[] args) {
        globalParameterHolder.setsResultsFolder(args[i + 1]);
        pairParameterArg(i);
    }

    /**
     * 解析ResultExtension.
     *
     * @param i
     * @param args
     */
    public void parseResultExtension(Integer i, String[] args) {
        globalParameterHolder.setsResultsFileExtension(args[i + 1]);
        pairParameterArg(i);
    }

    /**
     * 解析ResultsExtension.
     *
     * @param i
     * @param args
     */
    public void parseResultsExtension(Integer i, String[] args) {
        globalParameterHolder.setsResultsFileExtension(args[i + 1]);
        pairParameterArg(i);
    }

    /**
     * 解析FileSubstring.
     *
     * @param i
     * @param args
     */
    public void parseFileSubstring(Integer i, String[] args) {
        globalParameterHolder.setsFileSubString(args[i + 1]);
        pairParameterArg(i);
    }

    /**
     * 解析OverWrite.
     *
     * @param i
     * @param args
     */
    public void parseOverWrite(Integer i, String[] args) {
        globalParameterHolder.setbOkToOverwrite(true);
        oneParameterArg(i);
    }

    /**
     * 解析Text.
     *
     * @param i
     * @param args
     */
    public void parseText(Integer i, String[] args) {
        if (this.command == null) {
            this.type = CommandType.TextCommand;
            this.command = new TextCommand();
        }
        globalParameterHolder.setsTextToParse(args[i + 1]);
        pairParameterArg(i);
    }

    /**
     * 解析UrlEncoded.
     *
     * @param i
     * @param args
     */
    public void parseUrlEncoded(Integer i, String[] args) {
        globalParameterHolder.setbURLEncoded(true);
        oneParameterArg(i);
    }

    /**
     * 解析Listen.
     *
     * @param i
     * @param args
     */
    public void parseListen(Integer i, String[] args) {
        if (this.command == null) {
            this.type = CommandType.ListenCommand;
            this.command = new ListenCommand();
        }
        globalParameterHolder.setiListenPort(Integer.parseInt(args[i + 1]));
        pairParameterArg(i);
    }

    /**
     * 解析Stdin.
     *
     * @param i
     * @param args
     */
    public void parseStdin(Integer i, String[] args) {
        if (this.command == null) {
            this.type = CommandType.StdInCommand;
            this.command = new StdInCommand();
        }
        globalParameterHolder.setbStdIn(true);
        oneParameterArg(i);

    }

    /**
     * 解析TextCol.
     *
     * @param i
     * @param args
     */
    public void parseTextCol(Integer i, String[] args) {
        globalParameterHolder.setiTextCol(Integer.parseInt(args[i + 1]));
        pairParameterArg(i);
    }

    /**
     * 解析Cmd.
     *
     * @param i
     * @param args
     */
    public void parseCmd(Integer i, String[] args) {
        if (this.command == null) {
            this.type = CommandType.CmdCommand;
            this.command = new CmdCommand();
        }
        globalParameterHolder.setbCmd(true);
        oneParameterArg(i);
    }

    /**
     * 解析Optimise.
     *
     * @param i
     * @param args
     */
    public void parseOptimise(Integer i, String[] args) {
        globalParameterHolder.setsOptimalTermStrengths(args[i + 1]);
        pairParameterArg(i);
    }

    /**
     * 解析AnnotateCol.
     *
     * @param i
     * @param args
     */
    public void parseAnnotateCol(Integer i, String[] args) {
        globalParameterHolder.setiTextColForAnnotation(Integer.parseInt(args[i + 1]));
        pairParameterArg(i);
    }

    /**
     * 解析IdCol.
     *
     * @param i
     * @param args
     */
    public void parseIdCol(Integer i, String[] args) {
        globalParameterHolder.setiIdCol(Integer.parseInt(args[i + 1]));
        pairParameterArg(i);
    }

    /**
     * 解析Lang.
     *
     * @param i
     * @param args
     */
    public void parseLang(Integer i, String[] args) {
        globalParameterHolder.setsLanguage(args[i + 1]);
        pairParameterArg(i);
    }

    /**
     * 解析Train.
     *
     * @param i
     * @param args
     */
    public void parseTrain(Integer i, String[] args) {
        globalParameterHolder.setbTrain(true);
        oneParameterArg(i);
    }

    /**
     * 解析All.
     *
     * @param i
     * @param args
     */
    public void parseAll(Integer i, String[] args) {
        globalParameterHolder.setbDoAll(true);
        globalParameterHolder.setbTrain(true);
        oneParameterArg(i);
    }

    /**
     * 解析NumberCorrect.
     *
     * @param i
     * @param args
     */
    public void parseNumCorrect(Integer i, String[] args) {
        globalParameterHolder.setbUseTotalDifference(true);
        globalParameterHolder.setbTrain(true);
        oneParameterArg(i);
    }

    /**
     * 解析Iterations.
     *
     * @param i
     * @param args
     */
    public void parseIterations(Integer i, String[] args) {
        globalParameterHolder.setiIterations(Integer.parseInt(args[i + 1]));
        globalParameterHolder.setbTrain(true);
        pairParameterArg(i);
    }

    /**
     * 解析MinImprovement.
     *
     * @param i
     * @param args
     */
    public void parseMinImprovement(Integer i, String[] args) {
        globalParameterHolder.setiMinImprovement(Integer.parseInt(args[i + 1]));
        globalParameterHolder.setbTrain(true);
        pairParameterArg(i);
    }

    /**
     * 解析Multi.
     *
     * @param i
     * @param args
     */
    public void parseMulti(Integer i, String[] args) {
        globalParameterHolder.setiMultiOptimisations(Integer.parseInt(args[i + 1]));
        globalParameterHolder.setbTrain(true);
        pairParameterArg(i);
    }

    /**
     * 解析TermWeights.
     *
     * @param i
     * @param args
     */
    public void parseTermWeights(Integer i, String[] args) {
        globalParameterHolder.setbReportNewTermWeightsForBadClassifications(true);
        oneParameterArg(i);
    }


    /**
     * 设置globalParameterHolder.
     *
     * @return globalParameterHolder
     */
    public GlobalParameterHolder getGlobalParameterHolder() {
        return this.globalParameterHolder;
    }

    /**
     * 获取command.
     *
     * @return command
     */
    public Command getCommand() {
        return this.command;
    }

    /**
     * 获取bArgumentRecognised.
     *
     * @return bArgumentRecognised
     */
    public boolean[] getbArgumentRecognised() {
        return bArgumentRecognised;
    }
}

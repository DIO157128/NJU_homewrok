package uk.ac.wlv.sentistrength;

import java.io.File;
import java.util.Locale;

import uk.ac.wlv.sentistrength.command.Command;
import uk.ac.wlv.utilities.ArgsXMLUtil;
import uk.ac.wlv.utilities.globalParameter.GlobalParameterHolder;
import uk.ac.wlv.utilities.globalParameter.GlobalParameterParse;
import uk.ac.wlv.utilities.resourcesAndOptionsParameter.ParametersForCorpusParse;

/**
 * 相关用例:UC-11, UC-12, UC-13, UC-14, UC-15, UC-16, UC-17, UC-18, UC-19, UC-20, UC-22, UC-23, UC-24, UC-25, UC-26, UC-27, UC-28, UC-29
 * SentiStrength是一个主类，调用了多个方法，对成员变量进行初始化，用于对输入的命令进行处理，主要是对文本进行情感分值分析.
 */
public class SentiStrength {


    private Corpus c;
    private ArgsXMLUtil argsXMLUtil;

    //命令的引用
    private Command command;

    /**
     * SentiStrength类的构造方法，用于初始化对象，并为成员变量c创建一个Corpus对象.
     */
    public SentiStrength() {
        this.c = new Corpus();
        this.argsXMLUtil = new ArgsXMLUtil();
    }

    /**
     * SentiStrength类的构造方法，用于初始化对象.
     * <p>
     * 为SentiStrength类的成员变量c创建Corpus对象，执行方法initialiseAndRun(),同时向该方法传入参数args.
     *
     * @param args 输入命令
     */
    public SentiStrength(String[] args) {
        this.c = new Corpus();
        this.argsXMLUtil = new ArgsXMLUtil();
        this.initialiseAndRun(args);
    }

    /**
     * SentiStrength类中的main方法，用于创建一个SentiStrength对象，并执行其initialiseAndRun方法.
     *
     * @param args 输入的参数
     */
    public static void main(String[] args) {
        SentiStrength classifier = new SentiStrength();
        classifier.initialiseAndRun(args);
    }

    /**
     * 获取输入的方法.
     *
     * @return 返回<code>null</code>
     */
    public String[] getinput() {
        return null;
    }

    /**
     * initialiseAndRun方法用于接收并解析传入的命令，如果传入的命令格式不符合规范，程序会进行报错.
     * 每次确认第i个字符串符合规范，都将bArgumentRecognised[i]设置为<code>true</code>.
     *
     * @param args 一个字符串数组，内容为用户的命令
     */
    public void initialiseAndRun(String[] args) {
        //首选需要检查用户是否是想获取命令行帮助
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("help")) {
                this.printCommandLineOptions();
                return;
            }
        }


        boolean[] bArgumentRecognised;


        //解析全局参数
        GlobalParameterHolder globalParameterHolder = new GlobalParameterHolder(this.c);
        GlobalParameterParse globalParameterParse = new GlobalParameterParse(globalParameterHolder, this.argsXMLUtil);
        globalParameterParse.parse(args);


        //获取解析的参数， 生成对应的命令
        command = globalParameterParse.getCommand();

        //解析Corpus需要的参数
        ParametersForCorpusParse parametersForCorpusParse = new ParametersForCorpusParse(globalParameterParse.getbArgumentRecognised(), argsXMLUtil);
        parametersForCorpusParse.parse(args);

        //设置corpus对象需要的options和resources
        ClassificationOptions options = parametersForCorpusParse.getOptions();
        ClassificationResources resources = parametersForCorpusParse.getResources();
        command.getC().setOptions(options);
        command.getC().setResources(resources);




        //获取设置resources和options后的bArgumentRecognised
        bArgumentRecognised = parametersForCorpusParse.getbArgumentRecognised();


        //根据语言设置地区
        if (globalParameterHolder.getsLanguage().length() > 1) {
            Locale l = new Locale(globalParameterHolder.getsLanguage());
            Locale.setDefault(l);
        }

        //处理其中参数设置错误的情况
        for (int i = 0; i < args.length; ++i) {
            if (!bArgumentRecognised[i]) {
                System.out.println("Unrecognised command - wrong spelling or case?: " + args[i]);
                this.showBriefHelp();
                return;
            }
        }

        //使用命令模式运行实际逻辑
        if (command.getCommandOptions().getC().initialise()) {
            command.action();
        } else {
            System.out.println("Failed to initialise!");

            try {
                File f = new File(c.resources.sgSentiStrengthFolder);
                if (!f.exists()) {
                    System.out.println("Folder does not exist! " + c.resources.sgSentiStrengthFolder);
                }
            } catch (Exception var30) {
                System.out.println("Folder doesn't exist! " + c.resources.sgSentiStrengthFolder);
            }

            this.showBriefHelp();
        }

    }




    /**
     * showBriefHelp方法用于提示使用方法，多用于输入命令不符合规范的情况.
     */
    private void showBriefHelp() {
        System.out.println();
        System.out.println("====" + this.c.options.sgProgramName + "Brief Help====");
        System.out.println("For most operations, a minimum of two parameters must be set");
        System.out.println("1) folder location for the linguistic files");
        System.out.println("   e.g., on Windows: C:/mike/Lexical_Data/");
        System.out.println("   e.g., on Mac/Linux/Unix: /usr/Lexical_Data/");
        if (this.c.options.bgTensiStrength) {
            System.out.println("TensiiStrength_Data can be downloaded from...[not completed yet]");
        } else {
            System.out.println("SentiStrength_Data can be downloaded with the Windows version of SentiStrength from sentistrength.wlv.ac.uk");
        }

        System.out.println();
        System.out.println("2) text to be classified or file name of texts to be classified");
        System.out.println("   e.g., To classify one text: text love+u");
        System.out.println("   e.g., To classify a file of texts: input /bob/data.txt");
        System.out.println();
        System.out.println("Here is an example complete command:");
        if (this.c.options.bgTensiStrength) {
            System.out.println("java -jar TensiStrength.jar sentidata C:/a/Stress_Data/ text am+stressed");
        } else {
            System.out.println("java -jar SentiStrength.jar sentidata C:/a/SentStrength_Data/ text love+u");
        }

        System.out.println();
        if (!this.c.options.bgTensiStrength) {
            System.out.println("To list all commands: java -jar SentiStrength.jar help");
        }

    }

    /**
     * printCommandLineOptions方法用于输出所有命令行选项.
     */
    private void printCommandLineOptions() {
        System.out.println("====" + this.c.options.sgProgramName + " Command Line Options====");
        System.out.println("=Source of data to be classified=");
        System.out.println(" text [text to process] OR");
        System.out.println(" input [filename] (each line of the file is classified SEPARATELY");
        System.out.println("        May have +ve 1st col., -ve 2nd col. in evaluation mode) OR");
        System.out.println(" annotateCol [col # 1..] (classify text in col, result at line end) OR");
        System.out.println(" textCol, idCol [col # 1..] (classify text in col, result & ID in new file) OR");
        System.out.println(" inputFolder  [foldername] (all files in folder will be *annotated*)");
        System.out.println(" outputFolder [foldername where to put the output (default: folder of input)]");
        System.out.println(" resultsExtension [file-extension for output (default _out.txt)]");
        System.out.println("  fileSubstring [text] (string must be present in files to annotate)");
        System.out.println("  Ok to overwrite files [overwrite]");
        System.out.println(" listen [port number to listen at - call http://127.0.0.1:81/text]");
        System.out.println(" cmd (wait for stdin input, write to stdout, terminate on input: @end");
        System.out.println(" stdin (read from stdin input, write to stdout, terminate when stdin finished)");
        System.out.println(" wait (just initialise; allow calls to public String computeSentimentScores)");
        System.out.println("=Linguistic data source=");
        System.out.println(" sentidata [folder for " + this.c.options.sgProgramName + " data (end in slash, no spaces)]");
        System.out.println("=Options=");
        System.out.println(" keywords [comma-separated list - " + this.c.options.sgProgramMeasuring + " only classified close to these]");
        System.out.println("   wordsBeforeKeywords [words to classify before keyword (default 4)]");
        System.out.println("   wordsAfterKeywords [words to classify after keyword (default 4)]");
        System.out.println(" trinary (report positive-negative-neutral classifcation instead)");
        System.out.println(" binary (report positive-negative classifcation instead)");
        System.out.println(" scale (report single -4 to +4 classifcation instead)");
        System.out.println(" emotionLookupTable [filename (default: EmotionLookupTable.txt)]");
        System.out.println(" additionalFile [filename] (domain-specific terms and evaluations)");
        System.out.println(" lemmaFile [filename] (word tab lemma list for lemmatisation)");
        System.out.println("=Classification algorithm parameters=");
        System.out.println(" noBoosters (ignore sentiment booster words (e.g., very))");
        System.out.println(" noNegators (don't use negating words (e.g., not) to flip sentiment) -OR-");
        System.out.println(" noNegatingPositiveFlipsEmotion (don't use negating words to flip +ve words)");
        System.out.println(" bgNegatingNegativeNeutralisesEmotion (negating words don't neuter -ve words)");
        System.out.println(" negatedWordStrengthMultiplier (strength multiplier when negated (default=0.5))");
        System.out.println(" negatingWordsOccurAfterSentiment (negate " + this.c.options.sgProgramMeasuring + " occurring before negatives)");
        System.out.println("  maxWordsAfterSentimentToNegate (max words " + this.c.options.sgProgramMeasuring + " to negator (default 0))");
        System.out.println(" negatingWordsDontOccurBeforeSentiment (don't negate " + this.c.options.sgProgramMeasuring + " after negatives)");
        System.out.println("   maxWordsBeforeSentimentToNegate (max from negator to " + this.c.options.sgProgramMeasuring + " (default 0))");
        System.out.println(" noIdioms (ignore idiom list)");
        System.out.println(" questionsReduceNeg (-ve sentiment reduced in questions)");
        System.out.println(" noEmoticons (ignore emoticon list)");
        System.out.println(" exclamations2 (sentence with ! counts as +2 if otherwise neutral)");
        System.out.println(" minPunctuationWithExclamation (min punctuation with ! to boost term " + this.c.options.sgProgramMeasuring + ")");
        System.out.println(" mood [-1,0,1] (default 1: -1 assume neutral emphasis is neg, 1, assume is pos");
        System.out.println(" noMultiplePosWords (multiple +ve words don't increase " + this.c.options.sgProgramPos + ")");
        System.out.println(" noMultipleNegWords (multiple -ve words don't increase " + this.c.options.sgProgramNeg + ")");
        System.out.println(" noIgnoreBoosterWordsAfterNegatives (don't ignore boosters after negating words)");
        System.out.println(" noDictionary (don't try to correct spellings using the dictionary)");
        System.out.println(" noMultipleLetters (don't use additional letters in a word to boost " + this.c.options.sgProgramMeasuring + ")");
        System.out.println(" noDeleteExtraDuplicateLetters (don't delete extra duplicate letters in words)");
        System.out.println(" illegalDoubleLettersInWordMiddle [letters never duplicate in word middles]");
        System.out.println("    default for English: ahijkquvxyz (specify list without spaces)");
        System.out.println(" illegalDoubleLettersAtWordEnd [letters never duplicate at word ends]");
        System.out.println("    default for English: achijkmnpqruvwxyz (specify list without spaces)");
        System.out.println(" sentenceCombineAv (average " + this.c.options.sgProgramMeasuring + " strength of terms in each sentence) OR");
        System.out.println(" sentenceCombineTot (total the " + this.c.options.sgProgramMeasuring + " strength of terms in each sentence)");
        System.out.println(" paragraphCombineAv (average " + this.c.options.sgProgramMeasuring + " strength of sentences in each text) OR");
        System.out.println(" paragraphCombineTot (total the " + this.c.options.sgProgramMeasuring + " strength of sentences in each text)");
        System.out.println("  *the default for the above 4 options is the maximum, not the total or average");
        System.out.println(" negativeMultiplier [negative total strength polarity multiplier, default 1.5]");
        System.out.println(" capitalsBoostTermSentiment (" + this.c.options.sgProgramMeasuring + " words in CAPITALS are stronger)");
        System.out.println(" alwaysSplitWordsAtApostrophes (e.g., t'aime -> t ' aime)");
        System.out.println(" MinSentencePosForQuotesIrony [integer] quotes in +ve sentences indicate irony");
        System.out.println(" MinSentencePosForPunctuationIrony [integer] +ve ending in !!+ indicates irony");
        System.out.println(" MinSentencePosForTermsIrony [integer] irony terms in +ve sent. indicate irony");
        System.out.println(" MinSentencePosForAllIrony [integer] all of the above irony terms");
        System.out.println(" lang [ISO-639 lower-case two-letter langauge code] set processing language");
        System.out.println("=Input and Output=");
        System.out.println(" explain (explain classification after results)");
        System.out.println(" echo (echo original text after results [for pipeline processes])");
        System.out.println(" UTF8 (force all processing to be in UTF-8 format)");
        System.out.println(" urlencoded (input and output text is URL encoded)");
        System.out.println("=Advanced - machine learning [1st input line ignored]=");
        System.out.println(" termWeights (list terms in badly classified texts; must specify inputFile)");
        System.out.println(" optimise [Filename for optimal term strengths (eg. EmotionLookupTable2.txt)]");
        System.out.println(" train (evaluate " + this.c.options.sgProgramName + " by training term strengths on results in file)");
        System.out.println("   all (test all option variations rather than use default)");
        System.out.println("   numCorrect (optimise by # correct - not total classification difference)");
        System.out.println("   iterations [number of 10-fold iterations] (default 1)");
        System.out.println("   minImprovement [min. accuracy improvement to change " + this.c.options.sgProgramMeasuring + " weights (default 1)]");
        System.out.println("   multi [# duplicate term strength optimisations to change " + this.c.options.sgProgramMeasuring + " weights (default 1)]");
    }

    /**
     * getCorpus方法用于返回成员变量c.
     *
     * @return 成员变量c
     */
    public Corpus getCorpus() {
        return this.c;
    }


}

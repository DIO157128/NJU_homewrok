package uk.ac.wlv.utilities.resourcesAndOptionsParameter;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.ClassificationResources;
import uk.ac.wlv.utilities.ArgsXMLUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ParametersForCorpusParse {

    private ClassificationOptions options;
    private ClassificationResources resources;
    boolean[] bArgumentRecognised;


    private ArrayList<String> argsList;
    private ArrayList<String> methodsList;

    /**
     * 有参构造函数.
     *
     * @param bArgumentRecognised
     * @param argsXMLUtil
     */
    public ParametersForCorpusParse(boolean[] bArgumentRecognised, ArgsXMLUtil argsXMLUtil) {
        this.options = new ClassificationOptions();
        this.resources = new ClassificationResources();
        this.bArgumentRecognised = bArgumentRecognised;

        this.argsList = argsXMLUtil.getArgs("argsForCorpusConfig.xml");
        this.methodsList = argsXMLUtil.getMethods("argsForCorpusParse.xml");
    }

    /**
     * 无参构造函数.
     */
    public ParametersForCorpusParse() {
    }


    /**
     * 解析corpus所需的参数.
     *
     * @param args
     */
    public void parse(String[] args) {
        Class<Object> thisClass = null;
        Method method = null;

        try {
            thisClass = (Class<Object>) Class.forName("uk.ac.wlv.utilities.resourcesAndOptionsParameter.ParametersForCorpusParse");
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }

        for (int i = 0; i < args.length; i++) {
            if (argsList.contains(args[i].toLowerCase())) {
                String methodName = methodsList.get(argsList.indexOf(args[i].toLowerCase()));
                try {
                    Method[] methods = thisClass.getMethods();
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

    }

    /**
     * 获取options.
     *
     * @return options
     */
    public ClassificationOptions getOptions() {
        return options;
    }

    /**
     * 获取resources.
     *
     * @return resources
     */
    public ClassificationResources getResources() {
        return resources;
    }

    /**
     * 解析pair参数.
     *
     * @param i
     */
    private void pairParameterArg(int i) {
        bArgumentRecognised[i] = true;
        bArgumentRecognised[i + 1] = true;
    }

    /**
     * 解析one参数.
     *
     * @param i
     */
    private void oneParameterArg(int i) {
        bArgumentRecognised[i] = true;
    }


    /**
     * 解析sentidata.
     *
     * @param i
     * @param args
     */
    public void parseSentidata(Integer i, String[] args) {
        this.resources.sgSentiStrengthFolder = args[i + 1];
        pairParameterArg(i);
    }

    /**
     * 解析emotionLookupTable.
     *
     * @param i
     * @param args
     */
    public void parseEmotionlookuptable(Integer i, String[] args) {
        this.resources.sgSentimentWordsFile = args[i + 1];
        pairParameterArg(i);
    }

    /**
     * 解析additionalFile.
     *
     * @param i
     * @param args
     */
    public void parseAdditionalfile(Integer i, String[] args) {
        this.resources.sgAdditionalFile = args[i + 1];
        pairParameterArg(i);
    }

    /**
     * 解析keywords.
     *
     * @param i
     * @param args
     */
    public void parseKeywords(Integer i, String[] args) {
        this.options.parseKeywordList(args[i + 1].toLowerCase());
        pairParameterArg(i);
    }

    /**
     * 解析wordsBeforeKeywords.
     *
     * @param i
     * @param args
     */
    public void parseWordsBeforeKeywords(Integer i, String[] args) {
        this.options.igWordsToIncludeBeforeKeyword = Integer.parseInt(args[i + 1]);
        pairParameterArg(i);
    }

    /**
     * 解析wordsAfterKeyWords.
     *
     * @param i
     * @param args
     */
    public void parseWordsAfterKeywords(Integer i, String[] args) {
        this.options.igWordsToIncludeAfterKeyword = Integer.parseInt(args[i + 1]);
        pairParameterArg(i);
    }

    /**
     * 解析sentiment.
     *
     * @param i
     * @param args
     */
    public void parseSentiment(Integer i, String[] args) {
        this.options.nameProgram(false);
        oneParameterArg(i);
    }

    /**
     * 解析stress.
     *
     * @param i
     * @param args
     */
    public void parseStress(Integer i, String[] args) {
        this.options.nameProgram(true);
        oneParameterArg(i);
    }

    /**
     * 解析trinary.
     *
     * @param i
     * @param args
     */
    public void parseTrinary(Integer i, String[] args) {
        this.options.bgTrinaryMode = true;
        oneParameterArg(i);
    }

    /**
     * 解析binary.
     *
     * @param i
     * @param args
     */
    public void parseBinary(Integer i, String[] args) {
        this.options.bgBinaryVersionOfTrinaryMode = true;
        this.options.bgTrinaryMode = true;
        oneParameterArg(i);
    }

    /**
     * 解析scale.
     *
     * @param i
     * @param args
     */
    public void parseScale(Integer i, String[] args) {
        this.options.bgScaleMode = true;
        oneParameterArg(i);
        if (this.options.bgTrinaryMode) {
            System.out.println("Must choose binary/trinary OR scale mode");
            System.exit(-1);
        }
    }

    /**
     * 解析SentenceCombineAv.
     *
     * @param i
     * @param args
     */
    public void parseSentenceCombineAv(Integer i, String[] args) {
        ClassificationOptions var10000 = this.options;
        this.options.getClass();
        var10000.igEmotionSentenceCombineMethod = 1;
        oneParameterArg(i);
    }

    /**
     * 解析SentenceCombineTot.
     *
     * @param i
     * @param args
     */
    public void parseSentenceCombineTot(Integer i, String[] args) {
        ClassificationOptions var10000 = this.options;
        this.options.getClass();
        var10000.igEmotionSentenceCombineMethod = 2;
        oneParameterArg(i);
    }

    /**
     * 解析ParagraphCombineAv.
     *
     * @param i
     * @param args
     */
    public void parseParagraphCombineAv(Integer i, String[] args) {
        ClassificationOptions var10000 = this.options;
        this.options.getClass();
        var10000.igEmotionParagraphCombineMethod = 1;
        oneParameterArg(i);
    }

    /**
     * 解析ParagraphCombineTot.
     *
     * @param i
     * @param args
     */
    public void parseParagraphCombineTot(Integer i, String[] args) {
        ClassificationOptions var10000 = this.options;
        this.options.getClass();
        var10000.igEmotionParagraphCombineMethod = 2;
        oneParameterArg(i);
    }

    /**
     * 解析NegativeMultiplier.
     *
     * @param i
     * @param args
     */
    public void parseNegativeMultiplier(Integer i, String[] args) {
        this.options.fgNegativeSentimentMultiplier = Float.parseFloat(args[i + 1]);
        pairParameterArg(i);
    }

    /**
     * 解析NoBoosters.
     *
     * @param i
     * @param args
     */
    public void parseNoBoosters(Integer i, String[] args) {
        this.options.bgBoosterWordsChangeEmotion = false;
        oneParameterArg(i);
    }

    /**
     * 解析NoNegatingPositiveFlipsEmotion.
     *
     * @param i
     * @param args
     */
    public void parseNoNegatingPositiveFlipsEmotion(Integer i, String[] args) {
        this.options.bgNegatingPositiveFlipsEmotion = false;
        oneParameterArg(i);
    }

    /**
     * 解析NoNegatingNegativeNeutralisesEmotion.
     *
     * @param i
     * @param args
     */
    public void parseNoNegatingNegativeNeutralisesEmotion(Integer i, String[] args) {
        this.options.bgNegatingNegativeNeutralisesEmotion = false;
        oneParameterArg(i);
    }

    /**
     * 解析NoNegators.
     *
     * @param i
     * @param args
     */
    public void parseNoNegators(Integer i, String[] args) {
        this.options.bgNegatingWordsFlipEmotion = false;
        oneParameterArg(i);
    }

    /**
     * 解析NoIdioms.
     *
     * @param i
     * @param args
     */
    public void parseNoIdioms(Integer i, String[] args) {
        this.options.bgUseIdiomLookupTable = false;
        oneParameterArg(i);
    }

    /**
     * 解析QuestionsReduceNeg.
     *
     * @param i
     * @param args
     */
    public void parseQuestionsReduceNeg(Integer i, String[] args) {
        this.options.bgReduceNegativeEmotionInQuestionSentences = true;
        oneParameterArg(i);
    }

    /**
     * 解析NoEmoticons.
     *
     * @param i
     * @param args
     */
    public void parseNoEmoticons(Integer i, String[] args) {
        this.options.bgUseEmoticons = false;
        oneParameterArg(i);
    }

    /**
     * 解析Exclamations2.
     *
     * @param i
     * @param args
     */
    public void parseExclamations2(Integer i, String[] args) {
        this.options.bgExclamationInNeutralSentenceCountsAsPlus2 = true;
        oneParameterArg(i);
    }

    /**
     * 解析MinPunctuationWithExclamation.
     *
     * @param i
     * @param args
     */
    public void parseMinPunctuationWithExclamation(Integer i, String[] args) {
        this.options.igMinPunctuationWithExclamationToChangeSentenceSentiment = Integer.parseInt(args[i + 1]);
        pairParameterArg(i);
    }

    /**
     * 解析Mood.
     *
     * @param i
     * @param args
     */
    public void parseMood(Integer i, String[] args) {
        this.options.igMoodToInterpretNeutralEmphasis = Integer.parseInt(args[i + 1]);
        pairParameterArg(i);
    }

    /**
     * 解析NoMultiplePosWords.
     *
     * @param i
     * @param args
     */
    public void parseNoMultiplePosWords(Integer i, String[] args) {
        this.options.bgAllowMultiplePositiveWordsToIncreasePositiveEmotion = false;
        oneParameterArg(i);
    }

    /**
     * 解析NoMultipleNegWords.
     *
     * @param i
     * @param args
     */
    public void parseNoMultipleNegWords(Integer i, String[] args) {
        this.options.bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion = false;
        oneParameterArg(i);
    }

    /**
     * 解析NoIgnoreBoosterWordsAfterNegatives.
     *
     * @param i
     * @param args
     */
    public void parseNoIgnoreBoosterWordsAfterNegatives(Integer i, String[] args) {
        this.options.bgIgnoreBoosterWordsAfterNegatives = false;
        oneParameterArg(i);
    }

    /**
     * 解析NoDictionary.
     *
     * @param i
     * @param args
     */
    public void parseNoDictionary(Integer i, String[] args) {
        this.options.bgCorrectSpellingsUsingDictionary = false;
        oneParameterArg(i);
    }

    /**
     * 解析NoDeleteExtraDuplicateLetters.
     *
     * @param i
     * @param args
     */
    public void parseNoDeleteExtraDuplicateLetters(Integer i, String[] args) {
        this.options.bgCorrectExtraLetterSpellingErrors = false;
        oneParameterArg(i);
    }

    /**
     * 解析IllegalDoubleLettersInWordMiddle.
     *
     * @param i
     * @param args
     */
    public void parseIllegalDoubleLettersInWordMiddle(Integer i, String[] args) {
        this.options.sgIllegalDoubleLettersInWordMiddle = args[i + 1].toLowerCase();
        pairParameterArg(i);
    }

    /**
     * 解析IllegalDoubleLettersAtWordEnd.
     *
     * @param i
     * @param args
     */
    public void parseIllegalDoubleLettersAtWordEnd(Integer i, String[] args) {
        this.options.sgIllegalDoubleLettersAtWordEnd = args[i + 1].toLowerCase();
        pairParameterArg(i);
    }

    /**
     * 解析NoMultipleLetters.
     *
     * @param i
     * @param args
     */
    public void parseNoMultipleLetters(Integer i, String[] args) {
        this.options.bgMultipleLettersBoostSentiment = false;
        oneParameterArg(i);
    }

    /**
     * 解析NegatedWordStrengthMultiplier.
     *
     * @param i
     * @param args
     */
    public void parseNegatedWordStrengthMultiplier(Integer i, String[] args) {
        this.options.fgStrengthMultiplierForNegatedWords = Float.parseFloat(args[i + 1]);
        pairParameterArg(i);
    }

    /**
     * 解析MaxWordsBeforeSentimentToNegate.
     *
     * @param i
     * @param args
     */
    public void parseMaxWordsBeforeSentimentToNegate(Integer i, String[] args) {
        this.options.igMaxWordsBeforeSentimentToNegate = Integer.parseInt(args[i + 1]);
        pairParameterArg(i);
    }

    /**
     * 解析NegatingWordsDontOccurBeforeSentiment.
     *
     * @param i
     * @param args
     */
    public void parseNegatingWordsDontOccurBeforeSentiment(Integer i, String[] args) {
        this.options.bgNegatingWordsOccurBeforeSentiment = false;
        oneParameterArg(i);
    }

    /**
     * 解析MaxWordsAfterSentimentToNegate.
     *
     * @param i
     * @param args
     */
    public void parseMaxWordsAfterSentimentToNegate(Integer i, String[] args) {
        this.options.igMaxWordsAfterSentimentToNegate = Integer.parseInt(args[i + 1]);
        pairParameterArg(i);
    }

    /**
     * 解析NegatingWordsOccurAfterSentiment.
     *
     * @param i
     * @param args
     */
    public void parseNegatingWordsOccurAfterSentiment(Integer i, String[] args) {
        this.options.bgNegatingWordsOccurBeforeSentiment = true;
        oneParameterArg(i);
    }

    /**
     * 解析AlwaysSplitWordsAtApostrophes.
     *
     * @param i
     * @param args
     */
    public void parseAlwaysSplitWordsAtApostrophes(Integer i, String[] args) {
        this.options.bgAlwaysSplitWordsAtApostrophes = true;
        oneParameterArg(i);
    }

    /**
     * 解析CapitalsBoostTermSentiment.
     *
     * @param i
     * @param args
     */
    public void parseCapitalsBoostTermSentiment(Integer i, String[] args) {
        this.options.bgCapitalsBoostTermSentiment = true;
        oneParameterArg(i);
    }

    /**
     * 解析LemmaFile.
     *
     * @param i
     * @param args
     */
    public void parseLemmaFile(Integer i, String[] args) {
        this.options.bgUseLemmatisation = true;
        this.resources.sgLemmaFile = args[i + 1];
        pairParameterArg(i);
    }

    /**
     * 解析MinSentencePosForQuotesIrony.
     *
     * @param i
     * @param args
     */
    public void parseMinSentencePosForQuotesIrony(Integer i, String[] args) {
        this.options.igMinSentencePosForQuotesIrony = Integer.parseInt(args[i + 1]);
        pairParameterArg(i);
    }

    /**
     * 解析MinSentencePosForPunctuationIrony.
     *
     * @param i
     * @param args
     */
    public void parseMinSentencePosForPunctuationIrony(Integer i, String[] args) {
        this.options.igMinSentencePosForPunctuationIrony = Integer.parseInt(args[i + 1]);
        pairParameterArg(i);

    }

    /**
     * 解析MinSentencePosForTermsIrony.
     *
     * @param i
     * @param args
     */
    public void parseMinSentencePosForTermsIrony(Integer i, String[] args) {
        this.options.igMinSentencePosForTermsIrony = Integer.parseInt(args[i + 1]);
        pairParameterArg(i);

    }

    /**
     * 解析MinSentencePosForAllIrony.
     *
     * @param i
     * @param args
     */
    public void parseMinSentencePosForAllIrony(Integer i, String[] args) {
        this.options.igMinSentencePosForTermsIrony = Integer.parseInt(args[i + 1]);
        this.options.igMinSentencePosForPunctuationIrony = this.options.igMinSentencePosForTermsIrony;
        this.options.igMinSentencePosForQuotesIrony = this.options.igMinSentencePosForTermsIrony;
        pairParameterArg(i);
    }

    /**
     * 解析Explain.
     *
     * @param i
     * @param args
     */
    public void parseExplain(Integer i, String[] args) {
        this.options.bgExplainClassification = true;
        oneParameterArg(i);
    }

    /**
     * 解析Echo.
     *
     * @param i
     * @param args
     */
    public void parseEcho(Integer i, String[] args) {
        this.options.bgEchoText = true;
        oneParameterArg(i);
    }

    /**
     * 解析UTF8.
     *
     * @param i
     * @param args
     */
    public void parseUTF8(Integer i, String[] args) {
        this.options.bgForceUTF8 = true;
        oneParameterArg(i);
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

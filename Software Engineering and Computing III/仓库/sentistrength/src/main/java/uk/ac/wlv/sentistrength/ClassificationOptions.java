//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package uk.ac.wlv.sentistrength;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 * 相关用例: UC-26
 * classificationOptions 类包含了情绪分析的一系列参数和选项,
 * 包括了一些用于控制情绪是怎样预测、结合、增强的选项
 * 不仅如此,classificationOptions类也包含了是否纠正拼写错误和分词规则的选项
 * 以及一列类关于选项的设置方法和打印方法
 * 默认选项基于 SentiStrength program.
 */
public class ClassificationOptions {
    public boolean bgTensiStrength = false;
    public String sgProgramName = "SentiStrength";
    public String sgProgramMeasuring = "sentiment";
    public String sgProgramPos = "positive sentiment";
    public String sgProgramNeg = "negative sentiment";
    public boolean bgScaleMode = false;
    public boolean bgTrinaryMode = false;
    public boolean bgBinaryVersionOfTrinaryMode = false;
    public int igDefaultBinaryClassification = 1;
    public int igEmotionParagraphCombineMethod = 0;
    final int igCombineMax = 0;
    final int igCombineAverage = 1;
    final int igCombineTotal = 2;
    private final int ten = 10;
    private final int four = 4;
    public int igEmotionSentenceCombineMethod = 0;
    private static final float F = 1.5F;
    public float fgNegativeSentimentMultiplier = F;
    public boolean bgReduceNegativeEmotionInQuestionSentences = false;
    public boolean bgMissCountsAsPlus2 = true;
    public boolean bgYouOrYourIsPlus2UnlessSentenceNegative = false;
    //中性句中的感叹号+2
    public boolean bgExclamationInNeutralSentenceCountsAsPlus2 = false;
    //少用感叹号改变句子感情
    public int igMinPunctuationWithExclamationToChangeSentenceSentiment = 0;
    public boolean bgUseIdiomLookupTable = true;
    public boolean bgUseObjectEvaluationTable = false;
    public boolean bgCountNeutralEmotionsAsPositiveForEmphasis1 = true;
    //解释中性强调的语气
    public int igMoodToInterpretNeutralEmphasis = 1;
    public boolean bgAllowMultiplePositiveWordsToIncreasePositiveEmotion = true;
    public boolean bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion = true;
    public boolean bgIgnoreBoosterWordsAfterNegatives = true;
    public boolean bgCorrectSpellingsUsingDictionary = true;
    //纠正额外的字母拼写错误
    public boolean bgCorrectExtraLetterSpellingErrors = true;
    public String sgIllegalDoubleLettersInWordMiddle = "ahijkquvxyz";
    public String sgIllegalDoubleLettersAtWordEnd = "achijkmnpqruvwxyz";
    public boolean bgMultipleLettersBoostSentiment = true;
    public boolean bgBoosterWordsChangeEmotion = true;
    //在省略号处分开字符
    public boolean bgAlwaysSplitWordsAtApostrophes = false;
    public boolean bgNegatingWordsOccurBeforeSentiment = true;
    public int igMaxWordsBeforeSentimentToNegate = 0;
    public boolean bgNegatingWordsOccurAfterSentiment = false;
    public int igMaxWordsAfterSentimentToNegate = 0;
    //否定积极的情绪可以翻转情绪
    public boolean bgNegatingPositiveFlipsEmotion = true;
    //否定消极的情绪可以中和情绪
    public boolean bgNegatingNegativeNeutralisesEmotion = true;
    public boolean bgNegatingWordsFlipEmotion = false;
    public float fgStrengthMultiplierForNegatedWords = HALFF;
    public boolean bgCorrectSpellingsWithRepeatedLetter = true;
    public boolean bgUseEmoticons = true;
    public boolean bgCapitalsBoostTermSentiment = false;
    //最小强调字母重复数
    public int igMinRepeatedLettersForBoost = 2;
    public String[] sgSentimentKeyWords = null;
    public boolean bgIgnoreSentencesWithoutKeywords = false;
    public int igWordsToIncludeBeforeKeyword = four;
    public int igWordsToIncludeAfterKeyword = four;
    public boolean bgExplainClassification = false;
    public boolean bgEchoText = false;
    public boolean bgForceUTF8 = false;
    public boolean bgUseLemmatisation = false;
    //引用反语的最小句子位置
    public int igMinSentencePosForQuotesIrony = ten;
    public int igMinSentencePosForPunctuationIrony = ten;
    public int igMinSentencePosForTermsIrony = ten;
    private static final float HALFF = 0.5F;

    /**
     * 不带参的初始化.
     */
    public ClassificationOptions() {
    }

    /**
     * 分析以逗号分隔的关键字列表，并将其中的关键字内容设置为情绪分析的情绪关键字。还将标志设置为忽略不包含任何关键字的句子.
     *
     * @param sKeywordList 以逗号分隔的关键字字符串
     */
    public void parseKeywordList(String sKeywordList) {
        this.sgSentimentKeyWords = sKeywordList.split(",");
        this.bgIgnoreSentencesWithoutKeywords = true;
    }


    /**
     * 将分类选项打印到 BufferedWriter.
     * 这些选项包括组合段落和句子情绪的方法、使用总差分或精确计数的标准、多重优化的数量以及用于情绪分析的各种标志和参数。
     *
     * @param wWriter             用于将选项写入的缓冲编写器
     * @param iMinImprovement     表示分类最小改进的整数
     * @param bUseTotalDifference 指示是使用总差还是精确计数的布尔值
     * @param iMultiOptimisations 表示多重优化次数的整数
     * @return 如果打印成功，则为真，否则为假
     */
    public boolean printClassificationOptions(BufferedWriter wWriter, int iMinImprovement, boolean bUseTotalDifference, int iMultiOptimisations) {
        try {
            if (this.igEmotionParagraphCombineMethod == 0) {
                wWriter.write("Max");
            } else if (this.igEmotionParagraphCombineMethod == 1) {
                wWriter.write("Av");
            } else {
                wWriter.write("Tot");
            }

            if (this.igEmotionSentenceCombineMethod == 0) {
                wWriter.write("\tMax");
            } else if (this.igEmotionSentenceCombineMethod == 1) {
                wWriter.write("\tAv");
            } else {
                wWriter.write("\tTot");
            }

            if (bUseTotalDifference) {
                wWriter.write("\tTotDiff");
            } else {
                wWriter.write("\tExactCount");
            }

            wWriter.write("\t" + iMultiOptimisations + "\t" + this.bgReduceNegativeEmotionInQuestionSentences + "\t" + this.bgMissCountsAsPlus2 + "\t" + this.bgYouOrYourIsPlus2UnlessSentenceNegative + "\t" + this.bgExclamationInNeutralSentenceCountsAsPlus2 + "\t" + this.bgUseIdiomLookupTable + "\t" + this.igMoodToInterpretNeutralEmphasis + "\t" + this.bgAllowMultiplePositiveWordsToIncreasePositiveEmotion + "\t" + this.bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion + "\t" + this.bgIgnoreBoosterWordsAfterNegatives + "\t" + this.bgMultipleLettersBoostSentiment + "\t" + this.bgBoosterWordsChangeEmotion + "\t" + this.bgNegatingWordsFlipEmotion + "\t" + this.bgNegatingPositiveFlipsEmotion + "\t" + this.bgNegatingNegativeNeutralisesEmotion + "\t" + this.bgCorrectSpellingsWithRepeatedLetter + "\t" + this.bgUseEmoticons + "\t" + this.bgCapitalsBoostTermSentiment + "\t" + this.igMinRepeatedLettersForBoost + "\t" + this.igMaxWordsBeforeSentimentToNegate + "\t" + iMinImprovement);
            return true;
        } catch (IOException var6) {
            var6.printStackTrace();
            return false;
        }
    }

    /**
     * 将 "~    ~   BaselineMajorityClass	~	~	~	~	~	~	~	~	~	~	~	~	~	~	~	~	~	~	~	~	~" 打印到BufferedWriter,
     * 用于提示后续打印的内容.
     *
     * @param wWriter 用于将提示内容写入的缓冲编写器
     * @return 如果打印成功，则为真，否则为假
     */
    public boolean printBlankClassificationOptions(BufferedWriter wWriter) {
        try {
            wWriter.write("~");
            wWriter.write("\t~");
            wWriter.write("\tBaselineMajorityClass");
            wWriter.write("\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~\t~");
            return true;
        } catch (IOException var3) {
            var3.printStackTrace();
            return false;
        }
    }


    /**
     * 将情绪分析的分类选项的主题打印到缓冲编写器.
     * 主题由制表符分隔，包括组合段落和句子情感的方法、使用总差分或精确计数的标准、多重优化的数量以及用于情绪分析的各种标志和参数
     *
     * @param wWriter 用于将主题写入的缓冲编写器
     * @return 如果打印成功，则为真，否则为假
     */
    public boolean printClassificationOptionsHeadings(BufferedWriter wWriter) {
        try {
            wWriter.write("EmotionParagraphCombineMethod\tEmotionSentenceCombineMethod\tDifferenceCalculationMethodForTermWeightAdjustments\tMultiOptimisations\tReduceNegativeEmotionInQuestionSentences\tMissCountsAsPlus2\tYouOrYourIsPlus2UnlessSentenceNegative\tExclamationCountsAsPlus2\tUseIdiomLookupTable\tMoodToInterpretNeutralEmphasis\tAllowMultiplePositiveWordsToIncreasePositiveEmotion\tAllowMultipleNegativeWordsToIncreaseNegativeEmotion\tIgnoreBoosterWordsAfterNegatives\tMultipleLettersBoostSentiment\tBoosterWordsChangeEmotion\tNegatingWordsFlipEmotion\tNegatingPositiveFlipsEmotion\tNegatingNegativeNeutralisesEmotion\tCorrectSpellingsWithRepeatedLetter\tUseEmoticons\tCapitalsBoostTermSentiment\tMinRepeatedLettersForBoost\tWordsBeforeSentimentToNegate\tMinImprovement");
            return true;
        } catch (IOException var3) {
            var3.printStackTrace();
            return false;
        }
    }


    /**
     * 从文件中设置此对象的分类选项。该文件包含选项的名称和值，用制表符分隔，每行一个选项.
     *
     * @param sFilename 一个字符串，表示包含选项值的文件的名称
     * @return 如果设置成功，则为 true，否则为 false
     */
    public boolean setClassificationOptions(String sFilename) {
        try {
            BufferedReader rReader = new BufferedReader(new FileReader(sFilename));

            while (rReader.ready()) {
                String sLine = rReader.readLine();
                int iTabPos = sLine.indexOf("\t");
                if (iTabPos > 0) {
                    String[] sData = sLine.split("\t");
                    if (sData[0] == "EmotionParagraphCombineMethod") {
                        if (sData[1].indexOf("Max") >= 0) {
                            this.igEmotionParagraphCombineMethod = 0;
                        }

                        if (sData[1].indexOf("Av") >= 0) {
                            this.igEmotionParagraphCombineMethod = 1;
                        }

                        if (sData[1].indexOf("Tot") >= 0) {
                            this.igEmotionParagraphCombineMethod = 2;
                        }
                    } else if (sData[0] == "EmotionSentenceCombineMethod") {
                        if (sData[1].indexOf("Max") >= 0) {
                            this.igEmotionSentenceCombineMethod = 0;
                        }

                        if (sData[1].indexOf("Av") >= 0) {
                            this.igEmotionSentenceCombineMethod = 1;
                        }

                        if (sData[1].indexOf("Tot") >= 0) {
                            this.igEmotionSentenceCombineMethod = 2;
                        }
                    } else if (sData[0] == "IgnoreNegativeEmotionInQuestionSentences") {
                        this.bgReduceNegativeEmotionInQuestionSentences = Boolean.parseBoolean(sData[1]);
                    } else if (sData[0] == "MissCountsAsPlus2") {
                        this.bgMissCountsAsPlus2 = Boolean.parseBoolean(sData[1]);
                    } else if (sData[0] == "YouOrYourIsPlus2UnlessSentenceNegative") {
                        this.bgYouOrYourIsPlus2UnlessSentenceNegative = Boolean.parseBoolean(sData[1]);
                    } else if (sData[0] == "ExclamationCountsAsPlus2") {
                        this.bgExclamationInNeutralSentenceCountsAsPlus2 = Boolean.parseBoolean(sData[1]);
                    } else if (sData[0] == "UseIdiomLookupTable") {
                        this.bgUseIdiomLookupTable = Boolean.parseBoolean(sData[1]);
                    } else if (sData[0] == "Mood") {
                        this.igMoodToInterpretNeutralEmphasis = Integer.parseInt(sData[1]);
                    } else if (sData[0] == "AllowMultiplePositiveWordsToIncreasePositiveEmotion") {
                        this.bgAllowMultiplePositiveWordsToIncreasePositiveEmotion = Boolean.parseBoolean(sData[1]);
                    } else if (sData[0] == "AllowMultipleNegativeWordsToIncreaseNegativeEmotion") {
                        this.bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion = Boolean.parseBoolean(sData[1]);
                    } else if (sData[0] == "IgnoreBoosterWordsAfterNegatives") {
                        this.bgIgnoreBoosterWordsAfterNegatives = Boolean.parseBoolean(sData[1]);
                    } else if (sData[0] == "MultipleLettersBoostSentiment") {
                        this.bgMultipleLettersBoostSentiment = Boolean.parseBoolean(sData[1]);
                    } else if (sData[0] == "BoosterWordsChangeEmotion") {
                        this.bgBoosterWordsChangeEmotion = Boolean.parseBoolean(sData[1]);
                    } else if (sData[0] == "NegatingWordsFlipEmotion") {
                        this.bgNegatingWordsFlipEmotion = Boolean.parseBoolean(sData[1]);
                    } else if (sData[0] == "NegatingWordsFlipEmotion") {
                        this.bgNegatingPositiveFlipsEmotion = Boolean.parseBoolean(sData[1]);
                    } else if (sData[0] == "NegatingWordsFlipEmotion") {
                        this.bgNegatingNegativeNeutralisesEmotion = Boolean.parseBoolean(sData[1]);
                    } else if (sData[0] == "CorrectSpellingsWithRepeatedLetter") {
                        this.bgCorrectSpellingsWithRepeatedLetter = Boolean.parseBoolean(sData[1]);
                    } else if (sData[0] == "UseEmoticons") {
                        this.bgUseEmoticons = Boolean.parseBoolean(sData[1]);
                    } else if (sData[0] == "CapitalsAreSentimentBoosters") {
                        this.bgCapitalsBoostTermSentiment = Boolean.parseBoolean(sData[1]);
                    } else if (sData[0] == "MinRepeatedLettersForBoost") {
                        this.igMinRepeatedLettersForBoost = Integer.parseInt(sData[1]);
                    } else if (sData[0] == "WordsBeforeSentimentToNegate") {
                        this.igMaxWordsBeforeSentimentToNegate = Integer.parseInt(sData[1]);
                    } else if (sData[0] == "Trinary") {
                        this.bgTrinaryMode = true;
                    } else if (sData[0] == "Binary") {
                        this.bgTrinaryMode = true;
                        this.bgBinaryVersionOfTrinaryMode = true;
                    } else {
                        if (sData[0] != "Scale") {
                            rReader.close();
                            return false;
                        }

                        this.bgScaleMode = true;
                    }
                }
            }

            rReader.close();
            return true;
        } catch (FileNotFoundException var7) {
            var7.printStackTrace();
            return false;
        } catch (IOException var8) {
            var8.printStackTrace();
            return false;
        }
    }


    /**
     * 根据布尔标志命名程序.
     * 如果标志为真，则程序被命名为TensiStrength，并测量压力值和放松值。
     * 如果标志为假，则程序名为 SentiStrength 并测量情绪值。
     * 该方法还为测量的正方向（即放松值和积极情绪值）和负方向（即压力值和消极情绪值）设置相应的字符串。
     *
     * @param bTensiStrength 一个布尔标志，指示程序是TensiStrength还是SentiStrength。
     */
    public void nameProgram(boolean bTensiStrength) {
        this.bgTensiStrength = bTensiStrength;
        if (bTensiStrength) {
            this.sgProgramName = "TensiStrength";
            this.sgProgramMeasuring = "stress and relaxation";
            this.sgProgramPos = "relaxation";
            this.sgProgramNeg = "stress";
        } else {
            this.sgProgramName = "SentiStrength";
            this.sgProgramMeasuring = "sentiment";
            this.sgProgramPos = "positive sentiment";
            this.sgProgramNeg = "negative sentiment";
        }

    }
}

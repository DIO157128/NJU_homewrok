// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   ClassificationResources.java

package uk.ac.wlv.sentistrength;

import java.io.File;


import uk.ac.wlv.sentistrength.dictionary.special.EvaluativeTerms;
import uk.ac.wlv.sentistrength.dictionary.special.IdiomList;
import uk.ac.wlv.sentistrength.dictionary.special.Lemmatiser;
import uk.ac.wlv.sentistrength.dictionary.special.SentimentWords;
import uk.ac.wlv.sentistrength.dictionary.dicMap.*;
import uk.ac.wlv.sentistrength.dictionary.dicList.*;
import uk.ac.wlv.utilities.FileOps;

// Referenced classes of package uk.ac.wlv.sentistrength:
//            EmoticonsList, CorrectSpellingsList, SentimentWords, NegatingWordList,
//            QuestionWords, BoosterWordsList, IdiomList, EvaluativeTerms,
//            IronyList, Lemmatiser, ClassificationOptions


/**
 * 相关用例:UC-17,UC-18.
 * 分类资源包含用于文本分类的各种资源.
 * <p>
 * 这些资源包括：
 * <ul>
 *     <li> 表情符号列表
 *     <li> 拼写纠正列表
 *     <li> 情感词列表
 *     <li> 否定词列表
 *     <li> 疑问词列表
 *     <li> 助推词列表
 *     <li> 习语列表
 *     <li> 评估术语列表
 *     <li> 反讽词列表
 *     <li> 词形还原器
 * </ul>
 * 此外，此类还包含这些资源的文件路径。
 */
public class ClassificationResources {
    public EmoticonsMap emoticons; // 表情符号列表
    public CorrectSpellingsList correctSpellings; // 拼写纠正列表
    public SentimentWords sentimentWords; // 情感词列表
    public NegatingWordList negatingWords; // 否定词列表
    public QuestionWords questionWords; // 疑问词列表
    public BoosterWordsMap boosterWords; // 助推词列表
    public IdiomList idiomList; // 习语列表
    public EvaluativeTerms evaluativeTerms; // 评估术语列表
    public IronyList ironyList; // 反讽词列表
    public Lemmatiser lemmatiser; // 词形还原器
    public String sgSentiStrengthFolder; // SentiStrength 文件夹路径
    public String sgSentimentWordsFile; // 情感词文件路径
    public String sgSentimentWordsFile2; // 情感词文件路径2
    public String sgEmoticonLookupTable; // 表情符号查找表路径
    public String sgCorrectSpellingFileName; // 拼写纠正文件路径
    public String sgCorrectSpellingFileName2; // 拼写纠正文件路径2
    public String sgSlangLookupTable; // 俚语查找表路径
    public String sgNegatingWordListFile; // 否定词列表文件路径
    public String sgBoosterListFile; // 助推词列表文件路径
    public String sgIdiomLookupTableFile; // 习语查找表文件路径
    public String sgQuestionWordListFile; // 疑问词列表文件路径
    public String sgIronyWordListFile; // 反讽词列表文件路径
    public String sgAdditionalFile; // 额外文件路径
    public String sgLemmaFile; // 词形还原文件路径

    /**
     * 带参的初始化.
     */
    public ClassificationResources() {
        emoticons = new EmoticonsMap();
        correctSpellings = new CorrectSpellingsList();
        sentimentWords = new SentimentWords();
        negatingWords = new NegatingWordList();
        questionWords = new QuestionWords();
        boosterWords = new BoosterWordsMap();
        idiomList = new IdiomList();
        evaluativeTerms = new EvaluativeTerms();
        ironyList = new IronyList();
        lemmatiser = new Lemmatiser();
        sgSentiStrengthFolder = System.getProperty("user.dir") + "/src/main/java/SentStrength_Data/";
        sgSentimentWordsFile = "EmotionLookupTable.txt";
        sgSentimentWordsFile2 = "SentimentLookupTable.txt";
        sgEmoticonLookupTable = "EmoticonLookupTable.txt";
        sgCorrectSpellingFileName = "Dictionary.txt";
        sgCorrectSpellingFileName2 = "EnglishWordList.txt";
        sgSlangLookupTable = "SlangLookupTable_NOT_USED.txt";
        sgNegatingWordListFile = "NegatingWordList.txt";
        sgBoosterListFile = "BoosterWordList.txt";
        sgIdiomLookupTableFile = "IdiomLookupTable.txt";
        sgQuestionWordListFile = "QuestionWords.txt";
        sgIronyWordListFile = "IronyTerms.txt";
        sgAdditionalFile = "";
        sgLemmaFile = "";
    }


    /**
     * 初始化分类资源类的实例.
     *
     * @param options 包含初始化选项的ClassificationOptions对象， options需要corpus类主动提供
     * @return 如果初始化成功，则返回 true;否则，返回 false。
     * @see uk.ac.wlv.sentistrength.ClassificationOptions
     */
    public boolean initialise(ClassificationOptions options) {
        int iExtraLinesToReserve = 0;
        //处理额外文件
        if (sgAdditionalFile.compareTo("") != 0) {
            iExtraLinesToReserve = FileOps.iCountLinesInTextFile((new StringBuilder(String.valueOf(sgSentiStrengthFolder))).append(sgAdditionalFile).toString());
            if (iExtraLinesToReserve < 0) {
                System.out.println((new StringBuilder("No lines found in additional file! Ignoring ")).append(sgAdditionalFile).toString());
                return false;
            }
        }

        if (options.bgUseLemmatisation && !lemmatiser.initialise((new StringBuilder(String.valueOf(sgSentiStrengthFolder))).append(sgLemmaFile).toString(), false)) {
            System.out.println((new StringBuilder("Can't load lemma file! ")).append(sgLemmaFile).toString());
            return false;
        }
        File f = new File((new StringBuilder(String.valueOf(sgSentiStrengthFolder))).append(sgSentimentWordsFile).toString());
        if (!f.exists() || f.isDirectory()) {
            sgSentimentWordsFile = sgSentimentWordsFile2;
        }
        File f2 = new File((new StringBuilder(String.valueOf(sgSentiStrengthFolder))).append(sgCorrectSpellingFileName).toString());
        if (!f2.exists() || f2.isDirectory()) {
            sgCorrectSpellingFileName = sgCorrectSpellingFileName2;
        }
        if (emoticons.initialise((new StringBuilder(String.valueOf(sgSentiStrengthFolder))).append(sgEmoticonLookupTable).toString(), options.bgForceUTF8) && correctSpellings.initialise((new StringBuilder(String.valueOf(sgSentiStrengthFolder))).append(sgCorrectSpellingFileName).toString(), options.bgForceUTF8) && sentimentWords.initialise((new StringBuilder(String.valueOf(sgSentiStrengthFolder))).append(sgSentimentWordsFile).toString(), options, iExtraLinesToReserve) && negatingWords.initialise((new StringBuilder(String.valueOf(sgSentiStrengthFolder))).append(sgNegatingWordListFile).toString(), options.bgForceUTF8) && questionWords.initialise((new StringBuilder(String.valueOf(sgSentiStrengthFolder))).append(sgQuestionWordListFile).toString(), options.bgForceUTF8) && ironyList.initialise((new StringBuilder(String.valueOf(sgSentiStrengthFolder))).append(sgIronyWordListFile).toString(), options.bgForceUTF8) && boosterWords.initialise((new StringBuilder(String.valueOf(sgSentiStrengthFolder))).append(sgBoosterListFile).toString(), options.bgForceUTF8) && idiomList.initialise((new StringBuilder(String.valueOf(sgSentiStrengthFolder))).append(sgIdiomLookupTableFile).toString(), options, iExtraLinesToReserve)) {
            if (iExtraLinesToReserve > 0) {
                return evaluativeTerms.initialise((new StringBuilder(String.valueOf(sgSentiStrengthFolder))).append(sgAdditionalFile).toString(), options, idiomList, sentimentWords);
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   Corpus.java

package uk.ac.wlv.sentistrength;

import java.io.*;

import uk.ac.wlv.sentistrength.paragraph.paragraph.Paragraph;
import uk.ac.wlv.sentistrength.paragraph.paragraphFactory.ParagraphFactory;
import uk.ac.wlv.utilities.FileOps;
import uk.ac.wlv.utilities.Sort;


// Referenced classes of package uk.ac.wlv.sentistrength:
//            ClassificationOptions, ClassificationResources, UnusedTermsClassificationIndex, Paragraph, 
//            ClassificationStatistics, SentimentWords

/**
 * 相关用例:UC-12,UC-27,UC-28,UC-29.
 * Corpus类优化数据集、扩充词典、机器学习评估.
 * 对于各种选项(PosNeg,binary,trinary,scale)都有相应处理.
 */
public class Corpus {
    private static final float HUNDRED_PERCENT = 100F;

    // Corpus类需要根据不同的option来进行不同的数据处理
    public ClassificationOptions options;
    public ClassificationResources resources;
    private Paragraph[] paragraph;
    private int igParagraphCount;
    // 不同选项下的正确情感评分表
    private int[] igPosCorrect;
    private int[] igNegCorrect;
    private int[] igTrinaryCorrect;
    private int[] igScaleCorrect;
    // 不同选项下的预测情感评分表
    private int[] igPosClass;
    private int[] igNegClass;
    private int[] igTrinaryClass;
    private int[] igScaleClass;
    //
    private boolean bgCorpusClassified;
    private int[] igSentimentIDList;
    private int igSentimentIDListCount;
    private int[] igSentimentIDParagraphCount;
    private boolean bSentimentIDListMade;
    UnusedTermsClassificationIndex unusedTermsClassificationIndex;
    private boolean[] bgSupcorpusMember;
    int igSupcorpusMemberCount;
    private ParagraphFactory paragraphFactory;
    private final int trinine = 999;
    private final int four = 4;
    private final int segfour = -4;
    private final int five = 5;
    private final int segthree = -3;
    private final int segten = -10;
    private final int segnine = -9;
    private final int segtwo = -2;
    private final int three = 3;
    private final int onehundred = 100;
    private final int ten = 10;
    private final int twoeight = 28;
    private final int twofour = 24;
    private final int twentythousands = 20000;
    private static final long TEN = 10L;
    private static final double FOURNINED = 9999D;
    private static final float TENF = 10F;
    private static final double HALFD = 0.5D;
    private static final float MORENINE = 9999999F;

    /**
     * 带参数的初始化.
     */
    public Corpus() {
        options = new ClassificationOptions();
        resources = new ClassificationResources();
        paragraphFactory = new ParagraphFactory();
        igParagraphCount = 0;
        bgCorpusClassified = false;
        igSentimentIDListCount = 0;
        bSentimentIDListMade = false;
        unusedTermsClassificationIndex = null;
    }


    /**
     * 集成四种选项,将段落加入索引表.
     */
    public void indexClassifiedCorpus() {
        unusedTermsClassificationIndex = new UnusedTermsClassificationIndex();
        if (options.bgScaleMode) {
            unusedTermsClassificationIndex.initialise(true, false, false, false);
            for (int i = 1; i <= igParagraphCount; i++) {
                paragraph[i].addParagraphToIndexWithScaleValues(unusedTermsClassificationIndex, igScaleCorrect[i], igScaleClass[i]);
            }

        } else if (options.bgTrinaryMode && options.bgBinaryVersionOfTrinaryMode) {
            unusedTermsClassificationIndex.initialise(false, false, true, false);
            for (int i = 1; i <= igParagraphCount; i++) {
                paragraph[i].addParagraphToIndexWithBinaryValues(unusedTermsClassificationIndex, igTrinaryCorrect[i], igTrinaryClass[i]);
            }

        } else if (options.bgTrinaryMode && !options.bgBinaryVersionOfTrinaryMode) {
            unusedTermsClassificationIndex.initialise(false, false, false, true);
            for (int i = 1; i <= igParagraphCount; i++) {
                paragraph[i].addParagraphToIndexWithTrinaryValues(unusedTermsClassificationIndex, igTrinaryCorrect[i], igTrinaryClass[i]);
            }

        } else {
            unusedTermsClassificationIndex.initialise(false, true, false, false);
            for (int i = 1; i <= igParagraphCount; i++) {
                paragraph[i].addParagraphToIndexWithPosNegValues(unusedTermsClassificationIndex, igPosCorrect[i], igPosClass[i], igNegCorrect[i], igNegClass[i]);
            }
        }
    }

    /**
     * 在指定选项下,打印输入文件中所有term的权重表.
     *
     * @param saveFile 更新后的词典存储路径
     * @param iMinFreq 在给定的人工标注的文本集中，单个term需要出现的最小次数
     */
    public void printCorpusUnusedTermsClassificationIndex(String saveFile, int iMinFreq) {
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        if (unusedTermsClassificationIndex == null) {
            indexClassifiedCorpus();
        }
        if (options.bgScaleMode) {
            unusedTermsClassificationIndex.printIndexWithScaleValues(saveFile, iMinFreq);
        } else if (options.bgTrinaryMode && options.bgBinaryVersionOfTrinaryMode) {
            unusedTermsClassificationIndex.printIndexWithBinaryValues(saveFile, iMinFreq);
        } else if (options.bgTrinaryMode && !options.bgBinaryVersionOfTrinaryMode) {
            unusedTermsClassificationIndex.printIndexWithTrinaryValues(saveFile, iMinFreq);
        } else {
            unusedTermsClassificationIndex.printIndexWithPosNegValues(saveFile, iMinFreq);
        }
        System.out.println((new StringBuilder("Term weights saved to ")).append(saveFile));
    }

    /**
     * 设置Subcorpus.
     *
     * @param bSubcorpusMember
     */
    public void setSubcorpus(boolean[] bSubcorpusMember) {
        igSupcorpusMemberCount = 0;
        for (int i = 0; i <= igParagraphCount; i++) {
            if (bSubcorpusMember[i]) {
                bgSupcorpusMember[i] = true;
                igSupcorpusMemberCount++;
            } else {
                bgSupcorpusMember[i] = false;
            }
        }
    }

    /**
     * 启用整个语料库而非子集.
     */
    public void useWholeCorpusNotSubcorpus() {
        for (int i = 0; i <= igParagraphCount; i++) {
            bgSupcorpusMember[i] = true;
        }
        igSupcorpusMemberCount = igParagraphCount;
    }

    /**
     * @return 语料库的大小.
     */
    public int getCorpusSize() {
        return igParagraphCount;
    }

    /**
     * 设置SingleTextAsCorpus.
     *
     * @param sText
     * @param iPosCorrect
     * @param iNegCorrect
     * @return 是否成功
     */
    public boolean setSingleTextAsCorpus(String sText, int iPosCorrect, int iNegCorrect) {
        if (resources == null && !resources.initialise(options)) {
            return false;
        }
        igParagraphCount = 2;
        paragraph = new Paragraph[igParagraphCount];
        igPosCorrect = new int[igParagraphCount];
        igNegCorrect = new int[igParagraphCount];
        igTrinaryCorrect = new int[igParagraphCount];
        igScaleCorrect = new int[igParagraphCount];
        bgSupcorpusMember = new boolean[igParagraphCount];
        igParagraphCount = 1;
        paragraph[igParagraphCount] = paragraphFactory.createParagraph(options.igEmotionParagraphCombineMethod);
        paragraph[igParagraphCount].setParagraph(sText, resources, options);
        igPosCorrect[igParagraphCount] = iPosCorrect;
        if (iNegCorrect < 0) {
            iNegCorrect *= -1;
        }
        igNegCorrect[igParagraphCount] = iNegCorrect;
        useWholeCorpusNotSubcorpus();
        return true;
    }

    /**
     * 设置所设置的选项下(pos,neg,trinary,scale)的语料库得分, 用于优化现有数据集.
     * 填充 ~Correct 系列数组, 该系列数组存储人工分级的文本情感得分，作为正确参照.
     * 为输入文件中的每一段文本创建并设置paragraph对象.
     *
     * @param sInFilenameAndPath 输入文件的路径名
     * @return 是否设置成功
     */
    public boolean setCorpus(String sInFilenameAndPath) {
        if (resources == null && !resources.initialise(options)) {
            return false;
        }
        igParagraphCount = FileOps.iCountLinesInTextFile(sInFilenameAndPath) + 1;
        if (igParagraphCount <= 2) {
            igParagraphCount = 0;
            return false;
        }
        paragraph = new Paragraph[igParagraphCount];
        igPosCorrect = new int[igParagraphCount];
        igNegCorrect = new int[igParagraphCount];
        igTrinaryCorrect = new int[igParagraphCount];
        igScaleCorrect = new int[igParagraphCount];
        bgSupcorpusMember = new boolean[igParagraphCount];
        igParagraphCount = 0;
        try {
            BufferedReader rReader = new BufferedReader(new FileReader(sInFilenameAndPath));
            String sLine;
            if (rReader.ready()) {
                sLine = rReader.readLine();
            }
            while ((sLine = rReader.readLine()) != null) {
                if (sLine != "") {
                    paragraph[++igParagraphCount] = paragraphFactory.createParagraph(options.igEmotionParagraphCombineMethod);
                    int iLastTabPos = sLine.lastIndexOf("\t");
                    int iFirstTabPos = sLine.indexOf("\t");
                    if (iFirstTabPos < iLastTabPos || iFirstTabPos > 0 && (options.bgTrinaryMode || options.bgScaleMode)) {
                        paragraph[igParagraphCount].setParagraph(sLine.substring(iLastTabPos + 1), resources, options);
                        if (options.bgTrinaryMode) {
                            try {
                                igTrinaryCorrect[igParagraphCount] = Integer.parseInt(sLine.substring(0, iFirstTabPos).trim());
                            } catch (Exception e) {
                                System.out.println((new StringBuilder("Trinary classification could not be read and will be ignored!: ")).append(sLine).toString());
                                igTrinaryCorrect[igParagraphCount] = trinine; //999
                            }
                            if (igTrinaryCorrect[igParagraphCount] > 1 || igTrinaryCorrect[igParagraphCount] < -1) {
                                System.out.println((new StringBuilder("Trinary classification out of bounds and will be ignored!: ")).append(sLine).toString());
                                igParagraphCount--;
                            } else if (options.bgBinaryVersionOfTrinaryMode && igTrinaryCorrect[igParagraphCount] == 0) {
                                System.out.println((new StringBuilder("Warning, unexpected 0 in binary classification!: ")).append(sLine).toString());
                            }
                        } else if (options.bgScaleMode) {
                            try {
                                igScaleCorrect[igParagraphCount] = Integer.parseInt(sLine.substring(0, iFirstTabPos).trim());
                            } catch (Exception e) {
                                System.out.println((new StringBuilder("Scale classification could not be read and will be ignored!: ")).append(sLine).toString());
                                igScaleCorrect[igParagraphCount] = trinine; //999
                            }
                            if (igScaleCorrect[igParagraphCount] > four || igTrinaryCorrect[igParagraphCount] < segfour) {
                                System.out.println((new StringBuilder("Scale classification out of bounds (-4 to +4) and will be ignored!: ")).append(sLine).toString());
                                igParagraphCount--;
                            }
                        } else {
                            try {
                                igPosCorrect[igParagraphCount] = Integer.parseInt(sLine.substring(0, iFirstTabPos).trim());
                                igNegCorrect[igParagraphCount] = Integer.parseInt(sLine.substring(iFirstTabPos + 1, iLastTabPos).trim());
                                if (igNegCorrect[igParagraphCount] < 0) {
                                    igNegCorrect[igParagraphCount] = -igNegCorrect[igParagraphCount];
                                }
                            } catch (Exception e) {
                                System.out.println((new StringBuilder("Positive or negative classification could not be read and will be ignored!: ")).append(sLine).toString());
                                igPosCorrect[igParagraphCount] = 0;
                            }
                            if (igPosCorrect[igParagraphCount] > five || igPosCorrect[igParagraphCount] < 1) {
                                System.out.println((new StringBuilder("Warning, positive classification out of bounds and line will be ignored!: ")).append(sLine).toString());
                                igParagraphCount--;
                            } else if (igNegCorrect[igParagraphCount] > five || igNegCorrect[igParagraphCount] < 1) {
                                System.out.println((new StringBuilder("Warning, negative classification out of bounds (must be 1,2,3,4, or 5, with or without -) and line will be ignored!: ")).append(sLine).toString());
                                igParagraphCount--;
                            }
                        }
                    } else {
                        if (iFirstTabPos >= 0) {
                            if (options.bgTrinaryMode) {
                                igTrinaryCorrect[igParagraphCount] = Integer.parseInt(sLine.substring(0, iFirstTabPos).trim());
                            }
                            sLine = sLine.substring(iFirstTabPos + 1);
                        } else if (options.bgTrinaryMode) {
                            igTrinaryCorrect[igParagraphCount] = 0;
                        }
                        paragraph[igParagraphCount].setParagraph(sLine, resources, options);
                        igPosCorrect[igParagraphCount] = 0;
                        igNegCorrect[igParagraphCount] = 0;
                    }
                }
            }
            rReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        useWholeCorpusNotSubcorpus();
        System.out.println((new StringBuilder("Number of texts in corpus: ")).append(igParagraphCount).toString());
        return true;
    }

    /**
     * @return 词典资源是否可用
     */
    public boolean initialise() {
        return resources.initialise(options);
    }

    /**
     * 再次计算情感得分.
     */
    public void reCalculateCorpusSentimentScores() {
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]) {
                paragraph[i].recalculateParagraphSentimentScores();
            }
        }
        calculateCorpusSentimentScores();
    }

    /**
     * 获取积极得分（未被使用暂不予分析）.
     *
     * @param i
     * @return 积极情感得分
     */
    public int getCorpusMemberPositiveSentimentScore(int i) {
        if (i < 1 || i > igParagraphCount) {
            return 0;
        } else {
            return paragraph[i].getParagraphPositiveSentiment();
        }
    }

    /**
     * 获取消极得分（未被使用暂不予分析）.
     *
     * @param i
     * @return 消极情感得分
     */
    public int getCorpusMemberNegativeSentimentScore(int i) {
        if (i < 1 || i > igParagraphCount) {
            return 0;
        } else {
            return paragraph[i].getParagraphNegativeSentiment();
        }
    }

    /**
     * 根据系统原有词典对输入文件中的文本进行情感得分计算.
     * 将结果存储至 ~Class 系列数组中.
     */
    public void calculateCorpusSentimentScores() {
        if (igParagraphCount == 0) {
            return;
        }
        if (igPosClass == null || igPosClass.length < igPosCorrect.length) {
            igPosClass = new int[igParagraphCount + 1];
            igNegClass = new int[igParagraphCount + 1];
            igTrinaryClass = new int[igParagraphCount + 1];
            igScaleClass = new int[igParagraphCount + 1];
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]) {
                igPosClass[i] = paragraph[i].getParagraphPositiveSentiment();
                igNegClass[i] = paragraph[i].getParagraphNegativeSentiment();
                if (options.bgTrinaryMode) {
                    igTrinaryClass[i] = paragraph[i].getParagraphTrinarySentiment();
                }
                if (options.bgScaleMode) {
                    igScaleClass[i] = paragraph[i].getParagraphScaleSentiment();
                }
            }
        }
        bgCorpusClassified = true;
    }

    /**
     * 在词语情感得分更新后,对于以及评估后的语料库进行重新评分.
     *
     * @param iSentimentWordID       情感词语ID
     * @param iMinParasToContainWord 包含该词语的最小段落数
     */
    public void reClassifyClassifiedCorpusForSentimentChange(int iSentimentWordID, int iMinParasToContainWord) {
        if (igParagraphCount == 0) {
            return;
        }
        if (!bSentimentIDListMade) {
            makeSentimentIDListForCompleteCorpusIgnoringSubcorpus();
        }
        int iSentimentWordIDArrayPos = Sort.iFindIntPositionInSortedArray(iSentimentWordID, igSentimentIDList, 1, igSentimentIDListCount);
        if (iSentimentWordIDArrayPos == -1 || igSentimentIDParagraphCount[iSentimentWordIDArrayPos] < iMinParasToContainWord) {
            return;
        }
        igPosClass = new int[igParagraphCount + 1];
        igNegClass = new int[igParagraphCount + 1];
        if (options.bgTrinaryMode) {
            igTrinaryClass = new int[igParagraphCount + 1];
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]) {
                paragraph[i].reClassifyClassifiedParagraphForSentimentChange(iSentimentWordID);
                igPosClass[i] = paragraph[i].getParagraphPositiveSentiment();
                igNegClass[i] = paragraph[i].getParagraphNegativeSentiment();
                if (options.bgTrinaryMode) {
                    igTrinaryClass[i] = paragraph[i].getParagraphTrinarySentiment();
                }
                if (options.bgScaleMode) {
                    igScaleClass[i] = paragraph[i].getParagraphScaleSentiment();
                }
            }
        }
        bgCorpusClassified = true;
    }

    /**
     * 输出情感得分.
     *
     * @param sOutFilenameAndPath
     * @return 是否有异常
     */
    public boolean printCorpusSentimentScores(String sOutFilenameAndPath) {
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        try {
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sOutFilenameAndPath));
            wWriter.write("Correct+\tCorrect-\tPredict+\tPredict-\tText\n");
            for (int i = 1; i <= igParagraphCount; i++) {
                if (bgSupcorpusMember[i]) {
                    wWriter.write((new StringBuilder(String.valueOf(igPosCorrect[i])))
                            .append("\t").append(igNegCorrect[i]).append("\t")
                            .append(igPosClass[i]).append("\t").append(igNegClass[i])
                            .append("\t").append(paragraph[i].getTaggedParagraph())
                            .append("\n").toString());
                }
            }
            wWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @return 正面情感得分准确率
     */
    public float getClassificationPositiveAccuracyProportion() {
        if (igSupcorpusMemberCount == 0) {
            return 0.0F;
        } else {
            return (float) getClassificationPositiveNumberCorrect() / (float) igSupcorpusMemberCount;
        }
    }

    /**
     * @return 负面情感得分准确率
     */
    public float getClassificationNegativeAccuracyProportion() {
        if (igSupcorpusMemberCount == 0) {
            return 0.0F;
        } else {
            return (float) getClassificationNegativeNumberCorrect() / (float) igSupcorpusMemberCount;
        }
    }

    /**
     * @return 负面得分基线准确率
     */
    public double getBaselineNegativeAccuracyProportion() {
        if (igParagraphCount == 0) {
            return 0.0D;
        } else {
            return ClassificationStatistics.baselineAccuracyMajorityClassProportion(igNegCorrect, igParagraphCount);
        }
    }

    /**
     * @return 正面得分基线准确率
     */
    public double getBaselinePositiveAccuracyProportion() {
        if (igParagraphCount == 0) {
            return 0.0D;
        } else {
            return ClassificationStatistics.baselineAccuracyMajorityClassProportion(igPosCorrect, igParagraphCount);
        }
    }

    /**
     * @return 预测负面得分正确的词语数目
     */
    public int getClassificationNegativeNumberCorrect() {
        if (igParagraphCount == 0) {
            return 0;
        }
        int iMatches = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i] && igNegCorrect[i] == -igNegClass[i]) {
                iMatches++;
            }
        }
        return iMatches;
    }

    /**
     * @return 预测正面得分正确的词语数目
     */
    public int getClassificationPositiveNumberCorrect() {
        if (igParagraphCount == 0) {
            return 0;
        }
        int iMatches = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i] && igPosCorrect[i] == igPosClass[i]) {
                iMatches++;
            }
        }
        return iMatches;
    }

    /**
     * 未被使用暂不分析.
     *
     * @return 差异
     */
    public double getClassificationPositiveMeanDifference() {
        if (igParagraphCount == 0) {
            return 0.0D;
        }
        double fTotalDiff = 0.0D;
        int iTotal = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]) {
                fTotalDiff += Math.abs(igPosCorrect[i] - igPosClass[i]);
                iTotal++;
            }
        }

        if (iTotal > 0) {
            return fTotalDiff / (double) iTotal;
        } else {
            return 0.0D;
        }
    }

    /**
     * @return 正确正面得分与预测正面得分的差异和
     */
    public int getClassificationPositiveTotalDifference() {
        if (igParagraphCount == 0) {
            return 0;
        }
        int iTotalDiff = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]) {
                iTotalDiff += Math.abs(igPosCorrect[i] - igPosClass[i]);
            }
        }
        return iTotalDiff;
    }

    /**
     * @return trinary选项下, 正确负面得分与预测负面得分的差异和
     */
    public int getClassificationTrinaryNumberCorrect() {
        if (igParagraphCount == 0) {
            return 0;
        }
        int iTrinaryCorrect = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i] && igTrinaryCorrect[i] == igTrinaryClass[i]) {
                iTrinaryCorrect++;
            }
        }
        return iTrinaryCorrect;
    }

    /**
     * @return 考虑整个语料库, scale选项下正确评分与预测得分的相关性
     */
    public float getClassificationScaleCorrelationWholeCorpus() {
        if (igParagraphCount == 0) {
            return 0.0F;
        } else {
            return (float) ClassificationStatistics.correlation(igScaleCorrect, igScaleClass, igParagraphCount);
        }
    }

    /**
     * @return scale选项下, 预测得分的准确率
     */
    public float getClassificationScaleAccuracyProportion() {
        if (igSupcorpusMemberCount == 0) {
            return 0.0F;
        } else {
            return (float) getClassificationScaleNumberCorrect() / (float) igSupcorpusMemberCount;
        }
    }

    /**
     * @return 考虑整个语料库, 默认选项下正确正面评分与预测正面得分的相关性
     */
    public float getClassificationPosCorrelationWholeCorpus() {
        if (igParagraphCount == 0) {
            return 0.0F;
        } else {
            return (float) ClassificationStatistics.correlationAbs(igPosCorrect, igPosClass, igParagraphCount);
        }
    }

    /**
     * @return 考虑整个语料库, 默认选项下正确负面评分与预测负面得分的相关性
     */
    public float getClassificationNegCorrelationWholeCorpus() {
        if (igParagraphCount == 0) {
            return 0.0F;
        } else {
            return (float) ClassificationStatistics.correlationAbs(igNegCorrect, igNegClass, igParagraphCount);
        }
    }

    /**
     * @return scale选项下, 得分预测正确的词语数目
     */
    public int getClassificationScaleNumberCorrect() {
        if (igParagraphCount == 0) {
            return 0;
        }
        int iScaleCorrect = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i] && igScaleCorrect[i] == igScaleClass[i]) {
                iScaleCorrect++;
            }
        }
        return iScaleCorrect;
    }

    /**
     * @return 正确负面得分与预测负面得分的差异和
     */
    public int getClassificationNegativeTotalDifference() {
        if (igParagraphCount == 0) {
            return 0;
        }
        int iTotalDiff = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]) {
                iTotalDiff += Math.abs(igNegCorrect[i] + igNegClass[i]);
            }
        }
        return iTotalDiff;
    }

    /**
     * 未被使用暂不分析.
     *
     * @return 差异
     */
    public double getClassificationNegativeMeanDifference() {
        if (igParagraphCount == 0) {
            return 0.0D;
        }
        double fTotalDiff = 0.0D;
        int iTotal = 0;
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        for (int i = 1; i <= igParagraphCount; i++) {
            if (bgSupcorpusMember[i]) {
                fTotalDiff += Math.abs(igNegCorrect[i] + igNegClass[i]);
                iTotal++;
            }
        }

        if (iTotal > 0) {
            return fTotalDiff / (double) iTotal;
        } else {
            return 0.0D;
        }
    }

    /**
     * 未被使用暂不分析.
     *
     * @param sOutFilenameAndPath
     * @return 是否有异常
     */
    public boolean printClassificationResultsSummaryNotDone(String sOutFilenameAndPath) {
        if (!bgCorpusClassified) {
            calculateCorpusSentimentScores();
        }
        try {
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sOutFilenameAndPath));
            for (int i = 1; i <= igParagraphCount; i++) {
                boolean tmp = bgSupcorpusMember[i];
            }
            wWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 忽略子集语料库,为整个语料库创建情感词语ID列表.
     * 置标签bSentimentIDListMade为true.
     */
    public void makeSentimentIDListForCompleteCorpusIgnoringSubcorpus() {
        igSentimentIDListCount = 0;
        for (int i = 1; i <= igParagraphCount; i++) {
            paragraph[i].makeSentimentIDList();
            if (paragraph[i].getSentimentIDList() != null) {
                igSentimentIDListCount += paragraph[i].getSentimentIDList().length;
            }
        }

        if (igSentimentIDListCount > 0) {
            igSentimentIDList = new int[igSentimentIDListCount + 1];
            igSentimentIDParagraphCount = new int[igSentimentIDListCount + 1];
            igSentimentIDListCount = 0;
            for (int i = 1; i <= igParagraphCount; i++) {
                int[] sentenceIDList = paragraph[i].getSentimentIDList();
                if (sentenceIDList != null) {
                    for (int j = 0; j < sentenceIDList.length; j++) {
                        if (sentenceIDList[j] != 0) {
                            igSentimentIDList[++igSentimentIDListCount] = sentenceIDList[j];
                        }
                    }
                }
            }

            Sort.quickSortInt(igSentimentIDList, 1, igSentimentIDListCount);
            for (int i = 1; i <= igParagraphCount; i++) {
                int[] sentenceIDList = paragraph[i].getSentimentIDList();
                if (sentenceIDList != null) {
                    for (int j = 0; j < sentenceIDList.length; j++) {
                        if (sentenceIDList[j] != 0) {
                            igSentimentIDParagraphCount[Sort.iFindIntPositionInSortedArray(sentenceIDList[j], igSentimentIDList, 1, igSentimentIDListCount)]++;
                        }
                    }
                }
            }
        }
        bSentimentIDListMade = true;
    }

    /**
     * 私有方法,多次运行10折交叉检验.
     *
     * @param iMinImprovement     在训练阶段,调整一个词语的权重所需的最小额外正确评分的次数
     * @param bUseTotalDifference 是否启用总体差异标签
     * @param iReplications       迭代次数
     * @param iMultiOptimisations 优化次数
     * @param sWriter             记录各种统计值(如正确预测的数目和比率、SentiStrength得分和人工标注的文本得分之间的相关性等)
     * @param wTermStrengthWriter 记录在每次迭代中情感词典的变化
     */
    private void run10FoldCrossValidationMultipleTimes(int iMinImprovement, boolean bUseTotalDifference, int iReplications, int iMultiOptimisations, BufferedWriter sWriter, BufferedWriter wTermStrengthWriter) {
        for (int i = 1; i <= iReplications; i++) {
            run10FoldCrossValidationOnce(iMinImprovement, bUseTotalDifference, iMultiOptimisations, sWriter, wTermStrengthWriter);
        }
        System.out.println((new StringBuilder("Set of ")).append(iReplications).append(" 10-fold cross validations finished").toString());
    }

    /**
     * 公共方法,多次运行10折交叉检验.
     *
     * @param iMinImprovement     在训练阶段,调整一个词语的权重所需的最小额外正确评分的次数
     * @param bUseTotalDifference 是否启用总体差异标签
     * @param iReplications       迭代次数
     * @param iMultiOptimisations 优化次数
     * @param sOutFileName        输出文件名称
     */
    public void run10FoldCrossValidationMultipleTimes(int iMinImprovement, boolean bUseTotalDifference, int iReplications, int iMultiOptimisations, String sOutFileName) {
        try {
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sOutFileName));
            BufferedWriter wTermStrengthWriter = new BufferedWriter(new FileWriter((new StringBuilder(String.valueOf(FileOps.sChopFileNameExtension(sOutFileName)))).append("_termStrVars.txt").toString()));
            options.printClassificationOptionsHeadings(wWriter);
            writeClassificationStatsHeadings(wWriter);
            options.printClassificationOptionsHeadings(wTermStrengthWriter);
            resources.sentimentWords.printSentimentTermsInSingleHeaderRow(wTermStrengthWriter);
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wWriter, wTermStrengthWriter);
            wWriter.close();
            wTermStrengthWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 为文本所有行进行评分并且根据ID进行记录.
     *
     * @param sInputFile  输入文件路径
     * @param iTextCol    待评级的text所在列,默认0表示第一列
     * @param iIDCol      ID列
     * @param sOutputFile 输出文件路径
     */
    public void classifyAllLinesAndRecordWithID(String sInputFile, int iTextCol, int iIDCol, String sOutputFile) {
        int iPos = 0;
        int iNeg = 0;
        int iTrinary = segthree; //-3
        int iScale = segten; //-10
        int iCount1 = 0;
        String sLine = "";
        try {
            BufferedReader rReader = new BufferedReader(new FileReader(sInputFile));
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sOutputFile));
            while (rReader.ready()) {
                sLine = rReader.readLine();
                iCount1++;
                if (sLine != "") {
                    String[] sData = sLine.split("\t");
                    if (sData.length > iTextCol && sData.length > iIDCol) {
                        Paragraph paragraph = paragraphFactory.createParagraph(options.igEmotionParagraphCombineMethod);
                        paragraph.setParagraph(sData[iTextCol], resources, options);
                        if (options.bgTrinaryMode) {
                            iTrinary = paragraph.getParagraphTrinarySentiment();
                            wWriter.write((new StringBuilder(String.valueOf(sData[iIDCol]))).append("\t").append(iTrinary).append("\n").toString());
                        } else if (options.bgScaleMode) {
                            iScale = paragraph.getParagraphScaleSentiment();
                            wWriter.write((new StringBuilder(String.valueOf(sData[iIDCol]))).append("\t").append(iScale).append("\n").toString());
                        } else {
                            iPos = paragraph.getParagraphPositiveSentiment();
                            iNeg = paragraph.getParagraphNegativeSentiment();
                            wWriter.write((new StringBuilder(String.valueOf(sData[iIDCol]))).append("\t").append(iPos).append("\t").append(iNeg).append("\n").toString());
                        }
                    }
                }
            }
            Thread.sleep(TEN);
            if (rReader.ready()) {
                System.out.println("Reader ready again after pause!");
            }
            int character = rReader.read();
            if (character != -1) {
                System.out.println((new StringBuilder("Reader returns char after reader.read() false! ")).append(character).toString());
            }
            rReader.close();
            wWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println((new StringBuilder("Could not find input file: ")).append(sInputFile).toString());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println((new StringBuilder("Error reading or writing from file: ")).append(sInputFile).toString());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println((new StringBuilder("Error reading from or writing to file: ")).append(sInputFile).toString());
            e.printStackTrace();
        }
        System.out.println((new StringBuilder("Processed ")).append(iCount1).append(" lines from file: ").append(sInputFile).append(". Last line was:\n").append(sLine).toString());
    }

    /**
     * 对于输入文本每一行,在iTextCol列的文本会被提取并评分.
     * 结果会被添加到每行末尾新增的列.
     *
     * @param sInputFile 输入文件路径
     * @param iTextCol   待评级的text所在列,默认0表示第一列
     */
    public void annotateAllLinesInInputFile(String sInputFile, int iTextCol) {
        int iPos = 0;
        int iNeg = 0;
        int iTrinary = segthree; //-3
        int iScale = segten; //-10
        String sTempFile = (new StringBuilder(String.valueOf(sInputFile))).append("_temp").toString();
        try {
            BufferedReader rReader = new BufferedReader(new FileReader(sInputFile));
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sTempFile));
            while (rReader.ready()) {
                String sLine = rReader.readLine();
                if (sLine != "") {
                    String[] sData = sLine.split("\t");
                    if (sData.length > iTextCol) {
                        Paragraph paragraph = paragraphFactory.createParagraph(options.igEmotionParagraphCombineMethod);
                        paragraph.setParagraph(sData[iTextCol], resources, options);
                        if (options.bgTrinaryMode) {
                            iTrinary = paragraph.getParagraphTrinarySentiment();
                            wWriter.write((new StringBuilder(String.valueOf(sLine))).append("\t").append(iTrinary).append("\n").toString());
                        } else if (options.bgScaleMode) {
                            iScale = paragraph.getParagraphScaleSentiment();
                            wWriter.write((new StringBuilder(String.valueOf(sLine))).append("\t").append(iScale).append("\n").toString());
                        } else {
                            iPos = paragraph.getParagraphPositiveSentiment();
                            iNeg = paragraph.getParagraphNegativeSentiment();
                            wWriter.write((new StringBuilder(String.valueOf(sLine))).append("\t").append(iPos).append("\t").append(iNeg).append("\n").toString());
                        }
                    } else {
                        wWriter.write((new StringBuilder(String.valueOf(sLine))).append("\n").toString());
                    }
                }
            }
            rReader.close();
            wWriter.close();
            File original = new File(sInputFile);
            original.delete();
            File newFile = new File(sTempFile);
            newFile.renameTo(original);
        } catch (FileNotFoundException e) {
            System.out.println((new StringBuilder("Could not find input file: ")).append(sInputFile).toString());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println((new StringBuilder("Error reading or writing from file: ")).append(sInputFile).toString());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println((new StringBuilder("Error reading from or writing to file: ")).append(sInputFile).toString());
            e.printStackTrace();
        }
    }

    /**
     * 为输入文件文本的所有行进行评分.
     *
     * @param sInputFile  输入文件路径
     * @param iTextCol    待评级的text所在列,默认0表示第一列
     * @param sOutputFile 输出文件路径
     */
    public void classifyAllLinesInInputFile(String sInputFile, int iTextCol, String sOutputFile) {
        int iPos = 0;
        int iNeg = 0;
        int iTrinary = segthree; //-3
        int iScale = segten; //-10
        int iFileTrinary = segtwo; //-2
        int iFileScale = segnine; //-9
        int iClassified = 0;
        int iCorrectPosCount = 0;
        int iCorrectNegCount = 0;
        int iCorrectTrinaryCount = 0;
        int iCorrectScaleCount = 0;
        int iPosAbsDiff = 0;
        int iNegAbsDiff = 0;
        int[][] confusion = {
                new int[three], new int[three], new int[three]
        };
        int maxClassifyForCorrelation = twentythousands; //20000
        int[] iPosClassCorr = new int[maxClassifyForCorrelation];
        int[] iNegClassCorr = new int[maxClassifyForCorrelation];
        int[] iPosClassPred = new int[maxClassifyForCorrelation];
        int[] iNegClassPred = new int[maxClassifyForCorrelation];
        int[] iScaleClassCorr = new int[maxClassifyForCorrelation];
        int[] iScaleClassPred = new int[maxClassifyForCorrelation];
        String sRationale = "";
        String sOutput = "";
        try {
            BufferedReader rReader;
            BufferedWriter wWriter;
            if (options.bgForceUTF8) {
                wWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sOutputFile), "UTF8"));
                rReader = new BufferedReader(new InputStreamReader(new FileInputStream(sInputFile), "UTF8"));
            } else {
                wWriter = new BufferedWriter(new FileWriter(sOutputFile));
                rReader = new BufferedReader(new FileReader(sInputFile));
            }
            if (options.bgTrinaryMode || options.bgScaleMode) {
                wWriter.write("Overall\tText");
            } else if (options.bgTensiStrength) {
                wWriter.write("Relax\tStress\tText");
            } else {
                wWriter.write("Positive\tNegative\tText");
            }
            if (options.bgExplainClassification) {
                wWriter.write("\tExplanation\n");
            } else {
                wWriter.write("\n");
            }
            while (rReader.ready()) {
                String sLine = rReader.readLine();
                if (sLine != "") {
                    int iTabPos = sLine.lastIndexOf("\t");
                    int iFilePos = 0;
                    int iFileNeg = 0;
                    if (iTabPos >= 0) {
                        String[] sData = sLine.split("\t");
                        if (sData.length > 1) {
                            if (iTextCol > -1) {
                                wWriter.write((new StringBuilder(String.valueOf(sLine))).append("\t").toString());
                                if (iTextCol < sData.length) {
                                    sLine = sData[iTextCol];
                                }
                            } else if (options.bgTrinaryMode) {
                                iFileTrinary = segtwo; //-2
                                try {
                                    iFileTrinary = Integer.parseInt(sData[0].trim());
                                    if (iFileTrinary > 1 || iFileTrinary < -1) {
                                        System.out.println((new StringBuilder("Invalid trinary sentiment ")).append(iFileTrinary).append(" (expected -1,0,1) at line: ").append(sLine).toString());
                                        iFileTrinary = 0;
                                    }
                                } catch (NumberFormatException numberformatexception) {
                                }
                            } else if (options.bgScaleMode) {
                                iFileScale = segnine; //-9
                                try {
                                    iFileScale = Integer.parseInt(sData[0].trim());
                                    if (iFileScale > four || iFileScale < segfour) {
                                        System.out.println((new StringBuilder("Invalid overall sentiment ")).append(iFileScale).append(" (expected -4 to +4) at line: ").append(sLine).toString());
                                        iFileScale = 0;
                                    }
                                } catch (NumberFormatException numberformatexception1) {
                                }
                            } else {
                                try {
                                    iFilePos = Integer.parseInt(sData[0].trim());
                                    iFileNeg = Integer.parseInt(sData[1].trim());
                                    if (iFileNeg < 0) {
                                        iFileNeg = -iFileNeg;
                                    }
                                } catch (NumberFormatException numberformatexception2) {
                                }
                            }
                        }
                        sLine = sLine.substring(iTabPos + 1);
                    }
                    Paragraph paragraph = paragraphFactory.createParagraph(options.igEmotionParagraphCombineMethod);
                    paragraph.setParagraph(sLine, resources, options);
                    if (options.bgTrinaryMode) {
                        iTrinary = paragraph.getParagraphTrinarySentiment();
                        if (options.bgExplainClassification) {
                            sRationale = (new StringBuilder("\t")).append(paragraph.getClassificationRationale()).toString();
                        }
                        sOutput = (new StringBuilder(String.valueOf(iTrinary))).append("\t").append(sLine).append(sRationale).append("\n").toString();
                    } else if (options.bgScaleMode) {
                        iScale = paragraph.getParagraphScaleSentiment();
                        if (options.bgExplainClassification) {
                            sRationale = (new StringBuilder("\t")).append(paragraph.getClassificationRationale()).toString();
                        }
                        sOutput = (new StringBuilder(String.valueOf(iScale))).append("\t").append(sLine).append(sRationale).append("\n").toString();
                    } else {
                        iPos = paragraph.getParagraphPositiveSentiment();
                        iNeg = paragraph.getParagraphNegativeSentiment();
                        if (options.bgExplainClassification) {
                            sRationale = (new StringBuilder("\t")).append(paragraph.getClassificationRationale()).toString();
                        }
                        sOutput = (new StringBuilder(String.valueOf(iPos))).append("\t").append(iNeg).append("\t").append(sLine).append(sRationale).append("\n").toString();
                    }
                    wWriter.write(sOutput);
                    if (options.bgTrinaryMode) {
                        if (iFileTrinary > segtwo && iFileTrinary < 2 && iTrinary > segtwo && iTrinary < 2) {
                            iClassified++;
                            if (iFileTrinary == iTrinary) {
                                iCorrectTrinaryCount++;
                            }
                            confusion[iTrinary + 1][iFileTrinary + 1]++;
                        }
                    } else if (options.bgScaleMode) {
                        if (iFileScale > segnine) {
                            iClassified++;
                            if (iFileScale == iScale) {
                                iCorrectScaleCount++;
                            }
                            if (iClassified < maxClassifyForCorrelation) {
                                iScaleClassCorr[iClassified] = iFileScale;
                            }
                            iScaleClassPred[iClassified] = iScale;
                        }
                    } else if (iFileNeg != 0) {
                        iClassified++;
                        if (iPos == iFilePos) {
                            iCorrectPosCount++;
                        }
                        iPosAbsDiff += Math.abs(iPos - iFilePos);
                        if (iClassified < maxClassifyForCorrelation) {
                            iPosClassCorr[iClassified] = iFilePos;
                        }
                        iPosClassPred[iClassified] = iPos;
                        if (iNeg == -iFileNeg) {
                            iCorrectNegCount++;
                        }
                        iNegAbsDiff += Math.abs(iNeg + iFileNeg);
                        if (iClassified < maxClassifyForCorrelation) {
                            iNegClassCorr[iClassified] = iFileNeg;
                        }
                        iNegClassPred[iClassified] = iNeg;
                    }
                }
            }
            rReader.close();
            wWriter.close();
            if (iClassified > 0) {
                if (options.bgTrinaryMode) {
                    System.out.println((new StringBuilder("Trinary correct: ")).append(iCorrectTrinaryCount).append(" (").append(((float) iCorrectTrinaryCount / (float) iClassified) * HUNDRED_PERCENT).append("%).").toString());
                    System.out.println("Correct -> -1   0   1");
                    System.out.println((new StringBuilder("Est = -1   ")).append(confusion[0][0]).append(" ").append(confusion[0][1]).append(" ").append(confusion[0][2]).toString());
                    System.out.println((new StringBuilder("Est =  0   ")).append(confusion[1][0]).append(" ").append(confusion[1][1]).append(" ").append(confusion[1][2]).toString());
                    System.out.println((new StringBuilder("Est =  1   ")).append(confusion[2][0]).append(" ").append(confusion[2][1]).append(" ").append(confusion[2][2]).toString());
                } else if (options.bgScaleMode) {
                    System.out.println((new StringBuilder("Scale correct: ")).append(iCorrectScaleCount).append(" (").append(((float) iCorrectScaleCount / (float) iClassified) * HUNDRED_PERCENT).append("%) out of ").append(iClassified).toString());
                    System.out.println((new StringBuilder("  Correlation: ")).append(ClassificationStatistics.correlation(iScaleClassCorr, iScaleClassPred, iClassified)).toString());
                } else {
                    System.out.print((new StringBuilder(String.valueOf(options.sgProgramPos))).append(" correct: ").append(iCorrectPosCount).append(" (").append(((float) iCorrectPosCount / (float) iClassified) * HUNDRED_PERCENT).append("%).").toString());
                    System.out.println((new StringBuilder(" Mean abs diff: ")).append((float) iPosAbsDiff / (float) iClassified).toString());
                    if (iClassified < maxClassifyForCorrelation) {
                        System.out.println((new StringBuilder(" Correlation: ")).append(ClassificationStatistics.correlationAbs(iPosClassCorr, iPosClassPred, iClassified)).toString());
                        int corrWithin1 = ClassificationStatistics.accuracyWithin1(iPosClassCorr, iPosClassPred, iClassified, false);
                        System.out.println((new StringBuilder(" Correct +/- 1: ")).append(corrWithin1).append(" (").append((float) (onehundred * corrWithin1) / (float) iClassified).append("%)").toString());
                    }
                    System.out.print((new StringBuilder(String.valueOf(options.sgProgramNeg))).append(" correct: ").append(iCorrectNegCount).append(" (").append(((float) iCorrectNegCount / (float) iClassified) * HUNDRED_PERCENT).append("%).").toString());
                    System.out.println((new StringBuilder(" Mean abs diff: ")).append((float) iNegAbsDiff / (float) iClassified).toString());
                    if (iClassified < maxClassifyForCorrelation) {
                        System.out.println((new StringBuilder(" Correlation: ")).append(ClassificationStatistics.correlationAbs(iNegClassCorr, iNegClassPred, iClassified)).toString());
                        int corrWithin1 = ClassificationStatistics.accuracyWithin1(iNegClassCorr, iNegClassPred, iClassified, true);
                        System.out.println((new StringBuilder(" Correct +/- 1: ")).append(corrWithin1).append(" (").append((float) (onehundred * corrWithin1) / (float) iClassified).append("%)").toString());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println((new StringBuilder("Could not find input file: ")).append(sInputFile).toString());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println((new StringBuilder("Error reading from input file: ")).append(sInputFile).append(" or writing to output file ").append(sOutputFile).toString());
            e.printStackTrace();
        }
    }

    /**
     * 向输出缓存中写入评分分析表的表头.
     *
     * @param w 输出缓存
     * @throws IOException
     */
    private void writeClassificationStatsHeadings(BufferedWriter w)
            throws IOException {
        String sPosOrScale;
        if (options.bgScaleMode) {
            sPosOrScale = "ScaleCorrel";
        } else {
            sPosOrScale = "PosCorrel";
        }
        w.write((new StringBuilder("\tPosCorrect\tiPosCorrect/Total\tNegCorrect\tNegCorrect/Total\tPosWithin1\tPosWithin1/Total\tNegWithin1\tNegWithin1/Total\t")).append(sPosOrScale).append("\tNegCorrel").append("\tPosMPE\tNegMPE\tPosMPEnoDiv\tNegMPEnoDiv").append("\tTrinaryOrScaleCorrect\tTrinaryOrScaleCorrect/TotalClassified").append("\tTrinaryOrScaleCorrectWithin1\tTrinaryOrScaleCorrectWithin1/TotalClassified").append("\test-1corr-1\test-1corr0\test-1corr1").append("\test0corr-1\test0corr0\test0corr1").append("\test1corr-1\test1corr0\test1corr1").append("\tTotalClassified\n").toString());
    }

    /**
     * 各种选项下,运行10折交叉检验.
     *
     * @param iMinImprovement     在训练阶段,调整一个词语的权重所需的最小额外正确评分的次数
     * @param bUseTotalDifference 是否启用总体差异标签
     * @param iReplications       迭代次数
     * @param iMultiOptimisations 优化次数
     * @param sOutFileName        输出文件名称
     */
    public void run10FoldCrossValidationForAllOptionVariations(int iMinImprovement, boolean bUseTotalDifference, int iReplications, int iMultiOptimisations, String sOutFileName) {
        try {
            BufferedWriter wResultsWriter = new BufferedWriter(new FileWriter(sOutFileName));
            BufferedWriter wTermStrengthWriter = new BufferedWriter(new FileWriter((new StringBuilder(String.valueOf(FileOps.sChopFileNameExtension(sOutFileName)))).append("_termStrVars.txt").toString()));
            if (igPosClass == null || igPosClass.length < igPosCorrect.length) {
                igPosClass = new int[igParagraphCount + 1];
                igNegClass = new int[igParagraphCount + 1];
                igTrinaryClass = new int[igParagraphCount + 1];
            }
            options.printClassificationOptionsHeadings(wResultsWriter);
            writeClassificationStatsHeadings(wResultsWriter);
            options.printClassificationOptionsHeadings(wTermStrengthWriter);
            resources.sentimentWords.printSentimentTermsInSingleHeaderRow(wTermStrengthWriter);
            System.out.println("About to start classifications for 20 different option variations");
            if (options.bgTrinaryMode) {
                ClassificationStatistics.baselineAccuracyMakeLargestClassPrediction(igTrinaryCorrect, igTrinaryClass, igParagraphCount, false);
            } else if (options.bgScaleMode) {
                ClassificationStatistics.baselineAccuracyMakeLargestClassPrediction(igScaleCorrect, igScaleClass, igParagraphCount, false);
            } else {
                ClassificationStatistics.baselineAccuracyMakeLargestClassPrediction(igPosCorrect, igPosClass, igParagraphCount, false);
                ClassificationStatistics.baselineAccuracyMakeLargestClassPrediction(igNegCorrect, igNegClass, igParagraphCount, true);
            }
            options.printBlankClassificationOptions(wResultsWriter);
            if (options.bgTrinaryMode) {
                printClassificationResultsRow(igPosClass, igNegClass, igTrinaryClass, wResultsWriter);
            } else {
                printClassificationResultsRow(igPosClass, igNegClass, igScaleClass, wResultsWriter);
            }
            options.printClassificationOptions(wResultsWriter, igParagraphCount, bUseTotalDifference, iMultiOptimisations);
            calculateCorpusSentimentScores();
            if (options.bgTrinaryMode) {
                printClassificationResultsRow(igPosClass, igNegClass, igTrinaryClass, wResultsWriter);
            } else {
                printClassificationResultsRow(igPosClass, igNegClass, igScaleClass, wResultsWriter);
            }
            options.printBlankClassificationOptions(wTermStrengthWriter);
            resources.sentimentWords.printSentimentValuesInSingleRow(wTermStrengthWriter);
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.igEmotionParagraphCombineMethod = 1 - options.igEmotionParagraphCombineMethod;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.igEmotionParagraphCombineMethod = 1 - options.igEmotionParagraphCombineMethod;
            options.igEmotionSentenceCombineMethod = 1 - options.igEmotionSentenceCombineMethod;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.igEmotionSentenceCombineMethod = 1 - options.igEmotionSentenceCombineMethod;
            options.bgReduceNegativeEmotionInQuestionSentences = !options.bgReduceNegativeEmotionInQuestionSentences;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgReduceNegativeEmotionInQuestionSentences = !options.bgReduceNegativeEmotionInQuestionSentences;
            options.bgMissCountsAsPlus2 = !options.bgMissCountsAsPlus2;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgMissCountsAsPlus2 = !options.bgMissCountsAsPlus2;
            options.bgYouOrYourIsPlus2UnlessSentenceNegative = !options.bgYouOrYourIsPlus2UnlessSentenceNegative;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgYouOrYourIsPlus2UnlessSentenceNegative = !options.bgYouOrYourIsPlus2UnlessSentenceNegative;
            options.bgExclamationInNeutralSentenceCountsAsPlus2 = !options.bgExclamationInNeutralSentenceCountsAsPlus2;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgExclamationInNeutralSentenceCountsAsPlus2 = !options.bgExclamationInNeutralSentenceCountsAsPlus2;
            options.bgUseIdiomLookupTable = !options.bgUseIdiomLookupTable;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgUseIdiomLookupTable = !options.bgUseIdiomLookupTable;
            int iTemp = options.igMoodToInterpretNeutralEmphasis;
            options.igMoodToInterpretNeutralEmphasis = -options.igMoodToInterpretNeutralEmphasis;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.igMoodToInterpretNeutralEmphasis = 0;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.igMoodToInterpretNeutralEmphasis = iTemp;
            System.out.println("About to start 10th option variation classification");
            options.bgAllowMultiplePositiveWordsToIncreasePositiveEmotion = !options.bgAllowMultiplePositiveWordsToIncreasePositiveEmotion;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgAllowMultiplePositiveWordsToIncreasePositiveEmotion = !options.bgAllowMultiplePositiveWordsToIncreasePositiveEmotion;
            options.bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion = !options.bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion = !options.bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion;
            options.bgIgnoreBoosterWordsAfterNegatives = !options.bgIgnoreBoosterWordsAfterNegatives;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgIgnoreBoosterWordsAfterNegatives = !options.bgIgnoreBoosterWordsAfterNegatives;
            options.bgMultipleLettersBoostSentiment = !options.bgMultipleLettersBoostSentiment;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgMultipleLettersBoostSentiment = !options.bgMultipleLettersBoostSentiment;
            options.bgBoosterWordsChangeEmotion = !options.bgBoosterWordsChangeEmotion;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgBoosterWordsChangeEmotion = !options.bgBoosterWordsChangeEmotion;
            if (options.bgNegatingWordsFlipEmotion) {
                options.bgNegatingWordsFlipEmotion = !options.bgNegatingWordsFlipEmotion;
                run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
                options.bgNegatingWordsFlipEmotion = !options.bgNegatingWordsFlipEmotion;
            } else {
                options.bgNegatingPositiveFlipsEmotion = !options.bgNegatingPositiveFlipsEmotion;
                run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
                options.bgNegatingPositiveFlipsEmotion = !options.bgNegatingPositiveFlipsEmotion;
                options.bgNegatingNegativeNeutralisesEmotion = !options.bgNegatingNegativeNeutralisesEmotion;
                run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
                options.bgNegatingNegativeNeutralisesEmotion = !options.bgNegatingNegativeNeutralisesEmotion;
            }
            options.bgCorrectSpellingsWithRepeatedLetter = !options.bgCorrectSpellingsWithRepeatedLetter;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgCorrectSpellingsWithRepeatedLetter = !options.bgCorrectSpellingsWithRepeatedLetter;
            options.bgUseEmoticons = !options.bgUseEmoticons;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgUseEmoticons = !options.bgUseEmoticons;
            options.bgCapitalsBoostTermSentiment = !options.bgCapitalsBoostTermSentiment;
            run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            options.bgCapitalsBoostTermSentiment = !options.bgCapitalsBoostTermSentiment;
            if (iMinImprovement > 1) {
                run10FoldCrossValidationMultipleTimes(iMinImprovement - 1, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            }
            run10FoldCrossValidationMultipleTimes(iMinImprovement + 1, bUseTotalDifference, iReplications, iMultiOptimisations, wResultsWriter, wTermStrengthWriter);
            wResultsWriter.close();
            wTermStrengthWriter.close();
            summariseMultiple10FoldValidations(sOutFileName, (new StringBuilder(String.valueOf(sOutFileName))).append("_sum.txt").toString());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * @param iMinImprovement     在训练阶段,调整一个词语的权重所需的最小额外正确评分的次数
     * @param bUseTotalDifference 是否启用总体差异标签
     * @param iMultiOptimisations 迭代次数
     * @param wWriter             记录各种统计值(如正确预测的数目和比率、SentiStrength得分和人工标注的文本得分之间的相关性等)
     * @param wTermStrengthWriter 记录在每次迭代中情感词典的变化
     */
    private void run10FoldCrossValidationOnce(int iMinImprovement, boolean bUseTotalDifference, int iMultiOptimisations, BufferedWriter wWriter, BufferedWriter wTermStrengthWriter) {
        int iTotalSentimentWords = resources.sentimentWords.getSentimentWordCount();
        int[] iParagraphRand = new int[igParagraphCount + 1];
        int[] iPosClassAll = new int[igParagraphCount + 1];
        int[] iNegClassAll = new int[igParagraphCount + 1];
        int[] iTrinaryOrScaleClassAll = new int[igParagraphCount + 1];
        int iTotalClassified = 0;
        Sort.makeRandomOrderList(iParagraphRand);
        int[] iOriginalSentimentStrengths = new int[iTotalSentimentWords + 1];
        for (int i = 1; i < iTotalSentimentWords; i++) {
            iOriginalSentimentStrengths[i] = resources.sentimentWords.getSentiment(i);
        }

        for (int iFold = 1; iFold <= ten; iFold++) {
            selectDecileAsSubcorpus(iParagraphRand, iFold, true);
            reCalculateCorpusSentimentScores();
            optimiseDictionaryWeightingsForCorpusMultipleTimes(iMinImprovement, bUseTotalDifference, iMultiOptimisations);
            options.printClassificationOptions(wTermStrengthWriter, iMinImprovement, bUseTotalDifference, iMultiOptimisations);
            resources.sentimentWords.printSentimentValuesInSingleRow(wTermStrengthWriter);
            selectDecileAsSubcorpus(iParagraphRand, iFold, false);
            reCalculateCorpusSentimentScores();
            for (int i = 1; i <= igParagraphCount; i++) {
                if (bgSupcorpusMember[i]) {
                    iPosClassAll[i] = igPosClass[i];
                    iNegClassAll[i] = igNegClass[i];
                    if (options.bgTrinaryMode) {
                        iTrinaryOrScaleClassAll[i] = igTrinaryClass[i];
                    } else {
                        iTrinaryOrScaleClassAll[i] = igScaleClass[i];
                    }
                }
            }

            iTotalClassified += igSupcorpusMemberCount;
            for (int i = 1; i < iTotalSentimentWords; i++) {
                resources.sentimentWords.setSentiment(i, iOriginalSentimentStrengths[i]);
            }

        }

        useWholeCorpusNotSubcorpus();
        options.printClassificationOptions(wWriter, iMinImprovement, bUseTotalDifference, iMultiOptimisations);
        printClassificationResultsRow(iPosClassAll, iNegClassAll, iTrinaryOrScaleClassAll, wWriter);
    }

    /**
     * 为10折交叉验证生成第一份结果.
     * 如正确预测的数目和比率、正确预测一次的数目和比率等.
     * 具体包含以下8个属性列
     * Pos Correct
     * Pos Correct/Total
     * Neg Correct
     * Neg Correct/Total
     * Pos Within1
     * Pos Within1/Total
     * Neg Within1
     * Neg Within1/Total
     *
     * @param iPosClassAll            所有正面预测评分
     * @param iNegClassAll            所有负面预测评分
     * @param iTrinaryOrScaleClassAll 所有三元/总体预测得分
     * @param wWriter                 输出缓冲
     * @return 是否运行成功
     */
    private boolean printClassificationResultsRow(int[] iPosClassAll, int[] iNegClassAll, int[] iTrinaryOrScaleClassAll, BufferedWriter wWriter) {
        int iPosCorrect = -1;
        int iNegCorrect = -1;
        int iPosWithin1 = -1;
        int iNegWithin1 = -1;
        int iTrinaryCorrect = -1;
        int iTrinaryCorrectWithin1 = -1;
        double fPosCorrectPoportion = -1D;
        double fNegCorrectPoportion = -1D;
        double fPosWithin1Poportion = -1D;
        double fNegWithin1Poportion = -1D;
        double fTrinaryCorrectPoportion = -1D;
        double fTrinaryCorrectWithin1Poportion = -1D;
        double fPosOrScaleCorr = FOURNINED;
        double fNegCorr = FOURNINED;
        double fPosMPE = FOURNINED;
        double fNegMPE = FOURNINED;
        double fPosMPEnoDiv = FOURNINED;
        double fNegMPEnoDiv = FOURNINED;
        int[][] estCorr = {
                new int[three], new int[three], new int[three]
        };
        try {
            if (options.bgTrinaryMode) {
                iTrinaryCorrect = ClassificationStatistics.accuracy(igTrinaryCorrect, iTrinaryOrScaleClassAll, igParagraphCount, false);
                iTrinaryCorrectWithin1 = ClassificationStatistics.accuracyWithin1(igTrinaryCorrect, iTrinaryOrScaleClassAll, igParagraphCount, false);
                fTrinaryCorrectPoportion = (float) iTrinaryCorrect / (float) igParagraphCount;
                fTrinaryCorrectWithin1Poportion = (float) iTrinaryCorrectWithin1 / (float) igParagraphCount;
                ClassificationStatistics.trinaryOrBinaryConfusionTable(iTrinaryOrScaleClassAll, igTrinaryCorrect, igParagraphCount, estCorr);
            } else if (options.bgScaleMode) {
                iTrinaryCorrect = ClassificationStatistics.accuracy(igScaleCorrect, iTrinaryOrScaleClassAll, igParagraphCount, false);
                iTrinaryCorrectWithin1 = ClassificationStatistics.accuracyWithin1(igScaleCorrect, iTrinaryOrScaleClassAll, igParagraphCount, false);
                fTrinaryCorrectPoportion = (float) iTrinaryCorrect / (float) igParagraphCount;
                fTrinaryCorrectWithin1Poportion = (float) iTrinaryCorrectWithin1 / (float) igParagraphCount;
                fPosOrScaleCorr = ClassificationStatistics.correlation(igScaleCorrect, iTrinaryOrScaleClassAll, igParagraphCount);
            } else {
                iPosCorrect = ClassificationStatistics.accuracy(igPosCorrect, iPosClassAll, igParagraphCount, false);
                iNegCorrect = ClassificationStatistics.accuracy(igNegCorrect, iNegClassAll, igParagraphCount, true);
                iPosWithin1 = ClassificationStatistics.accuracyWithin1(igPosCorrect, iPosClassAll, igParagraphCount, false);
                iNegWithin1 = ClassificationStatistics.accuracyWithin1(igNegCorrect, iNegClassAll, igParagraphCount, true);
                fPosOrScaleCorr = ClassificationStatistics.correlationAbs(igPosCorrect, iPosClassAll, igParagraphCount);
                fNegCorr = ClassificationStatistics.correlationAbs(igNegCorrect, iNegClassAll, igParagraphCount);
                fPosMPE = ClassificationStatistics.absoluteMeanPercentageError(igPosCorrect, iPosClassAll, igParagraphCount, false);
                fNegMPE = ClassificationStatistics.absoluteMeanPercentageError(igNegCorrect, iNegClassAll, igParagraphCount, true);
                fPosMPEnoDiv = ClassificationStatistics.absoluteMeanPercentageErrorNoDivision(igPosCorrect, iPosClassAll, igParagraphCount, false);
                fNegMPEnoDiv = ClassificationStatistics.absoluteMeanPercentageErrorNoDivision(igNegCorrect, iNegClassAll, igParagraphCount, true);
                fPosCorrectPoportion = (float) iPosCorrect / (float) igParagraphCount;
                fNegCorrectPoportion = (float) iNegCorrect / (float) igParagraphCount;
                fPosWithin1Poportion = (float) iPosWithin1 / (float) igParagraphCount;
                fNegWithin1Poportion = (float) iNegWithin1 / (float) igParagraphCount;
            }
            wWriter.write((new StringBuilder("\t")).append(iPosCorrect).append("\t").append(fPosCorrectPoportion).append("\t").append(iNegCorrect).append("\t").append(fNegCorrectPoportion).append("\t").append(iPosWithin1).append("\t").append(fPosWithin1Poportion).append("\t").append(iNegWithin1).append("\t").append(fNegWithin1Poportion).append("\t").append(fPosOrScaleCorr).append("\t").append(fNegCorr).append("\t").append(fPosMPE).append("\t").append(fNegMPE).append("\t").append(fPosMPEnoDiv).append("\t").append(fNegMPEnoDiv).append("\t").append(iTrinaryCorrect).append("\t").append(fTrinaryCorrectPoportion).append("\t").append(iTrinaryCorrectWithin1).append("\t").append(fTrinaryCorrectWithin1Poportion).append("\t").append(estCorr[0][0]).append("\t").append(estCorr[0][1]).append("\t").append(estCorr[0][2]).append("\t").append(estCorr[1][0]).append("\t").append(estCorr[1][1]).append("\t").append(estCorr[1][2]).append("\t").append(estCorr[2][0]).append("\t").append(estCorr[2][1]).append("\t").append(estCorr[2][2]).append("\t").append(igParagraphCount).append("\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * .
     *
     * @param iParagraphRand 随机段落索引数组
     * @param iDecile        十分位数
     * @param bInvert        是否转换
     */
    private void selectDecileAsSubcorpus(int[] iParagraphRand, int iDecile, boolean bInvert) {
        if (igParagraphCount == 0) {
            return;
        }
        int iMin = (int) (((float) igParagraphCount / TENF) * (float) (iDecile - 1)) + 1;
        int iMax = (int) (((float) igParagraphCount / TENF) * (float) iDecile);
        if (iDecile == ten) {
            iMax = igParagraphCount;
        }
        if (iDecile == 0) {
            iMin = 0;
        }
        igSupcorpusMemberCount = 0;
        for (int i = 1; i <= igParagraphCount; i++) {
            if (i >= iMin && i <= iMax) {
                bgSupcorpusMember[iParagraphRand[i]] = !bInvert;
                if (!bInvert) {
                    igSupcorpusMemberCount++;
                }
            } else {
                bgSupcorpusMember[iParagraphRand[i]] = bInvert;
                if (bInvert) {
                    igSupcorpusMemberCount++;
                }
            }
        }
    }

    /**
     * 运行语料库词典权重优化指定次数.
     *
     * @param iMinImprovement     在训练阶段,调整一个词语的权重所需的最小额外正确评分的次数
     * @param bUseTotalDifference 是否启用总体差异标签
     * @param iOptimisationTotal  优化总次数
     */
    public void optimiseDictionaryWeightingsForCorpusMultipleTimes(int iMinImprovement, boolean bUseTotalDifference, int iOptimisationTotal) {
        if (iOptimisationTotal < 1) {
            return;
        }
        if (iOptimisationTotal == 1) {
            optimiseDictionaryWeightingsForCorpus(iMinImprovement, bUseTotalDifference);
            return;
        }
        int iTotalSentimentWords = resources.sentimentWords.getSentimentWordCount();
        int[] iOriginalSentimentStrengths = new int[iTotalSentimentWords + 1];
        for (int j = 1; j <= iTotalSentimentWords; j++) {
            iOriginalSentimentStrengths[j] = resources.sentimentWords.getSentiment(j);
        }

        int[] iTotalWeight = new int[iTotalSentimentWords + 1];
        for (int j = 1; j <= iTotalSentimentWords; j++) {
            iTotalWeight[j] = 0;
        }

        for (int i = 0; i < iOptimisationTotal; i++) {
            optimiseDictionaryWeightingsForCorpus(iMinImprovement, bUseTotalDifference);
            for (int j = 1; j <= iTotalSentimentWords; j++) {
                iTotalWeight[j] += resources.sentimentWords.getSentiment(j);
            }
            for (int j = 1; j <= iTotalSentimentWords; j++) {
                resources.sentimentWords.setSentiment(j, iOriginalSentimentStrengths[j]);
            }
        }

        for (int j = 1; j <= iTotalSentimentWords; j++) {
            resources.sentimentWords.setSentiment(j, (int) ((double) ((float) iTotalWeight[j] / (float) iOptimisationTotal) + HALFD));
        }
        optimiseDictionaryWeightingsForCorpus(iMinImprovement, bUseTotalDifference);
    }

    /**
     * 运行一次语料库词典权重优化.
     *
     * @param iMinImprovement     在训练阶段,调整一个词语的权重所需的最小额外正确评分的次数
     * @param bUseTotalDifference 是否启用总体差异标签
     */
    public void optimiseDictionaryWeightingsForCorpus(int iMinImprovement, boolean bUseTotalDifference) {
        if (options.bgTrinaryMode) {
            optimiseDictionaryWeightingsForCorpusTrinaryOrBinary(iMinImprovement);
        } else if (options.bgScaleMode) {
            optimiseDictionaryWeightingsForCorpusScale(iMinImprovement);
        } else {
            optimiseDictionaryWeightingsForCorpusPosNeg(iMinImprovement, bUseTotalDifference);
        }
    }

    /**
     * 在scale选项下,运行一次语料库词典权重优化.
     *
     * @param iMinImprovement 在训练阶段,调整一个词语的权重所需的最小额外正确评分的次数
     */
    public void optimiseDictionaryWeightingsForCorpusScale(int iMinImprovement) {
        boolean bFullListChanges = true;
        int iLastScaleNumberCorrect = getClassificationScaleNumberCorrect();
        int iNewScaleNumberCorrect = 0;
        int iTotalSentimentWords = resources.sentimentWords.getSentimentWordCount();
        int[] iWordRand = new int[iTotalSentimentWords + 1];
        while (bFullListChanges) {
            Sort.makeRandomOrderList(iWordRand);
            bFullListChanges = false;
            for (int i = 1; i <= iTotalSentimentWords; i++) {
                int iOldTermSentimentStrength = resources.sentimentWords.getSentiment(iWordRand[i]);
                boolean bCurrentIDChange = false;
                int iAddOneImprovement = 0;
                int iSubtractOneImprovement = 0;
                if (iOldTermSentimentStrength < four) {
                    resources.sentimentWords.setSentiment(iWordRand[i], iOldTermSentimentStrength + 1);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i], 1);
                    iNewScaleNumberCorrect = getClassificationScaleNumberCorrect();
                    iAddOneImprovement = iNewScaleNumberCorrect - iLastScaleNumberCorrect;
                    if (iAddOneImprovement >= iMinImprovement) {
                        bCurrentIDChange = true;
                        iLastScaleNumberCorrect += iAddOneImprovement;
                    }
                }
                if (iOldTermSentimentStrength > segfour && !bCurrentIDChange) {
                    resources.sentimentWords.setSentiment(iWordRand[i], iOldTermSentimentStrength - 1);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i], 1);
                    iNewScaleNumberCorrect = getClassificationScaleNumberCorrect();
                    iSubtractOneImprovement = iNewScaleNumberCorrect - iLastScaleNumberCorrect;
                    if (iSubtractOneImprovement >= iMinImprovement) {
                        bCurrentIDChange = true;
                        iLastScaleNumberCorrect += iSubtractOneImprovement;
                    }
                }
                if (bCurrentIDChange) {
                    bFullListChanges = true;
                } else {
                    resources.sentimentWords.setSentiment(iWordRand[i], iOldTermSentimentStrength);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i], 1);
                }
            }

        }
    }

    /**
     * 在trinary或binary选项下,运行一次语料库词典权重优化.
     *
     * @param iMinImprovement 在训练阶段,调整一个词语的权重所需的最小额外正确评分的次数
     */
    public void optimiseDictionaryWeightingsForCorpusTrinaryOrBinary(int iMinImprovement) {
        boolean bFullListChanges = true;
        int iLastTrinaryCorrect = getClassificationTrinaryNumberCorrect();
        int iNewTrinary = 0;
        int iTotalSentimentWords = resources.sentimentWords.getSentimentWordCount();
        int[] iWordRand = new int[iTotalSentimentWords + 1];
        while (bFullListChanges) {
            Sort.makeRandomOrderList(iWordRand);
            bFullListChanges = false;
            for (int i = 1; i <= iTotalSentimentWords; i++) {
                int iOldSentimentStrength = resources.sentimentWords.getSentiment(iWordRand[i]);
                boolean bCurrentIDChange = false;
                int iAddOneImprovement = 0;
                int iSubtractOneImprovement = 0;
                if (iOldSentimentStrength < four) {
                    resources.sentimentWords.setSentiment(iWordRand[i], iOldSentimentStrength + 1);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i], 1);
                    iNewTrinary = getClassificationTrinaryNumberCorrect();
                    iAddOneImprovement = iNewTrinary - iLastTrinaryCorrect;
                    if (iAddOneImprovement >= iMinImprovement) {
                        bCurrentIDChange = true;
                        iLastTrinaryCorrect += iAddOneImprovement;
                    }
                }
                if (iOldSentimentStrength > segfour && !bCurrentIDChange) {
                    resources.sentimentWords.setSentiment(iWordRand[i], iOldSentimentStrength - 1);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i], 1);
                    iNewTrinary = getClassificationTrinaryNumberCorrect();
                    iSubtractOneImprovement = iNewTrinary - iLastTrinaryCorrect;
                    if (iSubtractOneImprovement >= iMinImprovement) {
                        bCurrentIDChange = true;
                        iLastTrinaryCorrect += iSubtractOneImprovement;
                    }
                }
                if (bCurrentIDChange) {
                    bFullListChanges = true;
                } else {
                    resources.sentimentWords.setSentiment(iWordRand[i], iOldSentimentStrength);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i], 1);
                }
            }

        }
    }

    /**
     * 在默认选项下,运行一次语料库词典权重优化.
     *
     * @param iMinImprovement     在训练阶段,调整一个词语的权重所需的最小额外正确评分的次数
     * @param bUseTotalDifference 是否启用总体差异的标签
     */
    public void optimiseDictionaryWeightingsForCorpusPosNeg(int iMinImprovement, boolean bUseTotalDifference) {
        boolean bFullListChanges = true;
        int iLastPos = 0;
        int iLastNeg = 0;
        int iLastPosTotalDiff = 0;
        int iLastNegTotalDiff = 0;
        if (bUseTotalDifference) {
            iLastPosTotalDiff = getClassificationPositiveTotalDifference();
            iLastNegTotalDiff = getClassificationNegativeTotalDifference();
        } else {
            iLastPos = getClassificationPositiveNumberCorrect();
            iLastNeg = getClassificationNegativeNumberCorrect();
        }
        int iNewPos = 0;
        int iNewNeg = 0;
        int iNewPosTotalDiff = 0;
        int iNewNegTotalDiff = 0;
        int iTotalSentimentWords = resources.sentimentWords.getSentimentWordCount();
        int[] iWordRand = new int[iTotalSentimentWords + 1];
        while (bFullListChanges) {
            Sort.makeRandomOrderList(iWordRand);
            bFullListChanges = false;
            for (int i = 1; i <= iTotalSentimentWords; i++) {
                int iOldSentimentStrength = resources.sentimentWords.getSentiment(iWordRand[i]);
                boolean bCurrentIDChange = false;
                if (iOldSentimentStrength < four) {
                    resources.sentimentWords.setSentiment(iWordRand[i], iOldSentimentStrength + 1);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i], 1);
                    if (bUseTotalDifference) {
                        iNewPosTotalDiff = getClassificationPositiveTotalDifference();
                        iNewNegTotalDiff = getClassificationNegativeTotalDifference();
                        if (((iNewPosTotalDiff - iLastPosTotalDiff) + iNewNegTotalDiff) - iLastNegTotalDiff <= -iMinImprovement) {
                            bCurrentIDChange = true;
                        }
                    } else {
                        iNewPos = getClassificationPositiveNumberCorrect();
                        iNewNeg = getClassificationNegativeNumberCorrect();
                        if (((iNewPos - iLastPos) + iNewNeg) - iLastNeg >= iMinImprovement) {
                            bCurrentIDChange = true;
                        }
                    }
                }
                if (iOldSentimentStrength > segfour && !bCurrentIDChange) {
                    resources.sentimentWords.setSentiment(iWordRand[i], iOldSentimentStrength - 1);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i], 1);
                    if (bUseTotalDifference) {
                        iNewPosTotalDiff = getClassificationPositiveTotalDifference();
                        iNewNegTotalDiff = getClassificationNegativeTotalDifference();
                        if (((iNewPosTotalDiff - iLastPosTotalDiff) + iNewNegTotalDiff) - iLastNegTotalDiff <= -iMinImprovement) {
                            bCurrentIDChange = true;
                        }
                    } else {
                        iNewPos = getClassificationPositiveNumberCorrect();
                        iNewNeg = getClassificationNegativeNumberCorrect();
                        if (((iNewPos - iLastPos) + iNewNeg) - iLastNeg >= iMinImprovement) {
                            bCurrentIDChange = true;
                        }
                    }
                }
                if (bCurrentIDChange) {
                    if (bUseTotalDifference) {
                        iLastNegTotalDiff = iNewNegTotalDiff;
                        iLastPosTotalDiff = iNewPosTotalDiff;
                        bFullListChanges = true;
                    } else {
                        iLastNeg = iNewNeg;
                        iLastPos = iNewPos;
                        bFullListChanges = true;
                    }
                } else {
                    resources.sentimentWords.setSentiment(iWordRand[i], iOldSentimentStrength);
                    reClassifyClassifiedCorpusForSentimentChange(iWordRand[i], 1);
                }
            }
        }
    }

    /**
     * 总结10折交叉验证结果.
     *
     * @param sInputFile  输入文件路径
     * @param sOutputFile 输出文件路径
     */
    public void summariseMultiple10FoldValidations(String sInputFile, String sOutputFile) {
        int iDataRows = twoeight;
        int iLastOptionCol = twofour;
        BufferedReader rResults = null;
        BufferedWriter wSummary = null;
        String sLine = null;
        String[] sPrevData = null;
        String[] sData = null;
        float[] total = new float[iDataRows];
        int iRows = 0;
        int i = 0;
        try {
            rResults = new BufferedReader(new FileReader(sInputFile));
            wSummary = new BufferedWriter(new FileWriter(sOutputFile));
            sLine = rResults.readLine();
            wSummary.write((new StringBuilder(String.valueOf(sLine))).append("\tNumber\n").toString());
            while (rResults.ready()) {
                sLine = rResults.readLine();
                sData = sLine.split("\t");
                boolean bMatching = true;
                if (sPrevData != null) {
                    for (i = 0; i < iLastOptionCol; i++) {
                        if (!sData[i].equals(sPrevData[i])) {
                            bMatching = false;
                        }
                    }
                }

                if (!bMatching) {
                    for (i = 0; i < iLastOptionCol; i++) {
                        wSummary.write((new StringBuilder(String.valueOf(sPrevData[i]))).append("\t").toString());
                    }
                    for (i = 0; i < iDataRows; i++) {
                        wSummary.write((new StringBuilder(String.valueOf(total[i] / (float) iRows))).append("\t").toString());
                    }
                    wSummary.write((new StringBuilder(String.valueOf(iRows))).append("\n").toString());
                    for (i = 0; i < iDataRows; i++) {
                        total[i] = 0.0F;
                    }
                    iRows = 0;
                }
                for (i = iLastOptionCol; i < iLastOptionCol + iDataRows; i++) {
                    try {
                        total[i - iLastOptionCol] += Float.parseFloat(sData[i]);
                    } catch (Exception e) {
                        total[i - iLastOptionCol] += MORENINE;
                    }
                }
                iRows++;
                sPrevData = sLine.split("\t");
            }
            for (i = 0; i < iLastOptionCol; i++) {
                wSummary.write((new StringBuilder(String.valueOf(sPrevData[i]))).append("\t").toString());
            }
            for (i = 0; i < iDataRows; i++) {
                wSummary.write((new StringBuilder(String.valueOf(total[i] / (float) iRows))).append("\t").toString());
            }
            wSummary.write((new StringBuilder(String.valueOf(iRows))).append("\n").toString());
            wSummary.close();
            rResults.close();
        } catch (IOException e) {
            System.out.println((new StringBuilder("SummariseMultiple10FoldValidations: File I/O error: ")).append(sInputFile).toString());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println((new StringBuilder("SummariseMultiple10FoldValidations: Error at line: ")).append(sLine).toString());
            System.out.println((new StringBuilder("Value of i: ")).append(i).toString());
            e.printStackTrace();
        }
    }

    /**
     * 设置option.
     * @param options
     */
    public void setOptions(ClassificationOptions options) {
        this.options = options;
    }

    /**
     * 设置resource.
     * @param resources
     */
    public void setResources(ClassificationResources resources) {
        this.resources = resources;
    }
}

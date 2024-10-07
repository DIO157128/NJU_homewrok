// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   UnusedTermsClassificationIndex.java

package uk.ac.wlv.sentistrength;

import java.io.*;

import uk.ac.wlv.utilities.Trie;

/**
 * 相关用例:UC-28.
 * UnusedTermsClassificationIndex类为“termWeights”选项生成词语权重表
 * 计算输入文件中所有term未被正确计算情感得分的比例，根据这些数据建议用户可以为词典增加的新term
 * 根据不同选项，相应结果会被存储至指定路径下的txt文件中
 */
public class UnusedTermsClassificationIndex {
    /**
     * 术语字符串数组.
     */
    private String[] sgTermList;
    /**
     * 术语数组长度.
     */
    private int igTermListCount;
    /**
     *
     */
    private int igTermListMax;
    private int[] igTermListLessPtr;
    private int[] igTermListMorePtr;
    // 不同term出现次数
    private int[] igTermListFreq;
    private int[] igTermListFreqTemp;
    // 加入到索引列表中的termID的临时数组
    private int[] iTermsAddedIDTemp;
    // 不同选项下,对单一term的预测情感得分和正确得分的差异和
    private int[] igTermListPosClassDiff;
    private int[] igTermListNegClassDiff;
    private int[] igTermListScaleClassDiff;
    private int[] igTermListBinaryClassDiff;
    private int[] igTermListTrinaryClassDiff;
    private int iTermsAddedIDTempCount;
    // 不同选项下,单一term在不同文本段落中的得分分布列表
    private int[][] igTermListPosCorrectClass;
    private int[][] igTermListNegCorrectClass;
    private int[][] igTermListScaleCorrectClass;
    private int[][] igTermListBinaryCorrectClass;
    private int[][] igTermListTrinaryCorrectClass;
    private final int three = 3;
    private final int four = 4;
    private final int segfour = -4;
    private final int five = 5;
    private final int nine = 9;
    private final int fivethousands = 5000;
    /**
     * 该类的构造函数.
     */
    public UnusedTermsClassificationIndex() {
        sgTermList = null;
        igTermListCount = 0;
        igTermListMax = fivethousands;
    }

    /**
     * 类的main函数.
     * @param args1 传入的参数
     */
    public static void main(String[] args1) {
    }

    /**
     * 通过字典树算法,为指定term生成ID,更新ID列表和临时频率表.
     * @param sTerm 待添加的词语
     */
    public void addTermToNewTermIndex(String sTerm) {
        if (sgTermList == null) {
            initialise(true, true, true, true);
        }
        if (sTerm == "") {
            return;
        }
        boolean bDontAddMoreElements = false;
        if (igTermListCount == igTermListMax) {
            bDontAddMoreElements = true;
        }
        int iTermID = Trie.iGetTriePositionForString(sTerm, sgTermList, igTermListLessPtr, igTermListMorePtr, 1, igTermListCount, bDontAddMoreElements);
        if (iTermID > 0) {
            iTermsAddedIDTemp[++iTermsAddedIDTempCount] = iTermID;
            igTermListFreqTemp[iTermID]++;
            if (iTermID > igTermListCount) {
                igTermListCount = iTermID;
            }
        }
    }

    /**
     * 在默认选项(PosNeg)下,将新索引加入到主索引表中.
     * 更新不同term的情感得分分布表和词频表.
     * @param iCorrectPosClass 正确的正面评分
     * @param iEstPosClass 预测的正面评分
     * @param iCorrectNegClass 正确的负面评分
     * @param iEstNegClass 预测的负面评分
     */
    public void addNewIndexToMainIndexWithPosNegValues(int iCorrectPosClass, int iEstPosClass, int iCorrectNegClass, int iEstNegClass) {
        if (iCorrectNegClass > 0 && iCorrectPosClass > 0) {
            for (int iTerm = 1; iTerm <= iTermsAddedIDTempCount; iTerm++) {
                int iTermID = iTermsAddedIDTemp[iTerm];
                if (igTermListFreqTemp[iTermID] != 0) {
                    try {
                        igTermListNegCorrectClass[iTermID][iCorrectNegClass - 1]++;
                        igTermListPosCorrectClass[iTermID][iCorrectPosClass - 1]++;
                        igTermListPosClassDiff[iTermID] += iCorrectPosClass - iEstPosClass;
                        igTermListNegClassDiff[iTermID] += iCorrectNegClass + iEstNegClass;
                        igTermListFreq[iTermID]++;
                        iTermsAddedIDTemp[iTerm] = 0;
                    } catch (Exception e) {
                        System.out.println((new StringBuilder("[UnusedTermsClassificationIndex] Error trying to add Pos + Neg to index. ")).append(e.getMessage()).toString());
                    }
                }
            }

        }
        iTermsAddedIDTempCount = 0;
    }

    /**
     * 在scale选项下,将新索引加入到主索引表中.
     * 更新不同term的情感总体得分表、差异表和词频表.
     * @param iCorrectScaleClass 正确的总体评分
     * @param iEstScaleClass 预测的总体评分
     */
    public void addNewIndexToMainIndexWithScaleValues(int iCorrectScaleClass, int iEstScaleClass) {
        for (int iTerm = 1; iTerm <= iTermsAddedIDTempCount; iTerm++) {
            int iTermID = iTermsAddedIDTemp[iTerm];
            if (igTermListFreqTemp[iTermID] != 0) {
                try {
                    igTermListScaleCorrectClass[iTermID][iCorrectScaleClass + four]++;
                    igTermListScaleClassDiff[iTermID] += iCorrectScaleClass - iEstScaleClass;
                    igTermListFreq[iTermID]++;
                    iTermsAddedIDTemp[iTerm] = 0;
                } catch (Exception e) {
                    System.out.println((new StringBuilder("Error trying to add scale values to index. ")).append(e.getMessage()).toString());
                }
            }
        }

        iTermsAddedIDTempCount = 0;
    }

    /**
     * 在trinary选项下,将新索引加入到主索引表中.
     * 更新不同term的情感三元得分表、差异表和词频表.
     * @param iCorrectTrinaryClass 正确的三元评分
     * @param iEstTrinaryClass 预测的三元评分
     */
    public void addNewIndexToMainIndexWithTrinaryValues(int iCorrectTrinaryClass, int iEstTrinaryClass) {
        for (int iTerm = 1; iTerm <= iTermsAddedIDTempCount; iTerm++) {
            int iTermID = iTermsAddedIDTemp[iTerm];
            if (igTermListFreqTemp[iTermID] != 0) {
                try {
                    igTermListTrinaryCorrectClass[iTermID][iCorrectTrinaryClass + 1]++;
                    igTermListTrinaryClassDiff[iTermID] += iCorrectTrinaryClass - iEstTrinaryClass;
                    igTermListFreq[iTermID]++;
                    iTermsAddedIDTemp[iTerm] = 0;
                } catch (Exception e) {
                    System.out.println((new StringBuilder("Error trying to add trinary values to index. ")).append(e.getMessage()).toString());
                }
            }
        }
        iTermsAddedIDTempCount = 0;
    }

    /**
     * 在binary选项下,将新索引加入到主索引表中.
     * 更新不同term的情感二元得分表、差异表和词频表.
     * @param iCorrectBinaryClass 正确的二元评分
     * @param iEstBinaryClass 预测的二元评分
     */
    public void addNewIndexToMainIndexWithBinaryValues(int iCorrectBinaryClass, int iEstBinaryClass) {
        for (int iTerm = 1; iTerm <= iTermsAddedIDTempCount; iTerm++) {
            int iTermID = iTermsAddedIDTemp[iTerm];
            if (igTermListFreqTemp[iTermID] != 0) {
                try {
                    igTermListBinaryClassDiff[iTermID] += iCorrectBinaryClass - iEstBinaryClass;
                    if (iCorrectBinaryClass == -1) {
                        iCorrectBinaryClass = 0;
                    }
                    igTermListBinaryCorrectClass[iTermID][iCorrectBinaryClass]++;
                    igTermListFreq[iTermID]++;
                    iTermsAddedIDTemp[iTerm] = 0;
                } catch (Exception e) {
                    System.out.println((new StringBuilder("Error trying to add scale values to index. ")).append(e.getMessage()).toString());
                }
            }
        }

        iTermsAddedIDTempCount = 0;
    }

    /**
     * 初始化所指定的选项下，term情感得分表、得分差异表、词频表以及其他基本数据.
     * @param bInitialiseScale scale选项标签
     * @param bInitialisePosNeg posNeg选项标签
     * @param bInitialiseBinary binary选项标签
     * @param bInitialiseTrinary trinary选项标签
     */
    public void initialise(boolean bInitialiseScale, boolean bInitialisePosNeg, boolean bInitialiseBinary, boolean bInitialiseTrinary) {
        igTermListCount = 0;
        igTermListMax = fivethousands;
        iTermsAddedIDTempCount = 0;
        sgTermList = new String[igTermListMax];
        igTermListLessPtr = new int[igTermListMax + 1];
        igTermListMorePtr = new int[igTermListMax + 1];
        igTermListFreq = new int[igTermListMax + 1];
        igTermListFreqTemp = new int[igTermListMax + 1];
        iTermsAddedIDTemp = new int[igTermListMax + 1];
        if (bInitialisePosNeg) {
            igTermListNegCorrectClass = new int[igTermListMax + 1][five];
            igTermListPosCorrectClass = new int[igTermListMax + 1][five];
            igTermListNegClassDiff = new int[igTermListMax + 1];
            igTermListPosClassDiff = new int[igTermListMax + 1];
        }
        if (bInitialiseScale) {
            igTermListScaleCorrectClass = new int[igTermListMax + 1][nine];
            igTermListScaleClassDiff = new int[igTermListMax + 1];
        }
        if (bInitialiseBinary) {
            igTermListBinaryCorrectClass = new int[igTermListMax + 1][2];
            igTermListBinaryClassDiff = new int[igTermListMax + 1];
        }
        if (bInitialiseTrinary) {
            igTermListTrinaryCorrectClass = new int[igTermListMax + 1][three];
            igTermListTrinaryClassDiff = new int[igTermListMax + 1];
        }
    }

    /**
     * 在默认选项posNeg下，打印输入文件中所有term的权重表.
     * @param sOutputFile 结果文件路径
     * @param iMinFreq 需满足的最小词频
     */
    public void printIndexWithPosNegValues(String sOutputFile, int iMinFreq) {
        try {
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sOutputFile));
            wWriter.write((new StringBuilder("Term\tTermFreq >= ")).append(iMinFreq).append("\t").append("PosClassDiff (correct-estimate)\t").append("NegClassDiff\t").append("PosClassAvDiff\t").append("NegClassAvDiff\t").toString());
            for (int i = 1; i <= five; i++) {
                wWriter.write((new StringBuilder("CorrectClass")).append(i).append("pos\t").toString());
            }
            for (int i = 1; i <= five; i++) {
                wWriter.write((new StringBuilder("CorrectClass")).append(i).append("neg\t").toString());
            }
            wWriter.write("\n");
            if (igTermListCount > 0) {
                for (int iTerm = 1; iTerm <= igTermListCount; iTerm++) {
                    if (igTermListFreq[iTerm] >= iMinFreq) {
                        wWriter.write((new StringBuilder(String.valueOf(sgTermList[iTerm]))).append("\t").append(igTermListFreq[iTerm]).append("\t").append(igTermListPosClassDiff[iTerm]).append("\t").append(igTermListNegClassDiff[iTerm]).append("\t").append((float) igTermListPosClassDiff[iTerm] / (float) igTermListFreq[iTerm]).append("\t").append((float) igTermListNegClassDiff[iTerm] / (float) igTermListFreq[iTerm]).append("\t").toString());
                        for (int i = 0; i < five; i++) {
                            wWriter.write((new StringBuilder(String.valueOf(igTermListPosCorrectClass[iTerm][i]))).append("\t").toString());
                        }
                        for (int i = 0; i < five; i++) {
                            wWriter.write((new StringBuilder(String.valueOf(igTermListNegCorrectClass[iTerm][i]))).append("\t").toString());
                        }
                        wWriter.write("\n");
                    }
                }
            } else {
                wWriter.write("No terms found in corpus!\n");
            }
            wWriter.close();
        } catch (IOException e) {
            System.out.println((new StringBuilder("Error printing index to ")).append(sOutputFile).toString());
            e.printStackTrace();
        }
    }

    /**
     * 在选项scale下，打印输入文件中所有term的权重表.
     * @param sOutputFile 结果文件路径
     * @param iMinFreq 需满足的最小词频
     */
    public void printIndexWithScaleValues(String sOutputFile, int iMinFreq) {
        try {
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sOutputFile));
            wWriter.write("Term\tTermFreq\tScaleClassDiff (correct-estimate)\tScaleClassAvDiff\t");
            for (int i = segfour; i <= four; i++) {
                wWriter.write((new StringBuilder("CorrectClass")).append(i).append("\t").toString());
            }
            wWriter.write("\n");
            for (int iTerm = 1; iTerm <= igTermListCount; iTerm++) {
                if (igTermListFreq[iTerm] > iMinFreq) {
                    wWriter.write((new StringBuilder(String.valueOf(sgTermList[iTerm]))).append("\t").append(igTermListFreq[iTerm]).append("\t").append(igTermListScaleClassDiff[iTerm]).append("\t").append((float) igTermListScaleClassDiff[iTerm] / (float) igTermListFreq[iTerm]).append("\t").toString());
                    for (int i = 0; i < nine; i++) {
                        wWriter.write((new StringBuilder(String.valueOf(igTermListScaleCorrectClass[iTerm][i]))).append("\t").toString());
                    }
                    wWriter.write("\n");
                }
            }
            wWriter.close();
        } catch (IOException e) {
            System.out.println((new StringBuilder("Error printing Scale index to ")).append(sOutputFile).toString());
            e.printStackTrace();
        }
    }

    /**
     * 在选项trinary下，打印输入文件中所有term的权重表.
     * @param sOutputFile 结果文件路径
     * @param iMinFreq 需满足的最小词频
     */
    public void printIndexWithTrinaryValues(String sOutputFile, int iMinFreq) {
        try {
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sOutputFile));
            wWriter.write("Term\tTermFreq\tTrinaryClassDiff (correct-estimate)\tTrinaryClassAvDiff\t");
            for (int i = -1; i <= 1; i++) {
                wWriter.write((new StringBuilder("CorrectClass")).append(i).append("\t").toString());
            }
            wWriter.write("\n");
            for (int iTerm = 1; iTerm <= igTermListCount; iTerm++) {
                if (igTermListFreq[iTerm] > iMinFreq) {
                    wWriter.write((new StringBuilder(String.valueOf(sgTermList[iTerm]))).append("\t").append(igTermListFreq[iTerm]).append("\t").append(igTermListTrinaryClassDiff[iTerm]).append("\t").append((float) igTermListTrinaryClassDiff[iTerm] / (float) igTermListFreq[iTerm]).append("\t").toString());
                    for (int i = 0; i < three; i++) {
                        wWriter.write((new StringBuilder(String.valueOf(igTermListTrinaryCorrectClass[iTerm][i]))).append("\t").toString());
                    }
                    wWriter.write("\n");
                }
            }
            wWriter.close();
        } catch (IOException e) {
            System.out.println((new StringBuilder("Error printing Trinary index to ")).append(sOutputFile).toString());
            e.printStackTrace();
        }
    }

    /**
     * 在选项binary下，打印输入文件中所有term的权重表.
     * @param sOutputFile 结果文件路径
     * @param iMinFreq 需满足的最小词频
     */
    public void printIndexWithBinaryValues(String sOutputFile, int iMinFreq) {
        try {
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sOutputFile));
            wWriter.write("Term\tTermFreq\tBinaryClassDiff (correct-estimate)\tBinaryClassAvDiff\t");
            wWriter.write("CorrectClass-1\tCorrectClass1\t");
            wWriter.write("\n");
            for (int iTerm = 1; iTerm <= igTermListCount; iTerm++) {
                if (igTermListFreq[iTerm] > iMinFreq) {
                    wWriter.write((new StringBuilder(String.valueOf(sgTermList[iTerm]))).append("\t").append(igTermListFreq[iTerm]).append("\t").append(igTermListBinaryClassDiff[iTerm]).append("\t").append((float) igTermListBinaryClassDiff[iTerm] / (float) igTermListFreq[iTerm]).append("\t").toString());
                    for (int i = 0; i < 2; i++) {
                        wWriter.write((new StringBuilder(String.valueOf(igTermListBinaryCorrectClass[iTerm][i]))).append("\t").toString());
                    }
                    wWriter.write("\n");
                }
            }
            wWriter.close();
        } catch (IOException e) {
            System.out.println((new StringBuilder("Error printing Binary index to ")).append(sOutputFile).toString());
            e.printStackTrace();
        }
    }
}

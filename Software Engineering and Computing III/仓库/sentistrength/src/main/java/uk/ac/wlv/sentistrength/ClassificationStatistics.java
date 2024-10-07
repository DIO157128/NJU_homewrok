// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   ClassificationStatistics.java

package uk.ac.wlv.sentistrength;

/**
 * 相关用例:UC-29.
 * ClassificationStatistics类包含了一系列关于统计学的静态方法，包括计算相关性，生成混淆矩阵，计算准确性，计算误差等一系列数学方法.
 */
public class ClassificationStatistics {

    private static final int segtwo = -2;
    private static final int onehundred = 100;

    /**
     * ClassificationStatistics类的构造函数.
     */
    public ClassificationStatistics() {
    }

    /**
     * 用来计算两个数组之间的绝对值相关性.
     *
     * @param iCorrect   正确值的整数数组
     * @param iPredicted 预测值的整数数组
     * @param iCount     数组元素的数量
     * @return 返回两个数组之间相关性的绝对值
     */
    public static double correlationAbs(int[] iCorrect, int[] iPredicted, int iCount) {
        double fMeanC = 0.0D;
        double fMeanP = 0.0D;
        double fProdCP = 0.0D; //正确值和预测值之间的乘积之和
        double fSumCSq = 0.0D; //正确值的平方和
        double fSumPSq = 0.0D; //预测值的平方和
        for (int iRow = 1; iRow <= iCount; iRow++) {
            fMeanC += Math.abs(iCorrect[iRow]);
            fMeanP += Math.abs(iPredicted[iRow]);
        }

        fMeanC /= iCount;
        fMeanP /= iCount;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            fProdCP += ((double) Math.abs(iCorrect[iRow]) - fMeanC) * ((double) Math.abs(iPredicted[iRow]) - fMeanP);
            fSumPSq += Math.pow((double) Math.abs(iPredicted[iRow]) - fMeanP, 2D);
            fSumCSq += Math.pow((double) Math.abs(iCorrect[iRow]) - fMeanC, 2D);
        }

        return fProdCP / (Math.sqrt(fSumPSq) * Math.sqrt(fSumCSq));
    }


    /**
     * 计算两个数组之间相关性的函数.
     *
     * @param iCorrect   正确值数组
     * @param iPredicted 预测值数组
     * @param iCount     数组中元素的数量
     * @return 两个数组之间的相关性
     */
    public static double correlation(int[] iCorrect, int[] iPredicted, int iCount) {
        double fMeanC = 0.0D;
        double fMeanP = 0.0D;
        double fProdCP = 0.0D;
        double fSumCSq = 0.0D;
        double fSumPSq = 0.0D;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            fMeanC += iCorrect[iRow];
            fMeanP += iPredicted[iRow];
        }

        fMeanC /= iCount;
        fMeanP /= iCount;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            fProdCP += ((double) iCorrect[iRow] - fMeanC) * ((double) iPredicted[iRow] - fMeanP);
            fSumPSq += Math.pow((double) iPredicted[iRow] - fMeanP, 2D);
            fSumCSq += Math.pow((double) iCorrect[iRow] - fMeanC, 2D);
        }

        return fProdCP / (Math.sqrt(fSumPSq) * Math.sqrt(fSumCSq));
    }

    /**
     * 生成三元或二元混淆矩阵的函数.
     *
     * @param iTrinaryEstimate 一个三元或二元估计值的整数数组
     * @param iTrinaryCorrect  一个三元或者二元正确值的整数数组
     * @param iDataCount       数组元素的数量
     * @param estCorr          用来储存混淆矩阵
     */
    public static void trinaryOrBinaryConfusionTable(int[] iTrinaryEstimate, int[] iTrinaryCorrect, int iDataCount, int[][] estCorr) {
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                estCorr[i][j] = 0;
            }
        }

        for (int i = 1; i <= iDataCount; i++) {
            if (iTrinaryEstimate[i] > segtwo && iTrinaryEstimate[i] < 2 && iTrinaryCorrect[i] > segtwo && iTrinaryCorrect[i] < 2) {
                estCorr[iTrinaryEstimate[i] + 1][iTrinaryCorrect[i] + 1]++;
            } else {
                System.out.println((new StringBuilder("Estimate or correct value ")).append(i).append(" out of range -1 to +1 (data count may be wrong): ").append(iTrinaryEstimate[i]).append(" ").append(iTrinaryCorrect[i]).toString());
            }
        }
    }


    /**
     * 用来计算两个数组之间的绝对值相关性.
     *
     * @param iCorrect   正确值的整数数组
     * @param iPredicted 预测值的整数数组
     * @param bSelected  选择标志数组，表示哪些元素参与计算
     * @param bInvert    反转标志，表示是否反转选择标志数组
     * @param iCount     数组长度
     * @return 相关系数，范围在-1到+1之间，-1表示负相关，+1表示正相关，0表示无关
     */
    public static double correlationAbs(int[] iCorrect, int[] iPredicted, boolean[] bSelected, boolean bInvert, int iCount) {
        double fMeanC = 0.0D;
        double fMeanP = 0.0D;
        double fProdCP = 0.0D;
        double fSumCSq = 0.0D;
        double fSumPSq = 0.0D;
        int iDataCount = 0;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            if (bSelected[iRow] && !bInvert || !bSelected[iRow] && bInvert) {
                fMeanC += Math.abs(iCorrect[iRow]);
                fMeanP += Math.abs(iPredicted[iRow]);
                iDataCount++;
            }
        }
        fMeanC /= iDataCount;
        fMeanP /= iDataCount;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            if (bSelected[iRow] && !bInvert || !bSelected[iRow] && bInvert) {
                fProdCP += ((double) Math.abs(iCorrect[iRow]) - fMeanC) * ((double) Math.abs(iPredicted[iRow]) - fMeanP);
                fSumPSq += Math.pow((double) Math.abs(iPredicted[iRow]) - fMeanP, 2D);
                fSumCSq += Math.pow((double) Math.abs(iCorrect[iRow]) - fMeanC, 2D);
            }
        }
        return fProdCP / (Math.sqrt(fSumPSq) * Math.sqrt(fSumCSq));
    }

    /**
     * 计算预测值与实际正确值之间的准确性.
     *
     * @param iCorrect              包含实际正确值的数组
     * @param iPredicted            包含预测值的数组
     * @param iCount                要考虑的数据点数
     * @param bChangeSignOfOneArray 是否在比较前更改数组的符号
     * @return 正确预测的数量
     */
    public static int accuracy(int[] iCorrect, int[] iPredicted, int iCount, boolean bChangeSignOfOneArray) {
        int iCorrectCount = 0;
        if (bChangeSignOfOneArray) {
            for (int iRow = 1; iRow <= iCount; iRow++) {
                if (iCorrect[iRow] == -iPredicted[iRow]) {
                    iCorrectCount++;
                }
            }
        } else {
            for (int iRow = 1; iRow <= iCount; iRow++) {
                if (iCorrect[iRow] == iPredicted[iRow]) {
                    iCorrectCount++;
                }
            }
        }
        return iCorrectCount;
    }

    /**
     * 计算分类准确率.
     *
     * @param iCorrect   正确的分类数组.
     * @param iPredicted 预测的分类数组.
     * @param bSelected  是否选择当前数据的布尔数组.
     * @param bInvert    是否取反选择的数据.
     * @param iCount     数据总数.
     * @return 准确分类的数据数.
     */
    public static int accuracy(int[] iCorrect, int[] iPredicted, boolean[] bSelected, boolean bInvert, int iCount) {
        int iCorrectCount = 0;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            if ((bSelected[iRow] && !bInvert || !bSelected[iRow] && bInvert) && iCorrect[iRow] == iPredicted[iRow]) {
                iCorrectCount++;
            }
        }
        return iCorrectCount;
    }


    /**
     * 计算两个整数数组之间的准确度.
     *
     * @param iCorrect   正确值数组
     * @param iPredicted 预测值数组
     * @param bSelected  选择数组，用于指定哪些元素应该被计算
     * @param bInvert    如果为true，则只计算未被选择的元素；否则，只计算被选择的元素
     * @param iCount     数组中元素的数量
     * @return 准确度（即预测值与正确值之差小于等于1的元素数量）
     */
    public static int accuracyWithin1(int[] iCorrect, int[] iPredicted, boolean[] bSelected, boolean bInvert, int iCount) {
        int iCorrectCount = 0;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            if ((bSelected[iRow] && !bInvert || !bSelected[iRow] && bInvert) && Math.abs(iCorrect[iRow] - iPredicted[iRow]) <= 1) {
                iCorrectCount++;
            }
        }
        return iCorrectCount;
    }

    /**
     * 计算预测值与正确值差距为1以内的样本数的准确率.
     *
     * @param iCorrect              正确值数组
     * @param iPredicted            预测值数组
     * @param iCount                样本数
     * @param bChangeSignOfOneArray 是否需要改变一个数组的符号
     * @return 预测值与正确值差距为1以内的样本数的准确率
     */
    public static int accuracyWithin1(int[] iCorrect, int[] iPredicted, int iCount, boolean bChangeSignOfOneArray) {
        int iCorrectCount = 0;
        if (bChangeSignOfOneArray) {
            for (int iRow = 1; iRow <= iCount; iRow++) {
                if (Math.abs(iCorrect[iRow] + iPredicted[iRow]) <= 1) {
                    iCorrectCount++;
                }
            }
        } else {
            for (int iRow = 1; iRow <= iCount; iRow++) {
                if (Math.abs(iCorrect[iRow] - iPredicted[iRow]) <= 1) {
                    iCorrectCount++;
                }
            }
        }
        return iCorrectCount;
    }

    /**
     * 计算不使用除法的平均百分比误差.
     *
     * @param iCorrect   int[] 包含正确的值。
     * @param iPredicted int[] 包含预测的值。
     * @param bSelected  boolean[] 一个标志数组，指示哪些值应该用于计算平均百分比误差。
     * @param bInvert    boolean 一个标志，指示是否应该选择未标记的值。
     * @param iCount     int 包含数组的大小。
     * @return double 平均百分比误差。
     */
    public static double absoluteMeanPercentageErrorNoDivision(int[] iCorrect, int[] iPredicted, boolean[] bSelected, boolean bInvert, int iCount) {
        int iDataCount = 0;
        double fAMeanPE = 0.0D;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            if (bSelected[iRow] && !bInvert || !bSelected[iRow] && bInvert) {
                fAMeanPE += Math.abs(iPredicted[iRow] - iCorrect[iRow]);
                iDataCount++;
            }
        }
        return fAMeanPE / (double) iDataCount;
    }

    /**
     * 计算平均百分比误差.
     *
     * @param iCorrect   int[] 包含正确的值。
     * @param iPredicted int[] 包含预测的值。
     * @param bSelected  boolean[] 一个标志数组，指示哪些值应该用于计算平均百分比误差。
     * @param bInvert    boolean 一个标志，指示是否应该选择未标记的值。
     * @param iCount     int 包含数组的大小。
     * @return double 平均百分比误差。
     */

    public static double absoluteMeanPercentageError(int[] iCorrect, int[] iPredicted, boolean[] bSelected, boolean bInvert, int iCount) {
        int iDataCount = 0;
        double fAMeanPE = 0.0D;
        for (int iRow = 1; iRow <= iCount; iRow++) {
            if (bSelected[iRow] && !bInvert || !bSelected[iRow] && bInvert) {
                fAMeanPE += Math.abs((double) (iPredicted[iRow] - iCorrect[iRow]) / (double) iCorrect[iRow]);
                iDataCount++;
            }
        }
        return fAMeanPE / (double) iDataCount;
    }

    /**
     * 不用除法，计算平均百分比误差的绝对值.
     *
     * @param iCorrect              一个整数数组，表示正确的值
     * @param iPredicted            一个整数数组，表示预测的值
     * @param iCount                一个整数，表示数组中的元素数量
     * @param bChangeSignOfOneArray 一个布尔值，表示是否需要更改 iCorrect 数组中的值的符号
     * @return 返回平均百分比误差的绝对值
     */
    public static double absoluteMeanPercentageErrorNoDivision(int[] iCorrect, int[] iPredicted, int iCount, boolean bChangeSignOfOneArray) {
        double fAMeanPE = 0.0D;
        if (bChangeSignOfOneArray) {
            for (int iRow = 1; iRow <= iCount; iRow++) {
                fAMeanPE += Math.abs(iPredicted[iRow] + iCorrect[iRow]);
            }
        } else {
            for (int iRow = 1; iRow <= iCount; iRow++) {
                fAMeanPE += Math.abs(iPredicted[iRow] - iCorrect[iRow]);
            }
        }
        return fAMeanPE / (double) iCount;
    }

    /**
     * 计算基线准确率（Majority Class Proportion）.
     * 基线准确率是指将所有实例都预测为数据集中出现最多的类别的准确率，
     * 即最大类别的数量除以数据集实例的总数.
     *
     * @param iCorrect 一个包含正确类别标签的整数数组
     * @param iCount   数据集实例的总数
     * @return 基线准确率
     */

    public static double baselineAccuracyMajorityClassProportion(int[] iCorrect, int iCount) {
        if (iCount == 0) {
            return 0.0D;
        }
        int[] iClassCount = new int[onehundred];
        int iMinClass = iCorrect[1];
        int iMaxClass = iCorrect[1];
        for (int i = 2; i <= iCount; i++) {
            if (iCorrect[i] < iMinClass) {
                iMinClass = iCorrect[i];
            }
            if (iCorrect[i] > iMaxClass) {
                iMaxClass = iCorrect[i];
            }
        }

        if (iMaxClass - iMinClass >= onehundred) {
            return 0.0D;
        }
        for (int i = 0; i <= iMaxClass - iMinClass; i++) {
            iClassCount[i] = 0;
        }
        for (int i = 1; i <= iCount; i++) {
            iClassCount[iCorrect[i] - iMinClass]++;
        }
        int iMaxClassCount = 0;
        for (int i = 0; i <= iMaxClass - iMinClass; i++) {
            if (iClassCount[i] > iMaxClassCount) {
                iMaxClassCount = iClassCount[i];
            }
        }
        return (double) iMaxClassCount / (double) iCount;
    }

    /**
     * 基准准确率-预测最大类别.
     *
     * @param iCorrect    实际类别数组
     * @param iPredict    预测类别数组
     * @param iCount      数据点总数
     * @param bChangeSign 是否更改符号
     */
    public static void baselineAccuracyMakeLargestClassPrediction(int[] iCorrect, int[] iPredict, int iCount, boolean bChangeSign) {
        if (iCount == 0) {
            return;
        }
        int[] iClassCount = new int[onehundred];
        int iMinClass = iCorrect[1];
        int iMaxClass = iCorrect[1];
        for (int i = 2; i <= iCount; i++) {
            if (iCorrect[i] < iMinClass) {
                iMinClass = iCorrect[i];
            }
            if (iCorrect[i] > iMaxClass) {
                iMaxClass = iCorrect[i];
            }
        }

        if (iMaxClass - iMinClass >= onehundred) {
            return;
        }
        for (int i = 0; i <= iMaxClass - iMinClass; i++) {
            iClassCount[i] = 0;
        }
        for (int i = 1; i <= iCount; i++) {
            iClassCount[iCorrect[i] - iMinClass]++;
        }
        int iMaxClassCount = 0;
        int iLargestClass = 0;
        for (int i = 0; i <= iMaxClass - iMinClass; i++) {
            if (iClassCount[i] > iMaxClassCount) {
                iMaxClassCount = iClassCount[i];
                iLargestClass = i + iMinClass;
            }
        }
        if (bChangeSign) {
            for (int i = 1; i <= iCount; i++) {
                iPredict[i] = -iLargestClass;
            }
        } else {
            for (int i = 1; i <= iCount; i++) {
                iPredict[i] = iLargestClass;
            }
        }
    }

    /**
     * 计算绝对平均百分比误差（Absolute Mean Percentage Error）.
     *
     * @param iCorrect              正确的预测值数组
     * @param iPredicted            预测的值数组
     * @param iCount                数组长度
     * @param bChangeSignOfOneArray 是否改变其中一个数组的符号
     *                              如果为true，则将预测值数组和正确值数组相加后取绝对值再除以正确值数组
     *                              如果为false，则将预测值数组减去正确值数组后取绝对值再除以正确值数组
     * @return 绝对平均百分比误差
     */

    public static double absoluteMeanPercentageError(int[] iCorrect, int[] iPredicted, int iCount, boolean bChangeSignOfOneArray) {
        double fAMeanPE = 0.0D;
        if (bChangeSignOfOneArray) {
            for (int iRow = 1; iRow <= iCount; iRow++) {
                fAMeanPE += Math.abs((double) (iPredicted[iRow] + iCorrect[iRow]) / (double) iCorrect[iRow]);
            }
        } else {
            for (int iRow = 1; iRow <= iCount; iRow++) {
                fAMeanPE += Math.abs((double) (iPredicted[iRow] - iCorrect[iRow]) / (double) iCorrect[iRow]);
            }
        }
        return fAMeanPE / (double) iCount;
    }
}

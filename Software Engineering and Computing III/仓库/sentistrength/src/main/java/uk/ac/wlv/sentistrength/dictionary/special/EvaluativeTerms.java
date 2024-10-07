// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   EvaluativeTerms.java

package uk.ac.wlv.sentistrength.dictionary.special;

import java.io.*;
import java.nio.charset.StandardCharsets;


import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.utilities.FileOps;

// Referenced classes of package uk.ac.wlv.sentistrength:
//            ClassificationOptions, IdiomList, SentimentWords

/**
 * 相关用例:UC-1,UC-2
 * EvaluativeTerms类创建了一系列与评估术语字符串数组有关的变量，并对其进行初始化、排序等操作.
 */
public class EvaluativeTerms {

    private int igObjectEvaluationMax;
    public String[] sgObject;
    public String[] sgObjectEvaluation;
    public int[] igObjectEvaluationStrength;
    public int igObjectEvaluationCount;

    /**
     * EvaluativeTerms方法是EvaluativeTerms类的构造函数，将成员变量igObjectEvaluationMax，igObjectEvaluationCount赋值为0.
     */
    public EvaluativeTerms() {
        igObjectEvaluationMax = 0;
        igObjectEvaluationCount = 0;
    }

    /**
     * initialise方法，对EvaluativeTerms类进行初始化.
     *
     * @param sSourceFile    字符串变量，进行处理的文件路径
     * @param options        传入方法的ClassificationOptions类型对象，使用其bgForceUTF8来判断是否要以UTF8读取sSourceFile内容
     * @param idiomList      传入方法的待处理习语列表对象
     * @param sentimentWords 传入的待处理情感词列表对象
     * @return true或者false，代表初始化成功或是失败
     */
    public boolean initialise(String sSourceFile, ClassificationOptions options, IdiomList idiomList, SentimentWords sentimentWords) {
        if (igObjectEvaluationCount > 0) {
            return true;
        }
        File f = new File(sSourceFile);
        if (!f.exists()) {
            System.out.println("Could not find additional (object/evaluation) file: " + sSourceFile);
            return false;
        }
        int iStrength;
        boolean bIdiomsAdded = false;
        boolean bSentimentWordsAdded = false;
        try {
            igObjectEvaluationMax = FileOps.iCountLinesInTextFile(sSourceFile) + 2;
            igObjectEvaluationCount = 0;
            sgObject = new String[igObjectEvaluationMax];
            sgObjectEvaluation = new String[igObjectEvaluationMax];
            igObjectEvaluationStrength = new int[igObjectEvaluationMax];
            BufferedReader rReader;
            if (options.bgForceUTF8) {
                rReader = new BufferedReader(new InputStreamReader(new FileInputStream(sSourceFile), StandardCharsets.UTF_8));

            } else {
                rReader = new BufferedReader(new FileReader(sSourceFile));
            }

            String sLine;
            while ((sLine = rReader.readLine()) != null) {
                if (!sLine.equals("") && sLine.indexOf("##") != 0 && sLine.indexOf("\t") > 0) {
                    String[] sData = sLine.split("\t");
                    if (sData.length > 2 && sData[2].indexOf("##") != 0) {
                        sgObject[++igObjectEvaluationCount] = sData[0];
                        sgObjectEvaluation[igObjectEvaluationCount] = sData[1];
                        try {
                            int temInt = Integer.parseInt(sData[2].trim());
                            if (temInt > 0) {
                                temInt -= 1;
                            } else if (temInt < 0) {
                                temInt += 1;
                            }
                            igObjectEvaluationStrength[igObjectEvaluationCount] = temInt;
                        } catch (NumberFormatException e) {
                            System.out.println("Failed to identify integer weight for object/evaluation! Ignoring object/evaluation");
                            System.out.println("Line: " + sLine);
                            igObjectEvaluationCount--;
                        }
                    } else if (sData[0].indexOf(" ") > 0) {
                        try {
                            iStrength = Integer.parseInt(sData[1].trim());
                            idiomList.addExtraIdiom(sData[0], iStrength, false);
                            bIdiomsAdded = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Failed to identify integer weight for idiom in additional file! Ignoring it");
                            System.out.println("Line: " + sLine);
                        }
                    } else {
                        try {
                            iStrength = Integer.parseInt(sData[1].trim());
                            sentimentWords.addOrModifySentimentTerm(sData[0], iStrength, false);
                            bSentimentWordsAdded = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Failed to identify integer weight for sentiment term in additional file! Ignoring it");
                            System.out.println("Line: " + sLine);
                            igObjectEvaluationCount--;
                        }
                    }

                }
            }

            rReader.close();
            if (igObjectEvaluationCount > 0) {
                options.bgUseObjectEvaluationTable = true;
            }

            if (bSentimentWordsAdded) {
                sentimentWords.sortSentimentList();
            }

            if (bIdiomsAdded) {
                idiomList.convertIdiomStringsToWordLists();
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not find additional (object/evaluation) file: " + sSourceFile);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println("Found additional (object/evaluation) file but could not read from it: " + sSourceFile);
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

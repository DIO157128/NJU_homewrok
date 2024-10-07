// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   SentimentWords.java

package uk.ac.wlv.sentistrength.dictionary.special;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.Corpus;
import uk.ac.wlv.utilities.FileOps;
import uk.ac.wlv.utilities.Sort;

// Referenced classes of package uk.ac.wlv.sentistrength:
//            Corpus, ClassificationOptions

/**
 * 相关用例:UC-1,UC-17
 * SentimentWords类创建了一系列情感词有关的数组和变量，并对其进行初始化、保存、修改、新增等操作.
 */
public class SentimentWords {
    private static final int NINENINENINE = 999;
    private String[] sgSentimentWords;
    private int[] igSentimentWordsStrengthTake1;
    private int igSentimentWordsCount;
    private String[] sgSentimentWordsWithStarAtStart;
    private int[] igSentimentWordsWithStarAtStartStrengthTake1;
    private int igSentimentWordsWithStarAtStartCount;
    private boolean[] bgSentimentWordsWithStarAtStartHasStarAtEnd;

    /**
     * SentimentWords方法是SentimentWords类的构造方法，为成员变量igSentimentWordsCount，igSentimentWordsWithStarAtStartCount赋值为0.
     */
    public SentimentWords() {
        igSentimentWordsCount = 0;
        igSentimentWordsWithStarAtStartCount = 0;
    }

    /**
     * 根据iWordID返回相应的字符串,过程中需要判断iWordID对应字符串首字符是否为*.
     *
     * @param iWordID 待判断的字符串序号
     * @return 找到则返回相应字符串，否则返回“”
     */
    public String getSentimentWord(int iWordID) {
        if (iWordID > 0) {
            if (iWordID <= igSentimentWordsCount) {
                return sgSentimentWords[iWordID];
            }

            if (iWordID <= igSentimentWordsCount + igSentimentWordsWithStarAtStartCount) {
                return sgSentimentWordsWithStarAtStart[iWordID - igSentimentWordsCount];
            }

        }
        return "";
    }

    /**
     * 根据给出的字符串返回其情感强度值并对sgSentimentWords进行排序处理，过程中需要判断sWord字符串首字符是否为*.
     *
     * @param sWord 待匹配的字符串
     * @return 找到则返回相应数值，否则返回999
     */
    public int getSentiment(String sWord) {
        int iWordID = Sort.iFindStringPositionInSortedArrayWithWildcardsInArray(sWord.toLowerCase(), sgSentimentWords, 1, igSentimentWordsCount);
        if (iWordID >= 0) {
            return igSentimentWordsStrengthTake1[iWordID];
        }

        int iStarWordID = getMatchingStarAtStartRawWordID(sWord);
        if (iStarWordID >= 0) {
            return igSentimentWordsWithStarAtStartStrengthTake1[iStarWordID];
        } else {
            return NINENINENINE;
        }

    }

    /**
     * setSentiment方法为igSentimentWordsWithStarAtStartStrengthTake1和igSentimentWordsStrengthTake1设置新的元素，即为其中的语句更新情感强度.
     *
     * @param sWord         字符串变量，指待设置的字符串
     * @param iNewSentiment int型变量，指该语句的情感强度
     * @return true或者false，代表设置成功或是失败
     */
    public boolean setSentiment(String sWord, int iNewSentiment) {
        int iWordID = Sort.iFindStringPositionInSortedArrayWithWildcardsInArray(sWord.toLowerCase(), sgSentimentWords, 1, igSentimentWordsCount);
        if (iWordID >= 0) {
            if (iNewSentiment > 0) {
                setSentiment(iWordID, iNewSentiment - 1);
            } else {
                setSentiment(iWordID, iNewSentiment + 1);
            }

            return true;
        }
        if (sWord.indexOf("*") == 0) {
            sWord = sWord.substring(1);
            if (sWord.indexOf("*") > 0) {
                sWord = sWord.substring(0, sWord.length() - 1);
            }

        }
        if (igSentimentWordsWithStarAtStartCount > 0) {
            for (int i = 1; i <= igSentimentWordsWithStarAtStartCount; i++) {
                if (sWord.equals(sgSentimentWordsWithStarAtStart[i])) {
                    if (iNewSentiment > 0) {
                        setSentiment(igSentimentWordsCount + i, iNewSentiment - 1);

                    } else {
                        setSentiment(igSentimentWordsCount + i, iNewSentiment + 1);
                    }
                    return true;
                }
            }


        }
        return false;
    }

    /**
     * saveSentimentList方法向文件sFilename中保存情感语和它的情感强度.
     *
     * @param sFilename 字符串型变量，待保存内容的文件
     * @param c         传入的语料库，需要使用c.options.bgForceUTF8来判断是否要以UTF8读取sFilename内容
     * @return true或者false，代表保存成功或是失败
     */
    public boolean saveSentimentList(String sFilename, Corpus c) {
        try {
            BufferedWriter wWriter = new BufferedWriter(new FileWriter(sFilename));
            for (int i = 1; i <= igSentimentWordsCount; i++) {
                int iSentimentStrength = igSentimentWordsStrengthTake1[i];
                if (iSentimentStrength < 0) {
                    iSentimentStrength--;
                } else {
                    iSentimentStrength++;
                }

                String sOutput = sgSentimentWords[i] + "\t" + iSentimentStrength + "\n";
                if (c.options.bgForceUTF8) {
                    sOutput = new String(sOutput.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                }
                wWriter.write(sOutput);
            }

            for (int i = 1; i <= igSentimentWordsWithStarAtStartCount; i++) {
                int iSentimentStrength = igSentimentWordsWithStarAtStartStrengthTake1[i];
                if (iSentimentStrength < 0) {
                    iSentimentStrength--;
                } else {
                    iSentimentStrength++;
                }

                String sOutput = "*" + sgSentimentWordsWithStarAtStart[i];
                if (bgSentimentWordsWithStarAtStartHasStarAtEnd[i]) {
                    sOutput = sOutput + "*";
                }

                sOutput = sOutput + "\t" + iSentimentStrength + "\n";
                if (c.options.bgForceUTF8) {
                    sOutput = new String(sOutput.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                }
                wWriter.write(sOutput);
            }

            wWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * printSentimentValuesInSingleRow方法打印出所有情感语的情感强度值.
     *
     * @param wWriter 用于输出的缓冲流
     */
    public void printSentimentValuesInSingleRow(BufferedWriter wWriter) {
        try {
            for (int i = 1; i <= igSentimentWordsCount; i++) {
                int iSentimentStrength = igSentimentWordsStrengthTake1[i];
                wWriter.write("\t" + iSentimentStrength);
            }

            for (int i = 1; i <= igSentimentWordsWithStarAtStartCount; i++) {
                int iSentimentStrength = igSentimentWordsWithStarAtStartStrengthTake1[i];
                wWriter.write("\t" + iSentimentStrength);
            }

            wWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * printSentimentTermsInSingleHeaderRow方法打印出所有情感词，如果情感词带星号则在该词结尾加上*.
     *
     * @param wWriter 用于输出的缓冲流
     * @return true或者false，代表输出成功或是失败
     */
    public boolean printSentimentTermsInSingleHeaderRow(BufferedWriter wWriter) {
        try {
            for (int i = 1; i <= igSentimentWordsCount; i++) {
                wWriter.write("\t" + sgSentimentWords[i]);
            }


            for (int i = 1; i <= igSentimentWordsWithStarAtStartCount; i++) {
                wWriter.write("\t*" + sgSentimentWordsWithStarAtStart[i]);
                if (bgSentimentWordsWithStarAtStartHasStarAtEnd[i]) {
                    wWriter.write("*");
                }

            }

            wWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * getSentiment方法根据参数iWordID寻找相应的情感词.
     *
     * @param iWordID int型变量，用于寻找对应的情感词
     * @return 目标情感词，未找到则返回999
     */
    public int getSentiment(int iWordID) {
        if (iWordID > 0) {
            if (iWordID <= igSentimentWordsCount) {
                return igSentimentWordsStrengthTake1[iWordID];
            } else {
                return igSentimentWordsWithStarAtStartStrengthTake1[iWordID - igSentimentWordsCount];

            }
        } else {
            return NINENINENINE;
        }
    }

    /**
     * setSentiment方法通过iWordID找到对应的情感词强度，并使用iNewSentiment更新其强度值.
     *
     * @param iWordID       int型变量，需要更新的情感词的索引
     * @param iNewSentiment int型变量，需要更新的新强度值
     */
    public void setSentiment(int iWordID, int iNewSentiment) {
        if (iWordID <= igSentimentWordsCount) {
            igSentimentWordsStrengthTake1[iWordID] = iNewSentiment;
        } else {
            igSentimentWordsWithStarAtStartStrengthTake1[iWordID - igSentimentWordsCount] = iNewSentiment;
        }
    }

    /**
     * getSentimentID方法根据sWord字符串找到其对应的索引.
     *
     * @param sWord 字符串型变量，需要寻找索引的情感词
     * @return 情感词对应的索引
     */
    public int getSentimentID(String sWord) {
        int iWordID = Sort.iFindStringPositionInSortedArrayWithWildcardsInArray(sWord.toLowerCase(), sgSentimentWords, 1, igSentimentWordsCount);
        if (iWordID >= 0) {
            return iWordID;
        }

        iWordID = getMatchingStarAtStartRawWordID(sWord);
        if (iWordID >= 0) {
            return iWordID + igSentimentWordsCount;
        } else {
            return -1;
        }

    }

    /**
     * getMatchingStarAtStartRawWordID方法返回带星号情感词在sWord第一次出现时对应的索引值.
     *
     * @param sWord 字符串变量，待处理的情感词
     * @return 对应的索引值
     */
    private int getMatchingStarAtStartRawWordID(String sWord) {
        int iSubStringPos;
        if (igSentimentWordsWithStarAtStartCount > 0) {
            for (int i = 1; i <= igSentimentWordsWithStarAtStartCount; i++) {
                iSubStringPos = sWord.indexOf(sgSentimentWordsWithStarAtStart[i]);
                if (iSubStringPos >= 0 && (bgSentimentWordsWithStarAtStartHasStarAtEnd[i] || iSubStringPos + sgSentimentWordsWithStarAtStart[i].length() == sWord.length())) {
                    return i;
                }

            }

        }
        return -1;
    }

    /**
     * getSentimentWordCount方法返回成员变量igSentimentWordsCount.
     *
     * @return 返回成员变量igSentimentWordsCount
     */
    public int getSentimentWordCount() {
        return igSentimentWordsCount;
    }

    /**
     * initialise方法，对SentimentWords类进行初始化，对不带*的情感词相关成员变量赋值.
     *
     * @param sFilename                        字符串变量，需要处理的文件路径
     * @param options                          传入方法的ClassificationOptions类型对象，使用其bgForceUTF8来判断是否要以UTF8读取sFilename内容
     * @param iExtraBlankArrayEntriesToInclude int型变量，代表多余的空格数量
     * @return true或者false，代表初始化成功或是失败
     */
    public boolean initialise(String sFilename, ClassificationOptions options, int iExtraBlankArrayEntriesToInclude) {
        int iWordStrength;
        int iWordsWithStarAtStart = 0;
        if (Objects.equals(sFilename, "")) {
            System.out.println("No sentiment file specified");
            return false;
        }
        File f = new File(sFilename);
        if (!f.exists()) {
            System.out.println("Could not find sentiment file: " + sFilename);
            return false;
        }
        int iLinesInFile = FileOps.iCountLinesInTextFile(sFilename);
        if (iLinesInFile < 2) {
            System.out.println("Less than 2 lines in sentiment file: " + sFilename);
            return false;
        }
        igSentimentWordsStrengthTake1 = new int[iLinesInFile + 1 + iExtraBlankArrayEntriesToInclude];
        sgSentimentWords = new String[iLinesInFile + 1 + iExtraBlankArrayEntriesToInclude];
        igSentimentWordsCount = 0;
        try {
            BufferedReader rReader;
            if (options.bgForceUTF8) {
                rReader = new BufferedReader(new InputStreamReader(new FileInputStream(sFilename), StandardCharsets.UTF_8));

            } else {
                rReader = new BufferedReader(new FileReader(sFilename));
            }

            String sLine;
            while ((sLine = rReader.readLine()) != null) {
                if (!sLine.equals("")) {
                    if (sLine.indexOf("*") == 0) {
                        iWordsWithStarAtStart++;
                    } else {
                        int iFirstTabLocation = sLine.indexOf("\t");
                        if (iFirstTabLocation >= 0) {
                            int iSecondTabLocation = sLine.indexOf("\t", iFirstTabLocation + 1);
                            try {
                                if (iSecondTabLocation > 0) {
                                    iWordStrength = Integer.parseInt(sLine.substring(iFirstTabLocation + 1, iSecondTabLocation).trim());

                                } else {
                                    iWordStrength = Integer.parseInt(sLine.substring(iFirstTabLocation + 1).trim());

                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Failed to identify integer weight for sentiment word! Ignoring word\nLine: " + sLine);
                                iWordStrength = 0;
                            }
                            sLine = sLine.substring(0, iFirstTabLocation);
                            if (sLine.contains(" ")) {
                                sLine = sLine.trim();
                            }

                            if (!sLine.equals("")) {
                                sgSentimentWords[++igSentimentWordsCount] = sLine;
                                if (iWordStrength > 0) {
                                    iWordStrength--;
                                } else if (iWordStrength < 0) {
                                    iWordStrength++;
                                }

                                igSentimentWordsStrengthTake1[igSentimentWordsCount] = iWordStrength;
                            }
                        }
                    }
                }
            }


            rReader.close();
            Sort.quickSortStringsWithInt(sgSentimentWords, igSentimentWordsStrengthTake1, 1, igSentimentWordsCount);
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find sentiment file: " + sFilename);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println("Found sentiment file but couldn't read from it: " + sFilename);
            e.printStackTrace();
            return false;
        }
        if (iWordsWithStarAtStart > 0) {
            return initialiseWordsWithStarAtStart(sFilename, options, iWordsWithStarAtStart, iExtraBlankArrayEntriesToInclude);

        } else {
            return true;
        }

    }

    /**
     * initialiseWordsWithStarAtStart方法，对SentimentWords类进行初始化，对带*的情感词相关成员变量赋值.
     *
     * @param sFilename                        字符串变量，需要处理的文件路径
     * @param options                          传入方法的ClassificationOptions类型对象，使用其bgForceUTF8来判断是否要以UTF8读取sFilename内容
     * @param iWordsWithStarAtStart            int型变量，带有星号的情感词数量
     * @param iExtraBlankArrayEntriesToInclude int型变量，代表多余的空格数量
     * @return true或者false，代表初始化成功或是失败
     */
    public boolean initialiseWordsWithStarAtStart(String sFilename, ClassificationOptions options, int iWordsWithStarAtStart, int iExtraBlankArrayEntriesToInclude) {
        int iWordStrength;
        File f = new File(sFilename);
        if (!f.exists()) {
            System.out.println("Could not find sentiment file: " + sFilename);
            return false;
        }
        igSentimentWordsWithStarAtStartStrengthTake1 = new int[iWordsWithStarAtStart + 1 + iExtraBlankArrayEntriesToInclude];
        sgSentimentWordsWithStarAtStart = new String[iWordsWithStarAtStart + 1 + iExtraBlankArrayEntriesToInclude];
        bgSentimentWordsWithStarAtStartHasStarAtEnd = new boolean[iWordsWithStarAtStart + 1 + iExtraBlankArrayEntriesToInclude];
        igSentimentWordsWithStarAtStartCount = 0;
        try {
            BufferedReader rReader;
            if (options.bgForceUTF8) {
                rReader = new BufferedReader(new InputStreamReader(new FileInputStream(sFilename), StandardCharsets.UTF_8));

            } else {
                rReader = new BufferedReader(new FileReader(sFilename));

            }
            while (rReader.ready()) {
                String sLine = rReader.readLine();
                if (!Objects.equals(sLine, "") && sLine.indexOf("*") == 0) {
                    int iFirstTabLocation = sLine.indexOf("\t");
                    if (iFirstTabLocation >= 0) {
                        int iSecondTabLocation = sLine.indexOf("\t", iFirstTabLocation + 1);
                        try {
                            if (iSecondTabLocation > 0) {
                                iWordStrength = Integer.parseInt(sLine.substring(iFirstTabLocation + 1, iSecondTabLocation));

                            } else {
                                iWordStrength = Integer.parseInt(sLine.substring(iFirstTabLocation + 1));

                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Failed to identify integer weight for *sentiment* word! Ignoring word\nLine: " + sLine);
                            iWordStrength = 0;
                        }
                        sLine = sLine.substring(1, iFirstTabLocation);
                        if (sLine.indexOf("*") > 0) {
                            sLine = sLine.substring(0, sLine.indexOf("*"));
                            bgSentimentWordsWithStarAtStartHasStarAtEnd[++igSentimentWordsWithStarAtStartCount] = true;
                        } else {
                            bgSentimentWordsWithStarAtStartHasStarAtEnd[++igSentimentWordsWithStarAtStartCount] = false;
                        }
                        if (sLine.contains(" ")) {
                            sLine = sLine.trim();
                        }

                        if (!sLine.equals("")) {
                            sgSentimentWordsWithStarAtStart[igSentimentWordsWithStarAtStartCount] = sLine;
                            if (iWordStrength > 0) {
                                iWordStrength--;
                            } else if (iWordStrength < 0) {
                                iWordStrength++;
                            }

                            igSentimentWordsWithStarAtStartStrengthTake1[igSentimentWordsWithStarAtStartCount] = iWordStrength;
                        } else {
                            igSentimentWordsWithStarAtStartCount--;
                        }
                    }
                }
            }
            rReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find *sentiment file*: " + sFilename);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println("Found *sentiment file* but couldn't read from it: " + sFilename);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * addOrModifySentimentTerm方法新增或修改情感词即它的情感强度值.
     *
     * @param sTerm                             字符串类型，需要处理的情感词
     * @param iTermStrength                     int型变量，情感词的情感强度值
     * @param bSortSentimentListAfterAddingTerm 布尔型变量，判断新增加情感词之后情感词列表是否经过排序
     * @return true或者false，代表新增或修改操作成功或是失败
     */
    public boolean addOrModifySentimentTerm(String sTerm, int iTermStrength, boolean bSortSentimentListAfterAddingTerm) {
        int iTermPosition = getSentimentID(sTerm);
        if (iTermPosition > 0) {
            if (iTermStrength > 0) {
                iTermStrength--;
            } else if (iTermStrength < 0) {
                iTermStrength++;
            }

            igSentimentWordsStrengthTake1[iTermPosition] = iTermStrength;
        } else {
            try {
                sgSentimentWords[++igSentimentWordsCount] = sTerm;
                if (iTermStrength > 0) {
                    iTermStrength--;
                } else if (iTermStrength < 0) {
                    iTermStrength++;
                }

                igSentimentWordsStrengthTake1[igSentimentWordsCount] = iTermStrength;
                if (bSortSentimentListAfterAddingTerm) {
                    Sort.quickSortStringsWithInt(sgSentimentWords, igSentimentWordsStrengthTake1, 1, igSentimentWordsCount);

                }
            } catch (Exception e) {
                System.out.println("Could not add extra sentiment term: " + sTerm);
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * sortSentimentList方法对情感词列表进行排序.
     */
    public void sortSentimentList() {
        Sort.quickSortStringsWithInt(sgSentimentWords, igSentimentWordsStrengthTake1, 1, igSentimentWordsCount);
    }
}

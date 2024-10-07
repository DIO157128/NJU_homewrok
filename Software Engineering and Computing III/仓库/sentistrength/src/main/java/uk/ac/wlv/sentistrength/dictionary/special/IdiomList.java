// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   IdiomList.java

package uk.ac.wlv.sentistrength.dictionary.special;

import java.io.*;
import java.nio.charset.StandardCharsets;


import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.utilities.FileOps;

// Referenced classes of package uk.ac.wlv.sentistrength:
//            ClassificationOptions

/**
 * 相关用例:UC-2
 * IdiomList类创建了一系列与习语词有关的变量和数组，并对其进行初始化等操作.
 */
public class IdiomList {
    private static final int NINENINENINE = 999;
    private static final int NINE = 9;
    private static final int TEN = 10;
    public String[] sgIdioms;
    public int[] igIdiomStrength;
    public int igIdiomCount;
    public String[][] sgIdiomWords;
    public int[] igIdiomWordCount;

    /**
     * IdiomList方法是IdiomList类的构造方法，为成员变量igIdiomCount赋值为0.
     */
    public IdiomList() {
        igIdiomCount = 0;
    }

    /**
     * initialise方法，对IdiomList类进行初始化.
     *
     * @param sFilename                        字符串变量，进行处理的文件路径
     * @param options                          传入方法的ClassificationOptions类型对象，使用其bgForceUTF8来判断是否要以UTF8读取sFilename内容
     * @param iExtraBlankArrayEntriesToInclude int型变量，代表需要保留的多余的空行数量
     * @return true或者false，代表初始化成功或是失败
     */

    public boolean initialise(String sFilename, ClassificationOptions options, int iExtraBlankArrayEntriesToInclude) {
        int iLinesInFile;
        int iIdiomStrength;
        if (sFilename.equals("")) {
            return false;
        }

        File f = new File(sFilename);
        if (!f.exists()) {
            System.out.println("Could not find idiom list file: " + sFilename);
            return false;
        }
        iLinesInFile = FileOps.iCountLinesInTextFile(sFilename);
        sgIdioms = new String[iLinesInFile + 2 + iExtraBlankArrayEntriesToInclude];
        igIdiomStrength = new int[iLinesInFile + 2 + iExtraBlankArrayEntriesToInclude];
        igIdiomCount = 0;
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
                    if (sLine.contains("\t")) {
                        String[] w = sLine.split("\t");
                        try {
                            iIdiomStrength = Integer.parseInt(w[1]);
                            if (iIdiomStrength > 0) {
                                iIdiomStrength--;
                            } else if (iIdiomStrength < 0) {
                                iIdiomStrength++;
                            }

                        } catch (NumberFormatException e) {
                            System.out.println("Failed to identify integer weight for the word! Assuming it is zero");
                            iIdiomStrength = 0;
                        }
                        sLine = w[0].trim();
                        if (!sLine.equals("")) {
                            igIdiomCount++;
                            sgIdioms[igIdiomCount] = sLine;
                            igIdiomStrength[igIdiomCount] = iIdiomStrength;
                        }
                    }

                }
            }

            rReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not find idiom list file: " + sFilename);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println("Found idiom list file but could not read from it: " + sFilename);
            e.printStackTrace();
            return false;
        }
        convertIdiomStringsToWordLists();
        return true;
    }

    //增加新的情感成语，可以作为us-28的具体行为函数

    /**
     * addExtraIdiom方法用于向字符串数组sgIdioms中新加入字符串，即新的习语.
     *
     * @param sIdiom                                          字符串变量，新加入的习语
     * @param iIdiomStrength                                  新加入习语的情感强度值
     * @param bConvertIdiomStringsToWordListsAfterAddingIdiom 判断新加习语之后是否需要转换操作
     */
    public void addExtraIdiom(String sIdiom, int iIdiomStrength, boolean bConvertIdiomStringsToWordListsAfterAddingIdiom) {
        try {
            igIdiomCount++;
            sgIdioms[igIdiomCount] = sIdiom;
            if (iIdiomStrength > 0) {
                iIdiomStrength--;
            } else if (iIdiomStrength < 0) {
                iIdiomStrength++;
            }

            igIdiomStrength[igIdiomCount] = iIdiomStrength;
            if (bConvertIdiomStringsToWordListsAfterAddingIdiom) {
                convertIdiomStringsToWordLists();
            }

        } catch (Exception e) {
            System.out.println("Could not add extra idiom: " + sIdiom);
            e.printStackTrace();
        }
    }

    /**
     * 将习语字符串转化为wordlist.
     */
    public void convertIdiomStringsToWordLists() {
        sgIdiomWords = new String[igIdiomCount + 1][TEN];
        igIdiomWordCount = new int[igIdiomCount + 1];
        for (int iIdiom = 1; iIdiom <= igIdiomCount; iIdiom++) {
            String[] sWordList = sgIdioms[iIdiom].split(" ");
            if (sWordList.length >= NINE) {
                System.out.println("Ignoring idiom! Too many words in it! (>9): " + sgIdioms[iIdiom]);
            } else {
                igIdiomWordCount[iIdiom] = sWordList.length;
                System.arraycopy(sWordList, 0, sgIdiomWords[iIdiom], 0, sWordList.length);
            }
        }

    }

    /**
     * 返回对应语句的情感强度值.
     *
     * @param sPhrase 字符串变量，需要对应的语句
     * @return 找到目标字符串则返回对应的请按强度值，否则返回999
     */
    public int getIdiomStrengthOldNotUseful(String sPhrase) {
        sPhrase = sPhrase.toLowerCase();
        for (int i = 1; i <= igIdiomCount; i++) {
            if (sPhrase.contains(sgIdioms[i])) {
                return igIdiomStrength[i];
            }

        }


        return NINENINENINE;
    }

    /**
     * 返回对应iIdiomID的Idiom字符串.
     *
     * @param iIdiomID 需要的对应Idiom的iIdiomID
     * @return 对应iIdiomID的Idiom字符串
     */
    public String getIdiom(int iIdiomID) {
        if (iIdiomID > 0 && iIdiomID < igIdiomCount) {
            return sgIdioms[iIdiomID];
        } else {
            return "";
        }

    }
}

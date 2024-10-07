// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   Lemmatiser.java

package uk.ac.wlv.sentistrength.dictionary.special;

import java.io.*;
import java.nio.charset.StandardCharsets;


import uk.ac.wlv.utilities.FileOps;
import uk.ac.wlv.utilities.Sort;

/**
 * Lemmatiser类创建了一系列词形还原器有关的变量和数组，并对其进行初始化等操作.
 */
public class Lemmatiser {

    private String[] sgWord;
    private String[] sgLemma;
    private int igWordLast;

    /**
     * Lemmatiser方法是Lemmatiser类的构造方法，为成员变量igWordLast赋值为-1.
     */
    public Lemmatiser() {
        igWordLast = -1;
    }

    /**
     * initialise方法，对Lemmatiser类进行初始化.
     *
     * @param sFileName  字符串变量，将要进行处理的文件路径
     * @param bForceUTF8 使用bgForceUTF8来判断是否要以UTF8读取sFilename内容
     * @return true或者false，代表初始化成功或是失败
     */
    public boolean initialise(String sFileName, boolean bForceUTF8) {
        int iLinesInFile;
        if (sFileName.equals("")) {
            System.out.println("No lemma file specified!");
            return false;
        }
        File f = new File(sFileName);
        if (!f.exists()) {
            System.out.println("Could not find lemma file: " + sFileName);
            return false;
        }
        iLinesInFile = FileOps.iCountLinesInTextFile(sFileName);
        if (iLinesInFile < 2) {
            System.out.println("Less than 2 lines in sentiment file: " + sFileName);
            return false;
        }
        sgWord = new String[iLinesInFile + 1];
        sgLemma = new String[iLinesInFile + 1];
        igWordLast = -1;
        try {
            BufferedReader rReader;
            if (bForceUTF8) {
                rReader = new BufferedReader(new InputStreamReader(new FileInputStream(sFileName), StandardCharsets.UTF_8));

            } else {
                rReader = new BufferedReader(new FileReader(sFileName));
            }

            String sLine;
            while ((sLine = rReader.readLine()) != null) {
                if (!sLine.equals("")) {
                    if (sLine.contains("\t")) {
                        String[] w = sLine.split("\t");
                        sgWord[++igWordLast] = w[0].trim();
                        sgLemma[igWordLast] = w[1].trim();
                    }
                }
            }

            rReader.close();
            Sort.quickSortStringsWithStrings(sgWord, sgLemma, 0, igWordLast);
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find lemma file: " + sFileName);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println("Found lemma file but couldn't read from it: " + sFileName);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * lemmatise方法对传入词语sWord进行词形还原.
     *
     * @param sWord 待处理的字符串
     * @return sWord或在sglemma数组中与sWord序号相同的字符串
     */
    public String lemmatise(String sWord) {
        int iLemmaID = Sort.iFindStringPositionInSortedArray(sWord, sgWord, 0, igWordLast);
        if (iLemmaID >= 0) {
            return sgLemma[iLemmaID];
        } else {
            return sWord;
        }
    }
}

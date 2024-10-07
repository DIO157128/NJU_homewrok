package uk.ac.wlv.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class StringIndex {
    private static final int MILLION = 10000000;
    private static final int TEN = 10;
    private int igTextMax = MILLION;
    public String[] sgText;
    public String[] sgTextComment;
    private int[] igTextLessPtr;
    private int[] igTextMorePtr;
    private int[] igTextCount;
    private int igTextLast = -1;
    private boolean bgIncludeCounts = false;

    /**
     * 初始化函数.
     *
     * @param iVocabMaxIfOverrideDefault 如果需要覆盖默认的词汇表大小，则传入新的词汇表大小
     * @param bIncludeCounts             是否包含词频信息
     * @param bIncludeComments           是否包含注释信息
     */
    public void initialise(int iVocabMaxIfOverrideDefault, boolean bIncludeCounts, boolean bIncludeComments) {
        this.bgIncludeCounts = bIncludeCounts;
        if (iVocabMaxIfOverrideDefault > 0) {
            this.igTextMax = iVocabMaxIfOverrideDefault;
        }

        this.sgText = new String[this.igTextMax];
        this.igTextLessPtr = new int[this.igTextMax];
        this.igTextMorePtr = new int[this.igTextMax];
        this.igTextLast = -1;
        int i;
        if (this.bgIncludeCounts) {
            this.igTextCount = new int[this.igTextMax];

            for (i = 0; i < this.igTextMax; ++i) {
                this.igTextCount[i] = 0;
            }
        }

        if (bIncludeComments) {
            this.sgTextComment = new String[this.igTextMax];

            for (i = 0; i < this.igTextMax; ++i) {
                this.igTextCount[i] = 0;
            }
        }

    }

    /**
     * 从指定文件中加载词汇表的词项指针和计数数据，并将其存储在相应的数组中.
     *
     * @param sVocabTermPtrsCountFileName 词汇表文件的路径和名称
     * @return 如果成功加载了词汇表，则返回 true；否则返回 false。
     */

    public boolean load(String sVocabTermPtrsCountFileName) {
        File f = new File(sVocabTermPtrsCountFileName);
        if (!f.exists()) {
            System.out.println("Could not find the vocab file: " + sVocabTermPtrsCountFileName);
            return false;
        } else {
            try {
                BufferedReader rReader = new BufferedReader(new InputStreamReader(new FileInputStream(sVocabTermPtrsCountFileName), "UTF8"));
                String sLine = rReader.readLine();
                String[] sData = sLine.split("\t");
                this.igTextLast = -1;

                while (rReader.ready()) {
                    sLine = rReader.readLine();
                    if (sLine.length() > 0) {
                        sData = sLine.split("\t");
                        if (sData.length > 2) {
                            if (this.igTextLast == this.igTextMax - 1) {
                                this.increaseArraySizes(this.igTextMax * 2);
                            }

                            this.sgText[++this.igTextLast] = sData[0];
                            this.igTextLessPtr[this.igTextLast] = Integer.parseInt(sData[1]);
                            this.igTextMorePtr[this.igTextLast] = Integer.parseInt(sData[2]);
                            this.igTextCount[this.igTextLast] = Integer.parseInt(sData[2 + 1]);
                        }
                    }
                }

                rReader.close();
                return true;
            } catch (IOException var7) {
                System.out.println("Could not open file for reading or read from file: " + sVocabTermPtrsCountFileName);
                var7.printStackTrace();
                return false;
            }
        }
    }

    /**
     * 返回最后一个字的id.
     *
     * @return 最后一个字的id(初始为 - 1)
     */
    public int getLastWordID() {
        return this.igTextLast;
    }

    /**
     * 保存词汇表到指定文件.
     *
     * @param sVocabTermPtrsCountFileName 保存词汇表的文件名
     * @return 如果成功保存词汇表则返回 true，否则返回 false
     */
    public boolean save(String sVocabTermPtrsCountFileName) {
        if (!FileOps.backupFileAndDeleteOriginal(sVocabTermPtrsCountFileName, TEN)) {
            System.out.println("Could not backup vocab! Perhaps no index exists yet.");
        }

        try {
            BufferedWriter wWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sVocabTermPtrsCountFileName), "UTF8"));
            wWriter.write("Word\tLessPtr\tMorePtr\tAllTopics");

            for (int i = 0; i <= this.igTextLast; ++i) {
                wWriter.write(this.sgText[i] + "\t" + this.igTextLessPtr[i] + "\t" + this.igTextMorePtr[i] + "\t" + this.igTextCount[i] + "\n");
            }

            wWriter.close();
            return true;
        } catch (IOException var4) {
            System.out.println("Could not open file for writing or write to file: " + sVocabTermPtrsCountFileName);
            var4.printStackTrace();
            return false;
        }
    }

    /**
     * 将字符串添加到数组中，并记录该字符串的出现次.
     *
     * @param sText        要添加的字符串。
     * @param bRecordCount 如果为真，则在数组中记录该字符串的出现次数，否则只添加字符串。
     * @return 字符串的索引位置。
     */
    public int addString(String sText, boolean bRecordCount) {
        boolean iPos = true;
        if (this.igTextLast == this.igTextMax - 1) {
            this.increaseArraySizes(this.igTextMax * 2);
        }

        int iPos1;
        if (bRecordCount) {
            iPos1 = Trie.iGetTriePositionForStringAndAddCount(sText, this.sgText, this.igTextCount, this.igTextLessPtr, this.igTextMorePtr, 0, this.igTextLast, false, 1);
        } else {
            iPos1 = Trie.iGetTriePositionForString(sText, this.sgText, this.igTextLessPtr, this.igTextMorePtr, 0, this.igTextLast, false);
        }

        if (iPos1 > this.igTextLast) {
            this.igTextLast = iPos1;
        }

        return iPos1;
    }

    /**
     * 调用iGetTriePositionForString，寻找字符串sText的位置.
     *
     * @param sText
     * @return sText的位置
     */
    public int findString(String sText) {
        return Trie.iGetTriePositionForString(sText, this.sgText, this.igTextLessPtr, this.igTextMorePtr, 0, this.igTextLast, true);
    }

    /**
     * count+1.
     *
     * @param iStringPos
     */
    public void add1ToCount(int iStringPos) {
        int var10002 = this.igTextCount[iStringPos]++;
    }

    /**
     * 增加数组大小.
     *
     * @param iNewArraySize 新数组大小
     */
    private void increaseArraySizes(int iNewArraySize) {
        if (iNewArraySize > this.igTextMax) {
            String[] sgTextTemp = new String[iNewArraySize];
            int[] iTextLessPtrTemp = new int[iNewArraySize];
            int[] iTextMorePtrTemp = new int[iNewArraySize];
            System.arraycopy(this.sgText, 0, sgTextTemp, 0, this.igTextMax);
            System.arraycopy(this.igTextLessPtr, 0, iTextLessPtrTemp, 0, this.igTextMax);
            System.arraycopy(this.igTextMorePtr, 0, iTextMorePtrTemp, 0, this.igTextMax);
            if (this.bgIncludeCounts) {
                int[] iVocabCountTemp = new int[iNewArraySize];
                System.arraycopy(this.igTextCount, 0, iVocabCountTemp, 0, this.igTextMax);

                for (int i = this.igTextMax; i < iNewArraySize; ++i) {
                    this.igTextCount[i] = 0;
                }

                this.igTextCount = iVocabCountTemp;
            }

            this.igTextMax = iNewArraySize;
            this.igTextLessPtr = iTextLessPtrTemp;
            this.igTextMorePtr = iTextMorePtrTemp;
        }
    }

    private static String[] increaseArraySize(String[] sArray, int iCurrentArraySize, int iNewArraySize) {
        if (iNewArraySize <= iCurrentArraySize) {
            return sArray;
        } else {
            String[] sArrayTemp = new String[iNewArraySize];
            System.arraycopy(sArray, 0, sArrayTemp, 0, iCurrentArraySize);
            return sArrayTemp;
        }
    }

    private static int[] increaseArraySize(int[] iArray, int iCurrentArraySize, int iNewArraySize) {
        if (iNewArraySize <= iCurrentArraySize) {
            return iArray;
        } else {
            int[] iArrayTemp = new int[iNewArraySize];
            System.arraycopy(iArray, 0, iArrayTemp, 0, iCurrentArraySize);
            return iArrayTemp;
        }
    }

    /**
     * 得到该位置的string.
     *
     * @param iStringPos
     * @return 索引为iStringPos的string
     */
    public String getString(int iStringPos) {
        return this.sgText[iStringPos];
    }

    /**
     * 得到该位置的comment.
     *
     * @param iStringPos
     * @return 索引为iStringPos的comment
     */
    public String getComment(int iStringPos) {
        return this.sgTextComment[iStringPos] == null ? "" : this.sgTextComment[iStringPos];
    }

    /**
     * 在iStringPos位置增加一条comment.
     *
     * @param iStringPos
     * @param sComment
     */
    public void addComment(int iStringPos, String sComment) {
        this.sgTextComment[iStringPos] = sComment;
    }

    /**
     * 得到该位置的count.
     *
     * @param iStringPos
     * @return 索引为iStringPos的count
     */
    public int getCount(int iStringPos) {
        return this.igTextCount[iStringPos];
    }

    /**
     * 将iStringPos位置下的count置为0.
     *
     * @param iStringPos
     */
    public void setCountToZero(int iStringPos) {
        this.igTextCount[iStringPos] = 0;
    }

    /**
     * 把所有count置为0.
     */
    public void setAllCountsToZero() {
        for (int i = 0; i <= this.igTextLast; ++i) {
            this.igTextCount[i] = 0;
        }

    }
}

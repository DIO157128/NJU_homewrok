package uk.ac.wlv.sentistrength.dictionary.dicList;

import uk.ac.wlv.sentistrength.dictionary.Dictionary;
import uk.ac.wlv.utilities.FileOps;
import uk.ac.wlv.utilities.Sort;

import java.io.*;
import java.nio.charset.StandardCharsets;

public abstract class DicList extends Dictionary {
    private String[] word;
    private int wordCount;
    private int wordMax;

    /**
     * DicList的构造函数.
     */
    public DicList() {
        wordCount = 0;
        wordMax = 0;
    }

    /**
     * 初始化方法，用于初始化list.
     *
     * @param fileName
     * @param forceUTF8
     * @return 是否初始化成功.
     */
    public boolean initialise(String fileName, boolean forceUTF8) {
        if (wordCount > 0) {
            return true;
        }
        wordMax = FileOps.iCountLinesInTextFile(fileName) + 2;
        word = new String[wordMax];
        try {
            BufferedReader reader;
            if (forceUTF8) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
            } else {
                reader = new BufferedReader(new FileReader(fileName));
            }

            String sLine;
            while ((sLine = reader.readLine()) != null) {
                if (!sLine.equals("")) {
                    wordCount++;
                    word[wordCount] = sLine;
                }
            }
            reader.close();
            Sort.quickSortStrings(word, 1, wordCount);
        } catch (FileNotFoundException e) {
            System.out.println("Could not find the file: " + fileName);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println("Found file but could not read from it: " + fileName);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断list中是否包含特定单词.
     *
     * @param w
     * @return 是否包含.
     */
    public boolean contain(String w) {
        return Sort.iFindStringPositionInSortedArray(w, word, 1, wordCount) >= 0;
    }
}

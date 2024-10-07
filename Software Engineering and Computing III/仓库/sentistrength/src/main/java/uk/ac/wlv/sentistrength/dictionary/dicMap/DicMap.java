package uk.ac.wlv.sentistrength.dictionary.dicMap;

import uk.ac.wlv.sentistrength.dictionary.Dictionary;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class DicMap extends Dictionary {
    /**
     * 获取map.
     *
     * @return map成员变量
     */
    public Map<String, Integer> getStrengthMap() {
        return strengthMap;
    }

    private final Map<String, Integer> strengthMap;
    private int wordCount;

    /**
     * DicMap无参构造函数，初始化成员变量.
     */
    public DicMap() {
        strengthMap = new HashMap<>();
        wordCount = 0;
    }

    /**
     * 初始化方法，用于初始化map.
     *
     * @param fileName
     * @param forceUTF8
     * @return 是否初始化成功
     */
    public boolean initialise(String fileName, boolean forceUTF8) {
        if (Objects.equals(fileName, "")) {
            System.out.println("No words file specified");
            return false;
        }
        File f = new File(fileName);
        if (!f.exists()) {
            System.out.println("Could not find words file: " + fileName);
            return false;
        }
        if (wordCount > 0) {
            return true;
        }
        try {
            BufferedReader reader;
            if (forceUTF8) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
            } else {
                reader = new BufferedReader(new FileReader(fileName));
            }
            String sLine;
            int strength;
            while ((sLine = reader.readLine()) != null) {
                if (!sLine.equals("")) {
                    if (sLine.contains("\t")) {
                        String[] w = sLine.split("\t");
                        try {
                            strength = Integer.parseInt(w[1]);
                        } catch (NumberFormatException e) {
                            System.out.println("Failed to identify integer weight for the word! Assuming it is zero");
                            strength = 0;
                        }
                        wordCount++;
                        strengthMap.put(w[0], strength);
                    }

                }
            }
            reader.close();
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
     * 获取特定单词的strength得分.
     *
     * @param w
     * @return 得分
     */
    public int getStrength(String w) {
        return strengthMap.getOrDefault(w, 0);
    }
}

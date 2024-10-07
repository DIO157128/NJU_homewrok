// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   EmoticonsList.java

package uk.ac.wlv.sentistrength.dictionary.dicMap;

// Referenced classes of package uk.ac.wlv.sentistrength:
//            ClassificationOptions


/**
 * 相关用例:UC-7
 * EmoticonsList类创建了一系列与表情符号字符串数组有关的变量，并对其进行初始化、排序等操作.
 */
public class EmoticonsMap extends DicMap {
    private static final int NINENINENINE = 999;

    /**
     * 获取情感得分.
     *
     * @param w
     * @return 情感得分
     */
    public int getStrength(String w) {
        return getStrengthMap().getOrDefault(w, NINENINENINE);
    }
}

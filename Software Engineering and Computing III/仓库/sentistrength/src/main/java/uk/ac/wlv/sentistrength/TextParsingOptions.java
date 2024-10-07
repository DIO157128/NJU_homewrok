// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   TextParsingOptions.java

package uk.ac.wlv.sentistrength;


/**
 * TextParsingOptions类记录用于情感分析的文本转换选项.
 * bgIncludePunctuation: 是否包含标点
 * igNgramSize: n-gram的大小
 * bgUseTranslations: 是否使用翻译
 * bgAddEmphasisCode: 是否加入 emphasis code
 */
public class TextParsingOptions {

    public boolean bgIncludePunctuation;
    public int igNgramSize;
    public boolean bgUseTranslations;
    public boolean bgAddEmphasisCode;

    /**
     * 初始化函数.
     */
    public TextParsingOptions() {
        bgIncludePunctuation = true;
        igNgramSize = 1;
        bgUseTranslations = true;
        bgAddEmphasisCode = false;
    }
}

package uk.ac.wlv.sentistrength.term.term;

import java.util.Objects;

public class Punctuation extends Term {
    private String sgPunctuation = "";
    private String sgPunctuationEmphasis = "";

    /**
     * 若执行该方法，则认为代表该term的内容为标点符号并进行处理.
     *
     * @param content 标点符号
     */
    @Override
    public void code(String content) {
        if (content.length() > 1) {
            this.sgPunctuation = content.substring(0, 1);
            this.sgPunctuationEmphasis = content.substring(1);
        } else {
            this.sgPunctuation = content;
            this.sgPunctuationEmphasis = "";
        }
    }


    /**
     * 判断term中是否含有强调.
     *
     * @return sgPunctuationEmphasis的长度大于一返回true，否则为false
     */
    @Override
    public boolean containsEmphasis() {
        return this.sgPunctuationEmphasis.length() > 1;
    }

    /**
     * 获得代表该term实例对象的内容.
     *
     * @return 返回term的内容
     */
    @Override
    public String getText() {
        return this.sgPunctuation;
    }


    /**
     * 获得未经过处理的源内容.
     *
     * @return 未经过处理的源内容
     */
    @Override
    public String getOriginalText() {
        return this.sgPunctuation + this.sgPunctuationEmphasis;
    }


    /**
     * 根据 sgPunctuationEmphasis 是否为空来返回不同的字符串.
     * 如果 sgPunctuationEmphasis 不为空，则返回带有 equiv 和 em 属性的 p 标签
     *
     * @return 返回相应的字符串
     */
    @Override
    public String getTag() {
        if (!Objects.equals(this.sgPunctuationEmphasis, "")) {
            return "<p equiv=\"" + this.sgPunctuation + "\" em=\"" + this.sgPunctuationEmphasis + "\">" + this.sgPunctuation + this.sgPunctuationEmphasis + "</p>";
        }

        return "<p>" + this.sgPunctuation + "</p>";
    }


    /**
     * 获得标点符号中的强调的长度.
     *
     * @return 标点符号中的强调的长度
     */
    @Override
    public int getEmphasisLength() {
        return this.sgPunctuationEmphasis.length();
    }

    /**
     * 获得标点符号中的强调的长度.
     *
     * @return 标点符号中的强调的长度
     */
    public int getPunctuationEmphasisLength() {
        return this.sgPunctuationEmphasis.length();
    }

    /**
     * 获得翻译处理过后的标点符号.
     *
     * @return 翻译处理过后的标点符号
     */
    public String getTranslatedPunctuation() {
        return this.sgPunctuation;
    }

    /**
     * 判断是否含有指定的标点符号.
     *
     * @param sPunctuation 指定的标点符号
     * @return 如果term的类型为标点符号并且搜索到了指定的标点，返回true，否则返回false
     */
    public boolean punctuationContains(String sPunctuation) {
        if (this.sgPunctuation.contains(sPunctuation)) {
            return true;
        } else {
            return !Objects.equals(this.sgPunctuationEmphasis, "") && this.sgPunctuationEmphasis.contains(sPunctuation);
        }
    }


    /**
     * 获得翻译处理过后的标点符号.
     *
     * @return 翻译处理过后的标点符号
     */
    @Override
    public String getTranslation() {
        return this.sgPunctuation;
    }
}

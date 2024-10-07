package uk.ac.wlv.sentistrength.term.term;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.ClassificationResources;
import uk.ac.wlv.sentistrength.term.termFunction.*;

/**
 * term的抽象类，所有的term都继承此类.
 */
public abstract class Term implements CodeProcess, TermEmphasis, TermSentimentFunc, GetTermVar {
    public static final int NOTPROCESSED = 999;
    protected static final int NOUN = 0;
    protected int igWordSentimentID = 0;
    protected boolean bgOverrideSentimentScore = false;
    protected boolean bgWordSentimentIDCalculated = false;
    protected ClassificationResources resources;
    protected ClassificationOptions options;
    protected int igOverrideSentimentScore = 0;


    /**
     * 处理内容，将内容转换为相应的term类.
     *
     * @param content 内容
     */
    public abstract void code(String content);

    /**
     * term是否含有强调.
     *
     * @return true 含有, false 不含有
     */
    public abstract boolean containsEmphasis();

    /**
     * 获得代表该term实例对象的内容.
     *
     * @return 返回term的内容
     */
    public abstract String getText();


    /**
     * 获得强调长度.
     *
     * @return 强调的长度
     */
    public int getEmphasisLength() {
        return NOUN;
    }


    /**
     * 获得未经过处理的源内容.
     *
     * @return 未经过处理的源内容
     */
    public abstract String getOriginalText();

    /**
     * 获得实例对象term的SentimentID,如果还没有calculated过，则从数据源中找到sentimentID并赋予this.igWordSentimentID并设置为已经calculated过.
     *
     * @return SentimentID
     */
    public int getSentimentID() {
        return -1;
    }

    /**
     * 设置OverrideSentimentScore的值，并令this.bgOverrideSentimentScore = true.
     *
     * @param iSentiment OverrideSentimentScore需要被设置的值
     */
    public void setSentimentOverrideValue(int iSentiment) {
        this.bgOverrideSentimentScore = true;
        this.igOverrideSentimentScore = iSentiment;
    }


    /**
     * 获得SentimentValue的值.
     *
     * @return 如果OverrideSentimentScore被设置过了，则返回OverrideSentimentScore的值，否则从数据源中获得value
     */
    public int getSentimentValue() {
        if (this.bgOverrideSentimentScore) {
            return this.igOverrideSentimentScore;
        } else {
            return this.getSentimentID() < 1 ? 0 : this.resources.sentimentWords.getSentiment(this.igWordSentimentID);
        }
    }

    /**
     * 设置ClassificationResources.
     *
     * @param classificationResources
     */
    public void setClassificationResources(ClassificationResources classificationResources) {
        this.resources = classificationResources;
    }

    /**
     * 设置options.
     *
     * @param option
     */
    public void setOptions(ClassificationOptions option) {
        this.options = option;
    }

    /**
     * 返回options.
     *
     * @return options
     */
    public ClassificationOptions getOptions() {
        return this.options;
    }
}

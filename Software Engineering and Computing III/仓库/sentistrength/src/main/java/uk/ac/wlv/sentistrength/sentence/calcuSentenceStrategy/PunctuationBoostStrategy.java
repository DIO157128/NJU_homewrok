package uk.ac.wlv.sentistrength.sentence.calcuSentenceStrategy;

import uk.ac.wlv.sentistrength.ClassificationOptions;


/**
 * 计算标点符号对句子情感分数产生的影响的计算策略类.
 */
public class PunctuationBoostStrategy extends CalcuSentenceStrategy {
    private boolean bSentencePunctuationBoost;
    private ClassificationOptions options;
    private int igPositiveSentiment;
    private int igNegativeSentiment;


    /**
     * 设置情感分析的参数.
     *
     * @param bSentencePunctuationBoost 是否使用句子标点符号提升情感值
     * @param options                   分类选项对象
     * @param igPositiveSentiment       正面情感的值
     * @param igNegativeSentiment       负面情感的值
     */
    public void setStrategy(boolean bSentencePunctuationBoost, ClassificationOptions options, int igPositiveSentiment, int igNegativeSentiment) {
        this.bSentencePunctuationBoost = bSentencePunctuationBoost;
        this.options = options;
        this.igPositiveSentiment = igPositiveSentiment;
        this.igNegativeSentiment = igNegativeSentiment;
    }


    /**
     * 计算情感分数.
     */
    @Override
    void calculateSentiment() {
        if (bSentencePunctuationBoost) {
            if (this.igPositiveSentiment < -this.igNegativeSentiment) {
                --this.igNegativeSentiment;
                this.sgClassificationRationale.append("[-1 punctuation emphasis] ");

            } else if (this.igPositiveSentiment > -this.igNegativeSentiment) {
                ++this.igPositiveSentiment;
                this.sgClassificationRationale.append("[+1 punctuation emphasis] ");

            } else if (this.options.igMoodToInterpretNeutralEmphasis > 0) {
                ++this.igPositiveSentiment;
                this.sgClassificationRationale.append("[+1 punctuation mood emphasis] ");
            } else if (this.options.igMoodToInterpretNeutralEmphasis < 0) {
                --this.igNegativeSentiment;
                this.sgClassificationRationale.append("[-1 punctuation mood emphasis] ");
            }
        }
    }


    /**
     * 获得积极情感分数.
     *
     * @return 积极情感分数
     */
    public int getIgPositiveSentiment() {
        return igPositiveSentiment;
    }

    /**
     * 获得消极情感分数.
     *
     * @return 消极情感分数
     */
    public int getIgNegativeSentiment() {
        return igNegativeSentiment;
    }
}

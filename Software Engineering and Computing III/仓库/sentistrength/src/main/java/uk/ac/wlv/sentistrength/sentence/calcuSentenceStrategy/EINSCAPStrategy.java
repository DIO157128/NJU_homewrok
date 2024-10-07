package uk.ac.wlv.sentistrength.sentence.calcuSentenceStrategy;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.term.term.Punctuation;
import uk.ac.wlv.sentistrength.term.term.Term;

/**
 * 中性的句子的感叹号积极情绪加2的计算策略类.
 */
public class EINSCAPStrategy extends CalcuSentenceStrategy {
    ClassificationOptions options;
    int igPositiveSentiment;
    int igNegativeSentiment;
    Term[] term;
    int igTermCount;

    /**
     * 设置情感分析的参数.
     *
     * @param options             分类选项对象
     * @param igPositiveSentiment 正面情感的值
     * @param igNegativeSentiment 负面情感的值
     * @param term                术语数组对象
     * @param igTermCount         术语的个数
     */
    public void setStrategy(ClassificationOptions options, int igPositiveSentiment, int igNegativeSentiment, Term[] term, int igTermCount) {
        this.options = options;
        this.igPositiveSentiment = igPositiveSentiment;
        this.igNegativeSentiment = igNegativeSentiment;
        this.term = term;
        this.igTermCount = igTermCount;
    }

    /**
     * 计算情感分数.
     */
    @Override
    void calculateSentiment() {
        if (this.igPositiveSentiment == 1 && this.igNegativeSentiment == -1 && this.options.bgExclamationInNeutralSentenceCountsAsPlus2) {
            for (int iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                if (this.term[iTerm] instanceof Punctuation && ((Punctuation) this.term[iTerm]).punctuationContains("!")) {
                    this.igPositiveSentiment = 2;
                    this.sgClassificationRationale.append("[pos = 2 for !]");
                    break;
                }
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
}

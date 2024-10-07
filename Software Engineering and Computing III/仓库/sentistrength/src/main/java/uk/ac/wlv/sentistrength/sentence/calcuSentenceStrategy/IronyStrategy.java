package uk.ac.wlv.sentistrength.sentence.calcuSentenceStrategy;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.ClassificationResources;
import uk.ac.wlv.sentistrength.term.term.Punctuation;
import uk.ac.wlv.sentistrength.term.term.Term;


/**
 * 计算反讽带来的情感分数影响的计算策略类.
 */
public class IronyStrategy extends CalcuSentenceStrategy {


    ClassificationOptions options;
    ClassificationResources resources;
    Term[] term;
    int igTermCount;
    //需要输出的字段
    int igNegativeSentiment;
    int igPositiveSentiment;
    private static final int THIRTYFOUR = 34;


    /**
     * 设置情感分析的参数.
     *
     * @param options             分类选项对象
     * @param resources           分类资源对象
     * @param term                术语数组对象
     * @param igTermCount         术语的个数
     * @param igNegativeSentiment 负面情感的值
     * @param igPositiveSentiment 正面情感的值
     */
    public void setStrategy(ClassificationOptions options, ClassificationResources resources, Term[] term, int igTermCount, int igNegativeSentiment, int igPositiveSentiment) {
        this.options = options;
        this.resources = resources;
        this.term = term;
        this.igTermCount = igTermCount;
        this.igNegativeSentiment = igNegativeSentiment;
        this.igPositiveSentiment = igPositiveSentiment;
    }

    /**
     * 计算情感分数.
     */
    @Override
    void calculateSentiment() {
        int iTerm;
        if (this.igPositiveSentiment >= this.options.igMinSentencePosForQuotesIrony) {
            for (iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                if (this.term[iTerm] instanceof Punctuation && this.term[iTerm].getText().indexOf(THIRTYFOUR) >= 0) {
                    if (this.igNegativeSentiment > -this.igPositiveSentiment) {
                        this.igNegativeSentiment = 1 - this.igPositiveSentiment;
                    }

                    this.igPositiveSentiment = 1;
                    this.sgClassificationRationale.append("[Irony change: pos = 1, neg = ").append(this.igNegativeSentiment).append("]");
                    return;
                }
            }
        }

        if (this.igPositiveSentiment >= this.options.igMinSentencePosForPunctuationIrony) {
            for (iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                if (this.term[iTerm] instanceof Punctuation && ((Punctuation) this.term[iTerm]).punctuationContains("!") && ((Punctuation) this.term[iTerm]).getPunctuationEmphasisLength() > 0) {
                    if (this.igNegativeSentiment > -this.igPositiveSentiment) {
                        this.igNegativeSentiment = 1 - this.igPositiveSentiment;
                    }

                    this.igPositiveSentiment = 1;
                    this.sgClassificationRationale.append("[Irony change: pos = 1, neg = ").append(this.igNegativeSentiment).append("]");
                    return;
                }
            }
        }

        if (this.igPositiveSentiment >= this.options.igMinSentencePosForTermsIrony) {
            for (iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                if (this.resources.ironyList.contain(this.term[iTerm].getText())) {
                    if (this.igNegativeSentiment > -this.igPositiveSentiment) {
                        this.igNegativeSentiment = 1 - this.igPositiveSentiment;
                    }

                    this.igPositiveSentiment = 1;
                    this.sgClassificationRationale.append("[Irony change: pos = 1, neg = ").append(this.igNegativeSentiment).append("]");
                    return;
                }
            }
        }
    }

    /**
     * 获得消极情感分数.
     *
     * @return 消极情感分数
     */
    public int getIgNegativeSentiment() {
        return igNegativeSentiment;
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

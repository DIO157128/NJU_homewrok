package uk.ac.wlv.sentistrength.sentence.calcuSentenceStrategy;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.term.term.Term;
import uk.ac.wlv.sentistrength.term.term.Word;

/**
 * 计算单词miss对句子的情感分数的影响的计算策略类.
 */
public class MCAP2Strategy extends CalcuSentenceStrategy {
    private ClassificationOptions options;
    private int igTermCount;
    private Term[] term;
    //需要输出的
    private int igPositiveSentiment;

    /**
     * 设置情感分析的参数.
     *
     * @param options             分类选项对象
     * @param igTermCount         术语的个数
     * @param term                术语数组对象
     * @param igPositiveSentiment 正面情感的值
     */
    public void setStrategy(ClassificationOptions options, int igTermCount, Term[] term, int igPositiveSentiment) {
        this.options = options;
        this.igTermCount = igTermCount;
        this.term = term;
        this.igPositiveSentiment = igPositiveSentiment;
    }

    /**
     * 计算情感分数.
     */
    @Override
    void calculateSentiment() {
        if (this.igPositiveSentiment == 1 && this.options.bgMissCountsAsPlus2) {
            for (int iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                if (this.term[iTerm] instanceof Word && ((Word) this.term[iTerm]).getTranslatedWord().toLowerCase().compareTo("miss") == 0) {
                    this.igPositiveSentiment = 2;
                    this.sgClassificationRationale.append("[pos = 2 for term 'miss']");
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
    public int getPositiveSentiment() {
        return igPositiveSentiment;
    }
}

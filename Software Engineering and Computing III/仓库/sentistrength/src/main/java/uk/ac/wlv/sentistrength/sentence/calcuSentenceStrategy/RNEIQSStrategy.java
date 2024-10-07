package uk.ac.wlv.sentistrength.sentence.calcuSentenceStrategy;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.ClassificationResources;
import uk.ac.wlv.sentistrength.term.term.Punctuation;
import uk.ac.wlv.sentistrength.term.term.Term;
import uk.ac.wlv.sentistrength.term.term.Word;

/**
 * 消弱在问句中的单词的消极情绪的计算策略类.
 */
public class RNEIQSStrategy extends CalcuSentenceStrategy {
    private ClassificationOptions options;
    private ClassificationResources resources;
    private Term[] term;
    private int igTermCount;
    //需要输出的字段
    private int igNegativeSentiment;


    /**
     * 设置情感分析的参数.
     *
     * @param options             分类选项对象
     * @param resources           分类资源对象
     * @param term                术语数组对象
     * @param igTermCount         术语的个数
     * @param igNegativeSentiment 负面情感的值
     */
    public void setStrategy(ClassificationOptions options, ClassificationResources resources, Term[] term, int igTermCount, int igNegativeSentiment) {
        this.options = options;
        this.resources = resources;
        this.term = term;
        this.igTermCount = igTermCount;
        this.igNegativeSentiment = igNegativeSentiment;
    }

    /**
     * 计算情感分数.
     */
    @Override
    void calculateSentiment() {
        if (this.options.bgReduceNegativeEmotionInQuestionSentences && this.igNegativeSentiment < -1) {
            for (int iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                if (this.term[iTerm] instanceof Word) {
                    if (this.resources.questionWords.contain(((Word) this.term[iTerm]).getTranslatedWord().toLowerCase())) {
                        ++this.igNegativeSentiment;
                        this.sgClassificationRationale.append("[+1 negative for question word]");
                        break;
                    }
                } else if (this.term[iTerm] instanceof Punctuation && ((Punctuation) this.term[iTerm]).punctuationContains("?")) {
                    ++this.igNegativeSentiment;
                    this.sgClassificationRationale.append("[+1 negative for question mark ?]");
                    break;
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
}

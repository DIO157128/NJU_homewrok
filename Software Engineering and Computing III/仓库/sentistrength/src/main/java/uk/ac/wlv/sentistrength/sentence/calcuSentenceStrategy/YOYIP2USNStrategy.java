package uk.ac.wlv.sentistrength.sentence.calcuSentenceStrategy;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.ClassificationResources;
import uk.ac.wlv.sentistrength.term.term.Term;
import uk.ac.wlv.sentistrength.term.term.Word;

/**
 * 计算由单词you和your和what's所带来的积极分数影响的计算策略类.
 */
public class YOYIP2USNStrategy extends CalcuSentenceStrategy {

    ClassificationOptions options;
    ClassificationResources resources;
    Term[] term;
    int igTermCount;
    //需要输出的字段
    int igNegativeSentiment;
    int igPositiveSentiment;


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
        if (this.igPositiveSentiment == 1 && this.igNegativeSentiment == -1 && this.options.bgYouOrYourIsPlus2UnlessSentenceNegative) {
            for (int iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                if (this.term[iTerm] instanceof Word) {
                    String sTranslatedWord = this.term[iTerm].getTranslation().toLowerCase();
                    if (sTranslatedWord.compareTo("you") == 0 || sTranslatedWord.compareTo("your") == 0 || sTranslatedWord.compareTo("whats") == 0) {
                        this.igPositiveSentiment = 2;
                        this.sgClassificationRationale.append("[pos = 2 for you/your/whats]");
                        break;
                    }
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

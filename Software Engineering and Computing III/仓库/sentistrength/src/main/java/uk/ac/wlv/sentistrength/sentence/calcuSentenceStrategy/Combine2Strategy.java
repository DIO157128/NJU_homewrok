package uk.ac.wlv.sentistrength.sentence.calcuSentenceStrategy;

import uk.ac.wlv.sentistrength.ClassificationOptions;

/**
 * 通过不同的EmotionSentenceCombineMethod决定情感分数的计算策略类.
 */
public class Combine2Strategy extends CalcuSentenceStrategy {
    private int igPositiveSentiment;
    private int igNegativeSentiment;
    private ClassificationOptions options;
    private static final int POSFIVE = 5;
    private static final int NEGFIVE = -5;

    /**
     * 设置参数.
     *
     * @param igPositiveSentiment 消极情感值
     * @param igNegativeSentiment 积极情感值
     * @param options             选项
     */
    public void setStrategy(int igPositiveSentiment, int igNegativeSentiment, ClassificationOptions options) {
        this.igNegativeSentiment = igNegativeSentiment;
        this.igPositiveSentiment = igPositiveSentiment;
        this.options = options;
    }

    /**
     * 计算情绪分数.
     */
    @Override
    void calculateSentiment() {
        if (this.options.igEmotionSentenceCombineMethod != 2) {
            if (this.igPositiveSentiment > POSFIVE) {
                this.igPositiveSentiment = POSFIVE;
            }

            if (this.igNegativeSentiment < NEGFIVE) {
                this.igNegativeSentiment = NEGFIVE;
            }
        }
    }

    /**
     * 获取正面情感的值.
     *
     * @return 正面情感的值，一个整数
     */
    public int getIgPositiveSentiment() {
        return igPositiveSentiment;
    }

    /**
     * 获取负面情感的值.
     *
     * @return 负面情感的值，一个整数
     */
    public int getIgNegativeSentiment() {
        return igNegativeSentiment;
    }
}

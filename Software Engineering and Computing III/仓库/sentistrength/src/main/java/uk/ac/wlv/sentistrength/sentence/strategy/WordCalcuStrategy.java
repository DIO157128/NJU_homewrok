package uk.ac.wlv.sentistrength.sentence.strategy;

import uk.ac.wlv.sentistrength.term.term.Word;

/**
 * 用于在sentence中计算word的分数的策略.
 */
public abstract class WordCalcuStrategy {
    protected StringBuilder sgClassificationRationale;


    /**
     * 构造类.
     */
    public WordCalcuStrategy() {
        sgClassificationRationale = new StringBuilder();
    }

    /**
     * 计算word的情感分数.
     *
     * @param word  单词
     * @param iTerm 当前位置
     * @return 情感分数
     */
    abstract float calculateSentiment(Word word, int iTerm);

    /**
     * 获得解释.
     *
     * @return 解释
     */
    public String getClassificationRationale() {
        return sgClassificationRationale.toString();
    }
}

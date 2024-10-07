package uk.ac.wlv.sentistrength.sentence.calcuSentenceStrategy;

/**
 * 根据获得的单词情绪分数表计算句子的情绪分数策略类的抽象类.
 */
public abstract class CalcuSentenceStrategy {
    protected StringBuilder sgClassificationRationale;

    /**
     * 构造函数.
     */
    public CalcuSentenceStrategy() {
        sgClassificationRationale = new StringBuilder();
    }

    /**
     * 计算情绪分数.
     */
    abstract void calculateSentiment();


    /**
     * 获得解释.
     *
     * @return 解释
     */
    public String getClassificationRationale() {
        return sgClassificationRationale.toString();
    }
}

package uk.ac.wlv.sentistrength.sentence.calcuSentenceStrategy;

/**
 * 计算最大和总的情绪分数的计算策略类.
 */
public class CalcuTotalAndMaxStrategy extends CalcuSentenceStrategy {
    private float fTotalNeg;
    private float fTotalPos;
    private float fMaxNeg;
    private float fMaxPos;
    private int iPosWords;
    private int iNegWords;
    private int igSentiCount;
    //需要被设置的
    private float[] fSentiment;
    private int iWordTotal;


    /**
     * 设置参数.
     *
     * @param sentiment 单词的情绪分数表
     * @param wordTotal 总的单词数
     */
    public void setStrategy(float[] sentiment, int wordTotal) {
        this.fSentiment = sentiment;
        this.iWordTotal = wordTotal;
    }

    /**
     * 计算句子的情绪分数.
     */
    @Override
    void calculateSentiment() {
        int iTerm;
        for (iTerm = 1; iTerm <= iWordTotal; ++iTerm) {
            if (fSentiment[iTerm] < 0.0F) {
                fTotalNeg += fSentiment[iTerm];
                ++iNegWords;
                if (fMaxNeg > fSentiment[iTerm]) {
                    fMaxNeg = fSentiment[iTerm];
                }
            } else if (fSentiment[iTerm] > 0.0F) {
                fTotalPos += fSentiment[iTerm];
                ++iPosWords;
                if (fMaxPos < fSentiment[iTerm]) {
                    fMaxPos = fSentiment[iTerm];
                }
            }
        }
        igSentiCount = iNegWords + iPosWords;
        --fMaxNeg;
        ++fMaxPos;
    }


    /**
     * 获得TotalNeg.
     *
     * @return TotalNeg
     */
    public float getTotalNeg() {
        return fTotalNeg;
    }


    /**
     * 获取总的正面情感值.
     *
     * @return 总的正面情感值，一个浮点数
     */
    public float getTotalPos() {
        return fTotalPos;
    }

    /**
     * 获取最大的负面情感值.
     *
     * @return 最大的负面情感值，一个浮点数
     */
    public float getMaxNeg() {
        return fMaxNeg;
    }

    /**
     * 获取最大的正面情感值.
     *
     * @return 最大的正面情感值，一个浮点数
     */
    public float getMaxPos() {
        return fMaxPos;
    }

    /**
     * 获取正面情感词的个数.
     *
     * @return 正面情感词的个数，一个整数
     */
    public int getPosWords() {
        return iPosWords;
    }

    /**
     * 获取负面情感词的个数.
     *
     * @return 负面情感词的个数，一个整数
     */
    public int getNegWords() {
        return iNegWords;
    }

    /**
     * 获取情感计数的值.
     *
     * @return 情感计数的值，一个整数
     */
    public int igSentiCount() {
        return igSentiCount;
    }
}

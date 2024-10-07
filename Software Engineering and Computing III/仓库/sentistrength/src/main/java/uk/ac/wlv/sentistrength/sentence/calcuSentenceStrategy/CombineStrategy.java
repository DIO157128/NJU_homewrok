package uk.ac.wlv.sentistrength.sentence.calcuSentenceStrategy;

import uk.ac.wlv.sentistrength.ClassificationOptions;

/**
 * 通过不同的EmotionSentenceCombineMethod决定情感分数的计算策略类.
 */
public class CombineStrategy extends CalcuSentenceStrategy {
    private int igPositiveSentiment;
    private int igNegativeSentiment;
    private ClassificationOptions options;
    private int iPosWords;
    private int iNegWords;
    private float fTotalNeg;
    private float fTotalPos;
    private float fMaxPos;
    private float fMaxNeg;
    private static final double D1 = 0.45D;
    private static final double D2 = 0.55D;
    /**
     * 设置情感分析的策略和参数.
     *
     * @param options   分类选项对象
     * @param iPosWords 正面情感词的个数
     * @param iNegWords 负面情感词的个数
     * @param fTotalNeg 总的负面情感值
     * @param fTotalPos 总的正面情感值
     * @param fMaxPos   最大的正面情感值
     * @param fMaxNeg   最大的负面情感值
     */
    public void setStrategy(ClassificationOptions options, int iPosWords, int iNegWords, float fTotalNeg, float fTotalPos, float fMaxPos, float fMaxNeg) {
        this.options = options;
        this.iPosWords = iPosWords;
        this.iNegWords = iNegWords;
        this.fTotalPos = fTotalPos;
        this.fTotalNeg = fTotalNeg;
        this.fMaxPos = fMaxPos;
        this.fMaxNeg = fMaxNeg;
    }


    /**
     * 计算情感分数.
     */
    @Override
    void calculateSentiment() {
        int var10000 = this.options.igEmotionSentenceCombineMethod;
        if (var10000 == 1) {
            if (iPosWords == 0) {
                this.igPositiveSentiment = 1;
            } else {
                this.igPositiveSentiment = (int) Math.round(((double) (fTotalPos + (float) iPosWords) + D1) / (double) iPosWords);
            }

            if (iNegWords == 0) {
                this.igNegativeSentiment = -1;
            } else {
                this.igNegativeSentiment = (int) Math.round(((double) (fTotalNeg - (float) iNegWords) + D2) / (double) iNegWords);
            }
        } else {
            if (var10000 == 2) {
                this.igPositiveSentiment = Math.round(fTotalPos) + iPosWords;
                this.igNegativeSentiment = Math.round(fTotalNeg) - iNegWords;
            } else {
                this.igPositiveSentiment = Math.round(fMaxPos);
                this.igNegativeSentiment = Math.round(fMaxNeg);
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

    /**
     * 获得消极情感分数.
     *
     * @return 消极情感分数
     */
    public int getNegativeSentiment() {
        return igNegativeSentiment;
    }


}

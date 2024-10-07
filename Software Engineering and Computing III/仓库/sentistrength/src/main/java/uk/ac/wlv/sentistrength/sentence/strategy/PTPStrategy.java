package uk.ac.wlv.sentistrength.sentence.strategy;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.term.term.Word;


/**
 * MultipleNegativeWordsToIncreaseNegativeEmotion计算策略类.
 */
public class PTPStrategy extends WordCalcuStrategy {
    private ClassificationOptions options;
    private int iWordTotal;
    private float currentSentiment;
    private float lastSentiment;


    /**
     * 填充参数.
     *
     * @param options          选项
     * @param currentSentiment 当前情感分数
     * @param iWordTotal       总共的word数
     * @param lastSentiment    上一个情感分数
     */
    public void setStrategy(ClassificationOptions options, float currentSentiment, int iWordTotal, float lastSentiment) {
        this.options = options;
        this.currentSentiment = currentSentiment;
        this.iWordTotal = iWordTotal;
        this.lastSentiment = lastSentiment;
    }

    /**
     * 计算情感分数.
     *
     * @param word  单词
     * @param iTerm 当前位置
     * @return 情感分数
     */
    @Override
    float calculateSentiment(Word word, int iTerm) {
        if (this.options.bgAllowMultipleNegativeWordsToIncreaseNegativeEmotion && currentSentiment < -1.0F && iWordTotal > 1 && lastSentiment < -1.0F) {
            currentSentiment--;
            if (this.options.bgExplainClassification) {
                this.sgClassificationRationale.append("[-1 consecutive negative words] ");
            }
        }
        return currentSentiment;
    }
}

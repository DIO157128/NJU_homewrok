package uk.ac.wlv.sentistrength.sentence.strategy;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.term.term.Word;


/**
 * 计算助词带来的情绪影响的类.
 */
public class BoostChangeEmotionStrategy extends WordCalcuStrategy {
    private ClassificationOptions options;
    private int iLastBoosterWordScore;
    private float currentSentiment;

    /**
     * 设置参数.
     *
     * @param options               选项
     * @param currentSentiment      当前的情绪分数
     * @param iLastBoosterWordScore 上一个助词分数
     */
    public void setStrategy(ClassificationOptions options, float currentSentiment, int iLastBoosterWordScore) {
        this.currentSentiment = currentSentiment;
        this.options = options;
        this.iLastBoosterWordScore = iLastBoosterWordScore;
    }


    /**
     * 计算情绪分数.
     *
     * @param word  单词
     * @param iTerm 当前位置
     * @return 情绪分数
     */
    @Override
    float calculateSentiment(Word word, int iTerm) {
        if (this.options.bgBoosterWordsChangeEmotion) {
            if (iLastBoosterWordScore != 0) {
                if (currentSentiment > 0.0F) {
                    currentSentiment += (float) iLastBoosterWordScore;
                    this.sgClassificationRationale.append("[+").append(iLastBoosterWordScore).append(" booster word] ");

                } else if (currentSentiment < 0.0F) {
                    currentSentiment -= (float) iLastBoosterWordScore;
                    if (this.options.bgExplainClassification) {
                        this.sgClassificationRationale.append("[-").append(iLastBoosterWordScore).append(" booster word] ");
                    }
                }
            }

            iLastBoosterWordScore = word.getBoosterWordScore();
        }
        return currentSentiment;
    }


    /**
     * 获得上一个助词分数.
     *
     * @return 上一个助词分数
     */
    public int getLastBoosterWordScore() {
        return iLastBoosterWordScore;
    }


}

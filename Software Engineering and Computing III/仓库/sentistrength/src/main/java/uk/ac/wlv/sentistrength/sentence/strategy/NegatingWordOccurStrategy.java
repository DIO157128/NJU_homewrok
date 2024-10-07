package uk.ac.wlv.sentistrength.sentence.strategy;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.sentence.strategy.WordCalcuStrategy;
import uk.ac.wlv.sentistrength.term.term.Word;

public class NegatingWordOccurStrategy extends WordCalcuStrategy {
    private int iWordsSinceNegative;
    private int iLastBoosterWordScore;
    private ClassificationOptions options;
    private float currentSentiment;

    /**
     * 设置参数.
     *
     * @param options               选项
     * @param currentSentiment      当前情感分数
     * @param iWordsSinceNegative   在遇到消极单词过后的单词计数
     * @param iLastBoosterWordScore 上一个助词分数
     */
    public void setStrategy(ClassificationOptions options, float currentSentiment, int iWordsSinceNegative, int iLastBoosterWordScore) {
        this.currentSentiment = currentSentiment;
        this.options = options;
        this.iLastBoosterWordScore = iLastBoosterWordScore;
        this.iWordsSinceNegative = iWordsSinceNegative;
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
        if (this.options.bgNegatingWordsOccurBeforeSentiment) {
            if (this.options.bgNegatingWordsFlipEmotion) {
                if (iWordsSinceNegative <= this.options.igMaxWordsBeforeSentimentToNegate) {
                    currentSentiment = -currentSentiment * this.options.fgStrengthMultiplierForNegatedWords;
                    this.sgClassificationRationale.append("[*-").append(this.options.fgStrengthMultiplierForNegatedWords).append(" approx. negated multiplier] ");

                }
            } else {
                if (this.options.bgNegatingNegativeNeutralisesEmotion && currentSentiment < 0.0F && iWordsSinceNegative <= this.options.igMaxWordsBeforeSentimentToNegate) {
                    currentSentiment = 0.0F;
                    this.sgClassificationRationale.append("[=0 negation] ");

                }

                if (this.options.bgNegatingPositiveFlipsEmotion && currentSentiment > 0.0F && iWordsSinceNegative <= this.options.igMaxWordsBeforeSentimentToNegate) {
                    currentSentiment = -currentSentiment * this.options.fgStrengthMultiplierForNegatedWords;
                    this.sgClassificationRationale.append("[*-").append(this.options.fgStrengthMultiplierForNegatedWords).append(" approx. negated multiplier] ");

                }
            }
        }

        if (word.isNegatingWord()) {
            iWordsSinceNegative = -1;
        }

        if (iLastBoosterWordScore == 0) {
            ++iWordsSinceNegative;
        }
        return currentSentiment;
    }

    /**
     * 获得在遇到消极单词过后的单词计数.
     *
     * @return 在遇到消极单词过后的单词计数
     */
    public int getWordsSinceNegative() {
        return iWordsSinceNegative;
    }
}

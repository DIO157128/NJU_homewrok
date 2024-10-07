package uk.ac.wlv.sentistrength.sentence.strategy;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.term.term.Word;


public class NWordsOccurAfterStrategy extends WordCalcuStrategy {
    private ClassificationOptions options;
    private int iWordTotal;
    private float[] fSentiment;
    private float currentSentiment;

    /**
     * 设置参数.
     *
     * @param options
     * @param iWordTotal
     * @param fSentiment
     * @param currentSentiment
     */
    public void setStrategy(ClassificationOptions options, int iWordTotal, float[] fSentiment, float currentSentiment) {
        this.options = options;
        this.iWordTotal = iWordTotal;
        this.fSentiment = fSentiment;
        this.currentSentiment = currentSentiment;
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
        int iTermsChecked;
        if (word.isNegatingWord() && this.options.bgNegatingWordsOccurAfterSentiment) {
            iTermsChecked = 0;

            for (int iPriorWord = iWordTotal - 1; iPriorWord > 0; --iPriorWord) {
                if (this.options.bgNegatingWordsFlipEmotion) {
                    fSentiment[iPriorWord] = -fSentiment[iPriorWord] * this.options.fgStrengthMultiplierForNegatedWords;
                    if (this.options.bgExplainClassification) {
                        this.sgClassificationRationale.append("[*-").append(this.options.fgStrengthMultiplierForNegatedWords).append(" approx. negated multiplier] ");
                    }
                } else {
                    if (this.options.bgNegatingNegativeNeutralisesEmotion && fSentiment[iPriorWord] < 0.0F) {
                        fSentiment[iPriorWord] = 0.0F;
                        if (this.options.bgExplainClassification) {
                            this.sgClassificationRationale.append("[=0 negation] ");
                        }
                    }

                    if (this.options.bgNegatingPositiveFlipsEmotion && fSentiment[iPriorWord] > 0.0F) {
                        fSentiment[iPriorWord] = -fSentiment[iPriorWord] * this.options.fgStrengthMultiplierForNegatedWords;
                        if (this.options.bgExplainClassification) {
                            this.sgClassificationRationale.append("[*-").append(this.options.fgStrengthMultiplierForNegatedWords).append(" approx. negated multiplier] ");
                        }
                    }
                }

                ++iTermsChecked;
                if (iTermsChecked > this.options.igMaxWordsAfterSentimentToNegate) {
                    break;
                }
            }
        }
        return currentSentiment;
    }

    /**
     * 获得fSentiment.
     *
     * @return fSentiment
     */
    public float[] getSentiment() {
        return fSentiment;
    }
}

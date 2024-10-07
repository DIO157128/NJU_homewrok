package uk.ac.wlv.sentistrength.sentence.strategy;

import uk.ac.wlv.sentistrength.term.term.Word;


/**
 * 用于word计算的第一步.
 */
public class GetSentimentValueStrategy extends WordCalcuStrategy {

    private static final int THREE = 3;
    /**
     * 构造函数.
     */
    public GetSentimentValueStrategy() {
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
        int iTemp;
        float result = 0.0F;
        if (iTerm == 1 || !word.isProperNoun() || word.getOriginalText().equals(":") || word.getOriginalText().length() > THREE && word.getOriginalText().charAt(0) == '@') {
            result = (float) word.getSentimentValue();


            iTemp = word.getSentimentValue();
            if (iTemp < 0) {
                --iTemp;
            } else {
                ++iTemp;
            }

            if (iTemp == 1) {
                this.sgClassificationRationale.append(word.getOriginalText()).append(" ");
            } else {
                this.sgClassificationRationale.append(word.getOriginalText()).append("[").append(iTemp).append("] ");
            }

        } else {
            this.sgClassificationRationale.append(word.getOriginalText()).append(" [proper noun] ");
        }
        return result;
    }
}

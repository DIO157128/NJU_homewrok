package uk.ac.wlv.sentistrength.sentence.strategy;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.term.term.Punctuation;
import uk.ac.wlv.sentistrength.term.term.Term;
import uk.ac.wlv.sentistrength.term.term.Word;

/**
 * 用于计算boost带来的分数影响的类.
 */
public class BoostSentimentStrategy extends WordCalcuStrategy {
    private ClassificationOptions options;
    private Term lastTerm;
    private float currentSentiment;
    private static final double D6 = 0.6D;

    /**
     * 设置参数.
     *
     * @param options          选项
     * @param currentSentiment 当前的情绪分数
     * @param lastTerm         上一个单词
     */
    public void setStrategy(ClassificationOptions options, float currentSentiment, Term lastTerm) {
        this.currentSentiment = currentSentiment;
        this.options = options;
        this.lastTerm = lastTerm;
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
        if (this.options.bgMultipleLettersBoostSentiment && word.getWordEmphasisLength() >= this.options.igMinRepeatedLettersForBoost && (iTerm == 1 || !(lastTerm instanceof Punctuation) || !lastTerm.getOriginalText().equals("@"))) {
            String sEmphasis = word.getWordEmphasis().toLowerCase();
            if (!sEmphasis.contains("xx") && !sEmphasis.contains("ww") && !sEmphasis.contains("ha")) {
                if (currentSentiment < 0.0F) {
                    currentSentiment = (float) ((double) currentSentiment - D6);
                    this.sgClassificationRationale.append("[-0.6 spelling emphasis] ");

                } else if (currentSentiment > 0.0F) {
                    currentSentiment = (float) ((double) currentSentiment + D6);
                    this.sgClassificationRationale.append("[+0.6 spelling emphasis] ");

                } else if (this.options.igMoodToInterpretNeutralEmphasis > 0) {
                    currentSentiment = (float) ((double) currentSentiment + D6);
                    this.sgClassificationRationale.append("[+0.6 spelling mood emphasis] ");

                } else if (this.options.igMoodToInterpretNeutralEmphasis < 0) {
                    currentSentiment = (float) ((double) currentSentiment - D6);
                    this.sgClassificationRationale.append("[-0.6 spelling mood emphasis] ");

                }
            }
        }
        return currentSentiment;
    }
}

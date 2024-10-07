package uk.ac.wlv.sentistrength.sentence.strategy;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.term.term.Term;
import uk.ac.wlv.sentistrength.term.term.Word;

import java.util.ArrayList;

/**
 * 计算类,计算sentence中单词的情感分数.
 */
public class CompositeCalcuWordStrategy {
    ArrayList<WordCalcuStrategy> strategies;
    private final ClassificationOptions options;
    private StringBuilder sgClassificationRationale;
    private final Term[] term;
    private float[] fSentiment;
    private final int iTerm;
    private final int iWordTotal;
    private final Word word;
    private int iLastBoosterWordScore;
    private int iWordsSinceNegative;

    /**
     * 构造类.
     *
     * @param options               选项
     * @param term                  单词表
     * @param fSentiment            单词的情绪分数表
     * @param iWordTotal            总共的单词数
     * @param word                  单词
     * @param iLastBoosterWordScore 上一个助词分数
     * @param iTerm                 单词索引
     * @param iWordsSinceNegative   在遇到消极单词过后的单词计数
     */
    public CompositeCalcuWordStrategy(ClassificationOptions options, Term[] term, float[] fSentiment, int iWordTotal, Word word, int iLastBoosterWordScore, int iTerm, int iWordsSinceNegative) {
        this.options = options;
        strategies = new ArrayList<>();
        sgClassificationRationale = new StringBuilder();
        this.term = term;
        this.fSentiment = fSentiment;
        this.iTerm = iTerm;
        this.iWordTotal = iWordTotal;
        this.word = word;
        this.iLastBoosterWordScore = iLastBoosterWordScore;
        this.iWordsSinceNegative = iWordsSinceNegative;
        addStrategies();
    }

    /**
     * 往策略列表里面添加策略.
     */
    private void addStrategies() {
        strategies.add(new GetSentimentValueStrategy());
        strategies.add(new BoostSentimentStrategy());
        strategies.add(new BoostChangeEmotionStrategy());
        strategies.add(new NegatingWordOccurStrategy());
        strategies.add(new NWordsOccurAfterStrategy());
        strategies.add(new PTPStrategy());
        strategies.add(new NTNStrategy());
    }


    /**
     * 执行,计算单词情绪分数.
     */
    public void execute() {
        for (WordCalcuStrategy wordCalcuStrategy : strategies) {
            setStrategy(wordCalcuStrategy);
            fSentiment[iWordTotal] = wordCalcuStrategy.calculateSentiment(word, iTerm);
            sgClassificationRationale.append(wordCalcuStrategy.getClassificationRationale());
            if (wordCalcuStrategy instanceof BoostChangeEmotionStrategy) {
                iLastBoosterWordScore = ((BoostChangeEmotionStrategy) wordCalcuStrategy).getLastBoosterWordScore();
            } else if (wordCalcuStrategy instanceof NegatingWordOccurStrategy) {
                iWordsSinceNegative = ((NegatingWordOccurStrategy) wordCalcuStrategy).getWordsSinceNegative();
            } else if (wordCalcuStrategy instanceof NWordsOccurAfterStrategy) {
                fSentiment = ((NWordsOccurAfterStrategy) wordCalcuStrategy).getSentiment();
            }
        }
    }

    /**
     * 为每一个策略设置参数.
     *
     * @param wordCalcuStrategy 具体策略
     */
    private void setStrategy(WordCalcuStrategy wordCalcuStrategy) {
        if (wordCalcuStrategy instanceof BoostSentimentStrategy) {
            ((BoostSentimentStrategy) wordCalcuStrategy).setStrategy(this.options, fSentiment[iWordTotal], this.term[iTerm - 1]);
        } else if (wordCalcuStrategy instanceof BoostChangeEmotionStrategy) {
            ((BoostChangeEmotionStrategy) wordCalcuStrategy).setStrategy(this.options, fSentiment[iWordTotal], iLastBoosterWordScore);
        } else if (wordCalcuStrategy instanceof NegatingWordOccurStrategy) {
            ((NegatingWordOccurStrategy) wordCalcuStrategy).setStrategy(this.options, fSentiment[iWordTotal], iWordsSinceNegative, iLastBoosterWordScore);
        } else if (wordCalcuStrategy instanceof NWordsOccurAfterStrategy) {
            ((NWordsOccurAfterStrategy) wordCalcuStrategy).setStrategy(this.options, iWordTotal, fSentiment, fSentiment[iWordTotal]);
        } else if (wordCalcuStrategy instanceof PTPStrategy) {
            ((PTPStrategy) wordCalcuStrategy).setStrategy(this.options, fSentiment[iWordTotal], iWordTotal, fSentiment[iWordTotal - 1]);
        } else if (wordCalcuStrategy instanceof NTNStrategy) {
            ((NTNStrategy) wordCalcuStrategy).setStrategy(this.options, fSentiment[iWordTotal], iWordTotal, fSentiment[iWordTotal - 1]);
        }
    }

    /**
     * 获得LastBoosterWordScore.
     *
     * @return iLastBoosterWordScore
     */
    public int getLastBoosterWordScore() {
        return iLastBoosterWordScore;
    }

    /**
     * 获得WordsSinceNegative.
     *
     * @return iWordsSinceNegative
     */
    public int getWordsSinceNegative() {
        return iWordsSinceNegative;
    }

    /**
     * 获得单词情绪分数表.
     *
     * @return 单词情绪分数表
     */
    public float[] getSentiment() {
        return fSentiment;
    }


    /**
     * 获得解释.
     *
     * @return 解释
     */
    public String getClassificationRationale() {
        return sgClassificationRationale.toString();
    }

}

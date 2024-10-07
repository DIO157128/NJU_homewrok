package uk.ac.wlv.sentistrength.sentence.calcuSentenceStrategy;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.ClassificationResources;
import uk.ac.wlv.sentistrength.term.term.Term;

import java.util.ArrayList;

/**
 * 计算类，用于计算sentence的情感分数.
 */
public class CompositeCalcuSentenceStrategy {
    private final float[] fSentiment;
    private final Term[] term;
    private final int iWordTotal;
    private ArrayList<CalcuSentenceStrategy> calcuSentenceStrategy;
    private final ClassificationOptions options;
    private final ClassificationResources resources;
    private final int igTermCount;
    private final boolean bSentencePunctuationBoost;
    private StringBuilder sgClassificationRationale;
    //自己生产的字段
    private int igPositiveSentiment;
    private int igNegativeSentiment;
    private float fTotalNeg;
    private float fTotalPos;
    private float fMaxNeg;
    private float fMaxPos;
    private int iPosWords;
    private int iNegWords;
    private int igSentiCount;


    /**
     * 构造一个复合计算句子策略的对象.
     *
     * @param term                      术语数组对象
     * @param fSentiment                情感值数组对象
     * @param igTermCount               术语的个数
     * @param iWordTotal                单词的总数
     * @param bSentencePunctuationBoost 是否使用句子标点符号提升情感值
     * @param options                   分类选项对象
     * @param resources                 分类资源对象
     */
    public CompositeCalcuSentenceStrategy(Term[] term, float[] fSentiment, int igTermCount, int iWordTotal, boolean bSentencePunctuationBoost, ClassificationOptions options, ClassificationResources resources) {
        sgClassificationRationale = new StringBuilder();
        this.calcuSentenceStrategy = new ArrayList<>();
        this.fSentiment = fSentiment;
        this.options = options;
        this.resources = resources;
        this.igTermCount = igTermCount;
        this.iWordTotal = iWordTotal;
        this.term = term;
        this.bSentencePunctuationBoost = bSentencePunctuationBoost;
        fTotalNeg = 0.0F;
        fTotalPos = 0.0F;
        fMaxNeg = 0.0F;
        fMaxPos = 0.0F;
        iPosWords = 0;
        iNegWords = 0;
        addStrategies();
    }

    /**
     * 策略列表中添加策略.
     */
    private void addStrategies() {
        calcuSentenceStrategy.add(new CalcuTotalAndMaxStrategy());
        calcuSentenceStrategy.add(new CombineStrategy());
        calcuSentenceStrategy.add(new RNEIQSStrategy());
        calcuSentenceStrategy.add(new MCAP2Strategy());
        calcuSentenceStrategy.add(new PunctuationBoostStrategy());
        calcuSentenceStrategy.add(new EINSCAPStrategy());
        calcuSentenceStrategy.add(new YOYIP2USNStrategy());
        calcuSentenceStrategy.add(new IronyStrategy());
        calcuSentenceStrategy.add(new Combine2Strategy());
    }


    /**
     * 执行策略列表中的策略类的计算方法.
     */
    public void execute() {
        for (CalcuSentenceStrategy calcuSentenceStrategy : calcuSentenceStrategy) {
            setStrategy(calcuSentenceStrategy);
            calcuSentenceStrategy.calculateSentiment();
            updateVar(calcuSentenceStrategy);
            sgClassificationRationale.append(calcuSentenceStrategy.getClassificationRationale());
        }
        this.sgClassificationRationale.append("[sentence: ").append(this.igPositiveSentiment).append(",").append(this.igNegativeSentiment).append("]");
    }


    /**
     * 为策略设置参数.
     *
     * @param calcuSentenceStrategy 具体的策略
     */
    private void setStrategy(CalcuSentenceStrategy calcuSentenceStrategy) {
        if (calcuSentenceStrategy instanceof CalcuTotalAndMaxStrategy) {
            ((CalcuTotalAndMaxStrategy) calcuSentenceStrategy).setStrategy(fSentiment, iWordTotal);
        } else if (calcuSentenceStrategy instanceof CombineStrategy) {
            ((CombineStrategy) calcuSentenceStrategy).setStrategy(options, iPosWords, iNegWords, fTotalNeg, fTotalPos, fMaxPos, fMaxNeg);
        } else if (calcuSentenceStrategy instanceof RNEIQSStrategy) {
            ((RNEIQSStrategy) calcuSentenceStrategy).setStrategy(options, resources, term, igTermCount, igNegativeSentiment);
        } else if (calcuSentenceStrategy instanceof MCAP2Strategy) {
            ((MCAP2Strategy) calcuSentenceStrategy).setStrategy(options, igTermCount, term, igPositiveSentiment);
        } else if (calcuSentenceStrategy instanceof PunctuationBoostStrategy) {
            ((PunctuationBoostStrategy) calcuSentenceStrategy).setStrategy(bSentencePunctuationBoost, options, igPositiveSentiment, igNegativeSentiment);
        } else if (calcuSentenceStrategy instanceof EINSCAPStrategy) {
            ((EINSCAPStrategy) calcuSentenceStrategy).setStrategy(options, igPositiveSentiment, igNegativeSentiment, term, igTermCount);
        } else if (calcuSentenceStrategy instanceof YOYIP2USNStrategy) {
            ((YOYIP2USNStrategy) calcuSentenceStrategy).setStrategy(options, resources, term, igTermCount, igNegativeSentiment, igPositiveSentiment);
        } else if (calcuSentenceStrategy instanceof IronyStrategy) {
            ((IronyStrategy) calcuSentenceStrategy).setStrategy(options, resources, term, igTermCount, igNegativeSentiment, igPositiveSentiment);
        } else if (calcuSentenceStrategy instanceof Combine2Strategy) {
            ((Combine2Strategy) calcuSentenceStrategy).setStrategy(igPositiveSentiment, igNegativeSentiment, options);
        }
    }

    /**
     * 对var进行update.
     *
     * @param calcuSentenceStrategy
     */
    private void updateVar(CalcuSentenceStrategy calcuSentenceStrategy) {
        if (calcuSentenceStrategy instanceof CalcuTotalAndMaxStrategy) {
            CalcuTotalAndMaxStrategy calcuTotalAndMaxStrategy = (CalcuTotalAndMaxStrategy) calcuSentenceStrategy;
            this.fTotalNeg = calcuTotalAndMaxStrategy.getTotalNeg();
            this.fTotalPos = calcuTotalAndMaxStrategy.getTotalPos();
            this.fMaxNeg = calcuTotalAndMaxStrategy.getMaxNeg();
            this.fMaxPos = calcuTotalAndMaxStrategy.getMaxPos();
            this.iPosWords = calcuTotalAndMaxStrategy.getPosWords();
            this.iNegWords = calcuTotalAndMaxStrategy.getNegWords();
            this.igSentiCount = calcuTotalAndMaxStrategy.igSentiCount();
        } else if (calcuSentenceStrategy instanceof CombineStrategy) {
            this.igPositiveSentiment = ((CombineStrategy) calcuSentenceStrategy).getPositiveSentiment();
            this.igNegativeSentiment = ((CombineStrategy) calcuSentenceStrategy).getNegativeSentiment();
        } else if (calcuSentenceStrategy instanceof RNEIQSStrategy) {
            this.igNegativeSentiment = ((RNEIQSStrategy) calcuSentenceStrategy).getIgNegativeSentiment();
        } else if (calcuSentenceStrategy instanceof MCAP2Strategy) {
            this.igPositiveSentiment = ((MCAP2Strategy) calcuSentenceStrategy).getPositiveSentiment();
        } else if (calcuSentenceStrategy instanceof PunctuationBoostStrategy) {
            this.igPositiveSentiment = ((PunctuationBoostStrategy) calcuSentenceStrategy).getIgPositiveSentiment();
            this.igNegativeSentiment = ((PunctuationBoostStrategy) calcuSentenceStrategy).getIgNegativeSentiment();
        } else if (calcuSentenceStrategy instanceof EINSCAPStrategy) {
            this.igPositiveSentiment = ((EINSCAPStrategy) calcuSentenceStrategy).getIgPositiveSentiment();
        } else if (calcuSentenceStrategy instanceof YOYIP2USNStrategy) {
            this.igPositiveSentiment = ((YOYIP2USNStrategy) calcuSentenceStrategy).getIgPositiveSentiment();
        } else if (calcuSentenceStrategy instanceof IronyStrategy) {
            this.igNegativeSentiment = ((IronyStrategy) calcuSentenceStrategy).getIgNegativeSentiment();
            this.igPositiveSentiment = ((IronyStrategy) calcuSentenceStrategy).getIgPositiveSentiment();
        } else if (calcuSentenceStrategy instanceof Combine2Strategy) {
            this.igPositiveSentiment = ((Combine2Strategy) calcuSentenceStrategy).getIgPositiveSentiment();
            this.igNegativeSentiment = ((Combine2Strategy) calcuSentenceStrategy).getIgNegativeSentiment();
        }
    }

    /**
     * 获得sgClassificationRationale.
     *
     * @return sgClassificationRationale
     */
    public String getSgClassificationRationale() {
        return sgClassificationRationale.toString();
    }

    /**
     * 获得igPositiveSentiment.
     *
     * @return igPositiveSentiment.
     */
    public int getIgPositiveSentiment() {
        return igPositiveSentiment;
    }

    /**
     * 获得igNegativeSentiment.
     *
     * @return igNegativeSentiment
     */
    public int getIgNegativeSentiment() {
        return igNegativeSentiment;
    }

    /**
     * 获得igSentiCount.
     *
     * @return igSentiCount
     */
    public int getIgSentiCount() {
        return igSentiCount;
    }
}

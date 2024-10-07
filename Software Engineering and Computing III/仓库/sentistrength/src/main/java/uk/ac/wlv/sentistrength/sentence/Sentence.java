//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package uk.ac.wlv.sentistrength.sentence;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.ClassificationResources;
import uk.ac.wlv.sentistrength.TextParsingOptions;
import uk.ac.wlv.sentistrength.UnusedTermsClassificationIndex;
import uk.ac.wlv.sentistrength.sentence.calcuSentenceStrategy.CompositeCalcuSentenceStrategy;
import uk.ac.wlv.sentistrength.sentence.strategy.CompositeCalcuWordStrategy;
import uk.ac.wlv.sentistrength.term.term.Punctuation;
import uk.ac.wlv.sentistrength.term.termConstructor.ConcreteTermBuilder;
import uk.ac.wlv.sentistrength.term.termConstructor.TermBuilder;
import uk.ac.wlv.sentistrength.term.termConstructor.TermDirector;
import uk.ac.wlv.utilities.Sort;
import uk.ac.wlv.utilities.StringIndex;
import uk.ac.wlv.wkaclass.Arff;
import uk.ac.wlv.sentistrength.term.term.Term;
import uk.ac.wlv.sentistrength.term.term.*;


/**
 * 相关用例:UC-8,UC-25.
 * Sentence类代表了由一系列对话中的基本元素组成的句子.
 */
public class Sentence {
    private Term[] term;
    private boolean[] bgSpaceAfterTerm;
    private int igTermCount = 0;


    private int igSentiCount = 0;
    private int igPositiveSentiment = 0;
    private int igNegativeSentiment = 0;
    private boolean bgNothingToClassify = true;
    private ClassificationResources resources;
    private ClassificationOptions options;
    private int[] igSentimentIDList;
    private int igSentimentIDListCount = 0;
    private boolean bSentimentIDListMade = false;
    private boolean[] bgIncludeTerm;
    private boolean bgIdiomsApplied = false;
    private boolean bgObjectEvaluationsApplied = false;
    private StringBuilder sgClassificationRationale = new StringBuilder();
    private boolean bSentencePunctuationBoost = false;
    //用于计算word的字段，需要传输和更新
    private float[] fSentiment = null;
    private int iWordTotal = 0;
    private int iLastBoosterWordScore = 0;
    private int iWordsSinceNegative = -1;
    private static final int MILLION = 100000;

    /**
     * 构造函数.
     */
    public Sentence() {
    }

    /**
     * 获得igTermCount.
     *
     * @return igTermCount
     */
    public int getIgTermCount() {
        return igTermCount;
    }

    /**
     * 获得igSentiCount.
     *
     * @return igSentiCount
     */
    public int getIgSentiCount() {
        return igSentiCount;
    }

    /**
     * 将句子添加到索引中.
     *
     * @param unusedTermClassificationIndex 未使用的Terms分类索引
     */
    public void addSentenceToIndex(UnusedTermsClassificationIndex unusedTermClassificationIndex) {
        for (int i = 1; i <= this.igTermCount; ++i) {
            unusedTermClassificationIndex.addTermToNewTermIndex(this.term[i].getText());
        }

    }

    /**
     * 根据指定的TextParsingOptions将此对象中的term添加到给定的StringIndex中.
     *
     * @param stringIndex        要向其中添加术语的stringIndex
     * @param textParsingOptions 指定如何添加术语的选项
     * @param bRecordCount       如果为真，则记录添加的每个项的计数
     * @param bArffIndex         如果为true，请在将术语添加到索引之前以ARFF格式对其进行编码
     * @return 将它们添加到索引时检查的项数
     */
    public int addToStringIndex(StringIndex stringIndex, TextParsingOptions textParsingOptions, boolean bRecordCount, boolean bArffIndex) {
        String sEncoded;
        int iStringPos;
        int iTermsChecked = 0;
        if (textParsingOptions.bgIncludePunctuation && textParsingOptions.igNgramSize == 1 && !textParsingOptions.bgUseTranslations && !textParsingOptions.bgAddEmphasisCode) {
            for (int i = 1; i <= this.igTermCount; ++i) {
                stringIndex.addString(this.term[i].getText(), bRecordCount);
            }

            iTermsChecked = this.igTermCount;
        } else {
            StringBuilder sText = new StringBuilder();
            int iCurrentTerm = 0;
            int iTermCount = 0;

            while (iCurrentTerm < this.igTermCount) {
                ++iCurrentTerm;
                if (textParsingOptions.bgIncludePunctuation || !(this.term[iCurrentTerm] instanceof Punctuation)) {
                    ++iTermCount;
                    if (iTermCount > 1) {
                        sText.append(" ");
                    } else {
                        sText = new StringBuilder();
                    }

                    if (textParsingOptions.bgUseTranslations) {
                        sText.append(this.term[iCurrentTerm].getTranslation());
                    } else {
                        sText.append(this.term[iCurrentTerm].getOriginalText());
                    }

                    if (textParsingOptions.bgAddEmphasisCode && this.term[iCurrentTerm].containsEmphasis()) {
                        sText.append("+");
                    }
                }

                if (iTermCount == textParsingOptions.igNgramSize) {
                    if (bArffIndex) {
                        sEncoded = Arff.arffSafeWordEncode(sText.toString().toLowerCase(), false);
                        iStringPos = stringIndex.findString(sEncoded);
                        iTermCount = 0;
                        if (iStringPos > -1) {
                            stringIndex.add1ToCount(iStringPos);
                        }
                    } else {
                        stringIndex.addString(sText.toString().toLowerCase(), bRecordCount);
                        iTermCount = 0;
                    }

                    iCurrentTerm += 1 - textParsingOptions.igNgramSize;
                    ++iTermsChecked;
                }
            }
        }

        return iTermsChecked;
    }


    /**
     * 设置此对象所表示的句子，并使用给定的先验分类资源和分类选项进行处理.
     *
     * @param sSentence                要设置的句子
     * @param classResources           用于处理句子的先验分类资源
     * @param newClassificationOptions 用于处理句子的分类选项
     */
    public void setSentence(String sSentence, ClassificationResources classResources, ClassificationOptions newClassificationOptions) {
        this.resources = classResources;
        this.options = newClassificationOptions;
        if (this.options.bgAlwaysSplitWordsAtApostrophes && sSentence.contains("'")) {
            sSentence = sSentence.replace("'", " ");
        }

        String[] sSegmentList = sSentence.split(" ");

        int iMaxTermListLength = sSentence.length() + 1;
        this.term = new Term[iMaxTermListLength];
        this.bgSpaceAfterTerm = new boolean[iMaxTermListLength];
        int iPos;
        this.igTermCount = 0;

        for (String s : sSegmentList) {
            for (iPos = 0; iPos >= 0 && iPos < s.length(); this.bgSpaceAfterTerm[this.igTermCount] = false) {
                TermBuilder termBuilder = new ConcreteTermBuilder();
                TermDirector director = new TermDirector(termBuilder);
                this.term[++this.igTermCount] = director.constructTerm(s.substring(iPos), this.resources, this.options);
                int iOffset = termBuilder.getPos();
                if (iOffset < 0) {
                    iPos = iOffset;
                } else {
                    iPos += iOffset;
                }
            }

            this.bgSpaceAfterTerm[this.igTermCount] = true;
        }

        this.bgSpaceAfterTerm[this.igTermCount] = false;
    }

    /**
     * 获取此对象所表示的句子的情感 ID 列表.
     *
     * @return 此对象所表示的句子的情感 ID 列表
     */
    public int[] getSentimentIDList() {
        if (!this.bSentimentIDListMade) {
            this.makeSentimentIDList();
        }

        return this.igSentimentIDList;
    }

    /**
     * 制作此对象所表示的句子的情感 ID 列表.
     */
    public void makeSentimentIDList() {
        int iSentimentIDTemp;
        this.igSentimentIDListCount = 0;

        int i;
        for (i = 1; i <= this.igTermCount; ++i) {
            if (this.term[i].getSentimentID() > 0) {
                ++this.igSentimentIDListCount;
            }
        }

        if (this.igSentimentIDListCount > 0) {
            this.igSentimentIDList = new int[this.igSentimentIDListCount + 1];
            this.igSentimentIDListCount = 0;

            for (i = 1; i <= this.igTermCount; ++i) {
                iSentimentIDTemp = this.term[i].getSentimentID();
                if (iSentimentIDTemp > 0) {
                    for (int j = 1; j <= this.igSentimentIDListCount; ++j) {
                        if (iSentimentIDTemp == this.igSentimentIDList[j]) {
                            iSentimentIDTemp = 0;
                            break;
                        }
                    }

                    if (iSentimentIDTemp > 0) {
                        this.igSentimentIDList[++this.igSentimentIDListCount] = iSentimentIDTemp;
                    }
                }
            }

            Sort.quickSortInt(this.igSentimentIDList, 1, this.igSentimentIDListCount);
        }

        this.bSentimentIDListMade = true;
    }

    /**
     * 获取此对象所表示的句子的标记版本.
     *
     * @return 此对象所表示的句子的标记版本
     */
    public String getTaggedSentence() {
        StringBuilder sTagged = new StringBuilder();

        for (int i = 1; i <= this.igTermCount; ++i) {
            if (this.bgSpaceAfterTerm[i]) {
                sTagged.append(this.term[i].getTag()).append(" ");
            } else {
                sTagged.append(this.term[i].getTag());
            }
        }

        return sTagged + "<br>";
    }

    /**
     * 获取此对象所表示的句子的分类原因.
     *
     * @return 此对象所表示的句子的分类原因
     */
    public String getClassificationRationale() {
        return this.sgClassificationRationale.toString();
    }

    /**
     * 获得经过翻译处理后的句子.
     *
     * @return 经过翻译处理后的句子
     */
    public String getTranslatedSentence() {
        StringBuilder sTranslated = new StringBuilder();

        for (int i = 1; i <= this.igTermCount; ++i) {
            sTranslated.append(this.term[i].getTranslation());
            if (this.bgSpaceAfterTerm[i]) {
                sTranslated.append(" ");
            }
        }

        return sTranslated + "<br>";
    }

    /**
     * 重新计算句子的情感分数.
     */
    public void recalculateSentenceSentimentScore() {
        this.calculateSentenceSentimentScore();
    }

    /**
     * 重新分类此对象所表示的句子，以便在情感词更改时更新其情感分数.
     *
     * @param iSentimentWordID 更改的情感词的 ID
     */
    public void reClassifyClassifiedSentenceForSentimentChange(int iSentimentWordID) {
        if (this.igNegativeSentiment == 0) {
            this.calculateSentenceSentimentScore();
        } else {
            if (!this.bSentimentIDListMade) {
                this.makeSentimentIDList();
            }

            if (this.igSentimentIDListCount != 0) {
                if (Sort.iFindIntPositionInSortedArray(iSentimentWordID, this.igSentimentIDList, 1, this.igSentimentIDListCount) >= 0) {
                    this.calculateSentenceSentimentScore();
                }

            }
        }
    }

    /**
     * 获得句子的积极情感分数.
     *
     * @return 句子的积极情感分数
     */
    public int getSentencePositiveSentiment() {
        if (this.igPositiveSentiment == 0) {
            this.calculateSentenceSentimentScore();
        }

        return this.igPositiveSentiment;
    }

    /**
     * 获得句子的消极情感分数.
     *
     * @return 句子的消极情感分数
     */
    public int getSentenceNegativeSentiment() {
        if (this.igNegativeSentiment == 0) {
            this.calculateSentenceSentimentScore();
        }

        return this.igNegativeSentiment;
    }


    /**
     * 标记哪些词语可以用于分类.
     * 如果选项中设置了忽略没有关键字的句子，
     * 那么该方法会检查每个词语是否与选项中的情感关键字匹配.
     * 如果匹配，则将该词语包含在分类中，
     * 并且将bgNothingToClassify设置为false.
     * 此外，该方法还会根据选项中设置的igWordsToIncludeAfterKeyword和igWordsToIncludeBeforeKeyword
     * 来确定在关键字之前和之后应包含多少个词语.
     */
    private void markTermsValidToClassify() {
        this.bgIncludeTerm = new boolean[this.igTermCount + 1];
        int iTermsSinceValid;
        if (this.options.bgIgnoreSentencesWithoutKeywords) {
            this.bgNothingToClassify = true;

            int iTerm;
            for (iTermsSinceValid = 1; iTermsSinceValid <= this.igTermCount; ++iTermsSinceValid) {
                this.bgIncludeTerm[iTermsSinceValid] = false;
                if (this.term[iTermsSinceValid] instanceof Word) {
                    for (iTerm = 0; iTerm < this.options.sgSentimentKeyWords.length; ++iTerm) {
                        if (((Word) this.term[iTermsSinceValid]).matchesString(this.options.sgSentimentKeyWords[iTerm], true)) {
                            this.bgIncludeTerm[iTermsSinceValid] = true;
                            this.bgNothingToClassify = false;
                        }
                    }
                }
            }

            if (!this.bgNothingToClassify) {
                iTermsSinceValid = MILLION;

                for (iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                    if (this.bgIncludeTerm[iTerm]) {
                        iTermsSinceValid = 0;
                    } else if (iTermsSinceValid < this.options.igWordsToIncludeAfterKeyword) {
                        this.bgIncludeTerm[iTerm] = true;
                        if (this.term[iTerm] instanceof Word) {
                            ++iTermsSinceValid;
                        }
                    }
                }

                iTermsSinceValid = MILLION;
                for (iTerm = this.igTermCount; iTerm >= 1; --iTerm) {
                    if (this.bgIncludeTerm[iTerm]) {
                        iTermsSinceValid = 0;
                    } else if (iTermsSinceValid < this.options.igWordsToIncludeBeforeKeyword) {
                        this.bgIncludeTerm[iTerm] = true;
                        if (this.term[iTerm] instanceof Word) {
                            ++iTermsSinceValid;
                        }
                    }
                }
            }
        } else {
            for (iTermsSinceValid = 1; iTermsSinceValid <= this.igTermCount; ++iTermsSinceValid) {
                this.bgIncludeTerm[iTermsSinceValid] = true;
            }

            this.bgNothingToClassify = false;
        }

    }


    /**
     * 计算句子中的表情分.
     *
     * @param emoticon 表情
     */
    private void calculateScoreForEmoticon(Emoticon emoticon) {
        int iTermsChecked = emoticon.getEmoticonSentimentStrength();
        if (iTermsChecked != 0) {
            if (iWordTotal > 0) {
                fSentiment[iWordTotal] += (float) emoticon.getEmoticonSentimentStrength();
                if (this.options.bgExplainClassification) {
                    this.sgClassificationRationale.append(emoticon.getEmoticon()).append(" [").append(emoticon.getEmoticonSentimentStrength()).append(" emoticon] ");
                }
            } else {
                ++iWordTotal;
                fSentiment[iWordTotal] = (float) iTermsChecked;
                if (this.options.bgExplainClassification) {
                    this.sgClassificationRationale.append(emoticon.getEmoticon()).append(" [").append(emoticon.getEmoticonSentimentStrength()).append(" emoticon]");
                }
            }
        }
    }


    /**
     * 计算句子中的标点符号的情绪分数.
     *
     * @param punctuation 标点符号
     */
    private void calculateScoreForPunctuation(Punctuation punctuation) {
        if (punctuation.getPunctuationEmphasisLength() >= this.options.igMinPunctuationWithExclamationToChangeSentenceSentiment && punctuation.punctuationContains("!") && iWordTotal > 0) {
            bSentencePunctuationBoost = true;
        }
        if (this.options.bgExplainClassification) {
            this.sgClassificationRationale.append(punctuation.getOriginalText());
        }
    }


    /**
     * 计算Word分数.
     *
     * @param word  实例化的word类
     * @param iTerm term的指针
     */
    private void calculateScoreForWord(Word word, int iTerm) {
        ++iWordTotal;
        CompositeCalcuWordStrategy compositeCalcuWordStrategy = new CompositeCalcuWordStrategy(options, term, fSentiment, iWordTotal, word, iLastBoosterWordScore, iTerm, iWordsSinceNegative);
        compositeCalcuWordStrategy.execute();
        if (options.bgExplainClassification) {
            sgClassificationRationale.append(compositeCalcuWordStrategy.getClassificationRationale());
        }
        iLastBoosterWordScore = compositeCalcuWordStrategy.getLastBoosterWordScore();
        iWordsSinceNegative = compositeCalcuWordStrategy.getWordsSinceNegative();
        fSentiment = compositeCalcuWordStrategy.getSentiment();
    }


    /**
     * 计算最终得分.
     */
    private void calculateSentiment() {
        CompositeCalcuSentenceStrategy calcuSentenceStrategy = new CompositeCalcuSentenceStrategy(term, fSentiment, igTermCount, iWordTotal, bSentencePunctuationBoost, options, resources);
        calcuSentenceStrategy.execute();
        this.igNegativeSentiment = calcuSentenceStrategy.getIgNegativeSentiment();
        this.igPositiveSentiment = calcuSentenceStrategy.getIgPositiveSentiment();
        this.igSentiCount = calcuSentenceStrategy.getIgSentiCount();
        if (options.bgExplainClassification) {
            sgClassificationRationale.append(calcuSentenceStrategy.getSgClassificationRationale());
        }
    }


    /**
     * 计算句子的情感分数.如果设置了解释分类，接需要给出分类原因.
     * 根据options选择情感处理模式，对于句子中的terms用情感处理模式进行解析并获得最终整个句子的分数
     */
    private void calculateSentenceSentimentScore() {
        if (this.options.bgExplainClassification && this.sgClassificationRationale.length() > 0) {
            this.sgClassificationRationale = new StringBuilder();
        }

        this.igNegativeSentiment = 1;
        this.igPositiveSentiment = 1;
        this.iWordTotal = 0;
        this.iLastBoosterWordScore = 0;

        if (this.igTermCount == 0) {
            this.bgNothingToClassify = true;
            this.igNegativeSentiment = -1;
            this.igPositiveSentiment = 1;
        } else {
            this.markTermsValidToClassify();
            if (this.bgNothingToClassify) {
                this.igNegativeSentiment = -1;
                this.igPositiveSentiment = 1;
            } else {
                bSentencePunctuationBoost = false;
                iWordsSinceNegative = this.options.igMaxWordsBeforeSentimentToNegate + 2;
                fSentiment = new float[this.igTermCount + 1];
                if (this.options.bgUseIdiomLookupTable) {
                    this.overrideTermStrengthsWithIdiomStrengths(false);
                }

                if (this.options.bgUseObjectEvaluationTable) {
                    this.overrideTermStrengthsWithObjectEvaluationStrengths(false);
                }
                for (int iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                    if (this.bgIncludeTerm[iTerm]) {
                        Term tempTerm = this.term[iTerm];
                        if (!(this.term[iTerm] instanceof Word)) {
                            if (this.term[iTerm] instanceof Emoticon) {
                                Emoticon emoticon = (Emoticon) tempTerm;
                                calculateScoreForEmoticon(emoticon);
                            } else if (this.term[iTerm] instanceof Punctuation) {
                                Punctuation punctuation = (Punctuation) tempTerm;
                                calculateScoreForPunctuation(punctuation);
                            }
                        } else {
                            Word word = (Word) tempTerm;
                            calculateScoreForWord(word, iTerm);
                        }
                    }
                }

                calculateSentiment();
            }
        }
    }


    /**
     * 使用对象评估表来覆盖词语的强度.
     * 如果bgObjectEvaluationsApplied为false或recalculateIfAlreadyDone为true，
     * 则遍历所有对象评估项，检查是否有匹配的对象和评估.
     * 如果有，则将对应词语的情感值设置为对象评估表中指定的值，
     * 并更新sgClassificationRationale.
     * 最后，将bgObjectEvaluationsApplied设置为true.
     *
     * @param recalculateIfAlreadyDone 是否重新计算
     */
    public void overrideTermStrengthsWithObjectEvaluationStrengths(boolean recalculateIfAlreadyDone) {
        boolean bMatchingObject;
        boolean bMatchingEvaluation;
        if (!this.bgObjectEvaluationsApplied || recalculateIfAlreadyDone) {
            for (int iObject = 1; iObject < this.resources.evaluativeTerms.igObjectEvaluationCount; ++iObject) {
                bMatchingObject = false;
                bMatchingEvaluation = false;

                int iTerm;
                for (iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                    if (this.term[iTerm] instanceof Word && ((Word) this.term[iTerm]).matchesStringWithWildcard(this.resources.evaluativeTerms.sgObject[iObject], true)) {
                        bMatchingObject = true;
                        break;
                    }
                }

                if (bMatchingObject) {
                    for (iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                        if (this.term[iTerm] instanceof Word && ((Word) this.term[iTerm]).matchesStringWithWildcard(this.resources.evaluativeTerms.sgObjectEvaluation[iObject], true)) {
                            bMatchingEvaluation = true;
                            break;
                        }
                    }
                }

                if (bMatchingEvaluation) {
                    if (this.options.bgExplainClassification) {
                        this.sgClassificationRationale.append("[term weight changed by object/evaluation]");
                    }

                    this.term[iTerm].setSentimentOverrideValue(this.resources.evaluativeTerms.igObjectEvaluationStrength[iObject]);
                }
            }

            this.bgObjectEvaluationsApplied = true;
        }

    }


    /**
     * 使用成语表来覆盖词语的强度.
     * 如果bgIdiomsApplied为false或recalculateIfAlreadyDone为true，
     * 则遍历所有词语和成语，检查是否有匹配的成语.
     * 如果有，则将对应词语的情感值设置为成语表中指定的值，
     * 并更新sgClassificationRationale.
     * 最后，将bgIdiomsApplied设置为true.
     *
     * @param recalculateIfAlreadyDone 是否重新计算
     */
    public void overrideTermStrengthsWithIdiomStrengths(boolean recalculateIfAlreadyDone) {
        if (!this.bgIdiomsApplied || recalculateIfAlreadyDone) {
            for (int iTerm = 1; iTerm <= this.igTermCount; ++iTerm) {
                if (this.term[iTerm] instanceof Word) {
                    for (int iIdiom = 1; iIdiom <= this.resources.idiomList.igIdiomCount; ++iIdiom) {
                        if (iTerm + this.resources.idiomList.igIdiomWordCount[iIdiom] - 1 <= this.igTermCount) {
                            boolean bMatchingIdiom = true;

                            int iIdiomTerm;
                            for (iIdiomTerm = 0; iIdiomTerm < this.resources.idiomList.igIdiomWordCount[iIdiom]; ++iIdiomTerm) {
                                if (!((Word) this.term[iTerm + iIdiomTerm]).matchesStringWithWildcard(this.resources.idiomList.sgIdiomWords[iIdiom][iIdiomTerm], true)) {
                                    bMatchingIdiom = false;
                                    break;
                                }
                            }

                            if (bMatchingIdiom) {
                                if (this.options.bgExplainClassification) {
                                    this.sgClassificationRationale.append("[term weight(s) changed by idiom ").append(this.resources.idiomList.getIdiom(iIdiom)).append("]");
                                }

                                this.term[iTerm].setSentimentOverrideValue(this.resources.idiomList.igIdiomStrength[iIdiom]);

                                for (iIdiomTerm = 1; iIdiomTerm < this.resources.idiomList.igIdiomWordCount[iIdiom]; ++iIdiomTerm) {
                                    this.term[iTerm + iIdiomTerm].setSentimentOverrideValue(0);
                                }
                            }
                        }
                    }
                }
            }

            this.bgIdiomsApplied = true;
        }
    }


}

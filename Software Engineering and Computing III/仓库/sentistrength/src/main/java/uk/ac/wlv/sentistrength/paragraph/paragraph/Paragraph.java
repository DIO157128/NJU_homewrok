package uk.ac.wlv.sentistrength.paragraph.paragraph;

import uk.ac.wlv.sentistrength.*;
import uk.ac.wlv.sentistrength.paragraph.paragraphFunction.ParagraphFunction;
import uk.ac.wlv.sentistrength.sentence.Sentence;
import uk.ac.wlv.utilities.Sort;
import uk.ac.wlv.utilities.StringIndex;

import java.util.Random;

/**
 * paragraph抽象类,是paragraph的模板，所有paragraph都继承此类.
 */
public abstract class Paragraph implements ParagraphFunction {
    private static final int THREE = 3;
    private static final float DOTFIVE = 0.5F;
    protected Sentence[] sentence;
    protected int igSentenceCount = 0; //句子计数
    protected int[] igSentimentIDList; //情绪ID列表
    protected int igSentimentIDListCount = 0; //情绪ID列表计数
    protected boolean bSentimentIDListMade = false; //情绪ID列表是否制作
    protected int igPositiveSentiment = 0; //积极情绪
    protected int igNegativeSentiment = 0; //消极情绪
    protected int igTrinarySentiment = 0; //三位一体（中性）情绪
    protected int igScaleSentiment = 0; //规模情绪
    protected ClassificationResources resources;
    protected ClassificationOptions options;
    protected final Random generator = new Random();
    protected StringBuilder sgClassificationRationale = new StringBuilder();


    /**
     * 将段落添加到索引中，并使用积极类和消极类值进行分类.
     *
     * @param unusedTermsClassificationIndex 未使用术语分类索引
     * @param iCorrectPosClass               正确的正类值
     * @param iEstPosClass                   预测的正类值
     * @param iCorrectNegClass               正确的负类值
     * @param iEstNegClass                   预测的负类值
     */
    public void addParagraphToIndexWithPosNegValues(UnusedTermsClassificationIndex unusedTermsClassificationIndex, int iCorrectPosClass, int iEstPosClass, int iCorrectNegClass, int iEstNegClass) {
        for (int i = 1; i <= this.igSentenceCount; ++i) {
            this.sentence[i].addSentenceToIndex(unusedTermsClassificationIndex);
        }

        unusedTermsClassificationIndex.addNewIndexToMainIndexWithPosNegValues(iCorrectPosClass, iEstPosClass, iCorrectNegClass, iEstNegClass);
    }


    /**
     * 将段落添加到索引中，并使用Scale Values进行分类.
     *
     * @param unusedTermsClassificationIndex 未使用术语分类索引
     * @param iCorrectScaleClass             正确的Scale类值
     * @param iEstScaleClass                 预测的Scale类值
     */
    public void addParagraphToIndexWithScaleValues(UnusedTermsClassificationIndex unusedTermsClassificationIndex, int iCorrectScaleClass, int iEstScaleClass) {
        for (int i = 1; i <= this.igSentenceCount; ++i) {
            this.sentence[i].addSentenceToIndex(unusedTermsClassificationIndex);
        }

        unusedTermsClassificationIndex.addNewIndexToMainIndexWithScaleValues(iCorrectScaleClass, iEstScaleClass);
    }


    /**
     * 将段落添加到索引中，并使用Binary Values进行分类.
     *
     * @param unusedTermsClassificationIndex 未使用术语分类索引
     * @param iCorrectBinaryClass            正确的Binary类值
     * @param iEstBinaryClass                预测的Binary类值
     */
    public void addParagraphToIndexWithBinaryValues(UnusedTermsClassificationIndex unusedTermsClassificationIndex, int iCorrectBinaryClass, int iEstBinaryClass) {
        for (int i = 1; i <= this.igSentenceCount; ++i) {
            this.sentence[i].addSentenceToIndex(unusedTermsClassificationIndex);
        }

        unusedTermsClassificationIndex.addNewIndexToMainIndexWithBinaryValues(iCorrectBinaryClass, iEstBinaryClass);
    }


    /**
     * 将文本中的词语添加到字符串索引中.
     * 遍历所有句子，调用每个句子的addToStringIndex方法.
     * 将词语添加到字符串索引中.
     * 返回检查过的词语数量.
     *
     * @param stringIndex        字符串索引
     * @param textParsingOptions 文本解析选项
     * @param bRecordCount       是否记录计数
     * @param bArffIndex         是否为arff索引
     * @return 检查过的词语数量
     */
    public int addToStringIndex(StringIndex stringIndex, TextParsingOptions textParsingOptions, boolean bRecordCount, boolean bArffIndex) {
        int iTermsChecked = 0;

        for (int i = 1; i <= this.igSentenceCount; ++i) {
            iTermsChecked += this.sentence[i].addToStringIndex(stringIndex, textParsingOptions, bRecordCount, bArffIndex);
        }

        return iTermsChecked;
    }


    /**
     * 遍历所有句子，调用每个句子的addSentenceToIndex方法，将词语添加到未使用词语分类索引中.
     * 然后调用unusedTermsClassificationIndex的addNewIndexToMainIndexWithTrinaryValues方法，使用三元值更新索引.
     *
     * @param unusedTermsClassificationIndex 未使用词语分类索引
     * @param iCorrectTrinaryClass           正确的三元类别
     * @param iEstTrinaryClass               预测的三元类别
     */
    public void addParagraphToIndexWithTrinaryValues(UnusedTermsClassificationIndex unusedTermsClassificationIndex, int iCorrectTrinaryClass, int iEstTrinaryClass) {
        for (int i = 1; i <= this.igSentenceCount; ++i) {
            this.sentence[i].addSentenceToIndex(unusedTermsClassificationIndex);
        }

        unusedTermsClassificationIndex.addNewIndexToMainIndexWithTrinaryValues(iCorrectTrinaryClass, iEstTrinaryClass);
    }


    /**
     * 设置段落文本并进行分句。将resources和options设置为给定的值并将段落中的双引号替换为单引号.
     * 接着，计算句子结束符的数量，包括"<br>"、"."、"!"和"?".
     * 然后创建一个新的Sentence数组，并遍历段落中的字符，根据句子结束符将段落分成多个句子.
     *
     * @param sParagraph               段落文本
     * @param classResources           先验分类资源
     * @param newClassificationOptions 分类选项
     */
    public void setParagraph(String sParagraph, ClassificationResources classResources, ClassificationOptions newClassificationOptions) {
        this.resources = classResources;
        this.options = newClassificationOptions;
        if (sParagraph.contains("\"")) {
            sParagraph = sParagraph.replace("\"", "'");
        }

        //count sentences
        int iSentenceEnds = countSentence(sParagraph);
        this.sentence = new Sentence[iSentenceEnds];

        //get sentence and bring them to the local var sentence
        getEachSentence(sParagraph);
    }


    /**
     * 数句子.
     *
     * @param paragraph 段落
     * @return 段落中的句子数
     */
    protected int countSentence(String paragraph) {
        int sentenceEnds = 2;
        int brCount = countStrInParagraph("<br>", paragraph);
        int dotCount = countStrInParagraph(".", paragraph);
        int exclamatoryMarkCount = countStrInParagraph("!", paragraph);
        int questionmark = countStrInParagraph("?", paragraph);
        sentenceEnds = sentenceEnds + brCount + dotCount + exclamatoryMarkCount + questionmark;
        return sentenceEnds;
    }


    /**
     * 特定字符串在段落中的数量.
     *
     * @param str       字符串
     * @param paragraph 段落
     * @return 特定字符串在段落中的数量
     */
    protected int countStrInParagraph(String str, String paragraph) {
        int iPos = 0;
        int count = 0;
        while (iPos >= 0 && iPos < paragraph.length()) {
            iPos = paragraph.indexOf(str, iPos);
            if (iPos >= 0) {
                iPos += THREE;
                ++count;
            }
        }
        return count;
    }

    /**
     * 将段落拆分为句子并将句子放入句子数组中.
     *
     * @param paragraph 段落
     */
    protected void getEachSentence(String paragraph) {
        this.igSentenceCount = 0;
        int iLastSentenceEnd = -1;
        boolean getPunctuationInEnd = false;
        int iNextBr = paragraph.indexOf("<br>");
        String sNextSentence = "";
        for (int iPos = 0; iPos < paragraph.length(); ++iPos) {
            String sNextChar = paragraph.substring(iPos, iPos + 1);
            if (iPos == paragraph.length() - 1) {
                sNextSentence = paragraph.substring(iLastSentenceEnd + 1);
            } else if (iPos == iNextBr) {
                sNextSentence = paragraph.substring(iLastSentenceEnd + 1, iPos);
                iLastSentenceEnd = iPos + THREE;
                iNextBr = paragraph.indexOf("<br>", iNextBr + 2);
            } else if (this.isSentenceEndPunctuation(sNextChar)) {
                getPunctuationInEnd = true;
            } else if (sNextChar.compareTo(" ") == 0) {
                if (getPunctuationInEnd) {
                    sNextSentence = paragraph.substring(iLastSentenceEnd + 1, iPos);
                    iLastSentenceEnd = iPos;
                }
            } else if (this.isAlphanumeric(sNextChar) && getPunctuationInEnd) {
                sNextSentence = paragraph.substring(iLastSentenceEnd + 1, iPos);
                iLastSentenceEnd = iPos - 1;
            }

            if (!sNextSentence.equals("")) {
                ++this.igSentenceCount;
                this.sentence[this.igSentenceCount] = new Sentence();
                this.sentence[this.igSentenceCount].setSentence(sNextSentence, this.resources, this.options);
                sNextSentence = "";
                getPunctuationInEnd = false;
            }
        }
    }

    /**
     * 获得情感ID列表，如果没有就创建一个.
     *
     * @return 情感ID列表
     */
    public int[] getSentimentIDList() {
        if (!this.bSentimentIDListMade) {
            this.makeSentimentIDList();
        }

        return this.igSentimentIDList;
    }

    /**
     * 获得分类原因.
     *
     * @return 分类原因
     */
    public String getClassificationRationale() {
        return this.sgClassificationRationale.toString();
    }

    /**
     * 创建一个情感 ID 列表，该列表包含所有句子中的情感 ID。计算出情感 ID 的总数并创建一个新的数组来存储这些 ID。
     * 然后，遍历每个句子中的情感 ID 列表，并检查是否有重复的 ID。
     * 如果没有重复，则将该 ID 添加到列表中。最后，使用快速排序对列表进行排序，
     * 并将 bSentimentIDListMade 设置为 true.
     */
    public void makeSentimentIDList() {
        boolean bIsDuplicate;
        this.igSentimentIDListCount = 0;

        int i;
        for (i = 1; i <= this.igSentenceCount; ++i) {
            if (this.sentence[i].getSentimentIDList() != null) {
                this.igSentimentIDListCount += this.sentence[i].getSentimentIDList().length;
            }
        }

        if (this.igSentimentIDListCount > 0) {
            this.igSentimentIDList = new int[this.igSentimentIDListCount + 1];
            this.igSentimentIDListCount = 0;

            for (i = 1; i <= this.igSentenceCount; ++i) {
                int[] sentenceIDList = this.sentence[i].getSentimentIDList();
                if (sentenceIDList != null) {
                    for (int j = 1; j < sentenceIDList.length; ++j) {
                        if (sentenceIDList[j] != 0) {
                            bIsDuplicate = false;

                            for (int k = 1; k <= this.igSentimentIDListCount; ++k) {
                                if (sentenceIDList[j] == this.igSentimentIDList[k]) {
                                    bIsDuplicate = true;
                                    break;
                                }
                            }

                            if (!bIsDuplicate) {
                                this.igSentimentIDList[++this.igSentimentIDListCount] = sentenceIDList[j];
                            }
                        }
                    }
                }
            }

            Sort.quickSortInt(this.igSentimentIDList, 1, this.igSentimentIDListCount);
        }

        this.bSentimentIDListMade = true;
    }

    /**
     * 获得带标签的段落.
     *
     * @return 带标签的段落
     */
    public String getTaggedParagraph() {
        StringBuilder sTagged = new StringBuilder();

        for (int i = 1; i <= this.igSentenceCount; ++i) {
            sTagged.append(this.sentence[i].getTaggedSentence());
        }

        return sTagged.toString();
    }


    /**
     * 获得经过翻译处理的段落.
     *
     * @return 经过翻译处理的段落
     */
    public String getTranslatedParagraph() {
        StringBuilder sTranslated = new StringBuilder();

        for (int i = 1; i <= this.igSentenceCount; ++i) {
            sTranslated.append(this.sentence[i].getTranslatedSentence());
        }

        return sTranslated.toString();
    }

    /**
     * 重新计算段落的情感分数.
     */
    public void recalculateParagraphSentimentScores() {
        for (int iSentence = 1; iSentence <= this.igSentenceCount; ++iSentence) {
            this.sentence[iSentence].recalculateSentenceSentimentScore();
        }

        this.calculateParagraphSentimentScores();
    }


    /**
     * 重新分类段落，以便在情感变化时进行更新.
     *
     * @param iSentimentWordID 情感词 ID
     */
    public void reClassifyClassifiedParagraphForSentimentChange(int iSentimentWordID) {
        if (this.igNegativeSentiment == 0) {
            this.calculateParagraphSentimentScores();
        } else {
            if (!this.bSentimentIDListMade) {
                this.makeSentimentIDList();
            }

            if (this.igSentimentIDListCount != 0) {
                if (Sort.iFindIntPositionInSortedArray(iSentimentWordID, this.igSentimentIDList, 1, this.igSentimentIDListCount) >= 0) {
                    for (int iSentence = 1; iSentence <= this.igSentenceCount; ++iSentence) {
                        this.sentence[iSentence].reClassifyClassifiedSentenceForSentimentChange(iSentimentWordID);
                    }

                    this.calculateParagraphSentimentScores();
                }

            }
        }
    }


    /**
     * 获得段落的积极情感分数，如果没有，则调用段落情感分数计算方法.
     *
     * @return 段落的积极情感分数
     */
    public int getParagraphPositiveSentiment() {
        if (this.igPositiveSentiment == 0) {
            this.calculateParagraphSentimentScores();
        }

        return this.igPositiveSentiment;
    }

    /**
     * 获得段落的消极情感分数，如果没有，则调用段落情感分数计算方法.
     *
     * @return 段落的消极情感分数
     */
    public int getParagraphNegativeSentiment() {
        if (this.igNegativeSentiment == 0) {
            this.calculateParagraphSentimentScores();
        }

        return this.igNegativeSentiment;
    }

    /**
     * 获得段落的三元情感分数，如果没有，则调用段落情感分数计算方法.
     *
     * @return 段落的三元情感分数
     */
    public int getParagraphTrinarySentiment() {
        if (this.igNegativeSentiment == 0) {
            this.calculateParagraphSentimentScores();
        }

        return this.igTrinarySentiment;
    }


    /**
     * 获得段落的规模情感分数（即单一正负量），如果没有，则调用段落情感分数计算方法.
     *
     * @return 段落的规模情感分数
     */
    public int getParagraphScaleSentiment() {
        if (this.igNegativeSentiment == 0) {
            this.calculateParagraphSentimentScores();
        }

        return this.igScaleSentiment;
    }

    /**
     * 判断传进来的字符是否为句子结束符号.
     *
     * @param sChar 需要判断的字符
     * @return 如果是“.","!"或者"?"则返回true,否则返回false
     */
    protected boolean isSentenceEndPunctuation(String sChar) {
        return sChar.compareTo(".") == 0 || sChar.compareTo("!") == 0 || sChar.compareTo("?") == 0;
    }


    /**
     * 判断传进来的字符是否为字母数字字符.
     *
     * @param sChar 需要判断的字符
     * @return 如果是字母数字字符，返回true,否则返回false
     */
    protected boolean isAlphanumeric(String sChar) {
        return sChar.compareToIgnoreCase("a") >= 0 && sChar.compareToIgnoreCase("z") <= 0
                || sChar.compareTo("0") >= 0 && sChar.compareTo("9") <= 0
                || sChar.compareTo("$") == 0 || sChar.compareTo("£") == 0 || sChar.compareTo("'") == 0;
    }

    /**
     * 设置默认值.
     */
    protected void setAsDefault() {
        this.igPositiveSentiment = 1;
        this.igNegativeSentiment = -1;
        this.igTrinarySentiment = 0;
        if (this.options.bgExplainClassification && this.sgClassificationRationale.length() > 0) {
            this.sgClassificationRationale = new StringBuilder();
        }
    }


    /**
     * 检查段落中是否有句子.
     *
     * @return 如果为true, 则没有, 反之则有
     */
    protected boolean isNoContent() {
        return igSentenceCount == 0;
    }

    /**
     * 计算段落的情感分数.
     * <p>
     * 遍历段落的每一个句子，并计算每一个句子的情感分数，再根据mode选择的模式通过情感分数计算段落的情感分数
     * 包括积极情感分数，消极情感分数，三元情感分数
     */
    protected void calculateParagraphSentimentScores() {
        setAsDefault();
        if (!isNoContent() && calculatePosAndNegSentimentScores()) {
            if (this.options.bgScaleMode) {
                calculateScaleScores();
            } else {
                calculateTrinaryScores();
            }
        }
    }

    /**
     * 计算积极和消极分数.
     *
     * @return 是否需要直接退出计算, true为不需要，false为需要
     */
    protected abstract boolean calculatePosAndNegSentimentScores();

    /**
     * 计算Trinary分数.
     */
    protected abstract void calculateTrinaryScores();

    /**
     * 计算规模分数.
     */
    protected void calculateScaleScores() {
        this.igScaleSentiment = this.igPositiveSentiment + this.igNegativeSentiment;
        sgClassificationRationale.append("[scale result = sum of pos and neg scores]");
    }

    /**
     * 当段落积极情感分数和消极情感分数打平时，提供一个随机的1或-1赋给this.igTrinarySentiment.
     *
     * @return 1或-1
     */
    protected int binarySelectionTieBreaker() {
        if (this.options.igDefaultBinaryClassification != 1 && this.options.igDefaultBinaryClassification != -1) {
            return this.generator.nextDouble() > DOTFIVE ? 1 : -1;
        } else {
            return this.options.igDefaultBinaryClassification != 1 && this.options.igDefaultBinaryClassification != -1 ? this.options.igDefaultBinaryClassification : this.options.igDefaultBinaryClassification;
        }
    }

}

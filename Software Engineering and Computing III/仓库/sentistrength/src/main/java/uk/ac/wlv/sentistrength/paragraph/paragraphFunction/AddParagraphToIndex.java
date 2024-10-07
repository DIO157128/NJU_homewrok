package uk.ac.wlv.sentistrength.paragraph.paragraphFunction;

import uk.ac.wlv.sentistrength.TextParsingOptions;
import uk.ac.wlv.sentistrength.UnusedTermsClassificationIndex;
import uk.ac.wlv.utilities.StringIndex;

public interface AddParagraphToIndex {
    /**
     * 将段落添加到索引中，并使用积极类和消极类值进行分类.
     *
     * @param unusedTermsClassificationIndex 未使用术语分类索引
     * @param iCorrectPosClass               正确的正类值
     * @param iEstPosClass                   预测的正类值
     * @param iCorrectNegClass               正确的负类值
     * @param iEstNegClass                   预测的负类值
     */
    void addParagraphToIndexWithPosNegValues(UnusedTermsClassificationIndex unusedTermsClassificationIndex, int iCorrectPosClass, int iEstPosClass, int iCorrectNegClass, int iEstNegClass);

    /**
     * 将段落添加到索引中，并使用Scale Values进行分类.
     *
     * @param unusedTermsClassificationIndex 未使用术语分类索引
     * @param iCorrectScaleClass             正确的Scale类值
     * @param iEstScaleClass                 预测的Scale类值
     */
    void addParagraphToIndexWithScaleValues(UnusedTermsClassificationIndex unusedTermsClassificationIndex, int iCorrectScaleClass, int iEstScaleClass);

    /**
     * 将段落添加到索引中，并使用Binary Values进行分类.
     *
     * @param unusedTermsClassificationIndex 未使用术语分类索引
     * @param iCorrectBinaryClass            正确的Binary类值
     * @param iEstBinaryClass                预测的Binary类值
     */
    void addParagraphToIndexWithBinaryValues(UnusedTermsClassificationIndex unusedTermsClassificationIndex, int iCorrectBinaryClass, int iEstBinaryClass);


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
    int addToStringIndex(StringIndex stringIndex, TextParsingOptions textParsingOptions, boolean bRecordCount, boolean bArffIndex);


    /**
     * 遍历所有句子，调用每个句子的addSentenceToIndex方法，将词语添加到未使用词语分类索引中.
     * 然后调用unusedTermsClassificationIndex的addNewIndexToMainIndexWithTrinaryValues方法，使用三元值更新索引.
     *
     * @param unusedTermsClassificationIndex 未使用词语分类索引
     * @param iCorrectTrinaryClass           正确的三元类别
     * @param iEstTrinaryClass               预测的三元类别
     */
    void addParagraphToIndexWithTrinaryValues(UnusedTermsClassificationIndex unusedTermsClassificationIndex, int iCorrectTrinaryClass, int iEstTrinaryClass);
}

package uk.ac.wlv.sentistrength.paragraph.paragraphFunction;


/**
 * 关于重新计算分数的最小接口.
 */
public interface CalculateParagraph {

    /**
     * 重新计算段落的情感分数.
     */
    void recalculateParagraphSentimentScores();


    /**
     * 重新分类段落，以便在情感变化时进行更新.
     *
     * @param iSentimentWordID 情感词 ID
     */
    void reClassifyClassifiedParagraphForSentimentChange(int iSentimentWordID);
}

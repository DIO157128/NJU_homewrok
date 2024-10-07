package uk.ac.wlv.sentistrength.paragraph.paragraphFunction;


/**
 * 关于获得分数的最小接口.
 */
public interface GetSentiment {

    /**
     * 获得段落的积极情感分数，如果没有，则调用段落情感分数计算方法.
     *
     * @return 段落的积极情感分数
     */
    int getParagraphPositiveSentiment();


    /**
     * 获得段落的消极情感分数，如果没有，则调用段落情感分数计算方法.
     *
     * @return 段落的消极情感分数
     */
    int getParagraphNegativeSentiment();


    /**
     * 获得段落的三元情感分数，如果没有，则调用段落情感分数计算方法.
     *
     * @return 段落的三元情感分数
     */
    int getParagraphTrinarySentiment();


    /**
     * 获得段落的规模情感分数（即单一正负量），如果没有，则调用段落情感分数计算方法.
     *
     * @return 段落的规模情感分数
     */
    int getParagraphScaleSentiment();
}

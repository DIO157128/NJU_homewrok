package uk.ac.wlv.sentistrength.paragraph.paragraphFunction;


/**
 * 关于SentimentIDList的最小接口.
 */
public interface SentimentIDList {

    /**
     * 创建一个情感 ID 列表，该列表包含所有句子中的情感 ID。计算出情感 ID 的总数并创建一个新的数组来存储这些 ID.
     * 然后，遍历每个句子中的情感 ID 列表，并检查是否有重复的 ID。
     * 如果没有重复，则将该 ID 添加到列表中。最后，使用快速排序对列表进行排序，
     * 并将 bSentimentIDListMade 设置为 true。
     */
    void makeSentimentIDList();

    /**
     * 获得情感ID列表，如果没有就创建一个.
     *
     * @return 情感ID列表
     */
    int[] getSentimentIDList();
}

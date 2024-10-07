package uk.ac.wlv.sentistrength.term.termFunction;


/**
 * 和term类中的Sentiment相关的最小接口.
 */
public interface TermSentimentFunc {
    /**
     * 获得实例对象term的SentimentID.
     *
     * @return SentimentID
     */
    int getSentimentID();

    /**
     * 设置OverrideSentimentScore的值.
     *
     * @param iSentiment OverrideSentimentScore需要被设置的值
     */
    void setSentimentOverrideValue(int iSentiment);

    /**
     * 获得SentimentValue的值.
     *
     * @return SentimentValue的值
     */
    int getSentimentValue();
}

package uk.ac.wlv.sentistrength.term.term;

/**
 * 表情类,Term的子类.
 */
public class Emoticon extends Term {
    private String sgEmoticon;
    int igEmoticonStrength;

    /**
     * 不带参的初始化，进行参数初始化.
     */
    public Emoticon() {
        sgEmoticon = "";
        igEmoticonStrength = 0;
    }

    /**
     * 若执行该方法，则认为代表该term的内容为表情符号并进行处理，若表情符号强度没有赋值则为其赋值.
     *
     * @param content 表情符号
     */
    @Override
    public void code(String content) {
        int iEmoticonStrength = this.resources.emoticons.getStrength(content);
        if (iEmoticonStrength != NOTPROCESSED) {
            this.sgEmoticon = content;
            this.igEmoticonStrength = iEmoticonStrength;
        }
    }

    /**
     * 表情符号不会有强调.
     *
     * @return false
     */
    @Override
    public boolean containsEmphasis() {
        return false;
    }


    /**
     * Emoticon包含的内容.
     *
     * @return Emoticon包含的内容
     */
    @Override
    public String getText() {
        return this.sgEmoticon;
    }


    /**
     * Emoticon包含的内容.
     *
     * @return Emoticon包含的内容
     */
    @Override
    public String getOriginalText() {
        return this.sgEmoticon;
    }

    /**
     * 获得表情的tag.
     *
     * @return 表情的tag
     */
    @Override
    public String getTag() {
        if (this.igEmoticonStrength == 0) {
            return "<e>" + this.sgEmoticon + "</e>";
        } else {
            if (this.igEmoticonStrength == 1) {
                return "<e em=\"+\">" + this.sgEmoticon + "</e>";
            }

            return "<e em=\"-\">" + this.sgEmoticon + "</e>";
        }
    }


    /**
     * 获得表情中的情绪强度.
     *
     * @return 表情中的情绪强度
     */
    public int getEmoticonSentimentStrength() {
        return this.igEmoticonStrength;
    }

    /**
     * 获得表情符号.
     *
     * @return 表情符号
     */
    public String getEmoticon() {
        return this.sgEmoticon;
    }


    /**
     * 获得表情符号.
     *
     * @return 表情符号
     */
    @Override
    public String getTranslation() {
        return this.sgEmoticon;
    }
}

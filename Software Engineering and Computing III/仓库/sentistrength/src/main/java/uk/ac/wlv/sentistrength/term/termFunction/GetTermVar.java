package uk.ac.wlv.sentistrength.term.termFunction;


/**
 * 获得term公共属性的最小接口.
 */
public interface GetTermVar {
    /**
     * 返回tag.
     *
     * @return 返回相应的tag
     */
    String getTag();

    /**
     * 获得代表该term实例对象的内容.
     *
     * @return 返回term的内容
     */
    String getText();

    /**
     * 获得未经过处理的源内容.
     *
     * @return 未经过处理的源内容
     */
    String getOriginalText();

    /**
     * 获得经过翻译处理后的term.
     *
     * @return 经过翻译处理后的term，可以是单词，标点，表情或者空字符串
     */
    String getTranslation();
}

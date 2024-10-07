package uk.ac.wlv.sentistrength.paragraph.paragraphFunction;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.ClassificationResources;

/**
 * 关于paragraph的set和get方法的最小接口.
 */
public interface GetAndSetParagraph {

    /**
     * 设置段落文本并进行分句。将resources和options设置为给定的值并将段落中的双引号替换为单引号.
     * 接着，计算句子结束符的数量，包括"<br>"、"."、"!"和"?"。
     * 然后创建一个新的Sentence数组，并遍历段落中的字符，根据句子结束符将段落分成多个句子。
     *
     * @param sParagraph               段落文本
     * @param classResources           先验分类资源
     * @param newClassificationOptions 分类选项
     */
    void setParagraph(String sParagraph, ClassificationResources classResources, ClassificationOptions newClassificationOptions);


    /**
     * 获得带标签的段落.
     *
     * @return 带标签的段落
     */
    String getTaggedParagraph();


    /**
     * 获得经过翻译处理的段落.
     *
     * @return 经过翻译处理的段落
     */
    String getTranslatedParagraph();
}

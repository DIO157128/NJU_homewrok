package uk.ac.wlv.sentistrength.term.termFunction;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.ClassificationResources;


/**
 * 用于分辨term的类型的函数的最小接口.
 */
public interface ExtractNextContent {
    /**
     * 为term实例对象指定先验分类资源和分类选项，并从给定的字符串中提取下一个单词、标点符号或表情符号.
     *
     * @param sWordAndPunctuation 包含单词、标点符号和表情符号的字符串
     * @param classResources      先验分类表资源，包含表情符号列表，拼写纠正列表等一系列用于分类的列表
     * @param classOptions        分类选项集合，包含了一系列指导如何分类的选项
     * @return 下一个单词、标点符号或表情符号在字符串中的位置，如果没有则返回 -1
     */
    int extractNextWordOrPunctuationOrEmoticon(String sWordAndPunctuation, ClassificationResources classResources, ClassificationOptions classOptions);
}

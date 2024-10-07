package uk.ac.wlv.sentistrength.term.termConstructor;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.ClassificationResources;
import uk.ac.wlv.sentistrength.term.term.Term;


/**
 * Director类.
 */
public class TermDirector {
    private TermBuilder builder;

    /**
     * 含参的初始化.
     *
     * @param termBuilder
     */
    public TermDirector(TermBuilder termBuilder) {
        this.builder = termBuilder;
    }

    /**
     * 用于调用builder制造term.
     *
     * @param content        内容
     * @param classResources 资源
     * @param classOptions   选项
     * @return term
     */
    public Term constructTerm(String content, ClassificationResources classResources, ClassificationOptions classOptions) {
        builder.buildResources(classResources);
        builder.buildClassificationOptions(classOptions);
        return builder.buildTerm(content);
    }

    /**
     * 设置TermBuilder.
     *
     * @param termBuilder
     */
    public void setTermBuilder(TermBuilder termBuilder) {
        this.builder = termBuilder;
    }

    /**
     * 返回TermBuilder.
     *
     * @return builder
     */
    public TermBuilder getTermBuilder() {
        return this.builder;
    }
}

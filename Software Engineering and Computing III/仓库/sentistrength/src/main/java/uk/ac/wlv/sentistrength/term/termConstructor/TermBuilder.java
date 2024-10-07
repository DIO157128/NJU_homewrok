package uk.ac.wlv.sentistrength.term.termConstructor;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.ClassificationResources;
import uk.ac.wlv.sentistrength.term.term.Term;

/**
 * termBuilder建造类接口.
 */
public interface TermBuilder {

    /**
     * 为term设置Resources.
     *
     * @param classificationResources 需要设置的classificationResources
     */
    void buildResources(ClassificationResources classificationResources);

    /**
     * 为term设置Options.
     *
     * @param options 需要设置的options
     */
    void buildClassificationOptions(ClassificationOptions options);

    /**
     * 建造Term.
     *
     * @param content 内容
     * @return 具体的Term
     */
    Term buildTerm(String content);

    /**
     * 获得pos.
     *
     * @return pos
     */
    int getPos();
}

package uk.ac.wlv.sentistrength.term.termFunction;


/**
 * term类的code函数的最小接口.
 */
public interface CodeProcess {
    /**
     * 若执行该方法，对内容进行处理.
     *
     * @param content 内容
     */
    void code(String content);
}

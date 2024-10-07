package uk.ac.wlv.sentistrength.term.termFunction;

/**
 * term基类的与强调相关的函数的最小接口.
 */
public interface TermEmphasis {

    /**
     * 判断term中是否含有强调.
     *
     * @return term有强调返回true，否则false
     */
    boolean containsEmphasis();

    /**
     * 获得强调的长度.
     *
     * @return 强调的长度
     */
    int getEmphasisLength();
}

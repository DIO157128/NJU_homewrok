package uk.ac.wlv.utilities.globalParameter.commandOptions;


import uk.ac.wlv.sentistrength.Corpus;

/**
 * 空接口，仅代表继承该接口的类是一个命令的options类.
 */
public abstract class CommandOptions {

    Corpus c;

    /**
     * 返回corpus对象.
     *
     * @return corpus
     */
    public Corpus getC() {
        return c;
    }

    /**
     * 设置corpus.
     *
     * @param c
     */
    public void setC(Corpus c) {
        this.c = c;
    }
}

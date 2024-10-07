package uk.ac.wlv.utilities.globalParameter.commandOptions;

public class TextCommandOptions extends CommandOptions {
    String sTextToParse;
    boolean bURLEncoded;

    /**
     * 有参构造函数.
     *
     * @param sTextToParse
     * @param bURLEncoded
     */
    public TextCommandOptions(String sTextToParse, boolean bURLEncoded) {
        this.sTextToParse = sTextToParse;
        this.bURLEncoded = bURLEncoded;
    }

    /**
     * 无参构造函数.
     */
    public TextCommandOptions() {
    }

    /**
     * 获取sTextToParse.
     *
     * @return sTextToParse
     */
    public String getsTextToParse() {
        return sTextToParse;
    }

    /**
     * 设置sTextToParse.
     *
     * @param sTextToParse
     */
    public void setsTextToParse(String sTextToParse) {
        this.sTextToParse = sTextToParse;
    }

    /**
     * 判断bURLEncoded.
     *
     * @return bURLEncoded
     */
    public boolean isbURLEncoded() {
        return bURLEncoded;
    }

    /**
     * 设置bURLEncoded.
     *
     * @param bURLEncoded
     */
    public void setbURLEncoded(boolean bURLEncoded) {
        this.bURLEncoded = bURLEncoded;
    }

}

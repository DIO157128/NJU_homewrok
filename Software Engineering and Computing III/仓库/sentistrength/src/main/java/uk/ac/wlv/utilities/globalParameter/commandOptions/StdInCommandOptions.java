package uk.ac.wlv.utilities.globalParameter.commandOptions;

public class StdInCommandOptions extends CommandOptions {

    int iTextCol;

    /**
     * 无参构造函数.
     */
    public StdInCommandOptions() {
    }

    /**
     * 有参构造函数.
     *
     * @param iTextCol
     */
    public StdInCommandOptions(int iTextCol) {
        this.iTextCol = iTextCol;
    }


    /**
     * 获取iTextCol.
     *
     * @return iTextCol
     */
    public int getiTextCol() {
        return iTextCol;
    }

    /**
     * 设置iTextCol.
     *
     * @param iTextCol
     */
    public void setiTextCol(int iTextCol) {
        this.iTextCol = iTextCol;
    }
}

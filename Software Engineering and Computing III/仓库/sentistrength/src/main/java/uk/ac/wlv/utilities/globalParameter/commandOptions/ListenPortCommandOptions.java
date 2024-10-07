package uk.ac.wlv.utilities.globalParameter.commandOptions;

public class ListenPortCommandOptions extends CommandOptions {

    int iListenPort;

    /**
     * 有参构造函数.
     *
     * @param iListenPort
     */
    public ListenPortCommandOptions(int iListenPort) {
        this.iListenPort = iListenPort;
    }

    /**
     * 无参构造函数.
     */
    public ListenPortCommandOptions() {
    }

    /**
     * 获得iListenPort.
     *
     * @return iListenPort
     */
    public int getiListenPort() {
        return iListenPort;
    }

    /**
     * 设置iListenPort.
     *
     * @param iListenPort iListenPort
     */
    public void setiListenPort(int iListenPort) {
        this.iListenPort = iListenPort;
    }
}

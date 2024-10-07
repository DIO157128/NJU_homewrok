package uk.ac.wlv.sentistrength.command;

import uk.ac.wlv.sentistrength.Corpus;
import uk.ac.wlv.utilities.globalParameter.commandOptions.CommandOptions;

public abstract class Command {
    /**
     * 抽象命令类.
     * 按照source of data进行具体类别的区分
     */

    protected CommandOptions cmdOpt;


    /**
     * 执行命令的行为，抽象方法，根据命令的类型自己定义行为.
     *
     * @return 是否执行成功
     */
    public abstract boolean action();

    /**
     * 设置cmdOpt.
     *
     * @param opt
     */
    public void setCommandOptions(CommandOptions opt) {
        this.cmdOpt = opt;
    }

    /**
     * 获取cmdOpt.
     *
     * @return cmdOpt
     */
    public CommandOptions getCommandOptions() {
        return this.cmdOpt;
    }

    /**
     * 获取corpus.
     *
     * @return corpus
     */
    public Corpus getC() {
        return cmdOpt.getC();
    }


}

package uk.ac.wlv.sentistrength.command.concreteCommand;

import uk.ac.wlv.sentistrength.command.Command;
import uk.ac.wlv.sentistrength.command.concreteCommand.inputStrategy.*;
import uk.ac.wlv.utilities.globalParameter.commandOptions.CommandOptions;
import uk.ac.wlv.utilities.globalParameter.commandOptions.InputCommandOptions;

import java.util.Objects;

public class InputCommand extends Command {

    private InputStrategy inputStrategy;

    /**
     * 带参的初始化函数.
     *
     * @param opt
     */
    public InputCommand(CommandOptions opt) {
        setCommandOptions(opt);
    }

    /**
     * 设置处理input file的策略.
     *
     * @param inputStrategy
     */
    public void setInputStrategy(InputStrategy inputStrategy) {
        this.inputStrategy = inputStrategy;
    }

    /**
     * 获取处理input file的策略.
     *
     * @return input file的策略
     */
    public InputStrategy getInputStrategy() {
        return inputStrategy;
    }

    /**
     * 不带参的初始化函数.
     */
    public InputCommand() {
    }

    /**
     * 根据不同策略进行不同的输出动作.
     *
     * @return 是否成功
     */
    @Override
    public boolean action() {
        InputCommandOptions inputCommandOptions = (InputCommandOptions) this.getCommandOptions();
        if (!Objects.equals(inputCommandOptions.getsOptimalTermStrengths(), "")) {
            setInputStrategy(new OptimalStrategy());
        } else if (inputCommandOptions.isbReportNewTermWeightsForBadClassifications()) {
            setInputStrategy(new TermWeightStrategy());
        } else if (inputCommandOptions.getiTextCol() > 0 && inputCommandOptions.getiIdCol() > 0) {
            setInputStrategy(new SaveWithIDStrategy());
        } else if (inputCommandOptions.getiTextColForAnnotation() > 0) {
            setInputStrategy(new AnnotationColStrategy());
        } else {
            setInputStrategy(new ClassifyAllLineStrategy());
        }
        return this.getInputStrategy().calculate(inputCommandOptions);
    }

}

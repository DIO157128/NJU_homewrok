package uk.ac.wlv.sentistrength.command.concreteCommand.inputStrategy;

import uk.ac.wlv.sentistrength.Corpus;
import uk.ac.wlv.utilities.globalParameter.commandOptions.InputCommandOptions;


public interface InputStrategy {
    /**
     * InputFileCommand类处理的策略，分为OptimalStrategy，TermWeightStrategy，SaveWithIDStrategy，AnnotationColStrategy，classifyAllLineStrategy.
     * @param opt
     * @return 是否成功
     */
    boolean calculate(InputCommandOptions opt);

    /**
     * showBriefHelp方法用于提示使用方法，多用于输入命令不符合规范的情况.
     * @param c
     */
    default void showBriefHelp(Corpus c) {
        System.out.println();
        System.out.println("====" + c.options.sgProgramName + "Brief Help====");
        System.out.println("For most operations, a minimum of two parameters must be set");
        System.out.println("1) folder location for the linguistic files");
        System.out.println("   e.g., on Windows: C:/mike/Lexical_Data/");
        System.out.println("   e.g., on Mac/Linux/Unix: /usr/Lexical_Data/");
        if (c.options.bgTensiStrength) {
            System.out.println("TensiiStrength_Data can be downloaded from...[not completed yet]");
        } else {
            System.out.println("SentiStrength_Data can be downloaded with the Windows version of SentiStrength from sentistrength.wlv.ac.uk");
        }

        System.out.println();
        System.out.println("2) text to be classified or file name of texts to be classified");
        System.out.println("   e.g., To classify one text: text love+u");
        System.out.println("   e.g., To classify a file of texts: input /bob/data.txt");
        System.out.println();
        System.out.println("Here is an example complete command:");
        if (c.options.bgTensiStrength) {
            System.out.println("java -jar TensiStrength.jar sentidata C:/a/Stress_Data/ text am+stressed");
        } else {
            System.out.println("java -jar SentiStrength.jar sentidata C:/a/SentStrength_Data/ text love+u");
        }

        System.out.println();
        if (!c.options.bgTensiStrength) {
            System.out.println("To list all commands: java -jar SentiStrength.jar help");
        }
    }

}

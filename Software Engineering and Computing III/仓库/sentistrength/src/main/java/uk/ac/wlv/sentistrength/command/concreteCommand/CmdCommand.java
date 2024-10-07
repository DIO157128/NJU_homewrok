package uk.ac.wlv.sentistrength.command.concreteCommand;

import uk.ac.wlv.sentistrength.paragraph.paragraph.Paragraph;
import uk.ac.wlv.sentistrength.command.Command;
import uk.ac.wlv.sentistrength.paragraph.paragraphFactory.ParagraphFactory;
import uk.ac.wlv.utilities.globalParameter.commandOptions.CmdCommandOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CmdCommand extends Command {
    /**
     * 带参的初始化函数.
     *
     * @param opt
     */
    public CmdCommand(CmdCommandOptions opt) {
        setCommandOptions(opt);
    }

    /**
     * 不带参的初始化函数.
     */
    public CmdCommand() {
    }

    /**
     * action函数用于处理命令行输入命令.
     *
     * @return 是否处理成功
     */
    @Override
    public boolean action() {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        CmdCommandOptions cmdCmdopt = (CmdCommandOptions) cmdOpt;

        while (true) {
            while (true) {
                try {
                    while (true) {
                        String sTextToParse = stdin.readLine();
                        if (sTextToParse.equalsIgnoreCase("@end")) {
                            return false;
                        }

                        int iPos;
                        int iNeg;
                        int iTrinary;
                        int iScale;
                        Paragraph paragraph = new ParagraphFactory().createParagraph(cmdCmdopt.getC().options.igEmotionParagraphCombineMethod);
                        paragraph.setParagraph(sTextToParse, cmdCmdopt.getC().resources, cmdCmdopt.getC().options);
                        iNeg = paragraph.getParagraphNegativeSentiment();
                        iPos = paragraph.getParagraphPositiveSentiment();
                        iTrinary = paragraph.getParagraphTrinarySentiment();
                        iScale = paragraph.getParagraphScaleSentiment();
                        String sRationale = "";
                        if (cmdCmdopt.getC().options.bgEchoText) {
                            sRationale = " " + sTextToParse;
                        }

                        if (cmdCmdopt.getC().options.bgExplainClassification) {
                            sRationale = " " + paragraph.getClassificationRationale();
                        }

                        String sOutput;
                        if (cmdCmdopt.getC().options.bgTrinaryMode) {
                            sOutput = iPos + " " + iNeg + " " + iTrinary + sRationale;
                        } else if (cmdCmdopt.getC().options.bgScaleMode) {
                            sOutput = iPos + " " + iNeg + " " + iScale + sRationale;
                        } else {
                            sOutput = iPos + " " + iNeg + sRationale;
                        }

                        if (!cmdCmdopt.getC().options.bgForceUTF8) {
                            System.out.println(sOutput);
                        } else {
                            System.out.println(new String(sOutput.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
                        }
                    }
                } catch (IOException var13) {
                    System.out.println(var13);
                }
                return false;
            }
        }
    }
}

package uk.ac.wlv.sentistrength.command.concreteCommand;

import uk.ac.wlv.sentistrength.paragraph.paragraph.Paragraph;
import uk.ac.wlv.sentistrength.command.Command;
import uk.ac.wlv.sentistrength.paragraph.paragraphFactory.ParagraphFactory;
import uk.ac.wlv.utilities.globalParameter.commandOptions.CommandOptions;
import uk.ac.wlv.utilities.globalParameter.commandOptions.StdInCommandOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class StdInCommand extends Command {

    /**
     * 带参的初始化函数.
     *
     * @param opt
     */
    public StdInCommand(CommandOptions opt) {
        setCommandOptions(opt);
    }

    /**
     * 不带参的初始化函数.
     */
    public StdInCommand() {
    }

    /**
     * action函数用于处理输入流stdin命令.
     *
     * @return 是否处理成功
     */
    @Override
    public boolean action() {
        StdInCommandOptions stdinCmdopt = (StdInCommandOptions) cmdOpt;
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        String sTextToParse;
        try {
            while ((sTextToParse = stdin.readLine()) != null) {
                boolean bSuccess;
                if (sTextToParse.contains("#Change_TermWeight")) {
                    String[] sData = sTextToParse.split("\t");
                    bSuccess = stdinCmdopt.getC().resources.sentimentWords.setSentiment(sData[1], Integer.parseInt(sData[2]));
                    if (bSuccess) {
                        System.out.println("1");
                    } else {
                        System.out.println("0");
                    }
                } else {
                    int iPos;
                    int iTrinary;
                    int iScale;
                    Paragraph paragraph = new ParagraphFactory().createParagraph(stdinCmdopt.getC().options.igEmotionParagraphCombineMethod);
                    if (stdinCmdopt.getiTextCol() > -1) {
                        String[] sData = sTextToParse.split("\t");
                        if (sData.length >= stdinCmdopt.getiTextCol()) {
                            paragraph.setParagraph(sData[stdinCmdopt.getiTextCol()], stdinCmdopt.getC().resources, stdinCmdopt.getC().options);
                        }
                    } else {
                        paragraph.setParagraph(sTextToParse, stdinCmdopt.getC().resources, stdinCmdopt.getC().options);
                    }

                    int iNeg = paragraph.getParagraphNegativeSentiment();
                    iPos = paragraph.getParagraphPositiveSentiment();
                    iTrinary = paragraph.getParagraphTrinarySentiment();
                    iScale = paragraph.getParagraphScaleSentiment();
                    String sRationale = "";
                    String sOutput;
                    if (stdinCmdopt.getC().options.bgEchoText) {
                        sOutput = sTextToParse + "\t";
                    } else {
                        sOutput = "";
                    }

                    if (stdinCmdopt.getC().options.bgExplainClassification) {
                        sRationale = paragraph.getClassificationRationale();
                    }

                    if (stdinCmdopt.getC().options.bgTrinaryMode) {
                        sOutput = sOutput + iPos + "\t" + iNeg + "\t" + iTrinary + "\t" + sRationale;
                    } else if (stdinCmdopt.getC().options.bgScaleMode) {
                        sOutput = sOutput + iPos + "\t" + iNeg + "\t" + iScale + "\t" + sRationale;
                    } else {
                        sOutput = sOutput + iPos + "\t" + iNeg + "\t" + sRationale;
                    }

                    if (stdinCmdopt.getC().options.bgForceUTF8) {
                        System.out.println(new String(sOutput.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
                    } else {
                        System.out.println(sOutput);
                    }
                }
                return true;
            }
        } catch (IOException var14) {
            System.out.println("Error reading input");
            var14.printStackTrace();
        }
        return false;
    }

}

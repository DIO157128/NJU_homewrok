package uk.ac.wlv.sentistrength.command.concreteCommand;

import uk.ac.wlv.sentistrength.paragraph.paragraph.Paragraph;
import uk.ac.wlv.sentistrength.command.Command;
import uk.ac.wlv.sentistrength.paragraph.paragraphFactory.ParagraphFactory;
import uk.ac.wlv.utilities.globalParameter.commandOptions.CommandOptions;
import uk.ac.wlv.utilities.globalParameter.commandOptions.TextCommandOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TextCommand extends Command {
    /**
     * 带参的初始化函数.
     *
     * @param opt
     */
    public TextCommand(CommandOptions opt) {
        setCommandOptions(opt);
    }

    /**
     * 不带参的初始化函数.
     */
    public TextCommand() {
    }

    /**
     * action函数用于处理text命令.
     *
     * @return 是否处理成功
     */
    @Override
    public boolean action() {
        TextCommandOptions textCmdopt = (TextCommandOptions) cmdOpt;
        String sTextToParse = textCmdopt.getsTextToParse();
        if (textCmdopt.isbURLEncoded()) {
            try {
                sTextToParse = URLDecoder.decode(sTextToParse, "UTF-8");
            } catch (UnsupportedEncodingException var31) {
                var31.printStackTrace();
            }
        } else {
            sTextToParse = sTextToParse.replace("+", " ");
        }
        int iPos;
        int iNeg;
        int iTrinary;
        int iScale;
        // Paragraph paragraph = new uk.ac.wlv.sentistrength.Paragraph();
        Paragraph paragraph = new ParagraphFactory().createParagraph(textCmdopt.getC().options.igEmotionParagraphCombineMethod);
        paragraph.setParagraph(sTextToParse, textCmdopt.getC().resources, textCmdopt.getC().options);
        iNeg = paragraph.getParagraphNegativeSentiment();
        iPos = paragraph.getParagraphPositiveSentiment();
        iTrinary = paragraph.getParagraphTrinarySentiment();
        iScale = paragraph.getParagraphScaleSentiment();
        String sRationale = "";
        if (textCmdopt.getC().options.bgEchoText) {
            sRationale = " " + sTextToParse;
        }

        if (textCmdopt.getC().options.bgExplainClassification) {
            sRationale = " " + paragraph.getClassificationRationale();
        }

        String sOutput;
        if (textCmdopt.getC().options.bgTrinaryMode) {
            sOutput = iPos + " " + iNeg + " " + iTrinary + sRationale;
        } else if (textCmdopt.getC().options.bgScaleMode) {
            sOutput = iPos + " " + iNeg + " " + iScale + sRationale;
        } else {
            sOutput = iPos + " " + iNeg + sRationale;
        }

        if (textCmdopt.isbURLEncoded()) {
            try {
                System.out.println(URLEncoder.encode(sOutput, "UTF-8"));
                return true;
            } catch (UnsupportedEncodingException var13) {
                var13.printStackTrace();
            }
        } else if (textCmdopt.getC().options.bgForceUTF8) {
            System.out.println(new String(sOutput.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            return true;
        } else {
            System.out.println(sOutput);
            return true;
        }
        return false;
    }
}

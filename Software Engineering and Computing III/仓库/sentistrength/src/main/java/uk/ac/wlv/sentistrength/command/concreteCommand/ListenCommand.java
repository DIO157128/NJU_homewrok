package uk.ac.wlv.sentistrength.command.concreteCommand;

import uk.ac.wlv.sentistrength.paragraph.paragraph.Paragraph;
import uk.ac.wlv.sentistrength.command.Command;
import uk.ac.wlv.sentistrength.paragraph.paragraphFactory.ParagraphFactory;
import uk.ac.wlv.utilities.globalParameter.commandOptions.ListenPortCommandOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class ListenCommand extends Command {
    private static final int FIVE = 5;

    /**
     * 带参的初始化函数.
     *
     * @param opt
     */
    public ListenCommand(ListenPortCommandOptions opt) {
        setCommandOptions(opt);
    }

    /**
     * 不带参的初始化函数.
     */
    public ListenCommand() {
    }

    /**
     * action函数用于处理端口输入命令.
     *
     * @return 是否处理成功
     */
    @Override
    public boolean action() {
        ListenPortCommandOptions listenCmdopt = (ListenPortCommandOptions) cmdOpt;
        ServerSocket serverSocket;
        String decodedText = "";

        try {
            serverSocket = new ServerSocket(listenCmdopt.getiListenPort());
        } catch (IOException var23) {
            System.out.println("Could not listen on port " + listenCmdopt.getiListenPort() + " because\n" + var23.getMessage());
            return false;
        }

        System.out.println("Listening on port: " + listenCmdopt.getiListenPort() + " IP: " + serverSocket.getInetAddress());

        while (true) {
            Socket clientSocket;

            try {
                clientSocket = serverSocket.accept();
            } catch (IOException var20) {
                System.out.println("Accept failed at port: " + listenCmdopt.getiListenPort());
                return false;
            }

            PrintWriter out;
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException var19) {
                System.out.println("IOException clientSocket.getOutputStream " + var19.getMessage());
                var19.printStackTrace();
                return false;
            }

            BufferedReader in;
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException var18) {
                System.out.println("IOException InputStreamReader " + var18.getMessage());
                var18.printStackTrace();
                return false;
            }

            String inputLine;
            try {
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.indexOf("GET /") == 0) {
                        int lastSpacePos = inputLine.lastIndexOf(" ");
                        if (lastSpacePos < FIVE) {
                            lastSpacePos = inputLine.length();
                        }

                        decodedText = URLDecoder.decode(inputLine.substring(FIVE, lastSpacePos), "UTF-8");
                        System.out.println("Analysis of text: " + decodedText);
                        break;
                    }

                    if (inputLine.equals("MikeSpecialMessageToEnd.")) {
                        break;
                    }
                }
            } catch (IOException var24) {
                System.out.println("IOException " + var24.getMessage());
                var24.printStackTrace();
                return false;
            } catch (Exception var25) {
                System.out.println("Non-IOException " + var25.getMessage());
                return false;
            }

            int iPos;
            int iNeg;
            int iTrinary;
            int iScale;
            Paragraph paragraph = new ParagraphFactory().createParagraph(listenCmdopt.getC().options.igEmotionParagraphCombineMethod);
            paragraph.setParagraph(decodedText, listenCmdopt.getC().resources, listenCmdopt.getC().options);
            iNeg = paragraph.getParagraphNegativeSentiment();
            iPos = paragraph.getParagraphPositiveSentiment();
            iTrinary = paragraph.getParagraphTrinarySentiment();
            iScale = paragraph.getParagraphScaleSentiment();
            String sRationale = "";
            if (listenCmdopt.getC().options.bgEchoText) {
                sRationale = " " + decodedText;
            }

            if (listenCmdopt.getC().options.bgExplainClassification) {
                sRationale = " " + paragraph.getClassificationRationale();
            }

            String sOutput;
            if (listenCmdopt.getC().options.bgTrinaryMode) {
                sOutput = iPos + " " + iNeg + " " + iTrinary + sRationale;
            } else if (listenCmdopt.getC().options.bgScaleMode) {
                sOutput = iPos + " " + iNeg + " " + iScale + sRationale;
            } else {
                sOutput = iPos + " " + iNeg + sRationale;
            }

            if (listenCmdopt.getC().options.bgForceUTF8) {
                out.print(new String(sOutput.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            } else {
                out.print(sOutput);
            }

            try {
                out.close();
                in.close();
                clientSocket.close();
                return true;
            } catch (IOException var21) {
                System.out.println("IOException closing streams or sockets" + var21.getMessage());
                var21.printStackTrace();
                return false;
            }
        }
    }
}

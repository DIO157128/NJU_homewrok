// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   SentiStrengthTestAppletOld.java

package uk.ac.wlv.utilities;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Referenced classes of package uk.ac.wlv.utilities:
//            SentiStrengthOld

public class SentiStrengthTestAppletOld extends Applet implements ActionListener {
    private static final long serialVersionUID = 0x858280b1L;
    Font fntTimesNewRoman;
    String sgEnteredText;
    TextField tfField;
    String sgWordList[];
    SentiStrengthOld ss;
    boolean bgSentiStrengthOK;
    private static final int FONT_SIZE = 36;
    private static final int FIELD_COLUMN = 12;
    private static final int TWENTY_FIVE = 25;
    private static final int HUNDRED = 100;
    private static final int TEN = 10;

    /**
     * 构造函数.
     */
    public SentiStrengthTestAppletOld() {
        fntTimesNewRoman = new Font("TimesRoman", 1, FONT_SIZE);
        sgEnteredText = "";
        tfField = new TextField(FIELD_COLUMN);
        bgSentiStrengthOK = false;
    }

    /**
     * 初始化.
     */
    public void init() {
        ss = new SentiStrengthOld();
        bgSentiStrengthOK = ss.initialise();
        setBackground(Color.lightGray);
        tfField.addActionListener(this);
        add(tfField);
    }

    /**
     * 画图(设定字体、字体大小、窗口尺寸等擦书).
     *
     * @param g
     */
    public void paint(Graphics g) {
        g.setFont(fntTimesNewRoman);
        if (bgSentiStrengthOK) {
            g.drawString("sentiStrength successfully initialised", HUNDRED, HUNDRED - TWENTY_FIVE);
        } else {
            g.drawString("Error - can't initalise sentiStrength", HUNDRED, HUNDRED - TWENTY_FIVE);
            g.drawString(ss.getErrorLog(), HUNDRED, HUNDRED + TWENTY_FIVE);
        }
        if (sgEnteredText != "") {
            if (sgEnteredText.indexOf("\\") >= 0) {
                if (ss.classifyAllTextInFile(sgEnteredText, (new StringBuilder(String.valueOf(sgEnteredText))).append("_output.txt").toString())) {
                    g.drawString("No problem with text file classification", TEN, HUNDRED + HUNDRED + HUNDRED - TWENTY_FIVE);
                } else {
                    g.drawString("Text file classification failed", TEN, HUNDRED + HUNDRED + HUNDRED - TWENTY_FIVE);
                }
            } else {
                ss.detectEmotionInText(sgEnteredText);
                g.drawString(ss.getOriginalText(), TEN, HUNDRED + HUNDRED + TWENTY_FIVE);
                g.drawString("was tagged as:", HUNDRED, HUNDRED + HUNDRED + HUNDRED - TWENTY_FIVE);
                g.drawString(ss.getTaggedText(), TEN, HUNDRED + HUNDRED + HUNDRED + TWENTY_FIVE);
                g.drawString((new StringBuilder("Positive sentiment of text: ")).append(ss.getPositiveClassification()).append(", negative: ").append(ss.getNegativeClassification()).toString(), TEN, HUNDRED + HUNDRED + HUNDRED + HUNDRED - TWENTY_FIVE);
            }
        }
    }

    /**
     * 得到文字后显示出来.
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        sgEnteredText = tfField.getText();
        repaint();
    }
}

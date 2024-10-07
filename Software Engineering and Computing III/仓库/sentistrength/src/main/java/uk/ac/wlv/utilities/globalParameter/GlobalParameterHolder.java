package uk.ac.wlv.utilities.globalParameter;

import uk.ac.wlv.sentistrength.Corpus;
import uk.ac.wlv.utilities.globalParameter.commandOptions.*;

public class GlobalParameterHolder {
    Corpus c;

    String sInputFile;
    String sInputFolder;
    String sTextToParse;
    String sOptimalTermStrengths;
    String sFileSubString;
    String sResultsFolder;
    String sResultsFileExtension;
    int iIterations;
    int iMinImprovement;
    int iMultiOptimisations;
    int iListenPort;
    int iTextColForAnnotation;
    int iIdCol;
    int iTextCol;
    boolean bDoAll;
    boolean bOkToOverwrite;
    boolean bTrain;
    boolean bReportNewTermWeightsForBadClassifications;
    boolean bStdIn;
    boolean bCmd;
    boolean bUseTotalDifference;
    boolean bURLEncoded;
    String sLanguage;


    /**
     * 带参构造函数.
     *
     * @param c
     */
    public GlobalParameterHolder(Corpus c) {
        this.c = c;
        sInputFile = "";
        sInputFolder = "";
        sTextToParse = "";
        sOptimalTermStrengths = "";
        sFileSubString = "\t";
        sResultsFolder = "";
        sResultsFileExtension = "_out.txt";
        iIterations = 1;
        iMinImprovement = 2;
        iMultiOptimisations = 1;
        iListenPort = 0;
        iTextColForAnnotation = -1;
        iIdCol = -1;
        iTextCol = -1;
        bDoAll = false;
        bOkToOverwrite = false;
        bTrain = false;
        bReportNewTermWeightsForBadClassifications = false;
        bStdIn = false;
        bCmd = false;
        bUseTotalDifference = true;
        bURLEncoded = false;
        sLanguage = "";
    }


    /**
     * 生成InputCommandOptions.
     *
     * @return inputCommandOptions
     */
    public InputCommandOptions createInputCommandOptions() {
        InputCommandOptions inputCommandOptions = new InputCommandOptions(sInputFile, sInputFolder, sFileSubString, iTextCol, iIdCol, iTextColForAnnotation, bOkToOverwrite, sResultsFileExtension, sResultsFolder);
        inputCommandOptions.setC(this.c);
        if (bTrain || !sOptimalTermStrengths.equals("")) {
            MachineLearningOptions machineLearningOptions = new MachineLearningOptions(sOptimalTermStrengths, bDoAll, iMinImprovement, bUseTotalDifference, iIterations, iMultiOptimisations, bReportNewTermWeightsForBadClassifications);
            inputCommandOptions.setMlOpt(machineLearningOptions);
        }
        return inputCommandOptions;
    }

    /**
     * 生成CmdCommandOptions.
     *
     * @return cmdCommandOptions
     */
    public CmdCommandOptions createCmdCommandOptions() {
        CmdCommandOptions cmdCommandOptions = new CmdCommandOptions();
        cmdCommandOptions.setC(this.c);
        return cmdCommandOptions;
    }

    /**
     * 生成ListenPortCommandOptions.
     *
     * @return listenPortCommandOptions
     */
    public ListenPortCommandOptions createListenPortCommandOptions() {
        ListenPortCommandOptions listenPortCommandOptions = new ListenPortCommandOptions(iListenPort);
        listenPortCommandOptions.setC(this.c);
        return listenPortCommandOptions;
    }

    /**
     * 生成StdInCommandOptions.
     *
     * @return stdInCommandOptions
     */
    public StdInCommandOptions createStdInCommandOptions() {
        StdInCommandOptions stdInCommandOptions = new StdInCommandOptions(iTextCol);
        stdInCommandOptions.setC(this.c);
        return stdInCommandOptions;
    }

    /**
     * 生成TextCommandOptions.
     *
     * @return textCommandOptions
     */
    public TextCommandOptions createTextCommandOptions() {
        TextCommandOptions textCommandOptions = new TextCommandOptions(sTextToParse, bURLEncoded);
        textCommandOptions.setC(this.c);
        return textCommandOptions;
    }

    /**
     * 获得sInputFile.
     *
     * @return sInputFile
     */
    public String getsInputFile() {
        return sInputFile;
    }

    /**
     * 获得sInputFolder.
     *
     * @return sInputFolder
     */
    public String getsInputFolder() {
        return sInputFolder;
    }

    /**
     * 获得sTextToParse.
     *
     * @return sTextToParse
     */
    public String getsTextToParse() {
        return sTextToParse;
    }

    /**
     * 获得sOptimalTermStrengths.
     *
     * @return sOptimalTermStrengths
     */
    public String getsOptimalTermStrengths() {
        return sOptimalTermStrengths;
    }

    /**
     * 获得sFileSubString.
     *
     * @return sFileSubString
     */
    public String getsFileSubString() {
        return sFileSubString;
    }

    /**
     * 获得sResultsFolder.
     *
     * @return sResultsFolder
     */
    public String getsResultsFolder() {
        return sResultsFolder;
    }

    /**
     * 获得sResultsFileExtension.
     *
     * @return sResultsFileExtension
     */
    public String getsResultsFileExtension() {
        return sResultsFileExtension;
    }

    /**
     * 获得iIterations.
     *
     * @return iIterations
     */
    public int getiIterations() {
        return iIterations;
    }

    /**
     * 获得iMinImprovement.
     *
     * @return iMinImprovement
     */
    public int getiMinImprovement() {
        return iMinImprovement;
    }

    /**
     * 获得iMultiOptimisations.
     *
     * @return iMultiOptimisations
     */
    public int getiMultiOptimisations() {
        return iMultiOptimisations;
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
     * 获得iTextColForAnnotation.
     *
     * @return iTextColForAnnotation
     */
    public int getiTextColForAnnotation() {
        return iTextColForAnnotation;
    }

    /**
     * 获得iIdCol.
     *
     * @return iIdCol
     */
    public int getiIdCol() {
        return iIdCol;
    }

    /**
     * 获得iTextCol.
     *
     * @return iTextCol
     */
    public int getiTextCol() {
        return iTextCol;
    }

    /**
     * 获得sLanguage.
     *
     * @return sLanguage
     */
    public String getsLanguage() {
        return sLanguage;
    }


    /**
     * 判断bDoAll.
     * bDoAll
     *
     * @return bDoAll
     */
    public boolean isbDoAll() {
        return bDoAll;
    }

    /**
     * 判断bOkToOverwrite.
     *
     * @return bOkToOverwrite
     */
    public boolean isbOkToOverwrite() {
        return bOkToOverwrite;
    }

    /**
     * 判断bTrain.
     *
     * @return bTrain
     */
    public boolean isbTrain() {
        return bTrain;
    }


    /**
     * 判断bReportNewTermWeightsForBadClassifications.
     *
     * @return bReportNewTermWeightsForBadClassifications
     */
    public boolean isbReportNewTermWeightsForBadClassifications() {
        return bReportNewTermWeightsForBadClassifications;
    }

    /**
     * 判断bStdIn.
     *
     * @return bStdIn
     */
    public boolean isbStdIn() {
        return bStdIn;
    }

    /**
     * 判断bCmd.
     *
     * @return bCmd
     */
    public boolean isbCmd() {
        return bCmd;
    }

    /**
     * 判断bUseTotalDifference.
     *
     * @return bUseTotalDifference
     */
    public boolean isbUseTotalDifference() {
        return bUseTotalDifference;
    }

    /**
     * 判断bURLEncoded.
     *
     * @return bURLEncoded
     */
    public boolean isbURLEncoded() {
        return bURLEncoded;
    }


    /**
     * 设置c.
     *
     * @param c c
     */
    public void setC(Corpus c) {
        this.c = c;
    }

    /**
     * 设置sInputFile.
     *
     * @param sInputFile sInputFile
     */
    public void setsInputFile(String sInputFile) {
        this.sInputFile = sInputFile;
    }

    /**
     * 设置sInputFolder.
     *
     * @param sInputFolder sInputFolder
     */
    public void setsInputFolder(String sInputFolder) {
        this.sInputFolder = sInputFolder;
    }

    /**
     * 设置sTextToParse.
     *
     * @param sTextToParse sTextToParse
     */
    public void setsTextToParse(String sTextToParse) {
        this.sTextToParse = sTextToParse;
    }

    /**
     * 设置sOptimalTermStrengths.
     *
     * @param sOptimalTermStrengths sOptimalTermStrengths
     */
    public void setsOptimalTermStrengths(String sOptimalTermStrengths) {
        this.sOptimalTermStrengths = sOptimalTermStrengths;
    }

    /**
     * 设置sFileSubString.
     *
     * @param sFileSubString sFileSubString
     */
    public void setsFileSubString(String sFileSubString) {
        this.sFileSubString = sFileSubString;
    }

    /**
     * 设置sResultsFolder.
     *
     * @param sResultsFolder sResultsFolder
     */
    public void setsResultsFolder(String sResultsFolder) {
        this.sResultsFolder = sResultsFolder;
    }

    /**
     * 设置sResultsFileExtension.
     *
     * @param sResultsFileExtension sResultsFileExtension
     */
    public void setsResultsFileExtension(String sResultsFileExtension) {
        this.sResultsFileExtension = sResultsFileExtension;
    }

    /**
     * 设置iIterations.
     *
     * @param iIterations iIterations
     */
    public void setiIterations(int iIterations) {
        this.iIterations = iIterations;
    }

    /**
     * 设置iMinImprovement.
     *
     * @param iMinImprovement iMinImprovement
     */
    public void setiMinImprovement(int iMinImprovement) {
        this.iMinImprovement = iMinImprovement;
    }

    /**
     * 设置iMultiOptimisations.
     *
     * @param iMultiOptimisations iMultiOptimisations
     */
    public void setiMultiOptimisations(int iMultiOptimisations) {
        this.iMultiOptimisations = iMultiOptimisations;
    }

    /**
     * 设置iListenPort.
     *
     * @param iListenPort iListenPort
     */
    public void setiListenPort(int iListenPort) {
        this.iListenPort = iListenPort;
    }

    /**
     * 设置iTextColForAnnotation.
     *
     * @param iTextColForAnnotation iTextColForAnnotation
     */
    public void setiTextColForAnnotation(int iTextColForAnnotation) {
        this.iTextColForAnnotation = iTextColForAnnotation;
    }

    /**
     * 设置iIdCol.
     *
     * @param iIdCol iIdCol
     */
    public void setiIdCol(int iIdCol) {
        this.iIdCol = iIdCol;
    }

    /**
     * 设置iTextCol.
     *
     * @param iTextCol iTextCol
     */
    public void setiTextCol(int iTextCol) {
        this.iTextCol = iTextCol;
    }

    /**
     * 设置bDoAll.
     *
     * @param bDoAll bDoAll
     */
    public void setbDoAll(boolean bDoAll) {
        this.bDoAll = bDoAll;
    }

    /**
     * 设置bOkToOverwrite.
     *
     * @param bOkToOverwrite bOkToOverwrite
     */
    public void setbOkToOverwrite(boolean bOkToOverwrite) {
        this.bOkToOverwrite = bOkToOverwrite;
    }

    /**
     * 设置bTrain.
     *
     * @param bTrain bTrain
     */
    public void setbTrain(boolean bTrain) {
        this.bTrain = bTrain;
    }

    /**
     * 设置bReportNewTermWeightsForBadClassifications.
     *
     * @param bReportNewTermWeightsForBadClassifications bReportNewTermWeightsForBadClassifications
     */
    public void setbReportNewTermWeightsForBadClassifications(boolean bReportNewTermWeightsForBadClassifications) {
        this.bReportNewTermWeightsForBadClassifications = bReportNewTermWeightsForBadClassifications;
    }

    /**
     * 设置bStdIn.
     *
     * @param bStdIn bStdIn
     */
    public void setbStdIn(boolean bStdIn) {
        this.bStdIn = bStdIn;
    }

    /**
     * 设置bCmd.
     *
     * @param bCmd bCmd
     */
    public void setbCmd(boolean bCmd) {
        this.bCmd = bCmd;
    }

    /**
     * 设置bUseTotalDifference.
     *
     * @param bUseTotalDifference bUseTotalDifference
     */
    public void setbUseTotalDifference(boolean bUseTotalDifference) {
        this.bUseTotalDifference = bUseTotalDifference;
    }

    /**
     * 设置bURLEncoded.
     *
     * @param bURLEncoded bURLEncoded
     */
    public void setbURLEncoded(boolean bURLEncoded) {
        this.bURLEncoded = bURLEncoded;
    }

    /**
     * 设置sLanguage.
     *
     * @param sLanguage sLanguage
     */
    public void setsLanguage(String sLanguage) {
        this.sLanguage = sLanguage;
    }
}

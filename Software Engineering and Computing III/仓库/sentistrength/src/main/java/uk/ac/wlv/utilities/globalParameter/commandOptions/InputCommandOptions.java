package uk.ac.wlv.utilities.globalParameter.commandOptions;

public class InputCommandOptions extends CommandOptions {
    String sInputFile;
    String sInputFolder;
    String sFileSubString;
    int iTextCol;
    int iIdCol;
    int iTextColForAnnotation;
    boolean bOkToOverwrite;


    String sResultsFileExtension;
    String sResultsFolder;

    MachineLearningOptions mlOpt = null;

    /**
     * 带参数的初始化.
     *
     * @param sInputFile
     * @param sInputFolder
     * @param sFileSubString
     * @param iTextCol
     * @param iIdCol
     * @param iTextColForAnnotation
     * @param bOkToOverwrite
     * @param sResultsFileExtension
     * @param sResultsFolder
     */
    public InputCommandOptions(String sInputFile, String sInputFolder, String sFileSubString, int iTextCol, int iIdCol, int iTextColForAnnotation, boolean bOkToOverwrite, String sResultsFileExtension, String sResultsFolder) {
        this.sInputFile = sInputFile;
        this.sInputFolder = sInputFolder;
        this.sFileSubString = sFileSubString;
        this.iTextCol = iTextCol;
        this.iIdCol = iIdCol;
        this.iTextColForAnnotation = iTextColForAnnotation;
        this.bOkToOverwrite = bOkToOverwrite;
        this.sResultsFileExtension = sResultsFileExtension;
        this.sResultsFolder = sResultsFolder;
    }

    /**
     * 不带参的初始化.
     */
    public InputCommandOptions() {
        this.mlOpt = null;
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
     * 获得sFileSubString.
     *
     * @return sFileSubString
     */
    public String getsFileSubString() {
        return sFileSubString;
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
     * 获得iIdCol.
     *
     * @return iIdCol
     */
    public int getiIdCol() {
        return iIdCol;
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
     * 获得sResultsFileExtension.
     *
     * @return sResultsFileExtension
     */
    public String getsResultsFileExtension() {
        return sResultsFileExtension;
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
     * 获得mlOpt.
     *
     * @return mlOpt
     */
    public MachineLearningOptions getMlOpt() {
        return mlOpt;
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
     * 设置sFileSubString.
     *
     * @param sFileSubString sFileSubString
     */
    public void setsFileSubString(String sFileSubString) {
        this.sFileSubString = sFileSubString;
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
     * 设置iIdCol.
     *
     * @param iIdCol iIdCol
     */
    public void setiIdCol(int iIdCol) {
        this.iIdCol = iIdCol;
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
     * 设置bOkToOverwrite.
     *
     * @param bOkToOverwrite bOkToOverwrite
     */
    public void setbOkToOverwrite(boolean bOkToOverwrite) {
        this.bOkToOverwrite = bOkToOverwrite;
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
     * 设置sResultsFolder.
     *
     * @param sResultsFolder sResultsFolder
     */
    public void setsResultsFolder(String sResultsFolder) {
        this.sResultsFolder = sResultsFolder;
    }

    /**
     * 设置mlOpt.
     *
     * @param mlOpt mlOpt
     */
    public void setMlOpt(MachineLearningOptions mlOpt) {
        this.mlOpt = mlOpt;
    }

    /**
     * 获取OptimalTermStrengths.
     *
     * @return mlOpt.getsOptimalTermStrengths()
     */
    public String getsOptimalTermStrengths() {
        if (mlOpt != null) {
            return mlOpt.getsOptimalTermStrengths();
        }
        return "";
    }

    /**
     * 判断doall.
     *
     * @return mlOpt.isbDoAll()
     */
    public boolean isbDoAll() {
        if (mlOpt != null) {
            return mlOpt.isbDoAll();
        }
        return false;
    }

    /**
     * 获取iMinImprovement.
     *
     * @return mlOpt.getiMinImprovement()
     */
    public int getiMinImprovement() {
        if (mlOpt != null) {
            return mlOpt.getiMinImprovement();
        }
        return 2;
    }

    /**
     * 判断bUseTotalDifference.
     *
     * @return mlOpt.isbUseTotalDifference
     */
    public boolean isbUseTotalDifference() {
        if (mlOpt != null) {
            return mlOpt.isbUseTotalDifference();
        }
        return false;
    }

    /**
     * 获取iIterations.
     *
     * @return mlOpt.getiIterations
     */
    public int getiIterations() {
        if (mlOpt != null) {
            return mlOpt.getiIterations();
        }
        return 1;
    }

    /**
     * 获取iMultiOptimisations.
     *
     * @return mlOpt.getiMultiOptimisations
     */
    public int getiMultiOptimisations() {
        if (mlOpt != null) {
            return mlOpt.getiMultiOptimisations();
        }
        return 1;
    }

    /**
     * 判断bReportNewTermWeightsForBadClassifications.
     *
     * @return mlOpt.isbReportNewTermWeightsForBadClassifications
     */
    public boolean isbReportNewTermWeightsForBadClassifications() {
        if (mlOpt != null) {
            return mlOpt.isbReportNewTermWeightsForBadClassifications();
        }
        return false;
    }

}

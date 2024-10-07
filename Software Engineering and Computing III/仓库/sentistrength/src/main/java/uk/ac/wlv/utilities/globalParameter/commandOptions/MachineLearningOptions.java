package uk.ac.wlv.utilities.globalParameter.commandOptions;

public class MachineLearningOptions {
    String sOptimalTermStrengths;
    boolean bDoAll;
    int iMinImprovement;
    boolean bUseTotalDifference;
    int iIterations;
    int iMultiOptimisations;
    boolean bReportNewTermWeightsForBadClassifications;


    /**
     * 无参构造函数.
     */
    public MachineLearningOptions() {
    }

    /**
     * 有参构造函数.
     *
     * @param sOptimalTermStrengths
     * @param bDoAll
     * @param iMinImprovement
     * @param bUseTotalDifference
     * @param iIterations
     * @param iMultiOptimisations
     * @param bReportNewTermWeightsForBadClassifications
     */
    public MachineLearningOptions(String sOptimalTermStrengths, boolean bDoAll, int iMinImprovement, boolean bUseTotalDifference, int iIterations, int iMultiOptimisations, boolean bReportNewTermWeightsForBadClassifications) {
        this.sOptimalTermStrengths = sOptimalTermStrengths;
        this.bDoAll = bDoAll;
        this.iMinImprovement = iMinImprovement;
        this.bUseTotalDifference = bUseTotalDifference;
        this.iIterations = iIterations;
        this.iMultiOptimisations = iMultiOptimisations;
        this.bReportNewTermWeightsForBadClassifications = bReportNewTermWeightsForBadClassifications;
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
     * 获得iMinImprovement.
     *
     * @return iMinImprovement
     */
    public int getiMinImprovement() {
        return iMinImprovement;
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
     * 获得iMultiOptimisations.
     *
     * @return iMultiOptimisations
     */
    public int getiMultiOptimisations() {
        return iMultiOptimisations;
    }

    /**
     * 判断doall.
     *
     * @return bDoAll
     */
    public boolean isbDoAll() {
        return bDoAll;
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
     * 判断bReportNewTermWeightsForBadClassifications.
     *
     * @return bReportNewTermWeightsForBadClassifications
     */
    public boolean isbReportNewTermWeightsForBadClassifications() {
        return bReportNewTermWeightsForBadClassifications;
    }

    /**
     * 设置bReportNewTermWeightsForBadClassifications.
     *
     * @param bReportNewTermWeightsForBadClassifications
     */
    public void setbReportNewTermWeightsForBadClassifications(boolean bReportNewTermWeightsForBadClassifications) {
        this.bReportNewTermWeightsForBadClassifications = bReportNewTermWeightsForBadClassifications;
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
     * 设置bDoAll.
     *
     * @param bDoAll bDoAll
     */
    public void setbDoAll(boolean bDoAll) {
        this.bDoAll = bDoAll;
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
     * 设置bUseTotalDifference.
     *
     * @param bUseTotalDifference bUseTotalDifference
     */
    public void setbUseTotalDifference(boolean bUseTotalDifference) {
        this.bUseTotalDifference = bUseTotalDifference;
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
     * 设置iMultiOptimisations.
     *
     * @param iMultiOptimisations iMultiOptimisations
     */
    public void setiMultiOptimisations(int iMultiOptimisations) {
        this.iMultiOptimisations = iMultiOptimisations;
    }
}

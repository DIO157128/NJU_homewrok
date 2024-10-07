package uk.ac.wlv.sentistrength.command.concreteCommand.inputStrategy;

import uk.ac.wlv.sentistrength.Corpus;
import uk.ac.wlv.utilities.FileOps;
import uk.ac.wlv.utilities.globalParameter.commandOptions.InputCommandOptions;

import java.io.File;

public class ClassifyAllLineStrategy implements InputStrategy {

    private static final float HUNDREDF = 100.0F;
    /**
     * @param opt
     * @return 是否成功
     */
    public boolean calculate(InputCommandOptions opt) {
        Corpus c = opt.getC();
        if (opt.getsInputFolder() != "") {
            System.out.println("Input folder specified but textCol and IDcol or annotateCol needed");
            return false;
        }

        if (opt.getsInputFile() == "") {
            System.out.println("No action taken because no input file nor text specified");
            this.showBriefHelp(c);
            return false;
        }

        String sOutputFile = FileOps.getNextAvailableFilename(FileOps.sChopFileNameExtension(opt.getsInputFile()), opt.getsResultsFileExtension());
        if (opt.getsResultsFolder().length() > 0) {
            sOutputFile = opt.getsResultsFolder() + (new File(sOutputFile)).getName();
        }

        if (opt.getMlOpt() != null) {
            this.runMachineLearning(c, opt.getsInputFile(), opt.isbDoAll(), opt.getiMinImprovement(), opt.isbUseTotalDifference(), opt.getiIterations(), opt.getiMultiOptimisations(), sOutputFile);
        } else {
            int col = opt.getiTextCol();
            opt.setiTextCol(col - 1);
            c.classifyAllLinesInInputFile(opt.getsInputFile(), opt.getiTextCol(), sOutputFile);
        }

        System.out.println("Finished! Results in: " + sOutputFile);
        return true;
    }

    /**
     * runMachineLearning方法，利用传入方法的参数，对语料库c进行机器学习训练.
     *
     * @param c                   传入方法的语料库
     * @param sInputFile          字符串变量，表示命令中的文件路径
     * @param bDoAll              布尔型变量，用于判断命令中是否有"all"，有则为true，无则为false
     * @param iMinImprovement     int型变量，为命令中”minimprovement“的值
     * @param bUseTotalDifference 布尔型变量，判断命令中是否有“numcorrect”，有则为false，无则为true
     * @param iIterations         int型变量，为命令中“iterations”的值
     * @param iMultiOptimisations int型变量，为命令中"imult"的值
     * @param sOutputFile         String型变量，为输出的文件路径
     */
    private void runMachineLearning(Corpus c, String sInputFile, boolean bDoAll, int iMinImprovement, boolean bUseTotalDifference, int iIterations, int iMultiOptimisations, String sOutputFile) {
        if (iMinImprovement < 1) {
            System.out.println("No action taken because min improvement < 1");
            this.showBriefHelp(c);
        } else {
            c.setCorpus(sInputFile);
            c.calculateCorpusSentimentScores();
            int corpusSize = c.getCorpusSize();
            if (c.options.bgTrinaryMode) {
                if (c.options.bgBinaryVersionOfTrinaryMode) {
                    System.out.print("Before training, binary accuracy: " + c.getClassificationTrinaryNumberCorrect() + " " + (float) c.getClassificationTrinaryNumberCorrect() / (float) corpusSize * HUNDREDF + "%");
                } else {
                    System.out.print("Before training, trinary accuracy: " + c.getClassificationTrinaryNumberCorrect() + " " + (float) c.getClassificationTrinaryNumberCorrect() / (float) corpusSize * HUNDREDF + "%");
                }
            } else if (c.options.bgScaleMode) {
                System.out.print("Before training, scale accuracy: " + c.getClassificationScaleNumberCorrect() + " " + (float) c.getClassificationScaleNumberCorrect() * HUNDREDF / (float) corpusSize + "% corr " + c.getClassificationScaleCorrelationWholeCorpus());
            } else {
                System.out.print("Before training, positive: " + c.getClassificationPositiveNumberCorrect() + " " + c.getClassificationPositiveAccuracyProportion() * HUNDREDF + "% negative " + c.getClassificationNegativeNumberCorrect() + " " + c.getClassificationNegativeAccuracyProportion() * HUNDREDF + "% ");
                System.out.print("   Positive corr: " + c.getClassificationPosCorrelationWholeCorpus() + " negative " + c.getClassificationNegCorrelationWholeCorpus());
            }

            System.out.println(" out of " + c.getCorpusSize());
            if (bDoAll) {
                System.out.println("Running " + iIterations + " iteration(s) of all options on file " + sInputFile + "; results in " + sOutputFile);
                c.run10FoldCrossValidationForAllOptionVariations(iMinImprovement, bUseTotalDifference, iIterations, iMultiOptimisations, sOutputFile);
            } else {
                System.out.println("Running " + iIterations + " iteration(s) for standard or selected options on file " + sInputFile + "; results in " + sOutputFile);
                c.run10FoldCrossValidationMultipleTimes(iMinImprovement, bUseTotalDifference, iIterations, iMultiOptimisations, sOutputFile);
            }

        }
    }
}

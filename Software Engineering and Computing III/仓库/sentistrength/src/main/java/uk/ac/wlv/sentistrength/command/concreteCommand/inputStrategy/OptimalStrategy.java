package uk.ac.wlv.sentistrength.command.concreteCommand.inputStrategy;

import uk.ac.wlv.sentistrength.Corpus;
import uk.ac.wlv.utilities.globalParameter.commandOptions.InputCommandOptions;

public class OptimalStrategy implements InputStrategy {

    /**
     * @param opt
     * @return 是否成功
     */
    public boolean calculate(InputCommandOptions opt) {
        if (opt.getsInputFile() == "") {
            System.out.println("Input file must be specified to optimise term weights");
            return false;
        }
        Corpus c = opt.getC();
        if (c.setCorpus(opt.getsInputFile())) {
            c.optimiseDictionaryWeightingsForCorpus(opt.getiMinImprovement(), opt.isbUseTotalDifference());
            c.resources.sentimentWords.saveSentimentList(opt.getsOptimalTermStrengths(), c);
            System.out.println("Saved optimised term weights to " + opt.getsOptimalTermStrengths());
        } else {
            System.out.println("Error: Too few texts in " + opt.getsInputFile());
            return false;
        }
        return true;
    }
}

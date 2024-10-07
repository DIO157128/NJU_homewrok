package uk.ac.wlv.sentistrength.command.concreteCommand.inputStrategy;

import uk.ac.wlv.sentistrength.Corpus;
import uk.ac.wlv.utilities.FileOps;
import uk.ac.wlv.utilities.globalParameter.commandOptions.InputCommandOptions;

public class TermWeightStrategy implements InputStrategy {
    /**
     * @param opt
     * @return 是否成功
     */
    public boolean calculate(InputCommandOptions opt) {
        Corpus c = opt.getC();
        if (c.setCorpus(opt.getsInputFile())) {
            c.printCorpusUnusedTermsClassificationIndex(FileOps.sChopFileNameExtension(opt.getsInputFile()) + "_unusedTerms.txt", 1);
        } else {
            System.out.println("Error: Too few texts in " + opt.getsInputFile());
            return false;
        }
        return true;
    }
}

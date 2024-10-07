package uk.ac.wlv.sentistrength.command.concreteCommand.inputStrategy;

import uk.ac.wlv.sentistrength.Corpus;
import uk.ac.wlv.utilities.FileOps;
import uk.ac.wlv.utilities.globalParameter.commandOptions.InputCommandOptions;

import java.io.File;

public class SaveWithIDStrategy implements InputStrategy {
    /**
     * @param opt
     * @return 是否成功
     */
    public boolean calculate(InputCommandOptions opt) {
        this.classifyAndSaveWithID(opt.getC(), opt.getsInputFile(), opt.getsInputFolder(), opt.getiTextCol(), opt.getiIdCol());
        return true;
    }

    /**
     * classifyAndSaveWithID方法，使用语料库c对文件进行分类并保存id，如果sInputFile不为空，则对sInputFile进行处理，否则对sInputFolder进行处理.
     *
     * @param c            传入方法的语料库
     * @param sInputFile   字符串变量，表示需要处理的文件的路径
     * @param sInputFolder 字符串变量，表示需要处理的的文件夹的路径
     * @param iTextCol     int型变量，表示文本量
     * @param iIdCol       int型变量，表示id长度
     * @return 是否成功
     */
    private boolean classifyAndSaveWithID(Corpus c, String sInputFile, String sInputFolder, int iTextCol, int iIdCol) {
        if (!sInputFile.equals("")) {
            c.classifyAllLinesAndRecordWithID(sInputFile, iTextCol - 1, iIdCol - 1, FileOps.sChopFileNameExtension(sInputFile) + "_classID.txt");
        } else {
            if (sInputFolder.equals("")) {
                System.out.println("No annotations done because no input file or folder specfied");
                this.showBriefHelp(c);
                return false;
            }

            File folder = new File(sInputFolder);
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles == null) {
                System.out.println("Incorrect or empty input folder specfied");
                this.showBriefHelp(c);
                return false;
            }

            for (int i = 0; i < listOfFiles.length; ++i) {
                if (listOfFiles[i].isFile()) {
                    System.out.println("Classify + save with ID: " + listOfFiles[i].getName());
                    c.classifyAllLinesAndRecordWithID(sInputFolder + "/" + listOfFiles[i].getName(), iTextCol - 1, iIdCol - 1, sInputFolder + "/" + FileOps.sChopFileNameExtension(listOfFiles[i].getName()) + "_classID.txt");
                }
            }
        }
        return true;
    }


}

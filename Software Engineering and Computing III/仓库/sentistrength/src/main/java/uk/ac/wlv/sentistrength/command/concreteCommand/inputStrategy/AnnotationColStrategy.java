package uk.ac.wlv.sentistrength.command.concreteCommand.inputStrategy;

import uk.ac.wlv.sentistrength.Corpus;
import uk.ac.wlv.utilities.globalParameter.commandOptions.InputCommandOptions;

import java.io.File;

public class AnnotationColStrategy implements InputStrategy {
    /**
     * @param opt
     * @return 是否成功
     */
    public boolean calculate(InputCommandOptions opt) {
        Corpus c = opt.getC();
        this.annotationTextCol(c, opt.getsInputFile(), opt.getsInputFolder(), opt.getsFileSubString(), opt.getiTextColForAnnotation(), opt.isbOkToOverwrite());
        // 这里的true or false还需要进一步商讨
        return true;
    }

    /**
     * annotationTextCol方法，通过语料库对传入文件的语句进行情绪分值注解.
     *
     * @param c                     传入方法的语料库
     * @param sInputFile            字符串变量，表示命令中的文件路径
     * @param sInputFolder          字符串变量，表示命令中的文件夹路径
     * @param sFileSubString        字符串变量，表示命令中“filesubstring”的内容
     * @param iTextColForAnnotation int型变量，命令中"annotatecol"的值
     * @param bOkToOverwrite        布尔型变量，判断命令中是否有“overwrite”，有则为true，无则为false
     * @return 是否成功
     */
    private boolean annotationTextCol(Corpus c, String sInputFile, String sInputFolder, String sFileSubString, int iTextColForAnnotation, boolean bOkToOverwrite) {
        if (!bOkToOverwrite) {
            System.out.println("Must include parameter overwrite to annotate");
        } else {
            if (!sInputFile.equals("")) {
                c.annotateAllLinesInInputFile(sInputFile, iTextColForAnnotation - 1);
            } else {
                if (sInputFolder.equals("")) {
                    System.out.println("No annotations done because no input file or folder specfied");
                    this.showBriefHelp(c);
                    return false;
                }
                File folder = new File(sInputFolder);
                File[] listOfFiles = folder.listFiles();
                for (int i = 0; i < listOfFiles.length; ++i) {
                    if (listOfFiles[i].isFile()) {
                        if (!sFileSubString.equals("") && listOfFiles[i].getName().indexOf(sFileSubString) <= 0) {
                            System.out.println("  Ignoring " + listOfFiles[i].getName());
                        } else {
                            System.out.println("Annotate: " + listOfFiles[i].getName());
                            c.annotateAllLinesInInputFile(sInputFolder + "/" + listOfFiles[i].getName(), iTextColForAnnotation - 1);
                        }
                    }
                }
            }
        }
        return true;
    }

}

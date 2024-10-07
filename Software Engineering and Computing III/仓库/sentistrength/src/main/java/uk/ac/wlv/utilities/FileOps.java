package uk.ac.wlv.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 本类主要用于执行文件相关的辅助处理工作.
 */


public class FileOps {

    /**
     * 获取下一个可用的文件名的最大搜索次数.
     */
    private static final int MAX_SEARCH = 1000;

    private FileOps() {
    }

    /**
     * 该函数的作用是备份文件并删除源文件.
     *
     * @param sFileName   源文件名
     * @param iMaxBackups 最大备份数
     * @return 是否成功
     */
    public static boolean backupFileAndDeleteOriginal(String sFileName, int iMaxBackups) {
        int iLastBackup;
        File f;
        for (iLastBackup = iMaxBackups; iLastBackup >= 0; --iLastBackup) {
            f = new File(sFileName + iLastBackup + ".bak");
            if (f.exists()) {
                break;
            }
        }

        if (iLastBackup < 1) {
            f = new File(sFileName);
            if (f.exists()) {
                f.renameTo(new File(sFileName + "1.bak"));
                return true;
            } else {
                return false;
            }
        } else {
            if (iLastBackup == iMaxBackups) {
                f = new File(sFileName + iLastBackup + ".bak");
                f.delete();
                --iLastBackup;
            }

            for (int i = iLastBackup; i > 0; --i) {
                f = new File(sFileName + i + ".bak");
                f.renameTo(new File(sFileName + (i + 1) + ".bak"));
            }

            f = new File(sFileName);
            f.renameTo(new File(sFileName + "1.bak"));
            return true;
        }
    }

    /**
     * 该函数的作用是计算文件的行数.
     *
     * @param sFileLocation 需要统计函数的文件的位置
     * @return 行数
     */

    public static int iCountLinesInTextFile(String sFileLocation) {
        int iLines = 0;

        try {
            BufferedReader rReader;
            for (rReader = new BufferedReader(new FileReader(sFileLocation)); rReader.ready(); ++iLines) {
                String sLine = rReader.readLine();
            }

            rReader.close();
            return iLines;
        } catch (FileNotFoundException var5) {
            var5.printStackTrace();
            return -1;
        } catch (IOException var6) {
            var6.printStackTrace();
            return -1;
        }
    }

    /**
     * 该函数的作用是获取下一个可用文件名，即可用于新文件命名的文件名.
     * 文件的命名格式为 nameStart + i + nameEnd.
     * 至多搜索1000次，1000次后未找到则返回空串.
     *
     * @param sFileNameStart
     * @param sFileNameEnd
     * @return 可用文件名
     */

    public static String getNextAvailableFilename(String sFileNameStart, String sFileNameEnd) {
        for (int i = 0; i <= MAX_SEARCH; ++i) {
            String sFileName = sFileNameStart + i + sFileNameEnd;
            File f = new File(sFileName);
            if (!f.isFile()) {
                return sFileName;
            }
        }

        return "";
    }

    /**
     * 截断文件扩展名.
     * 例如test.txt, 截断.txt返回tes.
     *
     * @param sFilename 待处理文件的完整文件名
     * @return 除去扩展的文件名
     */

    public static String sChopFileNameExtension(String sFilename) {
        if (sFilename != null && sFilename != "") {
            int iLastDotPos = sFilename.lastIndexOf(".");
            if (iLastDotPos > 0) {
                sFilename = sFilename.substring(0, iLastDotPos);
            }
        }

        return sFilename;
    }
}

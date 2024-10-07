package uk.ac.wlv.utilities;

public class Sort {


    /**
     * 该方法实现了对字符串数组进行快速排序.
     *
     * @param sArray 待排序的字符串数组
     * @param l      数组左边界
     * @param r      数组右边界
     */
    public static void quickSortStrings(String[] sArray, int l, int r) {
        String sMiddle = sArray[(l + r) / 2];
        int i = l;
        int j = r;

        while (i <= j) {
            while (sMiddle.compareTo(sArray[i]) > 0 && i < r) {
                ++i;
            }

            while (sMiddle.compareTo(sArray[j]) < 0 && j > l) {
                --j;
            }

            if (i < j) {
                String str = sArray[i];
                sArray[i] = sArray[j];
                sArray[j] = str;
            }

            if (i <= j) {
                ++i;
                --j;
            }
        }

        if (l < j) {
            quickSortStrings(sArray, l, j);
        }

        if (i < r) {
            quickSortStrings(sArray, i, r);
        }

    }

    /**
     * 该方法实现了对整型数组进行快速排序.
     *
     * @param iArray 待排序的整型数组
     * @param l      数组左边界
     * @param r      数组右边界
     */

    public static void quickSortInt(int[] iArray, int l, int r) {
        int x = iArray[(l + r) / 2];
        int i = l;
        int j = r;

        while (i <= j) {
            while (iArray[i] < x && i < r) {
                ++i;
            }

            while (x < iArray[j] && j > l) {
                --j;
            }

            if (i <= j) {
                int y = iArray[i];
                iArray[i] = iArray[j];
                iArray[j] = y;
                ++i;
                --j;
            }
        }

        if (l < j) {
            quickSortInt(iArray, l, j);
        }

        if (i < r) {
            quickSortInt(iArray, i, r);
        }

    }

    /**
     * 对一个双精度浮点数组和一个整型数组进行快速排序，排序基准为双精度浮点数数组.
     *
     * @param fArray  待排序的双精度浮点数组
     * @param iArray2 待排序的整型数组
     * @param l       待排序数组的左边界（inclusive）
     * @param r       待排序数组的右边界（inclusive）
     */
    public static void quickSortDoubleWithInt(double[] fArray, int[] iArray2, int l, int r) {
        double x = fArray[(l + r) / 2];
        int i = l;
        int j = r;

        while (i <= j) {
            while (fArray[i] < x && i < r) {
                ++i;
            }

            while (x < fArray[j] && j > l) {
                --j;
            }

            if (i <= j) {
                double fTemp = fArray[i];
                int iTemp = iArray2[i];
                fArray[i] = fArray[j];
                iArray2[i] = iArray2[j];
                fArray[j] = fTemp;
                iArray2[j] = iTemp;
                ++i;
                --j;
            }
        }

        if (l < j) {
            quickSortDoubleWithInt(fArray, iArray2, l, j);
        }

        if (i < r) {
            quickSortDoubleWithInt(fArray, iArray2, i, r);
        }

    }

    /**
     * 使用快速排序算法对int型数组进行排序，同时按照第二个int型数组的顺序对第一个数组进行排序.
     *
     * @param iArray  要排序的int型数组
     * @param iArray2 与要排序的int型数组配对的第二个int型数组
     * @param l       要排序的数组部分的起始下标
     * @param r       要排序的数组部分的终止下标
     */
    public static void quickSortIntWithInt(int[] iArray, int[] iArray2, int l, int r) {
        int x = iArray[(l + r) / 2];
        int i = l;
        int j = r;

        while (i <= j) {
            while (iArray[i] < x && i < r) {
                ++i;
            }

            while (x < iArray[j] && j > l) {
                --j;
            }

            if (i <= j) {
                int iTemp = iArray[i];
                int iTemp2 = iArray2[i];
                iArray[i] = iArray[j];
                iArray2[i] = iArray2[j];
                iArray[j] = iTemp;
                iArray2[j] = iTemp2;
                ++i;
                --j;
            }
        }

        if (l < j) {
            quickSortIntWithInt(iArray, iArray2, l, j);
        }

        if (i < r) {
            quickSortIntWithInt(iArray, iArray2, i, r);
        }

    }

    /**
     * 在有序字符串数组中查找指定字符串的位置.
     *
     * @param sText  要查找的字符串
     * @param sArray 有序字符串数组
     * @param iLower 查找范围的下界
     * @param iUpper 查找范围的上界
     * @return 如果找到了指定字符串，则返回其在数组中的索引；否则返回-1。
     */
    public static int iFindStringPositionInSortedArray(String sText, String[] sArray, int iLower, int iUpper) {
        boolean var4 = false;

        int iMiddle; //
        while (iUpper - iLower > 2) {
            iMiddle = (iLower + iUpper) / 2;
            if (sText.compareTo(sArray[iMiddle]) < 0) {
                iUpper = iMiddle;
            } else {
                iLower = iMiddle;
            }
        }

        for (iMiddle = iLower; iMiddle <= iUpper; ++iMiddle) {
            if (sArray[iMiddle].compareTo(sText) == 0) {
                return iMiddle;
            }
        }

        return -1;
    }


    /**
     * 在有序整数数组中查找指定整数的位置.
     *
     * @param iFind  要查找的整数
     * @param iArray 有序整数数组
     * @param iLower 查找范围的下界
     * @param iUpper 查找范围的上界
     * @return 如果找到了指定整数，则返回其在数组中的索引；否则返回-1。
     */
    public static int iFindIntPositionInSortedArray(int iFind, int[] iArray, int iLower, int iUpper) {
        boolean var4 = false;

        int iMiddle;
        while (iUpper - iLower > 2) {
            iMiddle = (iLower + iUpper) / 2;
            if (iFind < iArray[iMiddle]) {
                iUpper = iMiddle;
            } else {
                iLower = iMiddle;
            }
        }

        for (iMiddle = iLower; iMiddle <= iUpper; ++iMiddle) {
            if (iArray[iMiddle] == iFind) {
                return iMiddle;
            }
        }

        return -1;
    }

    /**
     * 在有序字符串数组中查找指定字符串的位置，同时支持通配符在数组中的匹配.
     *
     * @param sText  要查找的字符串
     * @param sArray 有序字符串数组
     * @param iLower 查找范围的下界
     * @param iUpper 查找范围的上界
     * @return 如果找到了指定字符串，则返回其在数组中的索引；否则返回-1。
     */
    public static int iFindStringPositionInSortedArrayWithWildcardsInArray(String sText, String[] sArray, int iLower, int iUpper) {
        int iOriginalLower = iLower;
        int iOriginalUpper = iUpper;

        int iMiddle;
        while (iUpper - iLower > 2) {
            iMiddle = (iLower + iUpper) / 2;
            if (sText.compareTo(sArray[iMiddle]) < 0) {
                iUpper = iMiddle;
            } else {
                iLower = iMiddle;
            }
        }

        for (iMiddle = iUpper; iMiddle >= iLower; --iMiddle) {
            if (sArray[iMiddle].compareTo(sText) == 0) {
                return iMiddle;
            }
        }

        if (iLower > iOriginalLower) {
            --iLower;
        }

        if (iLower > iOriginalLower) {
            --iLower;
        }

        if (iLower > iOriginalLower) {
            --iLower;
        }

        if (iUpper < iOriginalUpper) {
            ++iUpper;
        }

        int iTextLength = sText.length();

        for (iMiddle = iUpper; iMiddle >= iLower; --iMiddle) {
            int iLength = sArray[iMiddle].length();
            if (iLength > 1 && sArray[iMiddle].substring(iLength - 1, iLength).compareTo("*") == 0 && iTextLength >= iLength - 1 && sText.substring(0, iLength - 1).compareTo(sArray[iMiddle].substring(0, iLength - 1)) == 0) {
                return iMiddle;
            }
        }

        return -1;
    }

    /**
     * 对一个字符串数组和整数数组进行快速排序.
     *
     * @param sArray 待排序的字符串数组
     * @param iArray 对应的整数数组
     * @param l      数组的左边界（起始下标）
     * @param r      数组的右边界（结束下标）
     */
    public static void quickSortStringsWithInt(String[] sArray, int[] iArray, int l, int r) {
        String sMiddle = sArray[(l + r) / 2];
        int i = l;
        int j = r;

        while (i <= j) {
            while (sMiddle.compareTo(sArray[i]) > 0 && i < r) {
                ++i;
            }

            while (sMiddle.compareTo(sArray[j]) < 0 && j > l) {
                --j;
            }

            if (i < j) {
                String sTemp = sArray[i];
                int iTemp = iArray[i];
                sArray[i] = sArray[j];
                iArray[i] = iArray[j];
                sArray[j] = sTemp;
                iArray[j] = iTemp;
            }

            if (i <= j) {
                ++i;
                --j;
            }
        }

        if (l < j) {
            quickSortStringsWithInt(sArray, iArray, l, j);
        }

        if (i < r) {
            quickSortStringsWithInt(sArray, iArray, i, r);
        }

    }

    /**
     * 使用快速排序算法对给定的字符串数组sArray及其对应的sArray2进行排序.
     *
     * @param sArray  要排序的字符串数组
     * @param sArray2 要排序的第二个字符串数组，与sArray中的元素一一对应
     * @param l       数组起始下标
     * @param r       数组结束下标
     */
    public static void quickSortStringsWithStrings(String[] sArray, String[] sArray2, int l, int r) {
        String sMiddle = sArray[(l + r) / 2];
        int i = l;
        int j = r;

        while (i <= j) {
            while (sMiddle.compareTo(sArray[i]) > 0 && i < r) {
                ++i;
            }

            while (sMiddle.compareTo(sArray[j]) < 0 && j > l) {
                --j;
            }

            if (i < j) {
                String sTemp = sArray[i];
                String sTemp2 = sArray2[i];
                sArray[i] = sArray[j];
                sArray2[i] = sArray2[j];
                sArray[j] = sTemp;
                sArray2[j] = sTemp2;
            }

            if (i <= j) {
                ++i;
                --j;
            }
        }

        if (l < j) {
            quickSortStringsWithStrings(sArray, sArray2, l, j);
        }

        if (i < r) {
            quickSortStringsWithStrings(sArray, sArray2, i, r);
        }

    }

    /**
     * 对给定的整数数组iArray进行乱序操作.
     *
     * @param iArray 要进行乱序操作的整数数组
     */
    public static void makeRandomOrderList(int[] iArray) {
        if (iArray != null) {
            int iArraySize = iArray.length;
            if (iArraySize >= 1) {
                double[] fRandArray = new double[iArraySize--];

                for (int i = 1; i <= iArraySize; ++i) {
                    iArray[i] = i;
                    fRandArray[i] = Math.random();
                }

                quickSortDoubleWithInt(fRandArray, iArray, 1, iArraySize);
            }
        }
    }

    /**
     * 使用快速排序算法对给定的浮点数数组fArray，通过指定的索引数组iIndexArray，以降序排序.
     *
     * @param fArray      要排序的浮点数数组
     * @param iIndexArray 指定排序顺序的索引数组
     * @param l           数组起始下标
     * @param r           数组结束下标
     */
    public static void quickSortNumbersDescendingViaIndex(double[] fArray, int[] iIndexArray, int l, int r) {
        int i = l;
        int j = r;
        double fX = fArray[iIndexArray[(l + r) / 2]];

        while (i <= j) {
            while (fArray[iIndexArray[i]] > fX && i < r) {
                ++i;
            }

            while (fX > fArray[iIndexArray[j]] && j > l) {
                --j;
            }

            if (i <= j) {
                int iTemp = iIndexArray[i];
                iIndexArray[i] = iIndexArray[j];
                iIndexArray[j] = iTemp;
                ++i;
                --j;
            }
        }

        if (l < j) {
            quickSortNumbersDescendingViaIndex(fArray, iIndexArray, l, j);
        }

        if (i < r) {
            quickSortNumbersDescendingViaIndex(fArray, iIndexArray, i, r);
        }

    }
}

package uk.ac.wlv.utilities;

public class Trie {

    /**
     * 通过 Trie 树算法获取字符串在字符串数组中的位置或插入新字符串.
     * @param sText             待查询或插入的字符串。
     * @param sArray            字符串数组。
     * @param iLessPointer      字符串数组中元素对应的小于指针数组。
     * @param iMorePointer      字符串数组中元素对应的大于指针数组。
     * @param iFirstElement     字符串数组中第一个元素的索引。
     * @param iLastElement      字符串数组中最后一个元素的索引。
     * @param bDontAddNewString 是否不插入新的字符串。若为true，则返回-1表示未找到该字符串；若为false，则在字符串数组中插入该字符串并返回其索引。
     * @return 字符串在字符串数组中的索引，若未找到则返回-1。
     */
    public static int iGetTriePositionForString(String sText, String[] sArray, int[] iLessPointer, int[] iMorePointer, int iFirstElement, int iLastElement, boolean bDontAddNewString) {
        int iTriePosition = 0;
        int iLastTriePosition = 0;
        if (iLastElement < iFirstElement) {
            sArray[iFirstElement] = sText;
            iLessPointer[iFirstElement] = -1;
            iMorePointer[iFirstElement] = -1;
            return iFirstElement;
        } else {
            iTriePosition = iFirstElement;

//         int iLastTriePosition;
            label33:
            do {
                do {
                    iLastTriePosition = iTriePosition;
                    if (sText.compareTo(sArray[iTriePosition]) < 0) {
                        iTriePosition = iLessPointer[iTriePosition];
                        continue label33;
                    }

                    if (sText.compareTo(sArray[iTriePosition]) <= 0) {
                        return iTriePosition;
                    }

                    iTriePosition = iMorePointer[iTriePosition];
                } while (iTriePosition != -1);

                if (bDontAddNewString) {
                    return -1;
                }

                ++iLastElement;
                sArray[iLastElement] = sText;
                iLessPointer[iLastElement] = -1;
                iMorePointer[iLastElement] = -1;
                iMorePointer[iLastTriePosition] = iLastElement;
                return iLastElement;
            } while (iTriePosition != -1);

            if (bDontAddNewString) {
                return -1;
            } else {
                ++iLastElement;
                sArray[iLastElement] = sText;
                iLessPointer[iLastElement] = -1;
                iMorePointer[iLastElement] = -1;
                iLessPointer[iLastTriePosition] = iLastElement;
                return iLastElement;
            }
        }
    }

    /**
     *
     * @param sText
     * @param sArray
     * @param iLessPointer
     * @param iMorePointer
     * @param iLastElement
     * @param bDontAddNewString
     * @return position
     */
    public static int iGetTriePositionForStringold(String sText, String[] sArray, int[] iLessPointer, int[] iMorePointer, int iLastElement, boolean bDontAddNewString) {
        int iTriePosition = 0;
        int iLastTriePosition = 0;
        if (iLastElement == 0) {
            iLastElement = 1;
            sArray[iLastElement] = sText;
            iLessPointer[iLastElement] = 0;
            iMorePointer[iLastElement] = 0;
            return 1;
        } else {
            iTriePosition = 1;

//         int iLastTriePosition;
            label33:
            do {
                do {
                    iLastTriePosition = iTriePosition;
                    if (sText.compareTo(sArray[iTriePosition]) < 0) {
                        iTriePosition = iLessPointer[iTriePosition];
                        continue label33;
                    }

                    if (sText.compareTo(sArray[iTriePosition]) <= 0) {
                        return iTriePosition;
                    }

                    iTriePosition = iMorePointer[iTriePosition];
                } while (iTriePosition != 0);

                if (bDontAddNewString) {
                    return 0;
                }

                ++iLastElement;
                sArray[iLastElement] = sText;
                iLessPointer[iLastElement] = 0;
                iMorePointer[iLastElement] = 0;
                iMorePointer[iLastTriePosition] = iLastElement;
                return iLastElement;
            } while (iTriePosition != 0);

            if (bDontAddNewString) {
                return 0;
            } else {
                ++iLastElement;
                sArray[iLastElement] = sText;
                iLessPointer[iLastElement] = 0;
                iMorePointer[iLastElement] = 0;
                iLessPointer[iLastTriePosition] = iLastElement;
                return iLastElement;
            }
        }
    }

    /**
     * 根据给定字符串在Trie数组中查找其对应位置，若未找到且不禁止添加新字符串，则在数组中添加该字符串及其对应计数.
     *
     * @param sText             待查找的字符串
     * @param sArray            Trie数组
     * @param iCountArray       每个字符串的计数数组
     * @param iLessPointer      每个节点指向比其小的节点的指针数组
     * @param iMorePointer      每个节点指向比其大的节点的指针数组
     * @param iFirstElement     Trie数组第一个元素的下标
     * @param iLastElement      Trie数组最后一个元素的下标
     * @param bDontAddNewString 是否禁止添加新字符串
     * @param iCount            待添加字符串的计数
     * @return 字符串在Trie数组中对应的下标，若未找到返回-1
     */
    public static int iGetTriePositionForStringAndAddCount(String sText, String[] sArray, int[] iCountArray, int[] iLessPointer, int[] iMorePointer, int iFirstElement, int iLastElement, boolean bDontAddNewString, int iCount) {
        int iPos = iGetTriePositionForString(sText, sArray, iLessPointer, iMorePointer, iFirstElement, iLastElement, bDontAddNewString);
        if (iPos >= 0) {
            int var10002 = iCountArray[iPos]++;
        }

        return iPos;
    }
}

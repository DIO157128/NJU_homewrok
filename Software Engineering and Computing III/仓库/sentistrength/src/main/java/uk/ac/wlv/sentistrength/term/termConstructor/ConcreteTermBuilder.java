package uk.ac.wlv.sentistrength.term.termConstructor;

import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.ClassificationResources;
import uk.ac.wlv.sentistrength.term.term.Emoticon;
import uk.ac.wlv.sentistrength.term.term.Punctuation;
import uk.ac.wlv.sentistrength.term.term.Term;
import uk.ac.wlv.sentistrength.term.term.Word;

/**
 * 建造类，用于制造term.
 */
public class ConcreteTermBuilder implements TermBuilder {
    private int pos;
    private Term term;
    private ClassificationOptions classificationOptions;
    private ClassificationResources classificationResources;

    public ConcreteTermBuilder() {
        term = null;
    }


    /**
     * 为term设置Resources.
     *
     * @param cResources 需要设置的classificationResources
     */
    @Override
    public void buildResources(ClassificationResources cResources) {
        this.classificationResources = cResources;
    }

    /**
     * 为term设置Options.
     *
     * @param options 需要设置的options
     */
    @Override
    public void buildClassificationOptions(ClassificationOptions options) {
        this.classificationOptions = options;
    }


    /**
     * 建造Term.
     *
     * @param content 内容
     * @return 具体的Term
     */
    @Override
    public Term buildTerm(String content) {
        int iPos = 0;
        int iLastCharType = 0;
        String sChar;
        int iTextLength = content.length();

        int iEmoticonStrength = this.classificationResources.emoticons.getStrength(content);
        if (iEmoticonStrength != Term.NOTPROCESSED) {
            pos = -1;
            term = new Emoticon();
            constructTerm(content);
            return term;
        } else {
            for (; iPos < iTextLength; ++iPos) {
                sChar = content.substring(iPos, iPos + 1);
                if (!Character.isLetterOrDigit(content.charAt(iPos)) && (this.classificationOptions.bgAlwaysSplitWordsAtApostrophes || !sChar.equals("'") || iPos <= 0 || iPos >= iTextLength - 1 || !Character.isLetter(content.charAt(iPos + 1))) && !sChar.equals("$") && !sChar.equals("£") && !sChar.equals("@") && !sChar.equals("_")) {
                    if (iLastCharType == 1) {
                        term = new Word();
                        pos = iPos;
                        break;
                    }

                    iLastCharType = 2;
                } else {
                    if (iLastCharType == 2) {
                        term = new Punctuation();
                        pos = iPos;
                        break;
                    }

                    iLastCharType = 1;
                }
            }

            if (term != null) {
                constructTerm(content.substring(0, iPos));
                return term;
            }

            switch (iLastCharType) {
                case 1:
                    term = new Word();
                    pos = -1;
                    break;
                case 2:
                    term = new Punctuation();
                    pos = -1;
                    break;
                default:
            }

            constructTerm(content);
            return term;
        }
    }


    /**
     * 将term填充.
     *
     * @param content 内容
     */
    private void constructTerm(String content) {
        term.setOptions(classificationOptions);
        term.setClassificationResources(classificationResources);
        term.code(content);
    }

    /**
     * 获得pos.
     *
     * @return pos
     */
    public int getPos() {
        return pos;
    }

}

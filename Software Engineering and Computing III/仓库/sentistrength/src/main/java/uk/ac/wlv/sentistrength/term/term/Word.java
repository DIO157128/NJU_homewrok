package uk.ac.wlv.sentistrength.term.term;

/**
 * term的子类word.
 */
public class Word extends Term {
    private String sgOriginalWord;
    private String sgLCaseWord;
    protected String sgTranslatedWord;
    private String sgWordEmphasis;
    private int igBoosterWordScore;
    private boolean bgAllCapitals;
    private boolean bgAllCaptialsCalculated;
    private boolean bgNegatingWord;
    private boolean bgNegatingWordCalculated;
    private boolean bgProperNoun;
    private boolean bgProperNounCalculated;
    private static final int FIVE = 5;
    private static final int THREE = 3;

    /**
     * 不带参的初始化.
     */
    public Word() {
        sgOriginalWord = "";
        sgLCaseWord = "";
        sgTranslatedWord = "";
        sgWordEmphasis = "";
        igBoosterWordScore = NOTPROCESSED;
        bgAllCapitals = false;
        bgAllCaptialsCalculated = false;
        bgNegatingWord = false;
        bgNegatingWordCalculated = false;
        bgProperNoun = false;
        bgProperNounCalculated = false;
    }

    /**
     * 则处理给定单词，根据options为单词进行拼写纠正，重复字母化简，词性还原等一系列操作.
     *
     * @param content 内容
     */
    @Override
    public void code(String content) {
        StringBuilder contentNew = new StringBuilder();
        StringBuilder sEm = new StringBuilder();
        if (this.options.bgCorrectExtraLetterSpellingErrors) {
            int iSameCount = 0;
            int iLastCopiedPos = 0;
            int iWordEnd = content.length() - 1;

            int iPos;
            for (iPos = 1; iPos <= iWordEnd; ++iPos) {
                if (content.substring(iPos, iPos + 1).compareToIgnoreCase(content.substring(iPos - 1, iPos)) == 0) {
                    ++iSameCount;
                } else {
                    if (iSameCount > 0 && this.options.sgIllegalDoubleLettersInWordMiddle.contains(content.substring(iPos - 1, iPos))) {
                        ++iSameCount;
                    }

                    if (iSameCount > 1) {
                        if (sEm.toString().equals("")) {
                            contentNew = new StringBuilder(content.substring(0, iPos - iSameCount + 1));
                            sEm = new StringBuilder(content.substring(iPos - iSameCount, iPos - 1));
                        } else {
                            contentNew.append(content, iLastCopiedPos, iPos - iSameCount + 1);
                            sEm.append(content, iPos - iSameCount, iPos - 1);
                        }
                        iLastCopiedPos = iPos;
                    }

                    iSameCount = 0;
                }
            }

            if (iSameCount > 0 && this.options.sgIllegalDoubleLettersAtWordEnd.contains(content.substring(iPos - 1, iPos))) {
                ++iSameCount;
            }

            if (iSameCount > 1) {
                if (sEm.toString().equals("")) {
                    contentNew = new StringBuilder(content.substring(0, iPos - iSameCount + 1));
                    sEm = new StringBuilder(content.substring(iPos - iSameCount + 1));
                } else {
                    contentNew.append(content, iLastCopiedPos, iPos - iSameCount + 1);
                    sEm.append(content.substring(iPos - iSameCount + 1));
                }
            } else if (!sEm.toString().equals("")) {
                contentNew.append(content.substring(iLastCopiedPos));
            }
        }

        if (contentNew.toString().equals("")) {
            contentNew = new StringBuilder(content);
        }

        this.sgOriginalWord = content;
        this.sgWordEmphasis = sEm.toString();
        this.sgTranslatedWord = contentNew.toString();
        if (!this.sgTranslatedWord.contains("@")) {
            if (this.options.bgCorrectSpellingsUsingDictionary) {
                this.correctSpellingInTranslatedWord();
            }

            if (this.options.bgUseLemmatisation) {
                if (this.sgTranslatedWord.equals("")) {
                    contentNew = new StringBuilder(this.resources.lemmatiser.lemmatise(this.sgOriginalWord));
                    if (!contentNew.toString().equals(this.sgOriginalWord)) {
                        this.sgTranslatedWord = contentNew.toString();
                    }
                } else {
                    this.sgTranslatedWord = this.resources.lemmatiser.lemmatise(this.sgTranslatedWord);
                }
            }
        }

    }

    /**
     * 为单词进行拼写纠正.
     */
    private void correctSpellingInTranslatedWord() {
        if (!this.resources.correctSpellings.contain(this.sgTranslatedWord.toLowerCase())) {
            int iLastChar = this.sgTranslatedWord.length() - 1;

            for (int iPos = 1; iPos <= iLastChar; ++iPos) {
                if (this.sgTranslatedWord.substring(iPos, iPos + 1).compareTo(this.sgTranslatedWord.substring(iPos - 1, iPos)) == 0) {
                    String sReplaceWord = this.sgTranslatedWord.substring(0, iPos) + this.sgTranslatedWord.substring(iPos + 1);
                    if (this.resources.correctSpellings.contain(sReplaceWord.toLowerCase())) {
                        this.sgWordEmphasis = this.sgWordEmphasis + this.sgTranslatedWord.charAt(iPos);
                        this.sgTranslatedWord = sReplaceWord;
                        return;
                    }
                }
            }

            if (iLastChar > FIVE) {
                if (this.sgTranslatedWord.indexOf("haha") > 0) {
                    this.sgWordEmphasis = this.sgWordEmphasis + this.sgTranslatedWord.substring(THREE, this.sgTranslatedWord.indexOf("haha") + 2);
                    this.sgTranslatedWord = "haha";
                    return;
                }

                if (this.sgTranslatedWord.indexOf("hehe") > 0) {
                    this.sgWordEmphasis = this.sgWordEmphasis + this.sgTranslatedWord.substring(THREE, this.sgTranslatedWord.indexOf("hehe") + 2);
                    this.sgTranslatedWord = "hehe";
                }
            }

        }
    }


    /**
     * 获得WordEmphasisLength.
     *
     * @return WordEmphasis的长度
     */
    @Override
    public int getEmphasisLength() {
        return this.sgWordEmphasis.length();
    }

    /**
     * 获得WordEmphasisLength.
     *
     * @return WordEmphasis的长度
     */
    public int getWordEmphasisLength() {
        return this.sgWordEmphasis.length();
    }


    /**
     * 获得强调，例如单词中的重复字母，具体例子：Gooooood的oooooo.
     *
     * @return term中的强调
     */
    public String getWordEmphasis() {
        return this.sgWordEmphasis;
    }

    /**
     * 获得经过翻译处理后的单词.
     *
     * @return 经过翻译处理后的单词
     */
    public String getTranslatedWord() {
        return this.sgTranslatedWord;
    }

    /**
     * 获得经过翻译处理后的单词.
     *
     * @return 经过翻译处理后的单词
     */
    @Override
    public String getTranslation() {
        return this.sgTranslatedWord;
    }

    /**
     * 判断term中是否含有强调.
     *
     * @return sgWordEmphasis的长度大于一返回true，否则为false
     */
    @Override
    public boolean containsEmphasis() {
        return this.sgWordEmphasis.length() > 1;
    }


    /**
     * 获得代表该term实例对象的内容.
     *
     * @return 返回term的内容
     */
    @Override
    public String getText() {
        return this.sgTranslatedWord.toLowerCase();
    }


    /**
     * 获得未经过处理的源内容.
     *
     * @return 未经过处理的源内容
     */
    @Override
    public String getOriginalText() {
        return this.sgOriginalWord;
    }


    /**
     * 根据 sgWordEmphasis 是否为空来返回不同的字符串.
     * 如果 sgWordEmphasis 不为空，则返回带有 equiv 和 em 属性的 w 标签，
     * 否则只返回一个简单的 w 标签。
     *
     * @return 返回相应的字符串
     */
    @Override
    public String getTag() {
        if (!this.sgWordEmphasis.equals("")) {
            return "<w equiv=\"" + this.sgTranslatedWord + "\" em=\"" + this.sgWordEmphasis + "\">" + this.sgOriginalWord + "</w>";
        }
        return "<w>" + this.sgOriginalWord + "</w>";
    }


    /**
     * 获得助词分数，如果没有在实例对象中设置过则从数据源中找到助词分数.
     *
     * @return 助词分数
     */
    public int getBoosterWordScore() {
        if (this.igBoosterWordScore == NOTPROCESSED) {
            this.setBoosterWordScore();
        }

        return this.igBoosterWordScore;
    }


    /**
     * 判断是否全为大写，并将结果赋予bgAllCapitals，并设置bgAllCaptialsCalculated为true.
     *
     * @return 若sgOriginalWord全为大写字母则返回true，否则false
     */
    public boolean isAllCapitals() {
        if (!this.bgAllCaptialsCalculated) {
            this.bgAllCapitals = this.sgOriginalWord.equals(this.sgOriginalWord.toUpperCase());

            this.bgAllCaptialsCalculated = true;
        }

        return this.bgAllCapitals;
    }


    /**
     * 设置助词分数.
     */
    public void setBoosterWordScore() {
        this.igBoosterWordScore = this.resources.boosterWords.getStrength(this.sgTranslatedWord);
    }


    /**
     * 判断代表这个对象的单词是否是一个专有名词，并告知该term对象已经处理过该方法.
     *
     * @return 如果是专有名词，返回true，否则返回false
     */
    public boolean isProperNoun() {
        if (!this.bgProperNounCalculated) {
            if (this.sgOriginalWord.length() > 1) {
                String sFirstLetter = this.sgOriginalWord.substring(0, 1);
                if (!sFirstLetter.toLowerCase().equals(sFirstLetter.toUpperCase()) && !this.sgOriginalWord.substring(0, 2).equalsIgnoreCase("I'")) {
                    String sWordRemainder = this.sgOriginalWord.substring(1);
                    if (sFirstLetter.equals(sFirstLetter.toUpperCase()) && sWordRemainder.equals(sWordRemainder.toLowerCase())) {
                        this.bgProperNoun = true;
                    }
                }
            }
            this.bgProperNounCalculated = true;
        }
        return this.bgProperNoun;
    }

    /**
     * 判断代表该term对象的内容是否是一个否定词.
     *
     * @return 如果是一个否定词，返回true，否则返回false
     */
    public boolean isNegatingWord() {     //word专属
        if (!this.bgNegatingWordCalculated) {
            if (this.sgLCaseWord.length() == 0) {
                this.sgLCaseWord = this.sgTranslatedWord.toLowerCase();
            }

            this.bgNegatingWord = this.resources.negatingWords.contain(this.sgLCaseWord);
            this.bgNegatingWordCalculated = true;
        }

        return this.bgNegatingWord;
    }

    /**
     * 在代表term的内容中匹配指定的字符串.
     *
     * @param sText               指定的匹配字符串
     * @param bConvertToLowerCase 如果为true，则在比较之前，要匹配的单词和文本都将转换为小写
     * @return 如果成功匹配，返回true,否则false
     */
    public boolean matchesString(String sText, boolean bConvertToLowerCase) { //word专属
        if (sText.length() == this.sgTranslatedWord.length()) {
            if (bConvertToLowerCase) {
                if (this.sgLCaseWord.length() == 0) {
                    this.sgLCaseWord = this.sgTranslatedWord.toLowerCase();
                }

                if (sText.equals(this.sgLCaseWord)) {
                    return true;
                }
            } else if (sText.equals(this.sgTranslatedWord)) {
                return true;
            }

        }
        return false;
    }


    /**
     * 确定此对象表示的单词是否与带有通配符的给定文本匹配.
     *
     * @param sTextWithWildcard   要匹配的文本，可能包含通配符（*）
     * @param bConvertToLowerCase 如果为true，则在比较之前，要匹配的单词和文本都将转换为小写
     * @return 如果单词与带有通配符的给定文本匹配，则返回true；否则为false
     */
    public boolean matchesStringWithWildcard(String sTextWithWildcard, boolean bConvertToLowerCase) { //word专属
        int iStarPos = sTextWithWildcard.lastIndexOf("*");
        if (iStarPos >= 0 && iStarPos == sTextWithWildcard.length() - 1) {
            sTextWithWildcard = sTextWithWildcard.substring(0, iStarPos);
            if (bConvertToLowerCase) {
                if (this.sgLCaseWord.length() == 0) {
                    this.sgLCaseWord = this.sgTranslatedWord.toLowerCase();
                }

                if (sTextWithWildcard.equals(this.sgLCaseWord)) {
                    return true;
                }

                if (sTextWithWildcard.length() >= this.sgLCaseWord.length()) {
                    return false;
                }

                if (sTextWithWildcard.equals(this.sgLCaseWord.substring(0, sTextWithWildcard.length()))) {
                    return true;
                }
            } else {
                if (sTextWithWildcard.equals(this.sgTranslatedWord)) {
                    return true;
                }

                if (sTextWithWildcard.length() >= this.sgTranslatedWord.length()) {
                    return false;
                }

                if (sTextWithWildcard.equals(this.sgTranslatedWord.substring(0, sTextWithWildcard.length()))) {
                    return true;
                }
            }

            return false;
        } else {
            return this.matchesString(sTextWithWildcard, bConvertToLowerCase);
        }
    }


    /**
     * 获得实例对象term的SentimentID,如果还没有calculated过，则从数据源中找到sentimentID并赋予this.igWordSentimentID并设置为已经calculated过.
     *
     * @return SentimentID
     */
    public int getSentimentID() {
        if (!this.bgWordSentimentIDCalculated) {
            this.igWordSentimentID = this.resources.sentimentWords.getSentimentID(this.sgTranslatedWord.toLowerCase());
            this.bgWordSentimentIDCalculated = true;
        }

        return this.igWordSentimentID;
    }

    /**
     * 设置OverrideSentimentScore的值，并令this.bgOverrideSentimentScore = true.
     *
     * @param iSentiment OverrideSentimentScore需要被设置的值
     */
    public void setSentimentOverrideValue(int iSentiment) {
        this.bgOverrideSentimentScore = true;
        this.igOverrideSentimentScore = iSentiment;
    }

    /**
     * 获得SentimentValue的值.
     *
     * @return 如果OverrideSentimentScore被设置过了，则返回OverrideSentimentScore的值，否则从数据源中获得value
     */
    public int getSentimentValue() {
        if (this.bgOverrideSentimentScore) {
            return this.igOverrideSentimentScore;
        } else {
            return this.getSentimentID() < 1 ? 0 : this.resources.sentimentWords.getSentiment(this.igWordSentimentID);
        }
    }


}

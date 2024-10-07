package uk.ac.wlv.sentistrength.paragraph.paragraph;

public class AverageScoreParagraph extends Paragraph {
    private static final float DOTFIVE = 0.5f;

    /**
     * 调用父类方法.
     */
    protected void calculateParagraphSentimentScores() {
        super.calculateParagraphSentimentScores();
    }

    /**
     * 计算情感得分.
     *
     * @return 是否成功
     */
    protected boolean calculatePosAndNegSentimentScores() {

        int iPosTotal = 0;
        int iNegTotal = 0;
        int iSentencesUsed = 0;
        int sentenceIndex;
        int iPosTemp;
        int iNegTemp;
        for (sentenceIndex = 1; sentenceIndex <= this.igSentenceCount; ++sentenceIndex) {
            iNegTemp = this.sentence[sentenceIndex].getSentenceNegativeSentiment();
            iPosTemp = this.sentence[sentenceIndex].getSentencePositiveSentiment();
            if (iNegTemp != 0 || iPosTemp != 0) {
                iNegTotal += iNegTemp;
                ++iSentencesUsed;
                iPosTotal += iPosTemp;
            }
            sgClassificationRationale.append(this.sentence[sentenceIndex].getClassificationRationale()).append(" ");
        }

        if (iNegTotal == 0) {
            this.igPositiveSentiment = 0;
            this.igNegativeSentiment = 0;
            this.igTrinarySentiment = this.binarySelectionTieBreaker();
            return false;
        }

        this.igPositiveSentiment = (int) ((double) ((float) iPosTotal / (float) iSentencesUsed) + DOTFIVE);
        this.igNegativeSentiment = (int) ((double) ((float) iNegTotal / (float) iSentencesUsed) - DOTFIVE);

        sgClassificationRationale.append("[result = average (").append(iPosTotal).append(" and ").append(iNegTotal).append(") of ").append(iSentencesUsed).append(" sentences]");

        if (this.igPositiveSentiment == 0) {
            this.igPositiveSentiment = 1;
        }

        if (this.igNegativeSentiment == 0) {
            this.igNegativeSentiment = -1;
        }
        return true;
    }

    /**
     * 计算trinary得分.
     */
    protected void calculateTrinaryScores() {
        if (this.igPositiveSentiment == 1 && this.igNegativeSentiment == -1) {
            if (this.options.bgBinaryVersionOfTrinaryMode) {
                this.igTrinarySentiment = this.binarySelectionTieBreaker();
                sgClassificationRationale.append("[binary result = default value as pos=1 neg=-1]");
            } else {
                this.igTrinarySentiment = 0;
                sgClassificationRationale.append("[trinary result = 0 as pos=1 neg=-1]");
            }

            return;
        }

        if (this.igPositiveSentiment > -this.igNegativeSentiment) {
            this.igTrinarySentiment = 1;

            sgClassificationRationale.append("[overall result = 1 as pos>-neg]");

            return;
        }

        if (this.igPositiveSentiment < -this.igNegativeSentiment) {
            this.igTrinarySentiment = -1;
            sgClassificationRationale.append("[overall result = -1 as pos<-neg]");
            return;
        }

        int iNegTot = 0;
        int iPosTot = 0;

        for (int iSentence = 1; iSentence <= this.igSentenceCount; ++iSentence) {
            iNegTot += this.sentence[iSentence].getSentenceNegativeSentiment();
            iPosTot += this.sentence[iSentence].getSentencePositiveSentiment();
        }

        if (this.options.bgBinaryVersionOfTrinaryMode && iPosTot == -iNegTot) {
            this.igTrinarySentiment = this.binarySelectionTieBreaker();
            sgClassificationRationale.append("[binary result = default as posSentenceTotal>-negSentenceTotal]");
        } else {

            sgClassificationRationale.append("[overall result = largest of posSentenceTotal, negSentenceTotal]");
            if (iPosTot > -iNegTot) {
                this.igTrinarySentiment = 1;
            } else {
                this.igTrinarySentiment = -1;
            }
        }
    }

    /**
     * 计算Scale.
     */
    protected void calculateScaleScores() {
        super.calculateScaleScores();
    }
}


package uk.ac.wlv.sentistrength.paragraph.paragraph;

public class TotalScoreParagraph extends Paragraph {
    /**
     * 计算段落情感得分.
     */
    protected void calculateParagraphSentimentScores() {
        super.calculateParagraphSentimentScores();
    }

    /**
     * 计算积极消极得分.
     *
     * @return 是否成功
     */
    protected boolean calculatePosAndNegSentimentScores() {

        int iPosTotal = 0;
        int iNegTotal = 0;
        int iPosTemp;
        int iNegTemp;
        int sentenceIndex;
        for (sentenceIndex = 1; sentenceIndex <= this.igSentenceCount; ++sentenceIndex) {
            iNegTemp = this.sentence[sentenceIndex].getSentenceNegativeSentiment();
            iPosTemp = this.sentence[sentenceIndex].getSentencePositiveSentiment();
            if (iNegTemp != 0 || iPosTemp != 0) {
                iNegTotal += iNegTemp;
                iPosTotal += iPosTemp;
            }
            sgClassificationRationale.append(this.sentence[sentenceIndex].getClassificationRationale()).append(" ");
        }

        this.igPositiveSentiment = iPosTotal;
        this.igNegativeSentiment = iNegTotal;
        sgClassificationRationale.append("[result: total positive; total negative]");

        return true;
    }

    /**
     * 计算trinary得分.
     */
    protected void calculateTrinaryScores() {
        if (this.igPositiveSentiment == 0 && this.igNegativeSentiment == 0) {
            if (this.options.bgBinaryVersionOfTrinaryMode) {
                this.igTrinarySentiment = this.options.igDefaultBinaryClassification;
                sgClassificationRationale.append("[binary result set to default value]");
            } else {
                this.igTrinarySentiment = 0;
                sgClassificationRationale.append("[trinary result 0 as pos=1, neg=-1]");
            }
        } else {
            if ((float) this.igPositiveSentiment > this.options.fgNegativeSentimentMultiplier * (float) (-this.igNegativeSentiment)) {
                this.igTrinarySentiment = 1;
                sgClassificationRationale.append("[overall result 1 as pos > -neg * ").append(this.options.fgNegativeSentimentMultiplier).append("]");
                return;
            }

            if ((float) this.igPositiveSentiment < this.options.fgNegativeSentimentMultiplier * (float) (-this.igNegativeSentiment)) {
                this.igTrinarySentiment = -1;
                sgClassificationRationale.append("[overall result -1 as pos < -neg * ").append(this.options.fgNegativeSentimentMultiplier).append("]");
                return;
            }

            if (this.options.bgBinaryVersionOfTrinaryMode) {
                this.igTrinarySentiment = this.options.igDefaultBinaryClassification;
                sgClassificationRationale.append("[binary result = default value as pos = -neg * ").append(this.options.fgNegativeSentimentMultiplier).append("]");
            } else {
                this.igTrinarySentiment = 0;
                sgClassificationRationale.append("[trinary result = 0 as pos = -neg * ").append(this.options.fgNegativeSentimentMultiplier).append("]");
            }
        }
    }

    /**
     * 计算scale得分.
     */
    protected void calculateScaleScores() {
        super.calculateScaleScores();
    }
}

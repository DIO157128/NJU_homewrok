package uk.ac.wlv.sentistrength.paragraph.paragraph;

public class MaxScoreParagraph extends Paragraph {
    /**
     * 调用父类方法.
     */
    protected void calculateParagraphSentimentScores() {
        super.calculateParagraphSentimentScores();
    }

    /**
     * 用所有句子中最高的积极情绪和最高的消极情绪作为最终输出值.
     *
     * @return 成功计算 true,否则false
     */
    protected boolean calculatePosAndNegSentimentScores() {
        int iPosMax = 0;
        int iNegTotal = 0;
        int iNegMax = 0;
        int iPosTemp;
        int iNegTemp;
        int sentenceIndex;
        for (sentenceIndex = 1; sentenceIndex <= this.igSentenceCount; ++sentenceIndex) {
            iNegTemp = this.sentence[sentenceIndex].getSentenceNegativeSentiment();
            iPosTemp = this.sentence[sentenceIndex].getSentencePositiveSentiment();
            if (iNegTemp != 0 || iPosTemp != 0) {
                iNegTotal += iNegTemp;
                if (iNegMax > iNegTemp) {
                    iNegMax = iNegTemp;
                }
                if (iPosMax < iPosTemp) {
                    iPosMax = iPosTemp;
                }
            }

            sgClassificationRationale.append(this.sentence[sentenceIndex].getClassificationRationale()).append(" ");
        }


        if (iNegTotal == 0) {
            this.igPositiveSentiment = 0;
            this.igNegativeSentiment = 0;
            this.igTrinarySentiment = this.binarySelectionTieBreaker();
            return false;
        }

        this.igPositiveSentiment = iPosMax;
        this.igNegativeSentiment = iNegMax;
        sgClassificationRationale.append("[result: max + and - of any sentence]");

        if (this.igPositiveSentiment == 0) {
            this.igPositiveSentiment = 1;
        }

        if (this.igNegativeSentiment == 0) {
            this.igNegativeSentiment = -1;
        }

        return true;
    }


    /**
     * 计算中性得分.
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
     * 计算规模分数.
     */
    protected void calculateScaleScores() {
        super.calculateScaleScores();
    }
}

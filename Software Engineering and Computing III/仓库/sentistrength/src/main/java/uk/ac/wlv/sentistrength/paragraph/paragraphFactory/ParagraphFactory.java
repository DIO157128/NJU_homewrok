package uk.ac.wlv.sentistrength.paragraph.paragraphFactory;

import uk.ac.wlv.sentistrength.paragraph.paragraph.AverageScoreParagraph;
import uk.ac.wlv.sentistrength.paragraph.paragraph.MaxScoreParagraph;
import uk.ac.wlv.sentistrength.paragraph.paragraph.Paragraph;
import uk.ac.wlv.sentistrength.paragraph.paragraph.TotalScoreParagraph;

/**
 * 工厂类，用于制造paragraph.
 */
public class ParagraphFactory {

    /**
     * 根据传入的值选择paragraph.
     *
     * @param option 选项
     * @return paragraph
     */
    public Paragraph createParagraph(int option) {
        Paragraph paragraph = null;
        switch (option) {
            case 0:
                paragraph = new MaxScoreParagraph();
                break;
            case 1:
                paragraph = new AverageScoreParagraph();
                break;
            case 2:
                paragraph = new TotalScoreParagraph();
                break;
            default:
                break;
        }
        return paragraph;
    }
}

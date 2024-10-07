package uk.ac.wlv.sentistrength.dictionary;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import org.junit.Assert;
import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.dictionary.dicList.CorrectSpellingsList;
import uk.ac.wlv.sentistrength.dictionary.dicList.IronyList;
import uk.ac.wlv.sentistrength.dictionary.dicList.NegatingWordList;
import uk.ac.wlv.sentistrength.dictionary.dicList.QuestionWords;

import java.io.*;

/**
* DicList Tester.
*
* @author <Authors name>
* @since <pre>04/08/2023</pre>
* @version 1.0
*/
public class DicListTest extends TestCase {
public DicListTest(String name) {
super(name);
}

public void setUp() throws Exception {
super.setUp();
}

public void tearDown() throws Exception {
super.tearDown();
}


    public void testCorrectSpellingsListInitialise() throws Exception {


    CorrectSpellingsList correctSpellingsList = new CorrectSpellingsList();
    String filename = "src/main/java/SentStrength_Data/QuestionWords.txt";

    ClassificationOptions classificationOptions = new ClassificationOptions();
    Assert.assertTrue(correctSpellingsList.initialise(filename,classificationOptions.bgForceUTF8));
}

    public void testCorrectSpellingsListContain() throws Exception {



    CorrectSpellingsList correctSpellingsList = new CorrectSpellingsList();
    String filename = "src/main/java/SentStrength_Data/QuestionWords.txt";

    ClassificationOptions classificationOptions = new ClassificationOptions();

    correctSpellingsList.initialise(filename,classificationOptions.bgForceUTF8);
    FileInputStream fin = new FileInputStream(filename);
    InputStreamReader reader = new InputStreamReader(fin);
    BufferedReader buffReader = new BufferedReader(reader);
    String strTmp = "";
    while((strTmp = buffReader.readLine())!=null){
        Assert.assertTrue(correctSpellingsList.contain(strTmp));
    }
    buffReader.close();
}

    public void testIronyListInitialise() throws Exception {


        IronyList ironyList = new IronyList();
        String filename = "src/main/java/SentStrength_Data/QuestionWords.txt";

        ClassificationOptions classificationOptions = new ClassificationOptions();
        Assert.assertTrue(ironyList.initialise(filename,classificationOptions.bgForceUTF8));
    }
    public void testIronyListContain() throws Exception {



        IronyList ironyList = new IronyList();
        String filename = "src/main/java/SentStrength_Data/QuestionWords.txt";

        ClassificationOptions classificationOptions = new ClassificationOptions();

        ironyList.initialise(filename,classificationOptions.bgForceUTF8);
        FileInputStream fin = new FileInputStream(filename);
        InputStreamReader reader = new InputStreamReader(fin);
        BufferedReader buffReader = new BufferedReader(reader);
        String strTmp = "";
        while((strTmp = buffReader.readLine())!=null){
            Assert.assertTrue(ironyList.contain(strTmp));
        }
        buffReader.close();
    }
    /**
     *
     * Method: contain(String w)
     *
     */

    public void testNegatingWordListInitialise() throws Exception {


        NegatingWordList negatingWordList = new NegatingWordList();
        String filename = "src/main/java/SentStrength_Data/QuestionWords.txt";

        ClassificationOptions classificationOptions = new ClassificationOptions();
        Assert.assertTrue(negatingWordList.initialise(filename,classificationOptions.bgForceUTF8));
    }
    public void testNegatingWordListContain() throws Exception {



        NegatingWordList negatingWordList = new NegatingWordList();
        String filename = "src/main/java/SentStrength_Data/QuestionWords.txt";

        ClassificationOptions classificationOptions = new ClassificationOptions();

        negatingWordList.initialise(filename,classificationOptions.bgForceUTF8);
        FileInputStream fin = new FileInputStream(filename);
        InputStreamReader reader = new InputStreamReader(fin);
        BufferedReader buffReader = new BufferedReader(reader);
        String strTmp = "";
        while((strTmp = buffReader.readLine())!=null){
            Assert.assertTrue(negatingWordList.contain(strTmp));
        }
        buffReader.close();
    }
    /**
     *
     * Method: contain(String w)
     *
     */

    public void testQuestionWordsInitialise() throws Exception {


        QuestionWords questionWords = new QuestionWords();
        String filename = "src/main/java/SentStrength_Data/QuestionWords.txt";

        ClassificationOptions classificationOptions = new ClassificationOptions();
        Assert.assertTrue(questionWords.initialise(filename,classificationOptions.bgForceUTF8));
    }

    /**
     *
     * Method: contain(String w)
     *
     */
    public void testQuestionWordsContain() throws Exception {



        QuestionWords questionWords = new QuestionWords();
        String filename = "src/main/java/SentStrength_Data/QuestionWords.txt";

        ClassificationOptions classificationOptions = new ClassificationOptions();

        questionWords.initialise(filename,classificationOptions.bgForceUTF8);
        FileInputStream fin = new FileInputStream(filename);
        InputStreamReader reader = new InputStreamReader(fin);
        BufferedReader buffReader = new BufferedReader(reader);
        String strTmp = "";
        while((strTmp = buffReader.readLine())!=null){
            Assert.assertTrue(questionWords.contain(strTmp));
        }
        buffReader.close();
    }



    public static Test suite() {
return new TestSuite(DicListTest.class);
}
}

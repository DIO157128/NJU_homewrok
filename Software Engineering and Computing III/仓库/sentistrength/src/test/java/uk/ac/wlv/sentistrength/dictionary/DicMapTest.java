package uk.ac.wlv.sentistrength.dictionary;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import org.junit.Assert;
import uk.ac.wlv.sentistrength.ClassificationOptions;
import uk.ac.wlv.sentistrength.dictionary.dicMap.BoosterWordsMap;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
* DicMap Tester.
*
* @author <Authors name>
* @since <pre>04/17/2023</pre>
* @version 1.0
*/
public class DicMapTest extends TestCase {
public DicMapTest(String name) {
super(name);
}

public void setUp() throws Exception {
super.setUp();
}

public void tearDown() throws Exception {
super.tearDown();
}

/**
*
* Method: initialise(String fileName, boolean forceUTF8)
*
*/
public void testInitialise() throws Exception {
    BoosterWordsMap boosterWordsMap = new BoosterWordsMap();
    String filename = "src/main/java/SentStrength_Data/BoosterWordList.txt";
    ClassificationOptions classificationOptions = new ClassificationOptions();
    Assert.assertTrue(boosterWordsMap.initialise(filename,classificationOptions.bgForceUTF8));
}

/**
*
* Method: getStrength(String w)
*
*/
public void testGetStrength() throws Exception {
    BoosterWordsMap boosterWordsMap = new BoosterWordsMap();
    String filename = "src/main/java/SentStrength_Data/BoosterWordList.txt";
    ClassificationOptions classificationOptions = new ClassificationOptions();
    boosterWordsMap.initialise(filename,classificationOptions.bgForceUTF8);
    FileInputStream fin = new FileInputStream(filename);
    InputStreamReader reader = new InputStreamReader(fin);
    BufferedReader buffReader = new BufferedReader(reader);
    String strTmp = "";
    while((strTmp = buffReader.readLine())!=null){
        String[] w = strTmp.split("\t");
        Assert.assertEquals(Integer.parseInt(w[1]),boosterWordsMap.getStrength(w[0]));
    }
    buffReader.close();
}



public static Test suite() {
return new TestSuite(DicMapTest.class);
}
}

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.File;

public class classfier {
    public void NavieBayes(String trainpath, String testpath) throws Exception {
        DataReader dataReader = new DataReader();
        Instances  trainData =  dataReader.readData(trainpath);
        Instances  testData =  dataReader.readData(testpath);
        Classifier navieBayes = new NaiveBayes();
        navieBayes.buildClassifier(trainData);

        Evaluation testEvaluation = new Evaluation(testData);
        int length = testData.numInstances();
        for (int i = 0; i < length; i++) {
            Instance temins = testData.instance(i);
            testEvaluation.evaluateModelOnceAndRecordPrediction(navieBayes, temins);
        }
        System.out.println("分类的正确率" + (1 - testEvaluation.errorRate()));
        System.out.println(testData.toSummaryString());
        System.out.println(testEvaluation.toMatrixString("=== Confusion Matrix ===\n"));
        System.out.println(testEvaluation.toClassDetailsString());

    }

    public static void main(String[] args) throws Exception {
        classfier classfier = new classfier();
        classfier.NavieBayes("C:\\Users\\dell\\Desktop\\大数据分析\\第四次作业\\classfier\\src\\main\\java\\car_data.arff","C:\\Users\\dell\\Desktop\\大数据分析\\第四次作业\\classfier\\src\\main\\java\\car_data.arff");
    }

}

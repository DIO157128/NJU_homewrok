import weka.attributeSelection.PrincipalComponents;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Bagging;
import weka.core.Instances;

public class DimensionReducer {

    public void PCA(String path) throws Exception{
        DataReader dataReader = new DataReader();
        Instances dataSet = dataReader.readData(path);
        PrincipalComponents pca = new PrincipalComponents();

        String[] options = {"-R","0.95"};
        pca.setOptions(options);
        pca.buildEvaluator(dataSet);
        System.out.println(pca.toString());
    }
    public static void main(String[] args) throws Exception {
        DimensionReducer dimensionReducer = new DimensionReducer();
        dimensionReducer.PCA("C:\\Users\\dell\\Desktop\\大数据分析\\第三次作业\\reduceDimension\\src\\main\\java\\cpu.arff");
    }
}

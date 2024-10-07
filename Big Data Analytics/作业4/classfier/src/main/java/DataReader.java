import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class DataReader {
    public Instances readData(String path) throws Exception {
        Instances dataSet = DataSource.read(path);
        if (dataSet.classIndex() == -1)
            dataSet.setClassIndex(dataSet.numAttributes() - 1);
        return dataSet;
    }
}

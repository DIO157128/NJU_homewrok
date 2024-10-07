import weka.clusterers.SimpleKMeans;
import weka.core.DistanceFunction;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.File;

public class cluster {
    public void K_means(String path) throws Exception {
        File file = new File(path);
        ArffLoader loader = new ArffLoader();
        loader.setFile(file);
        Instances dataSet = loader.getDataSet();
        SimpleKMeans kMeans = new SimpleKMeans();
        kMeans.setNumClusters(4);
        kMeans.buildClusterer(dataSet);
        System.out.println(kMeans.preserveInstancesOrderTipText());
        // 打印聚类结果
        System.out.println(kMeans.toString());
    }
    public static void main(String[] args) throws Exception {
        cluster cluster = new cluster();
        cluster.K_means("C:\\Users\\dell\\Desktop\\大数据分析\\第五次作业\\cluster\\src\\main\\java\\bmw-browsers.arff");
    }
}

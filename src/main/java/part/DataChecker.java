package part;

public class DataChecker {

    private double[][] data;
    private DataSpreader spreader;
    private double covarianceThreshold;
    //reverse spreading data buffer
    private double[][] rsdBuf;

    public DataChecker(DataSpreader spreader, double covarianceThreshold) {
        this.spreader = spreader;
        this.data = spreader.getData();
        this.covarianceThreshold = covarianceThreshold;
    }

    //为避免内存溢出，增加一种读取缓存文件的构造方式
    public DataChecker(String dataFilePath, DataSpreader spreader, double covarianceThreshold) {
        //todo 增加一种读取缓存文件的构造方式
    }

    public void doReverseSpreading(int x, int y) {
        //todo 用扩散器进行逆扩散
    }

    public boolean checkCovariance() {
        if(this.covarianceThreshold<this.getCovarianceBetweenDataAndRsdBuf()){
            return false;
        }
        else{
            return true;
        }
    }

    public double getCovarianceBetweenDataAndRsdBuf() {
        //todo 求取原矩阵与扩散矩阵的协方差
        return 0.0;
    }

    public boolean isPassed() {
        for (int i = 0; i < data.length; i++) {
            for (int u = 0; u < data[i].length; u++) {
                this.doReverseSpreading(i,u);
                if(!this.checkCovariance()){
                    return false;
                }
            }
        }
        return true;
    }

}

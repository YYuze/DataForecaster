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
//        if(this.covarianceThreshold<this.getCovMatrixBetweenDataAndRsdBuf()){
//            return false;
//        }
//        else{
//            return true;
//        }
        return false;
    }

    private double[][] getCovMatrix(double[][] data) {
        double[] varX = new double[data.length * data[0].length];
        double[] varY = new double[data.length * data[0].length];
        double[] varZ = new double[data.length * data[0].length];
        int index = 0;
        for (int i = 0; i < data.length; i++) {
            for (int u = 0; u < data[i].length; u++) {
                varX[index] = u;
                varY[index] = i;
                varZ[index] = data[i][u];
                index++;
            }
        }
        double covXX = this.getCov(varX, varX);
        double covYY = this.getCov(varY, varY);
        double covZZ = this.getCov(varZ, varZ);
        double covXY = this.getCov(varX, varY);
        double covXZ = this.getCov(varX, varZ);
        double covYZ = this.getCov(varY, varZ);
        /*
                     | cov(x,x) cov(x,y) cov(x,z) |
         covMatrix = | cov(y,x) cov(y,y) cov(y,z) |
                     | cov(z,x) cov(z,y) cov(z,z) |
         */
        double[][] covMatrix = new double[3][3];
        covMatrix[0][0] = covXX;
        covMatrix[1][1] = covYY;
        covMatrix[2][2] = covZZ;
        covMatrix[0][1] = covXY;
        covMatrix[1][0] = covXY;
        covMatrix[0][2] = covXZ;
        covMatrix[2][0] = covXZ;
        covMatrix[2][1] = covYZ;
        covMatrix[1][2] = covYZ;

        return covMatrix;
    }

    private double getCov(double[] var1, double[] var2) {
        if (var1.length != var2.length) {
            return -1.0;
        }
        double mean1 = this.getMean(var1);
        double mean2 = this.getMean(var2);
        int dataLength = var1.length;
        double sum = 0;
        for (int i = 0; i < dataLength; i++) {
            sum += (var1[i] - mean1) * (var2[i] - mean2);
        }
        return sum / dataLength;
    }

    private double getMean(double[] var) {
        double sum = 0;
        for (int i = 0; i < var.length; i++) {
            sum += var[i];
        }
        return sum / var.length;
    }

    public boolean isPassed() {
        for (int i = 0; i < data.length; i++) {
            for (int u = 0; u < data[i].length; u++) {
                this.doReverseSpreading(u, i);
                if (!this.checkCovariance()) {
                    return false;
                }
            }
        }
        return true;
    }

}

package part;

import vo.WeightModel;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.function.BiConsumer;

/*
 * 思想：实现以有限数据逼近所有数据的目标
 * 实现：首先以赋值点为中心扩散数据
 * 		 然后在扩散数据中加入抖动系数
 * 		 对扩散数据进行加权处理，权重主要分为两部分
 * 			1.扩散半径权重
 * 			2.被扩散的点相对于其他赋值点的位置权重
 */

/*
 * 优化方向：
 * 	1.优化扩散权重模型
 *  2.优化赋值点权重模型
 *  3.优化抖动系数
 *  4（重点）.引入残差分析 改变抖动系数进行多次模拟，最终将得到一个相对稳定的结果，即真实结果
 */
public class DataSpreader {

    private int width;
    private int height;
    private double[][] data;
    /*
     * 每个点都有到所有赋值点的权重 这个权重是用距离作为变量
     */
    private WeightModel[][] weightModel;
    private HashMap<Point, Double> valueMap;

    public DataSpreader(String csvFilePath, int width, int height) {
        this.width = width;
        this.height = height;
        data = new double[width][height];
        valueMap = new HashMap<Point, Double>();
        this.initValueMapFromCSV(csvFilePath, height);
        weightModel = new WeightModel[width][height];
        this.initWeight();
        this.initData();
    }

    private void initValueMapFromCSV(String csvFilePath, int height) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(csvFilePath), "utf-8"));
            String content = "";
            while ((content = reader.readLine()) != null) {
                String[] vars = content.split(",");
                int x = (int) (Math.cos((Double.valueOf(vars[0])) / 180 * Math.PI) * Double.valueOf(vars[1])) * 10;
                int y = height
                        - (int) (Math.sin((Double.valueOf(vars[0])) / 180 * Math.PI) * Double.valueOf(vars[1])) * 10;
                Point position = new Point(x, y);
                Double value = new Double(vars[2]);
                this.valueMap.put(position, value);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initWeight() {
        Point[] points = new Point[this.valueMap.size()];
        Iterator<Point> iter = this.valueMap.keySet().iterator();
        int index = 0;
        while (iter.hasNext()) {
            points[index] = (Point) iter.next();
            index++;
        }
        for (int i = 0; i < width; i++) {
            for (int u = 0; u < height; u++) {
                weightModel[i][u] = new WeightModel(i, u, points);
            }
        }
    }

    private void initData() {
        this.valueMap.forEach(new BiConsumer<Point, Double>() {
            @Override
            public void accept(Point position, Double value) {
                int x = (int) position.getX();
                int y = (int) position.getY();
                data[x][y] = value;
                spread(position, value);
            }
        });
    }

    private void spread(Point position, Double value) {
        int spreadRadium = (int) Math.sqrt(width * width + height * height);
        Random shake = new Random();
        for (int i = 1; i < spreadRadium; i++) {
            shake.setSeed(System.currentTimeMillis());
            int change = shake.nextInt() % 11 - 5;
            int startX = (int) position.getX() - i;
            int startY = (int) position.getY() - i;
            // 四条边
            for (int u = 0; u < 2 * i + 1; u++) {
                int spreadToX = startX;
                int spreadToY = startY + u;
                this.setSpreadData(position, new Point(spreadToX, spreadToY), value + change);
            }
            for (int u = 0; u < 2 * i + 1; u++) {
                int spreadToX = startX + 2 * i;
                int spreadToY = startY + u;
                this.setSpreadData(position, new Point(spreadToX, spreadToY), value + change);
            }
            // 去顶点
            for (int u = 1; u < 2 * i; u++) {
                int spreadToX = startX + u;
                int spreadToY = startY;
                this.setSpreadData(position, new Point(spreadToX, spreadToY), value + change);
            }
            for (int u = 1; u < 2 * i; u++) {
                int spreadToX = startX + u;
                int spreadToY = startY + 2 * i;
                this.setSpreadData(position, new Point(spreadToX, spreadToY), value + change);
            }
        }
    }

    private void setSpreadData(Point origin, Point spreadTo, double spreadValue) {
        if (spreadTo.getX() >= this.width || spreadTo.getY() >= this.height || spreadTo.getX() < 0
                || spreadTo.getY() < 0) {
            return;
        }
        double spreadWeight = this.weightModel[(int) spreadTo.getX()][(int) spreadTo.getY()].getWeightBySpreadRadium(origin.distance(spreadTo));
        double pointWeight = this.weightModel[(int) spreadTo.getX()][(int) spreadTo.getY()].getWeightToPoint(origin);
        data[(int) spreadTo.getX()][(int) spreadTo.getY()] += spreadValue * pointWeight * spreadWeight;
    }

    public double[][] getData() {
        return data;
    }

    public void generateDataFile(String filePath) throws IOException {
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
        for (int i = 0; i < data.length; i++) {
            for (int u = 0; u < data[i].length; u++) {
                String txt = String.valueOf(data[i][u]) + "\n";
                os.write(txt.getBytes());
                os.flush();
            }
        }
        os.close();
    }
}

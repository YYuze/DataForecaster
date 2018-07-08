package part;

import model.LineModel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ConrecMesher {

    private double[][] data;
    private double[] level;

    public ConrecMesher(double[][] data) {
        this.initData(data);
        this.initLevel();
    }

    private void initData(double[][] data) {
        this.data = data;
        for (int i = 0; i < this.data.length; i++) {
            for (int u = 0; u < this.data[i].length; u++) {
                if (this.data[i][u] > 100) {
                    this.data[i][u] = 100;
                }
                if (this.data[i][u] < 0) {
                    this.data[i][u] = 0;
                }
            }
        }
    }

    private void initLevel() {
        this.level = new double[100];
        for (int i = 0; i < 100; i++) {
            this.level[i] = i;
        }
    }

    private short getCase(short[] state) {
        if ((state[0] == state[1]) && (state[0] == 0) && (state[2] != 0)) {
            return 1;
        }
        if ((state[0] == state[2]) && (state[0] == 0) && (state[1] != 0)) {
            return 2;
        }
        if ((state[1] == state[2]) && (state[1] == 0) && (state[0] != 0)) {
            return 3;
        }
        if ((state[0] == 0) && (state[1] != state[2]) && (state[1] != 0) && (state[2] != 0)) {
            return 4;
        }
        if ((state[2] == 0) && (state[0] != state[1]) && (state[0] != 0) && (state[1] != 0)) {
            return 5;
        }
        if ((state[1] == 0) && (state[0] != state[2]) && (state[0] != 0) && (state[2] != 0)) {
            return 6;
        }
        if ((state[0] != 0) && (state[1] != 0) && (state[2] != 0)) {
            if ((state[1] == state[2]) && (state[0] != state[1])) {
                return 7;
            }
            if ((state[0] == state[2]) && (state[0] != state[1])) {
                return 8;
            }
            if ((state[0] == state[1]) && (state[0] != state[2])) {
                return 9;
            }
        }
        return 0;
    }

    /*
     * vertex第一个维度为点 第二个维度 0表示横坐标 1表示纵坐标 vertex[0]vertex[1]为网格顶点 vertex[2]点为网格中心
     */
    private LineModel getLine(short cas, double[] h, double[][] vertex, double horizen) {
        double x1 = 0;
        double y1 = 0;
        double x2 = 0;
        double y2 = 0;
        switch (cas) {
            case 1:
                x1 = vertex[0][0];
                y1 = vertex[0][1];
                x2 = vertex[1][0];
                y2 = vertex[1][1];
                break;
            case 2:
                x1 = vertex[0][0];
                y1 = vertex[0][1];
                x2 = vertex[2][0];
                y2 = vertex[2][1];
                break;
            case 3:
                x1 = vertex[1][0];
                y1 = vertex[1][1];
                x2 = vertex[2][0];
                y2 = vertex[2][1];
                break;
            case 4:
                x1 = vertex[0][0];
                y1 = vertex[0][1];
                x2 = this.sect(vertex[2][0], vertex[1][0], h[2], h[1], horizen);
                y2 = this.sect(vertex[2][1], vertex[1][1], h[2], h[1], horizen);
                break;
            case 5:
                x1 = vertex[2][0];
                y1 = vertex[2][1];
                x2 = this.sect(vertex[0][0], vertex[1][0], h[0], h[1], horizen);
                y2 = this.sect(vertex[0][1], vertex[1][1], h[0], h[1], horizen);
                break;
            case 6:
                x1 = vertex[1][0];
                y1 = vertex[1][1];
                x2 = this.sect(vertex[0][0], vertex[2][0], h[0], h[2], horizen);
                y2 = this.sect(vertex[0][1], vertex[2][1], h[0], h[2], horizen);
                break;
            case 7:
                x1 = this.sect(vertex[0][0], vertex[1][0], h[0], h[1], horizen);
                y1 = this.sect(vertex[0][1], vertex[1][1], h[0], h[1], horizen);
                x2 = this.sect(vertex[0][0], vertex[2][0], h[0], h[2], horizen);
                y2 = this.sect(vertex[0][1], vertex[2][1], h[0], h[2], horizen);
                break;
            case 8:
                x1 = this.sect(vertex[0][0], vertex[1][0], h[0], h[1], horizen);
                y1 = this.sect(vertex[0][1], vertex[1][1], h[0], h[1], horizen);
                x2 = this.sect(vertex[2][0], vertex[1][0], h[2], h[1], horizen);
                y2 = this.sect(vertex[2][1], vertex[1][1], h[2], h[1], horizen);
                break;
            case 9:
                x1 = this.sect(vertex[0][0], vertex[2][0], h[0], h[2], horizen);
                y1 = this.sect(vertex[0][1], vertex[2][1], h[0], h[2], horizen);
                x2 = this.sect(vertex[2][0], vertex[1][0], h[2], h[1], horizen);
                y2 = this.sect(vertex[2][1], vertex[1][1], h[2], h[1], horizen);
                break;
            default:
                return new LineModel(0, 0, 0, 0, 0);
        }
        return new LineModel(x1, y1, x2, y2, horizen);
    }

    private double sect(double v1, double v2, double h1, double h2, double horizen) {
        double length = h1 - h2;
        double k = 0;
        if (length < 0) {
            k = horizen / (-length);
        }
        else {
            k = 1.0 - horizen / length;
        }

        return (1 - k) * (v2 - v1) + v1;
    }

    private LineModel[] getLinesInGrid(int x, int y, double horizen) {
        double[] h = new double[5];
        double[][] vertex = {{x + 0.5, y + 0.5}, {x, y}, {x + 1, y}, {x + 1, y + 1}, {x, y + 1}};
        h[1] = this.data[x][y] - horizen;
        h[2] = this.data[x + 1][y] - horizen;
        h[3] = this.data[x + 1][y + 1] - horizen;
        h[4] = this.data[x][y + 1] - horizen;
        h[0] = (h[1] + h[2] + h[3] + h[4]) / 4;
        short[] state = new short[5];
        for (int i = 0; i < 5; i++) {
            if (h[i] > 0) {
                state[i] = 1;
            }
            else if (h[i] < 0) {
                state[i] = -1;
            }
            else {
                state[i] = 0;
            }
        }

        // 第一个三角由h[4],h[1],h[0]
        short[] state1 = {state[4], state[1], state[0]};
        double[] h1 = {h[4], h[1], h[0]};
        short cas1 = this.getCase(state1);
        double[][] v1 = {vertex[4], vertex[1], vertex[0]};
        LineModel lineModel1 = this.getLine(cas1, h1, v1, horizen);

        // 第二个三角由h[1],[2],h[0]
        short[] state2 = {state[1], state[2], state[0]};
        double[] h2 = {h[1], h[2], h[0]};
        short cas2 = this.getCase(state2);
        double[][] v2 = {vertex[1], vertex[2], vertex[0]};
        LineModel lineModel2 = this.getLine(cas2, h2, v2, horizen);

        // 第三个三角由h[2],h[3],h[0]
        short[] state3 = {state[2], state[3], state[0]};
        double[] h3 = {h[2], h[3], h[0]};
        short cas3 = this.getCase(state3);
        double[][] v3 = {vertex[2], vertex[3], vertex[0]};
        LineModel lineModel3 = this.getLine(cas3, h3, v3, horizen);

        // 第四个三角由h[3],h[4],h[0]
        short[] state4 = {state[3], state[4], state[0]};
        double[] h4 = {h[3], h[4], h[0]};
        short cas4 = this.getCase(state4);
        double[][] v4 = {vertex[3], vertex[4], vertex[0]};
        LineModel lineModel4 = this.getLine(cas4, h4, v4, horizen);

        LineModel[] lineModels = {lineModel1, lineModel2, lineModel3, lineModel4};
        return lineModels;
    }

    private ArrayList<LineModel> getLinesOnHorizen(double horizen) {
        ArrayList<LineModel> collector = new ArrayList<LineModel>();
        for (int i = 1; i < data.length - 1; i++) {
            for (int u = 1; u < data[i].length - 1; u++) {

                LineModel[] gridLineModels = this.getLinesInGrid(i, u, horizen);
                for (int index = 0; index < gridLineModels.length; index++) {
                    collector.add(gridLineModels[index]);
                }
            }
        }
        return collector;
    }

    public void generateMeshingFile(String filePath) throws IOException {
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
        for (int i = 0; i < this.level.length; i++) {
            ArrayList<LineModel> linesOnLevel = this.getLinesOnHorizen(this.level[i]);
            Iterator<LineModel> iter = linesOnLevel.iterator();
            while (iter.hasNext()) {
                LineModel lineModel = iter.next();
                String x1 = String.valueOf(lineModel.getLine().getX1());
                String y1 = String.valueOf(lineModel.getLine().getY1());
                String x2 = String.valueOf(lineModel.getLine().getX2());
                String y2 = String.valueOf(lineModel.getLine().getY2());
                String rgb = String.valueOf(lineModel.getLineRGB());
                String txt = x1 + "," + y1 + "," + x2 + "," + y2 + "," + rgb + "\n";
                os.write(txt.getBytes());
            }
            System.out.println("processing: " + i + "/" + this.level.length);
        }
        os.close();
    }
}

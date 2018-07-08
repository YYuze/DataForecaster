package Layer;

import java.awt.*;
import java.util.HashMap;

public class Weight {

    private HashMap<Point, Double> weight;

    public Weight(int x, int y, Point[] valuedPoints) {
        this.weight = new HashMap<Point, Double>();
        for (int i = 0; i < valuedPoints.length; i++) {
            double distance = valuedPoints[i].distance(x, y);
            double w = this.getWeightByDistance(distance);
            this.weight.put(valuedPoints[i], w);
        }
    }

    // 模型可优化
    private double getWeightByDistance(double distance) {
        return Math.pow(0.97, distance);
    }

    public double getWeightToPoint(Point target) {
        return weight.get(target);
    }
}

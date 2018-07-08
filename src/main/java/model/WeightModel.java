package model;

import java.awt.*;
import java.util.HashMap;

public class WeightModel {

    private HashMap<Point, Double> weight;

    private double[][] pointWeightCoefficientMatrix = {
            {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1}
    };

    private double pointWeightRadix = Math.E;

    private double[][] distanceWeightCoefficientMatrix = {
            {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1}
    };

    private double distanceWeightRadix = Math.E;

    public WeightModel(int x, int y, Point[] valuedPoints) {
        this.weight = new HashMap<>();
        for (int i = 0; i < valuedPoints.length; i++) {
            double distance = valuedPoints[i].distance(x, y);
            double w = this.getWeightByPoint(distance);
            this.weight.put(valuedPoints[i], w);
        }
    }

    // 模型可优化
    private double getWeightByPoint(double distance) {
        return new GlobalMathematicModel(this.pointWeightCoefficientMatrix,this.pointWeightRadix).getModelValue(distance);
    }

    public double getWeightBySpreadRadium(double spreadRadium) {
        return new GlobalMathematicModel(this.distanceWeightCoefficientMatrix,this.distanceWeightRadix).getModelValue(spreadRadium);
    }

    public double getWeightToPoint(Point target) {
        return weight.get(target);
    }


    class GlobalMathematicModel{
        private double[][] coefficientMatrix;
        private double radix;

        public GlobalMathematicModel(double[][] coefficientMatrix, double radix) {
            this.coefficientMatrix = coefficientMatrix;
            this.radix = radix;
        }

        public double getModelValue(double var){
            double[] c = new double[this.coefficientMatrix.length];
            for (int i = 0; i < c.length; i++) {
                c[i] = 0;
            }
            for (int i = 0; i < this.coefficientMatrix.length; i++) {
                for (int j = 0; j < this.coefficientMatrix[i].length; j++) {
                    c[i] += Math.pow(var, i) * this.coefficientMatrix[i][j];
                }
            }
            double result = c[0] * Math.pow(this.radix, c[1]) + c[2];
            return result;
        }
    }
}


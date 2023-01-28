package backend.signal.continuous;

import backend.signal.AbstractSignal;

import java.util.Map;

public class ContinuousSignal extends AbstractSignal {
    protected double t1;
    protected double d;

    public ContinuousSignal(double A, double t1, double d) {
        super(A, d);
        this.t1 = t1;
    }

    public ContinuousSignal(Map<Double, Double> points) {
        super(points);
    }

    public void calculateAllPoints() {
        int t1Rounded = (int) (t1 * pointsDecimalPlacesDivision);
        int t2Rounded = (int) ((t1 + d) * pointsDecimalPlacesDivision);
        for (int i = t1Rounded; i < t2Rounded; i++) {
            double pointX = i / pointsDecimalPlacesDivision;
            points.put(pointX, calculatePointValue(pointX));
        }
    }

    public double calculatePointValue(double x) {
        return 0;
    }

    @Override
    public double getAverage() {
        return 0;
    }

    @Override
    public double getAbsoluteAverage() {
        return 0;
    }

    @Override
    public double getEffectiveValue() {
        return 0;
    }

    @Override
    public double getVariance() {
        return 0;
    }

    @Override
    public double getMeanSpeed() {
        return 0;
    }

    @Override
    public Map<Double, Double> getHistogramData() {
        return null;
    }
}

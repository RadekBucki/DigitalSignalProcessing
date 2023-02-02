package backend.signal;

import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;

import java.util.Map;

public class ContinuousSignal extends AbstractSignal {
    private final SimpsonIntegrator si = new SimpsonIntegrator();
    protected double t1;
    private final double t2 = t1 + d;

    public ContinuousSignal(double A, double d, double t1) {
        super(A, d);
        this.t1 = t1;
    }

    public ContinuousSignal(Map<Double, Double> points) {
        super(points);
    }

    public void calculateAllPoints() {
        int t1Rounded = (int) (t1 * POINTS_DECIMAL_PLACES_DIVISION);
        int t2Rounded = (int) ((t1 + d) * POINTS_DECIMAL_PLACES_DIVISION);
        for (int i = t1Rounded; i <= t2Rounded; i++) {
            double pointX = i / POINTS_DECIMAL_PLACES_DIVISION;
            points.put(pointX, calculatePointValue(pointX));
        }
    }

    public double calculatePointValue(double x) {
        return 0;
    }

    @Override
    public double getAverage() {
        return (1 / (t2 - t1)) * si.integrate(Integer.MAX_VALUE, (x) -> {
            double xRounded = Math.round(x * POINTS_DECIMAL_PLACES_DIVISION) / POINTS_DECIMAL_PLACES_DIVISION;
            return points.get(xRounded);
        }, t1, t2);
    }

    @Override
    public double getAbsoluteAverage() {
        return (1 / (t2 - t1)) * si.integrate(Integer.MAX_VALUE, (x) -> {
            double xRounded = Math.round(x * POINTS_DECIMAL_PLACES_DIVISION) / POINTS_DECIMAL_PLACES_DIVISION;
            return Math.abs(points.get(xRounded));
        }, t1, t2);
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

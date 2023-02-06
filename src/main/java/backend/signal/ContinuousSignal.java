package backend.signal;

import backend.simpson.DSPSimpsonIntegrator;

import java.util.Collections;
import java.util.Map;

public class ContinuousSignal extends AbstractSignal {
    private final DSPSimpsonIntegrator si = new DSPSimpsonIntegrator();
    protected double t1;
    protected Double t2;

    public ContinuousSignal(double A, double d, double t1) {
        super(A, d);
        this.t1 = t1;
        this.t2 = t1 + d;
    }

    public ContinuousSignal(Map<Double, Double> points) {
        super(points);
        this.t1 = Collections.min(points.keySet());
        this.t2 = Collections.max(points.keySet());
        this.A = Collections.max(points.values());
        this.d = t2 - t1;
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
        return points.get(Math.round(x * POINTS_DECIMAL_PLACES_DIVISION) / POINTS_DECIMAL_PLACES_DIVISION);
    }

    @Override
    public double getAverage() {
        return (1 / (t2 - t1)) * si.integrate(Integer.MAX_VALUE, this::calculatePointValue, t1, t2);
    }

    @Override
    public double getAbsoluteAverage() {
        return (1 / (t2 - t1)) * si.integrate(Integer.MAX_VALUE, x -> Math.abs(calculatePointValue(x)), t1, t2);
    }

    @Override
    public double getAveragePower() {
        return (1 / (t2 - t1)) * si.integrate(Integer.MAX_VALUE,
                x -> calculatePointValue(x) * calculatePointValue(x), t1, t2);
    }

    @Override
    public double getVariance() {
        double average = getAverage();
        return (1 / (t2 - t1)) * si.integrate(Integer.MAX_VALUE,
                x -> (calculatePointValue(x) - average) * (calculatePointValue(x) - average), t1, t2);
    }

    @Override
    public double getEffectiveValue() {
        return Math.sqrt((1 / (t2 - t1)) * si.integrate(Integer.MAX_VALUE,
                x -> calculatePointValue(x) * calculatePointValue(x), t1, t2));
    }

    public double getT1() {
        return t1;
    }

    public Double getT2() {
        return t2;
    }
}

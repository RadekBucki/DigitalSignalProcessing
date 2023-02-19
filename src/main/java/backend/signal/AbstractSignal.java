package backend.signal;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractSignal implements Serializable {
    protected static final double POINTS_DECIMAL_PLACES_DIVISION = 10000;
    protected Map<Double, Double> points = new LinkedHashMap<>();
    protected double d;
    protected double A;

    protected AbstractSignal(double A, double d) {
        this.A = A;
        this.d = d;
    }

    protected AbstractSignal(Map<Double, Double> points) {
        this.points = points;
    }

    public abstract double getAverage();
    public abstract double getAbsoluteAverage();
    public abstract double getAveragePower();
    public abstract double getVariance();
    public abstract double getEffectiveValue();
    public Map<Double, Double> getPoints() {
        return new LinkedHashMap<>(points);
    }

    public double getD() {
        return d;
    }

    public double getA() {
        return A;
    }
    public abstract double calculatePointValue(double x);

    public static double getPointsDecimalPlacesDivision() {
        return POINTS_DECIMAL_PLACES_DIVISION;
    }
}

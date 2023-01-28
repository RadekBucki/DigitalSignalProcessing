package backend.signal;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractSignal {
    protected static final double POINTS_DECIMAL_PLACES_DIVISION = 10000;
    protected Map<Double, Double> points = new LinkedHashMap<>();
    protected double A;
    protected double d;

    protected AbstractSignal(double A, double d) {
        this.A = A;
        this.d = d;
    }

    public abstract double getAverage();
    public abstract double getAbsoluteAverage();
    public abstract double getEffectiveValue();
    public abstract double getVariance();
    public abstract double getMeanSpeed();
    public Map<Double, Double> getAmplitudeFromTimeChartData() {
        return points;
    }
    public abstract Map<Double, Double> getHistogramData();
}

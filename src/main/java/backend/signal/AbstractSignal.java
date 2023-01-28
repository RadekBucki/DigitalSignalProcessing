package backend.signal;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSignal {
    protected static final double POINTS_DECIMAL_PLACES_DIVISION = 10000;
    protected Map<Double, Double> points = new HashMap<>();
    protected double A;

    protected AbstractSignal(double a) {
        A = a;
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

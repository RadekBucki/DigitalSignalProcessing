package backend.Signal;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSignal {
    protected final double pointsDecimalPlacesDivision = 10000;
    protected Map<Double, Double> points = new HashMap<>();
    protected double A;

    public AbstractSignal(double a) {
        A = a;
    }

    public abstract double getAverage();
    public abstract double getAbsoluteAverage();
    public abstract double getEffectiveValue();
    public abstract double getVariance();
    public abstract double getMeanSpeed();
    public abstract Map<Double, Double> getAmplitudeFromTimeChartData();
    public abstract Map<Double, Double> getHistogramData();
}

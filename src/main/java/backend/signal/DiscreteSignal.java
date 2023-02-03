package backend.signal;

import java.util.Map;

public class DiscreteSignal extends AbstractSignal {
    protected double f;

    public DiscreteSignal(double A, double d, double f) {
        super(A, d);
        this.f = f;
    }

    public DiscreteSignal(Map<Double, Double> points) {
        super(points);
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
    public double getAveragePower() {
        return 0;
    }

    @Override
    public double getVariance() {
        return 0;
    }

    @Override
    public double getEffectiveValue() {
        return 0;
    }

    @Override
    public Map<Double, Double> getHistogramData() {
        return null;
    }
}

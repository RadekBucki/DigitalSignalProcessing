package backend.signal;

import java.util.Map;

public class ContinuousSignal extends AbstractSignal {
    protected double t1;
    protected double d;

    public ContinuousSignal(double A, double t1, double d) {
        super(A, d);
        this.t1 = t1;
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

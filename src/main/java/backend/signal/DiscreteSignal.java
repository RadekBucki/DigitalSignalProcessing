package backend.signal;

import java.util.Map;

public class DiscreteSignal extends AbstractSignal {
    protected double f;

    public DiscreteSignal(double a, double f) {
        super(a);
        this.f = f;
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

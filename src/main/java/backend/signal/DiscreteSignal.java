package backend.signal;

import java.util.Map;

public class DiscreteSignal extends AbstractSignal {
    protected double t;

    public DiscreteSignal(double a, double t) {
        super(a);
        this.t = t;
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
    public Map<Double, Double> getAmplitudeFromTimeChartData() {
        return null;
    }

    @Override
    public Map<Double, Double> getHistogramData() {
        return null;
    }
}

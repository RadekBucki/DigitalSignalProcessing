package backend.signal.continuous;

import backend.signal.ContinuousSignal;

public class ContinuousNoise extends ContinuousSignal {
    public ContinuousNoise(double A, double d, double t1) {
        super(A, d, t1);
    }

    @Override
    public double getAverage() {
        return (1 / (t2 - t1)) * points.values().stream().mapToDouble(value -> value).sum();
    }

    @Override
    public double getAbsoluteAverage() {
        return (1 / (t2 - t1)) * points.values().stream().mapToDouble(Math::abs).sum();
    }

    @Override
    public double getAveragePower() {
        return (1 / (t2 - t1)) * points.values().stream().mapToDouble(value -> value * value).sum();
    }

    @Override
    public double getVariance() {
        return (1 / (t2 - t1)) * points.values().stream()
                .mapToDouble(value -> (value - getAverage()) * (value - getAverage())).sum();
    }

    @Override
    public double getEffectiveValue() {
        return Math.sqrt((1 / (t2 - t1)) * points.values().stream().mapToDouble(value -> value * value).sum());
    }
}

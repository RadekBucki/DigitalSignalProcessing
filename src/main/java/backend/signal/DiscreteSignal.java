package backend.signal;

import java.util.Collections;
import java.util.Map;

public class DiscreteSignal extends AbstractSignal {
    protected double f;
    protected Integer n1;
    protected Integer n2;

    public DiscreteSignal(double A, double d, double f) {
        super(A, d);
        this.f = f;
    }

    public DiscreteSignal(Map<Double, Double> points) {
        super(points);
        this.n1 = (int) Math.round(Collections.min(points.keySet()));
        this.n2 = (int) Math.round(Collections.max(points.keySet()));
    }

    @Override
    public double getAverage() {
        return (1.0 / (n2 - n1 + 1)) * points.values().stream().mapToDouble(v -> v).sum();
    }

    @Override
    public double getAbsoluteAverage() {
        return (1.0 / (n2 - n1 + 1)) * points.values().stream().mapToDouble(Math::abs).sum();
    }

    @Override
    public double getAveragePower() {
        return (1.0 / (n2 - n1 + 1)) * points.values().stream().mapToDouble(v -> v * v).sum();
    }

    @Override
    public double getVariance() {
        return (1.0 / (n2 - n1 + 1)) * points.values().stream().mapToDouble(v -> (v - getAverage()) * (v - getAverage())).sum();
    }

    @Override
    public double getEffectiveValue() {
        return Math.sqrt((1.0 / (n2 - n1 + 1)) * points.values().stream().mapToDouble(v -> v * v).sum());
    }

    @Override
    public Map<Double, Double> getHistogramData() {
        return null;
    }
}

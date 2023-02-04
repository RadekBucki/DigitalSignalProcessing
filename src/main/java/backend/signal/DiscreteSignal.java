package backend.signal;

import java.util.Collections;
import java.util.Map;

public class DiscreteSignal extends AbstractSignal {
    protected double f;
    protected int n1;
    protected int n2;

    public DiscreteSignal(double A, double d, double f) {
        super(A, d);
        this.f = f;
    }

    public DiscreteSignal(Map<Double, Double> points) {
        super(points);
        double t1 = Collections.min(points.keySet());
        double t2 = Collections.max(points.keySet());
        this.A = Collections.max(points.values());
        this.d = t2 - t1;
        this.f = (points.size() - 1) / d;
        this.n1 = (int) Math.round(f * t1);
        this.n2 = (int) Math.round(f * t2);
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

    public double getF() {
        return f;
    }

    public int getN1() {
        return n1;
    }

    public int getN2() {
        return n2;
    }
}

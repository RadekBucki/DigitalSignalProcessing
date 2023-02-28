package backend.signal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public DiscreteSignal(double A, double d, double f, double t1) {
        super(A, d);
        this.f = f;
        this.n1 = (int) Math.round(f * t1);
        this.n2 = (int) Math.round(f * (t1 + d));
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

    @JsonCreator
    public DiscreteSignal(
            @JsonProperty("a") double A,
            @JsonProperty("d") double d,
            @JsonProperty("f") double f,
            @JsonProperty("n1") Integer n1,
            @JsonProperty("n2") Integer n2,
            @JsonProperty("points") Map<Double, Double> points
    ) {
        super(A, d, points);
        this.f = f;
        this.n1 = n1;
        this.n2 = n2;
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
        double average = getAverage();
        return (1.0 / (n2 - n1 + 1)) * points.values().stream().mapToDouble(v -> (v - average) * (v - average)).sum();
    }

    @Override
    public double getEffectiveValue() {
        return Math.sqrt((1.0 / (n2 - n1 + 1)) * points.values().stream().mapToDouble(v -> v * v).sum());
    }

    @Override
    public double calculatePointValue(double x) {
        return points.getOrDefault(x, 0.0);
    }

    public double getF() {
        return f;
    }

    public Integer getN1() {
        return n1;
    }

    public Integer getN2() {
        return n2;
    }
}

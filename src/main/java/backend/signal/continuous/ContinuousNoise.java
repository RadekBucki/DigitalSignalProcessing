package backend.signal.continuous;

import backend.signal.ContinuousSignal;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class ContinuousNoise extends ContinuousSignal {
    public ContinuousNoise(double A, double d, double t1) {
        super(A, d, t1);
    }

    @JsonCreator
    public ContinuousNoise(
            @JsonProperty("a") double A,
            @JsonProperty("d") double d,
            @JsonProperty("t1") double t1,
            @JsonProperty("t2") Double t2,
            @JsonProperty("points") Map<Double, Double> points
    ) {
        super(A, d, t1, t2, points);
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
        double average = getAverage();
        return (1 / (t2 - t1)) * points.values().stream()
                .mapToDouble(value -> (value - average) * (value - average)).sum();
    }

    @Override
    public double getEffectiveValue() {
        return Math.sqrt((1 / (t2 - t1)) * points.values().stream().mapToDouble(value -> value * value).sum());
    }
}

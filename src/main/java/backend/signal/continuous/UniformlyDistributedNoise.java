package backend.signal.continuous;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class UniformlyDistributedNoise extends ContinuousNoise {
    public UniformlyDistributedNoise(double A, double d, double t1) {
        super(A, d, t1);
        calculateAllPoints();
    }

    @JsonCreator
    public UniformlyDistributedNoise(
            @JsonProperty("a") double A,
            @JsonProperty("d") double d,
            @JsonProperty("t1") double t1,
            @JsonProperty("t2") Double t2,
            @JsonProperty("points") Map<Double, Double> points
    ) {
        super(A, d, t1, t2, points);
    }

    @Override
    public double calculatePointValue(double x) {
        return Math.random() * 2 * A - A;
    }
}

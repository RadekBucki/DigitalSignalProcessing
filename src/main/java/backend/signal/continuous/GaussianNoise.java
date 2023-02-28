package backend.signal.continuous;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Random;

public class GaussianNoise extends ContinuousNoise {
    private final Random r = new Random();
    public GaussianNoise(double A, double d, double t1) {
        super(A, d, t1);
        calculateAllPoints();
    }

    @JsonCreator
    public GaussianNoise(
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
        return r.nextGaussian() * 2 * A - A;
    }
}

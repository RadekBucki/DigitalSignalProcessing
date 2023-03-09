package backend.signal.continuous;

import backend.signal.ContinuousSignal;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class OneHaltRectifiedSinusoidalSignal extends ContinuousSignal {

    private final double T;

    public OneHaltRectifiedSinusoidalSignal(double A, double d, double t1, double T) {
        super(A, d, t1);
        this.T = T;
        calculateAllPoints();
        setFunction(createFunction(this::calculatePointValue));
    }

    @JsonCreator
    public OneHaltRectifiedSinusoidalSignal(
            @JsonProperty("a") double A,
            @JsonProperty("d") double d,
            @JsonProperty("t1") double t1,
            @JsonProperty("t2") Double t2,
            @JsonProperty("points") Map<Double, Double> points,
            @JsonProperty("t") double T
    ) {
        super(A, d, t1, t2, points);
        this.T = T;
    }

    @Override
    public double calculatePointValue(double x) {
        return A / 2 * (Math.sin(2 * Math.PI * (x - t1) / T) + Math.abs(Math.sin(2 * Math.PI * (x - t1) / T)));
    }
    public double getT() {
        return T;
    }
}

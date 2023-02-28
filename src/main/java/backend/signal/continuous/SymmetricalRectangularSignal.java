package backend.signal.continuous;

import backend.signal.ContinuousSignal;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class SymmetricalRectangularSignal extends ContinuousSignal {
    private final double kw;
    private final double T;

    public SymmetricalRectangularSignal(double A, double d, double t1, double T, double kw) {
        super(A, d, t1);
        this.T = T;
        this.kw = kw;
        calculateAllPoints();
    }

    @JsonCreator
    public SymmetricalRectangularSignal(
            @JsonProperty("a") double A,
            @JsonProperty("d") double d,
            @JsonProperty("t1") double t1,
            @JsonProperty("t2") Double t2,
            @JsonProperty("points") Map<Double, Double> points,
            @JsonProperty("t") double T,
            @JsonProperty("kw") double kw
    ) {
        super(A, d, t1, t2, points);
        this.T = T;
        this.kw = kw;
    }

    @Override
    public double calculatePointValue(double x) {
        int k = (int) Math.floor((x - t1) / T);
        if (x >= k * T + t1 && x < kw * T + k * T + t1) {
            return A;
        }
        return -A;
    }

    public double getKw() {
        return kw;
    }

    public double getT() {
        return T;
    }
}

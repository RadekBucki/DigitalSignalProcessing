package backend.signal.continuous;

import backend.signal.ContinuousSignal;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class UnitJump extends ContinuousSignal {
    private final double ts;

    public UnitJump(double A, double d, double t1, double ts) {
        super(A, d, t1);
        this.ts = ts;
        calculateAllPoints();
    }

    @JsonCreator
    public UnitJump(
            @JsonProperty("a") double A,
            @JsonProperty("d") double d,
            @JsonProperty("t1") double t1,
            @JsonProperty("t2") Double t2,
            @JsonProperty("points") Map<Double, Double> points,
            @JsonProperty("ts") double ts
    ) {
        super(A, d, t1, t2, points);
        this.ts = ts;
    }

    @Override
    public double calculatePointValue(double x) {
        if (x > ts) {
            return A;
        }
        if (x < ts) {
            return 0;
        }
        return A / 2;
    }

    public double getTs() {
        return ts;
    }
}

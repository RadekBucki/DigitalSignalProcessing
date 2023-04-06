package backend.signal.discrete;

import backend.signal.DiscreteSignal;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class UnitImpulse extends DiscreteSignal {
    private final int ns;
    private int n1;

    public UnitImpulse(double A, double d, double f, int n1, int ns) {
        super(A, d, f);
        this.ns = ns;
        super.n1 = n1;
        super.n2 = (int) (n1 + Math.floor(d * f));
        for (int i = n1; i <= n2; i++) {
            double time = i / f;
            if (i == ns) {
                points.put(time, A);
            } else {
                points.put(time, 0.0);
            }
        }
    }

    @JsonCreator
    public UnitImpulse(
            @JsonProperty("a") double A,
            @JsonProperty("d") double d,
            @JsonProperty("f") double f,
            @JsonProperty("n1") int n1,
            @JsonProperty("n2") int n2,
            @JsonProperty("ns") int ns,
            @JsonProperty("points") Map<Double, Double> points
    ) {
        super(A, d, f, n1, n2, points);
        this.ns = ns;
    }

    public int getNs() {
        return ns;
    }
}

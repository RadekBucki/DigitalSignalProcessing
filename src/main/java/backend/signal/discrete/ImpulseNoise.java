package backend.signal.discrete;

import backend.signal.DiscreteSignal;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Random;

public class ImpulseNoise extends DiscreteSignal {
    private final double t1;
    private final double p;

    public ImpulseNoise(double A, double d, double f, double p, double t1) {
        super(A, d, f);
        this.t1 = t1;
        this.p = p;
        Random random = new Random();
        super.n1 = (int) Math.floor(t1 * f);
        super.n2 = (int) (n1 + Math.floor(d * f));
        for (int i = n1; i <= n2; i++) {
            double time = i / f;
            if (random.nextDouble(0,1) <= p) {
                points.put(time, A);
            } else {
                points.put(time, 0.0);
            }
        }
    }

    @JsonCreator
    public ImpulseNoise(
            @JsonProperty("a") double A,
            @JsonProperty("d") double d,
            @JsonProperty("f") double f,
            @JsonProperty("t1") double t1,
            @JsonProperty("p") double p,
            @JsonProperty("n1") int n1,
            @JsonProperty("n2") int n2,
            @JsonProperty("points") Map<Double, Double> points
    ) {
        super(A, d, f, n1, n2, points);
        this.t1 = t1;
        this.p = p;
    }

    public double getT1() {
        return t1;
    }

    public double getP() {
        return p;
    }
}

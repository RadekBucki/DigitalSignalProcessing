package backend.signal.discrete;

import backend.signal.DiscreteSignal;

import java.util.Random;

public class ImpulseNoise extends DiscreteSignal {
    private final double t1;
    private final double p;

    public ImpulseNoise(double A, double d, double f, double p, double t1) {
        super(A, d, f);
        this.t1 = t1;
        this.p = p;
        Random random = new Random();
        double time = t1;
        for (int i = 0; i < Math.floor(d * f); i++) {
            if (random.nextDouble(0,1) <= p) {
                points.put(time, A);
            } else {
                points.put(time, 0.0);
            }
            time = i / f;
        }
    }
}

package backend.signal.discrete;

import backend.signal.DiscreteSignal;

import java.util.Random;

public class ImpulseNoise extends DiscreteSignal {
    private double t1;
    private double p;

    public ImpulseNoise(double a, double t, double t1, double d, double p) {
        super(a, t, d);
        this.t1 = t1;
        this.p = p;
        Random random = new Random();
        for (double i = t1; i < t1 + d; i += d) {
            if (random.nextDouble(0,1) <= p) {
                points.put(i, A);
            } else {
                points.put(i, 0.0);
            }
        }
    }
}

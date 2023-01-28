package backend.signal.continuous;

import backend.signal.ContinuousSignal;

public class UniformlyDistributedNoise extends ContinuousSignal {
    public UniformlyDistributedNoise(double a, double t1, double d) {
        super(a, t1, d);
        calculateAllPoints();
    }

    public double calculatePointValue(double x) {
        return Math.random() * 2 * A - A;
    }
}

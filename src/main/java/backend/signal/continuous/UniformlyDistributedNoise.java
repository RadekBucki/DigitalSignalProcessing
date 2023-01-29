package backend.signal.continuous;

import backend.signal.ContinuousSignal;

public class UniformlyDistributedNoise extends ContinuousSignal {
    public UniformlyDistributedNoise(double A, double t1, double d) {
        super(A, t1, d);
        calculateAllPoints();
    }

    @Override
    public double calculatePointValue(double x) {
        return Math.random() * 2 * A - A;
    }
}

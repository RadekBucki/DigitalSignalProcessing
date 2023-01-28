package backend.signal.continuous;

import backend.signal.ContinuousSignal;

import java.util.Random;

public class GaussianNoise extends ContinuousSignal {
    private final Random r = new Random();
    public GaussianNoise(double A, double t1, double d) {
        super(A, t1, d);
        calculateAllPoints();
    }
    @Override
    public double calculatePointValue(double x) {
        return r.nextGaussian() * 2 * A - A;
    }
}

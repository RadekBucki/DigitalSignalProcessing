package backend.signal.continuous;

import java.util.Random;

public class GaussianNoise extends ContinuousSignal {
    private final Random r = new Random();
    public GaussianNoise(double a, double t1, double d) {
        super(a, t1, d);
        calculateAllPoints();
    }

    public double calculatePointValue(double x) {
        return r.nextGaussian() * 2 * A - A;
    }
}

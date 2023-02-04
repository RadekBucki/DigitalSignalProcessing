package backend.signal.continuous;

import java.util.Random;

public class GaussianNoise extends Noise {
    private final Random r = new Random();
    public GaussianNoise(double A, double d, double t1) {
        super(A, d, t1);
        calculateAllPoints();
    }
    @Override
    public double calculatePointValue(double x) {
        return r.nextGaussian() * 2 * A - A;
    }
}

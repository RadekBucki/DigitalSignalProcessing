package backend.signal;

import java.util.Random;

public class GaussianNoise extends ContinuousSignal {
    private final Random r = new Random();
    public GaussianNoise(double a, double t1, double d) {
        super(a, t1, d);
        int t1Rounded = (int) (t1 * POINTS_DECIMAL_PLACES_DIVISION);
        int t2Rounded = (int) ((t1 + d) * POINTS_DECIMAL_PLACES_DIVISION);
        for (int i = t1Rounded; i < t2Rounded; i++) {
            points.put(i / POINTS_DECIMAL_PLACES_DIVISION, calculatePointValue());
        }
    }

    private double calculatePointValue() {
        return r.nextGaussian() * 2 * A - A;
    }
}

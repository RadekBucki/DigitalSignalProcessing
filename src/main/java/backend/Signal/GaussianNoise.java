package backend.Signal;

import java.util.Random;

public class GaussianNoise extends ContinuousSignal {
    private final Random r = new Random();
    public GaussianNoise(double a, double t1, double d) {
        super(a, t1, d);
        int t1Rounded = (int) (t1 * pointsDecimalPlacesDivision);
        int t2Rounded = (int) ((t1 + d) * pointsDecimalPlacesDivision);
        for (int i = t1Rounded; i < t2Rounded; i++) {
            points.put(i / pointsDecimalPlacesDivision, calculatePointValue());
        }
    }

    private double calculatePointValue() {
        return r.nextGaussian() * 2 * A - A;
    }
}

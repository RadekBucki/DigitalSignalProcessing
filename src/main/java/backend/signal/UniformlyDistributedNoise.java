package backend.signal;

public class UniformlyDistributedNoise extends ContinuousSignal {
    public UniformlyDistributedNoise(double a, double t1, double d) {
        super(a, t1, d);
        int t1Rounded = (int) (t1 * POINTS_DECIMAL_PLACES_DIVISION);
        int t2Rounded = (int) ((t1 + d) * POINTS_DECIMAL_PLACES_DIVISION);
        for (int i = t1Rounded; i < t2Rounded; i++) {
            points.put(i / POINTS_DECIMAL_PLACES_DIVISION, calculatePointValue());
        }
    }

    private double calculatePointValue() {
        return Math.random() * 2 * A - A;
    }
}

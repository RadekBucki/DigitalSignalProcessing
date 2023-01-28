package backend.Signal;

public class UniformlyDistributedNoise extends ContinuousSignal {
    public UniformlyDistributedNoise(double a, double t1, double d) {
        super(a, t1, d);
        int t1Rounded = (int) (t1 * pointsDecimalPlacesDivision);
        int t2Rounded = (int) ((t1 + d) * pointsDecimalPlacesDivision);
        for (int i = t1Rounded; i < t2Rounded; i++) {
            points.put(i / pointsDecimalPlacesDivision, calculatePointValue());
        }
    }

    private double calculatePointValue() {
        return Math.random() * 2 * A - A;
    }
}

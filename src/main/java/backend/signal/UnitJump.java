package backend.signal;

public class UnitJump extends ContinuousSignal {
    private double ts;

    public UnitJump(double a, double t1, double d, double ts) {
        super(a, t1, d);
        this.ts = ts;
        int t1Rounded = (int) (t1 * pointsDecimalPlacesDivision);
        int t2Rounded = (int) ((t1 + d) * pointsDecimalPlacesDivision);
        for (int i = t1Rounded; i < t2Rounded; i++) {
            double pointX = i / pointsDecimalPlacesDivision;
            points.put(pointX, calculatePointValue(pointX));
        }
    }

    private double calculatePointValue(double x) {
        if (x > ts) {
            return A;
        }
        if (x < ts) {
            return 0;
        }
        return A / 2;
    }
}

package backend.signal.continuous;

import backend.signal.ContinuousSignal;

public class UnitJump extends ContinuousSignal {
    private final double ts;

    public UnitJump(double A, double t1, double d, double ts) {
        super(A, t1, d);
        this.ts = ts;
        calculateAllPoints();
    }

    public double calculatePointValue(double x) {
        if (x > ts) {
            return A;
        }
        if (x < ts) {
            return 0;
        }
        return A / 2;
    }
}

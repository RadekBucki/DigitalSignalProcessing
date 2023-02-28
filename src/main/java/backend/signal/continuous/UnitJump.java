package backend.signal.continuous;

import backend.signal.ContinuousSignal;

public class UnitJump extends ContinuousSignal {
    private final double ts;

    public UnitJump(double A, double d, double t1, double ts) {
        super(A, d, t1);
        this.ts = ts;
        calculateAllPoints();
        setFunction(createFunction(this::calculatePointValue));
    }

    @Override
    public double calculatePointValue(double x) {
        if (x > ts) {
            return A;
        }
        if (x < ts) {
            return 0;
        }
        return A / 2;
    }

    public double getTs() {
        return ts;
    }
}

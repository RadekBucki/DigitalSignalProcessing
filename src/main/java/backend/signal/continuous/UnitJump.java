package backend.signal.continuous;

public class UnitJump extends ContinuousSignal {
    private double ts;

    public UnitJump(double a, double t1, double d, double ts) {
        super(a, t1, d);
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

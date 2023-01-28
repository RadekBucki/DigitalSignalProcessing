package backend.signal;

public class UnitJump extends ContinuousSignal {
    private double ts;

    public UnitJump(double a, double t1, double d, double ts) {
        super(a, t1, d);
        this.ts = ts;
    }
}

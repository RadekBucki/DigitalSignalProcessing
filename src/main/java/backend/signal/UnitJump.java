package backend.signal;

public class UnitJump extends ContinuousSignal {
    private double ts;

    public UnitJump(double A, double t1, double d, double ts) {
        super(A, t1, d);
        this.ts = ts;
    }
}

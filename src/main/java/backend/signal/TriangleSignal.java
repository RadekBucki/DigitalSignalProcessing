package backend.signal;

public class TriangleSignal extends ContinuousSignal {
    private double T;
    private double kw;

    public TriangleSignal(double a, double t1, double d, double T, double kw) {
        super(a, t1, d);
        this.T = T;
        this.kw = kw;
    }
}

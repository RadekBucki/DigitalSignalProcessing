package backend.signal;

public class RectangularSignal extends ContinuousSignal {
    private double T;
    private double kw;

    public RectangularSignal(double A, double t1, double d, double T, double kw) {
        super(A, t1, d);
        this.T = T;
        this.kw = kw;
    }
}

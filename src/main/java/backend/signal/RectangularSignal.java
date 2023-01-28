package backend.signal;

public class RectangularSignal extends ContinuousSignal {
    private double T;
    private double kw;

    public RectangularSignal(double a, double t1, double d, double T, double kw) {
        super(a, t1, d);
        this.T = T;
        this.kw = kw;
    }
}

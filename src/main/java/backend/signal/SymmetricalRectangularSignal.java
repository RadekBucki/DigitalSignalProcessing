package backend.signal;

public class SymmetricalRectangularSignal extends ContinuousSignal {
    private double T;
    private double kw;

    public SymmetricalRectangularSignal(double a, double t1, double d, double T, double kw) {
        super(a, t1, d);
        this.T = T;
        this.kw = kw;
    }
}

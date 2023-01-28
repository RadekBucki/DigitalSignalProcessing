package backend.signal;

public class SinusoidalSignal extends ContinuousSignal {
    private double T;

    public SinusoidalSignal(double A, double t1, double d, double T) {
        super(A, t1, d);
        this.T = T;
    }
}

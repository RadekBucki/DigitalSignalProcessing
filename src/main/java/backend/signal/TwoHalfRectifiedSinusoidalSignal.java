package backend.signal;

public class TwoHalfRectifiedSinusoidalSignal extends ContinuousSignal {
    private double T;

    public TwoHalfRectifiedSinusoidalSignal(double A, double t1, double d, double T) {
        super(A, t1, d);
        this.T = T;
    }
}

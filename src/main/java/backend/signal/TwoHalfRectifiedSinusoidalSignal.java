package backend.signal;

public class TwoHalfRectifiedSinusoidalSignal extends ContinuousSignal {
    private double T;

    public TwoHalfRectifiedSinusoidalSignal(double a, double t1, double d, double T) {
        super(a, t1, d);
        this.T = T;
    }
}

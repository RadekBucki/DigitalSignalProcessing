package backend.signal;

public class OneHaltRectifiedSinusoidalSignal extends ContinuousSignal {
    private double T;

    public OneHaltRectifiedSinusoidalSignal(double A, double t1, double d, double T) {
        super(A, t1, d);
        this.T = T;
    }
}

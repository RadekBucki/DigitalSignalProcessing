package backend.signal;

public class OneHaltRectifiedSinusoidalSignal extends ContinuousSignal {
    private double T;

    public OneHaltRectifiedSinusoidalSignal(double a, double t1, double d, double T) {
        super(a, t1, d);
        this.T = T;
    }
}

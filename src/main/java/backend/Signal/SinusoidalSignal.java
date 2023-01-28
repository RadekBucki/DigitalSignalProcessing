package backend.Signal;

public class SinusoidalSignal extends ContinuousSignal {
    private double T;

    public SinusoidalSignal(double a, double t1, double d, double T) {
        super(a, t1, d);
        this.T = T;
    }
}

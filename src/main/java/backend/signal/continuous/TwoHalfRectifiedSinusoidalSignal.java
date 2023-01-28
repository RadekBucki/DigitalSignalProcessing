package backend.signal.continuous;

public class TwoHalfRectifiedSinusoidalSignal extends ContinuousSignal {
    private double T;

    public TwoHalfRectifiedSinusoidalSignal(double a, double t1, double d, double T) {
        super(a, t1, d);
        this.T = T;
        calculateAllPoints();
    }

    public double calculatePointValue(double x) {
        return A * Math.abs(Math.sin(2 * Math.PI * (x - t1) / T));
    }
}

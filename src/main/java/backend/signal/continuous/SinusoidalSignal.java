package backend.signal.continuous;

public class SinusoidalSignal extends ContinuousSignal {
    private double T;

    public SinusoidalSignal(double a, double t1, double d, double T) {
        super(a, t1, d);
        this.T = T;
        calculateAllPoints();
    }

    public double calculatePointValue(double x) {
        return A * Math.sin(2 * Math.PI * (x - t1) / T);
    }
}

package backend.signal.continuous;

public class OneHaltRectifiedSinusoidalSignal extends ContinuousSignal {
    private double T;

    public OneHaltRectifiedSinusoidalSignal(double A, double t1, double d, double T) {
        super(A, t1, d);
        this.T = T;
        calculateAllPoints();
    }

    public double calculatePointValue(double x) {
        return A / 2 * (Math.sin(2 * Math.PI * (x - t1) / T) + Math.abs(Math.sin(2 * Math.PI * (x - t1) / T)));
    }

}

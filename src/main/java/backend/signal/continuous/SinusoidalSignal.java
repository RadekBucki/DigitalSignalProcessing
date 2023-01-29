package backend.signal.continuous;

import backend.signal.ContinuousSignal;

public class SinusoidalSignal extends ContinuousSignal {
    private final double T;

    public SinusoidalSignal(double A, double t1, double d, double T) {
        super(A, t1, d);
        this.T = T;
        calculateAllPoints();
    }

    @Override
    public double calculatePointValue(double x) {
        return A * Math.sin(2 * Math.PI * (x - t1) / T);
    }
}

package backend.signal.continuous;

import backend.signal.ContinuousSignal;

public class TwoHalfRectifiedSinusoidalSignal extends ContinuousSignal {
    private final double T;

    public TwoHalfRectifiedSinusoidalSignal(double A, double t1, double d, double T) {
        super(A, t1, d);
        this.T = T;
        calculateAllPoints();
    }

    @Override
    public double calculatePointValue(double x) {
        return A * Math.abs(Math.sin(2 * Math.PI * (x - t1) / T));
    }
}

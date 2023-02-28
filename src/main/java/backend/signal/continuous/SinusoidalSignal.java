package backend.signal.continuous;

import backend.signal.ContinuousSignal;

public class SinusoidalSignal extends ContinuousSignal {
    private final double T;

    public SinusoidalSignal(double A, double d, double t1, double T) {
        super(A, d, t1);
        this.T = T;
        calculateAllPoints();
        setFunction(createFunction(this::calculatePointValue));
    }

    @Override
    public double calculatePointValue(double x) {
        return A * Math.sin(2 * Math.PI * (x - t1) / T);
    }

    public double getT() {
        return T;
    }
}

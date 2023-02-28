package backend.signal.continuous;

import backend.signal.ContinuousSignal;

public class OneHaltRectifiedSinusoidalSignal extends ContinuousSignal {

    private final double T;

    public OneHaltRectifiedSinusoidalSignal(double A, double d, double t1, double T) {
        super(A, d, t1);
        this.T = T;
        calculateAllPoints();
        setFunction(createFunction(this::calculatePointValue));
    }

    @Override
    public double calculatePointValue(double x) {
        return A / 2 * (Math.sin(2 * Math.PI * (x - t1) / T) + Math.abs(Math.sin(2 * Math.PI * (x - t1) / T)));
    }
    public double getT() {
        return T;
    }
}

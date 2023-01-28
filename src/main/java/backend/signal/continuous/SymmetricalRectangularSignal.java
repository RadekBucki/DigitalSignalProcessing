package backend.signal.continuous;

import backend.signal.ContinuousSignal;

public class SymmetricalRectangularSignal extends ContinuousSignal {
    private double T;
    private double kw;

    public SymmetricalRectangularSignal(double A, double t1, double d, double T, double kw) {
        super(A, t1, d);
        this.T = T;
        this.kw = kw;
        calculateAllPoints();
    }

    public double calculatePointValue(double x) {
        int k = (int) Math.floor(x / T);
        if (x >= k * T + t1 && x < kw * T + k * T + t1) {
            return A;
        }
        return -A;
    }
}

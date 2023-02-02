package backend.signal.continuous;

import backend.signal.ContinuousSignal;

public class SymmetricalRectangularSignal extends ContinuousSignal {
    private final double kw;
    private final double T;

    public SymmetricalRectangularSignal(double A, double d, double t1, double T, double kw) {
        super(A, d, t1);
        this.T = T;
        this.kw = kw;
        calculateAllPoints();
    }

    @Override
    public double calculatePointValue(double x) {
        int k = (int) Math.floor((x - t1) / T);
        if (x >= k * T + t1 && x < kw * T + k * T + t1) {
            return A;
        }
        return -A;
    }
}

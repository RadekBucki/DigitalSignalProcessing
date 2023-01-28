package backend.signal.continuous;

public class RectangularSignal extends ContinuousSignal {
    private double T;
    private double kw;

    public RectangularSignal(double a, double t1, double d, double T, double kw) {
        super(a, t1, d);
        this.T = T;
        this.kw = kw;
        calculateAllPoints();
    }

    public double calculatePointValue(double x) {
        int k = (int) Math.floor(x / T);
        if (x >= k * T + t1 && x < kw * T + k * T + t1) {
            return A;
        }
        return 0;
    }
}

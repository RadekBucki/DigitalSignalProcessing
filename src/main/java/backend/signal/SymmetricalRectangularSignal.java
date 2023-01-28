package backend.signal;

public class SymmetricalRectangularSignal extends ContinuousSignal {
    private double T;
    private double kw;

    public SymmetricalRectangularSignal(double a, double t1, double d, double T, double kw) {
        super(a, t1, d);
        this.T = T;
        this.kw = kw;
        int t1Rounded = (int) (t1 * pointsDecimalPlacesDivision);
        int t2Rounded = (int) ((t1 + d) * pointsDecimalPlacesDivision);
        for (int i = t1Rounded; i < t2Rounded; i++) {
            double pointX = i / pointsDecimalPlacesDivision;
            points.put(pointX, calculatePointValue(pointX));
        }
    }

    private double calculatePointValue(double x) {
        int k = (int) Math.floor(x / T);
        if (x >= k * T + t1 && x < kw * T + k * T + t1) {
            return A;
        }
        return -A;
    }
}

package backend.signal;

public class OneHaltRectifiedSinusoidalSignal extends ContinuousSignal {
    private double T;

    public OneHaltRectifiedSinusoidalSignal(double a, double t1, double d, double T) {
        super(a, t1, d);
        this.T = T;
        int t1Rounded = (int) (t1 * pointsDecimalPlacesDivision);
        int t2Rounded = (int) ((t1 + d) * pointsDecimalPlacesDivision);
        for (int i = t1Rounded; i < t2Rounded; i++) {
            double pointX = i / pointsDecimalPlacesDivision;
            points.put(pointX, calculatePointValue(pointX));
        }
    }

    private double calculatePointValue(double x) {
        return A / 2 * (Math.sin(2 * Math.PI * (x - t1) / T) + Math.abs(Math.sin(2 * Math.PI * (x - t1) / T)));
    }

}

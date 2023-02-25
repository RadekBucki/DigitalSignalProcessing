package backend.signal;

import java.util.Map;

public class DiscreteFourierTransformedSignal extends DiscreteSignal {
    protected Map<Double, Double> imaginaryPartPoints;
    public DiscreteFourierTransformedSignal(double A, double d, double f) {
        super(A, d, f);
    }

    public DiscreteFourierTransformedSignal(double A, double d, double f, double t1) {
        super(A, d, f, t1);
    }

    public DiscreteFourierTransformedSignal(
            Map<Double, Double> realPartPoints,
            Map<Double, Double> imaginaryPartPoints
    ) {
        super(realPartPoints);
        this.imaginaryPartPoints = imaginaryPartPoints;
    }
    public Map<Double, Double> getRealPartPoints() {
        return points;
    }
    public Map<Double, Double> getImaginaryPartPoints() {
        return imaginaryPartPoints;
    }
}

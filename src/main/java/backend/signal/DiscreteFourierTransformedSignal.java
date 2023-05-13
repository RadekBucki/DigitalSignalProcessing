package backend.signal;

import org.apache.commons.math3.complex.Complex;

import java.util.Map;
import java.util.stream.Collectors;

public class DiscreteFourierTransformedSignal extends DiscreteSignal {
    protected Map<Double, Double> imaginaryPartPoints;

    public DiscreteFourierTransformedSignal(double A, double d, double f) {
        super(A, d, f);
    }

    public DiscreteFourierTransformedSignal(double A, double d, double f, double t1) {
        super(A, d, f, t1);
    }

    public DiscreteFourierTransformedSignal(Map<Double, Complex> points) {
        super(
                points.entrySet()
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        entry -> entry.getValue().getReal()
                                )
                        )
        );
        this.imaginaryPartPoints = points.entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().getImaginary()
                        )
                );
    }

    public Map<Double, Double> getRealPartPoints() {
        return points;
    }

    public Map<Double, Double> getImaginaryPartPoints() {
        return imaginaryPartPoints;
    }

    public Map<Double, Double> getModulePoints() {
        return points.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> Math.sqrt(
                                Math.pow(entry.getValue(), 2) + Math.pow(imaginaryPartPoints.get(entry.getKey()), 2)
                        )
                ));
    }

    public Map<Double, Double> getPhasePoints() {
        return points.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> Math.atan(imaginaryPartPoints.get(entry.getKey()) / entry.getValue())
                ));
    }
}

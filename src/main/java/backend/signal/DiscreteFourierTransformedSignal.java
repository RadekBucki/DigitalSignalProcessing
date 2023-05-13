package backend.signal;

import backend.Rounder;
import org.apache.commons.math3.complex.Complex;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
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
        // It's because supe must be first statement in constructor
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
        AtomicInteger numOfSample = new AtomicInteger(0);
        AtomicInteger pointsSize = new AtomicInteger(points.size());
        points = points.entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                entry -> Rounder.round((numOfSample.getAndIncrement() / (double) pointsSize.get()) * f),
                                Map.Entry::getValue
                        )
                );
        this.points = points.entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> Rounder.round(entry.getValue().getReal())
                        )
                );
        this.imaginaryPartPoints = points.entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> Rounder.round(entry.getValue().getImaginary())
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
                .limit(points.size() / 2 + 1)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> Rounder.round(Math.sqrt(
                                Math.pow(entry.getValue(), 2) + Math.pow(imaginaryPartPoints.get(entry.getKey()), 2)
                        ))
                ));
    }

    public Map<Double, Double> getPhasePoints() {
        return points.entrySet()
                .stream()
                .limit(points.size() / 2 + 1)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> Rounder.round(Math.atan2(
                                imaginaryPartPoints.get(entry.getKey()),
                                entry.getValue()
                        ))
                ));
    }
}

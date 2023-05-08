package backend.signal_operation;

import backend.SignalFactory;
import backend.SignalOperationFactory;
import backend.signal.DiscreteSignal;
import org.apache.commons.math3.complex.Complex;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DiscreteFourierTransformWithDecimationInTimeDomain {
    private final SignalFactory signalFactory;
    private final SignalOperationFactory signalOperationFactory = new SignalOperationFactory(new SignalFactory());

    public DiscreteFourierTransformWithDecimationInTimeDomain(SignalFactory signalFactory) {
        this.signalFactory = signalFactory;
    }

    public DiscreteSignal execute(DiscreteSignal signal, TransformType type) {
        if (type == TransformType.DIRECT) {
            return executeDirect(signal);
        } else {
            return executeFast(signal);
        }
    }

    private DiscreteSignal executeFast(DiscreteSignal signal) {
        return (DiscreteSignal) signalFactory.createDiscreteFourierTransformedSignal(executeFast(signal.getPoints()));
    }

    private static Map<Double, Complex> executeFast(Map<Double, Double> points) {
        points = getProperPointsNumber(points);
        int n = points.size();
        Map<Double, Double> finalPoints = points;
        List<Complex> x = IntStream.range(0, n)
                .mapToObj(i -> new Complex(finalPoints.getOrDefault((double) i, 0.0), 0))
                .collect(Collectors.toList());
        List<Complex> y = fft(x);
        Map<Double, Complex> result = new HashMap<>();
        for (int i = 0; i < n / 2; i++) {
            Complex value = y.get(i).multiply(2.0 / n);
            result.put((double) i, value);
        }
        return result;
    }

    private static List<Complex> fft(List<Complex> x) {
        int n = x.size();
        if (n == 1) return List.of(x.get(0));
        if (n % 2 != 0) {
            throw new IllegalArgumentException("Number of points must be a power of 2");
        }

        List<Complex> even = new LinkedList<>();
        List<Complex> odd = new LinkedList<>();
        for (int k = 0; k < n / 2; k++) {
            even.add(x.get(2 * k));
            odd.add(x.get(2 * k + 1));
        }
        List<Complex> q = fft(even);
        List<Complex> r = fft(odd);

        Complex[] y = new Complex[n];
        for (int k = 0; k < n/2; k++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = q.get(k).add(wk.multiply(r.get(k)));
            y[k + n/2] = q.get(k).subtract(wk.multiply(r.get(k)));
        }
        return List.of(y);
    }

    private DiscreteSignal executeDirect(DiscreteSignal signal) {
        Map<Double, Double> points = signal.getPoints();
        points = getProperPointsNumber(points);
        List<Double> frequencies = points.keySet().stream().toList();
        int N = frequencies.size();
        return (DiscreteSignal) signalFactory.createDiscreteFourierTransformedSignal(
                points.keySet()
                        .stream()
                        .map(frequency -> Map.entry(
                                frequency,
                                IntStream.range(0, N)
                                        .mapToObj(n -> new Complex(signal.getPoints().get(frequencies.get(n)), 0)
                                                .multiply(new Complex(
                                                        Math.cos(-2 * Math.PI * frequency * n / N),
                                                        Math.sin(-2 * Math.PI * frequency * n / N)
                                                ))
                                        )
                                        .reduce(Complex.ZERO, Complex::add)
                        ))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    private static Map<Double, Double> getProperPointsNumber(Map<Double, Double> points) {
        if (points.size() % 2 != 0) {
            int nearestPowerOfTwoUnderPointsSize = 1;
            while (nearestPowerOfTwoUnderPointsSize < points.size()) {
                nearestPowerOfTwoUnderPointsSize *= 2;
            }
            nearestPowerOfTwoUnderPointsSize /= 2;
            points = points.entrySet()
                    .stream()
                    .limit(nearestPowerOfTwoUnderPointsSize)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        return points;
    }
}

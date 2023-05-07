package backend.signal_operation;

import backend.SignalFactory;
import backend.SignalOperationFactory;
import backend.signal.DiscreteSignal;
import org.apache.commons.math3.complex.Complex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        Complex[] x = new Complex[n];
        for (int i = 0; i < n; i++) {
            x[i] = new Complex(points.getOrDefault((double) i, 0.0), 0);
        }
        Complex[] y = fft(x);
        Map<Double, Complex> result = new HashMap<>();
        for (int i = 0; i < n/2; i++) {
            Complex value = y[i].multiply(2.0/n);
            result.put((double) i, value);
        }
        return result;
    }

    private static Complex[] fft(Complex[] x) {
        int n = x.length;
        if (n == 1) return new Complex[] { x[0] };
        if (n % 2 != 0) {
            throw new IllegalArgumentException("Number of points must be a power of 2");
        }

        Complex[] even = new Complex[n/2];
        Complex[] odd = new Complex[n/2];
        for (int k = 0; k < n/2; k++) {
            even[k] = x[2*k];
            odd[k] = x[2*k + 1];
        }
        Complex[] q = fft(even);
        Complex[] r = fft(odd);

        Complex[] y = new Complex[n];
        for (int k = 0; k < n/2; k++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].add(wk.multiply(r[k]));
            y[k + n/2] = q[k].subtract(wk.multiply(r[k]));
        }
        return y;
    }

    private DiscreteSignal executeDirect(DiscreteSignal signal) {
        Map<Double,Double> points = signal.getPoints();
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

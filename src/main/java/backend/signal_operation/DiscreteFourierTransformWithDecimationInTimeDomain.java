package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.DiscreteSignal;
import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DiscreteFourierTransformWithDecimationInTimeDomain {
    private final SignalFactory signalFactory;

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
        List<Double> points = signal.getPoints().values().stream().toList();
        List<Complex> map = points.stream()
                .map(point -> new Complex(point, 0))
                .collect(Collectors.toList());

        List<Complex> y = fft(map);

        Map<Double, Complex> result = IntStream.range(0, y.size())
                .boxed()
                .collect(Collectors.toMap(Double::valueOf, y::get, (a, b) -> b, LinkedHashMap::new));

        return (DiscreteSignal) signalFactory.createDiscreteFourierTransformedSignal(result);
    }

    private DiscreteSignal executeDirect(DiscreteSignal signal) {
        List<Double> frequencies = signal.getPoints().keySet().stream().toList();
        int N = frequencies.size();
        return (DiscreteSignal) signalFactory.createDiscreteFourierTransformedSignal(
                signal.getPoints()
                        .keySet()
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

    List<Complex> fft(List<Complex> points) {
        int n = points.size();
        if (n == 1) {
            return points;
        }

        List<Complex> fftResult = new ArrayList<>(IntStream.range(0, n)
                .mapToObj(i -> new Complex(0, 0))
                .toList());

        int log2n = (int) (Math.log(n) / Math.log(2));
        IntStream.range(0, n)
                .forEach(i -> fftResult.set(
                        i,
                        points.get(Integer.reverse(i) >>> (32 - log2n))
                ));

        IntStream.rangeClosed(1, log2n)
                .forEach(i -> {
                    int m = 1 << i;
                    double theta = -2 * Math.PI / m;
                    Complex wM = new Complex(1, 0);
                    Complex w = new Complex(Math.cos(theta), Math.sin(theta));
                    for (int j = 0; j < m / 2; j++) {
                        for (int k = j; k < n; k += m) {
                            Complex u = fftResult.get(k);
                            int index = k + m / 2 < n ? k + m / 2 : 0;
                            Complex t = wM.multiply(fftResult.get(index));
                            fftResult.set(k, u.add(t));
                            fftResult.set(index, u.subtract(t));
                        }
                        wM = wM.multiply(w);
                    }
                });

        return fftResult;
    }
}

package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.DiscreteSignal;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexUtils;

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
        int nHalf = n / 2;
        List<Complex> fftEven = fft(
                points.stream()
                        .filter(point -> points.indexOf(point) % 2 == 0)
                        .toList()
        );
        List<Complex> fftOdd = fft(
                points.stream()
                        .filter(point -> points.indexOf(point) % 2 == 1)
                        .toList()
        );
        return IntStream.range(0, n)
                .mapToObj(i -> {
                    Complex wn = ComplexUtils.polar2Complex(1, -2 * Math.PI * i / n);
                    int index = i % (nHalf);
                    Complex sum = fftEven.get(index)
                            .add(wn.multiply(fftOdd.get(index)));
                    if (i < nHalf) {
                        return sum;
                    }
                    return fftEven.get(index)
                            .subtract(wn.multiply(fftOdd.get(index)));

                })
                .toList();
    }
}

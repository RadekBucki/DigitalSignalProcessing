package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.DiscreteSignal;
import org.apache.commons.math3.complex.Complex;

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
        Map<Double, Double> points = signal.getPoints();
        Map<Double, Complex> map = new LinkedHashMap<>();
        return (DiscreteSignal) signalFactory.createDiscreteFourierTransformedSignal(map);
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
}

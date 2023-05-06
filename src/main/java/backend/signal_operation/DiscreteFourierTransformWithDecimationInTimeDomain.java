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
        Map<Double, Complex> outputSignalPoints = new HashMap<>();

        Filter filter = signalOperationFactory.createFilter(PassType.LOW_PASS, WindowType.HAMMING, 1, 2, 3);

        DiscreteSignal filteredSignal = filter.execute(signal);
        Map<Double, Double> filteredSignalPoints = filteredSignal.getPoints();

        int N = filteredSignalPoints.size();
        List<Double> filteredSignalXes = filteredSignalPoints.keySet().stream().toList();
        double step = filteredSignalXes.get(1) - filteredSignalXes.get(0);
        double lastX = filteredSignalXes.get(filteredSignalXes.size() - 1);
        for (double k = 0; k < lastX; k += step) {
            Complex sum = new Complex(0, 0);
            for (double n = 0; n < lastX; n += step) {
                double real = filteredSignalPoints.get(n) * Math.cos(2 * Math.PI * k * n / N);
                double imag = -filteredSignalPoints.get(n) * Math.sin(2 * Math.PI * k * n / N);
                sum = sum.add(new Complex(real, imag));
            }
            outputSignalPoints.put(k, sum);
        }

        return (DiscreteSignal) signalFactory.createDiscreteFourierTransformedSignal(outputSignalPoints);
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

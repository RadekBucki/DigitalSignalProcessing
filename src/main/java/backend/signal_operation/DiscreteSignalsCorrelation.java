package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.DiscreteSignal;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.LinkedHashMap;

public class DiscreteSignalsCorrelation {
    SignalFactory signalFactory;
    Convolution convolution;

    public DiscreteSignalsCorrelation(SignalFactory signalFactory, Convolution convolution) {
        this.signalFactory = signalFactory;
        this.convolution = convolution;
    }

    public DiscreteSignal execute(
            DiscreteSignal signal1,
            DiscreteSignal signal2,
            DiscreteSignalsCorrelationType discreteSignalsCorrelationType) {
        if (discreteSignalsCorrelationType == DiscreteSignalsCorrelationType.DIRECT) {
            return executeDirect(signal1, signal2);
        } else {
            return executeUsingConvolution(signal1, signal2);
        }
    }

    private DiscreteSignal executeUsingConvolution(DiscreteSignal signal1, DiscreteSignal signal2) {
        return convolution.execute(
                signal1,
                (DiscreteSignal) signalFactory.createDiscreteSignal(
                        signal2.getPoints().entrySet()
                                .stream()
                                .collect(Collectors.toMap(
                                        entry -> signal1.getPoints().size() - entry.getKey() - 1,
                                        Map.Entry::getValue,
                                        (u, v) -> {
                                            throw new IllegalStateException(String.format("Duplicate key %s", u));
                                        },
                                        LinkedHashMap::new
                                ))
                )
        );
    }

    private DiscreteSignal executeDirect(DiscreteSignal signal1, DiscreteSignal signal2) {
        ArrayList<Double> signal1Points = new ArrayList<>(signal1.getPoints().values());
        ArrayList<Double> signal2Points = new ArrayList<>(signal2.getPoints().values());
        return (DiscreteSignal) signalFactory.createDiscreteSignal(
                IntStream.range(0, signal1Points.size() + signal2Points.size() - 1)
                        .boxed()
                        .collect(Collectors.toMap(
                                n -> n / signal1.getF(),
                                n -> IntStream.range(0, signal1.getPoints().size())
                                        .filter(k -> (n - k) >= 0 && (n - k) < signal2Points.size())
                                        .mapToDouble(k -> signal1Points.get(k) * signal2Points.get(n - k))
                                        .sum()
                        ))
        );
    }
}

package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.DiscreteSignal;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Convolution {
    private final SignalFactory signalFactory;

    public Convolution(SignalFactory signalFactory) {
        this.signalFactory = signalFactory;
    }

    public DiscreteSignal execute(DiscreteSignal signal1, DiscreteSignal signal2) {
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
                                        .sum(),
                                (u, v) -> {
                                    throw new IllegalStateException(String.format("Duplicate key %s", u));
                                },
                                TreeMap::new
                        ))
        );
    }
}

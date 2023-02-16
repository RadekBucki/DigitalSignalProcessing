package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.DiscreteSignal;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Convolution {
    private final SignalFactory signalFactory;

    public Convolution(SignalFactory signalFactory) {
        this.signalFactory = signalFactory;
    }

    public DiscreteSignal execute(DiscreteSignal signal1, DiscreteSignal signal2) {
        AtomicReference<ArrayList<Double>> signal1Points = new AtomicReference<>(new ArrayList<>(signal1.getPoints().values()));
        ArrayList<Double> signal2Points = new ArrayList<>(signal2.getPoints().values());
        return (DiscreteSignal) signalFactory.createDiscreteSignal(
                IntStream.range(0, signal1Points.get().size() + signal2Points.size() - 1)
                        .boxed()
                        .collect(Collectors.toMap(
                                n -> n / signal1.getF(),
                                n -> IntStream.range(0, signal1.getPoints().size())
                                        .filter(k -> (n - k) >= 0 && (n - k) < signal2Points.size())
                                        .mapToDouble(k -> signal1Points.get().get(k) * signal2Points.get(n - k))
                                        .sum()
                        ))
        );
    }
}

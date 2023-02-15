package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.DiscreteSignal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Convolution {
    private final SignalFactory signalFactory;

    public Convolution(SignalFactory signalFactory) {
        this.signalFactory = signalFactory;
    }

    public DiscreteSignal execute(DiscreteSignal signal1, DiscreteSignal signal2) {
        Map<Double, Double> points = new LinkedHashMap<>();
        ArrayList<Double> signal1Points = new ArrayList<>(signal1.getPoints().values());
        ArrayList<Double> signal2Points = new ArrayList<>(signal2.getPoints().values());

        IntStream.range(0, signal1.getPoints().size() + signal2.getPoints().size() - 1)
                .forEach(n -> points.put(n / signal1.getF(), IntStream.range(0, signal1.getPoints().size())
                        .filter(k -> (n - k) >= 0 && (n - k) < signal2.getPoints().size())
                        .mapToDouble(k -> signal1Points.get(k) * signal2Points.get(n - k))
                        .sum()));

        return (DiscreteSignal) signalFactory.createDiscreteSignal(points);
    }
}

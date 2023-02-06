package backend.signal_operation.signal_reconstruction;

import backend.signal.DiscreteSignal;

import java.util.Map;

public class FirstOrderHold implements ReconstructMethod {
    @Override
    public double reconstruct(DiscreteSignal signal, double time) {
        return signal.getPoints().entrySet().stream()
                .filter(entry -> entry.getKey() <= time)
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .orElseThrow();
    }
}

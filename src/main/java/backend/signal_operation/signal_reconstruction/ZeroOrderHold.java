package backend.signal_operation.signal_reconstruction;

import backend.signal.DiscreteSignal;

import java.util.Comparator;
import java.util.Map;

public class ZeroOrderHold implements ReconstructMethod {
    @Override
    public double reconstruct(DiscreteSignal signal, double time) {
        return signal.getPoints().entrySet().stream()
                .min(Comparator.comparingDouble(entry -> Math.abs(time - entry.getKey())))
                .map(Map.Entry::getValue)
                .orElseThrow();
    }
}

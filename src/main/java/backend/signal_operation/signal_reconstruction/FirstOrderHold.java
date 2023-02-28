package backend.signal_operation.signal_reconstruction;

import backend.signal.DiscreteSignal;

import java.util.List;
import java.util.Map;

public class FirstOrderHold implements ReconstructMethod {
    @Override
    public double reconstruct(DiscreteSignal signal, double time, double frequency) {
        List<Map.Entry<Double, Double>> nearestSamples = signal.getPoints().entrySet().stream()
                .filter(x -> x.getKey() < (time + 1 / frequency))
                .filter(x -> x.getKey() > (time - 1 / frequency))
                .toList();
        if (nearestSamples.size() == 1 || nearestSamples.size() == 3) {
            return nearestSamples.get(0).getValue();
        }
        return (nearestSamples.get(0).getValue() * Math.abs(time - nearestSamples.get(1).getKey()) +
                nearestSamples.get(1).getValue() * Math.abs(time - nearestSamples.get(0).getKey())) /
                        Math.abs(nearestSamples.get(0).getKey() - nearestSamples.get(1).getKey());
    }
}

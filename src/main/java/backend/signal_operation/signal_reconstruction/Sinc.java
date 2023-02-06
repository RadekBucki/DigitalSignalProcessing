package backend.signal_operation.signal_reconstruction;

import backend.signal.DiscreteSignal;

import java.util.Map;

public class Sinc implements ReconstructMethod {
    @Override
    public double reconstruct(DiscreteSignal signal, double time) {
        return signal.getPoints().entrySet().stream()
                .mapToDouble(entry -> entry.getValue() * sinc(Math.PI * (time - entry.getKey()) / signal.getD()))
                .sum();
    }

    private double sinc(double x) {
        return x == 0 ? 1 : Math.sin(x) / x;
    }
}

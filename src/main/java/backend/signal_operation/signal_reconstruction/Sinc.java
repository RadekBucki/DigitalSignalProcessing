package backend.signal_operation.signal_reconstruction;

import backend.signal.DiscreteSignal;

public class Sinc implements ReconstructMethod {
    @Override
    public double reconstruct(DiscreteSignal signal, double time, double frequency) {
        return signal.getPoints().entrySet().stream()
                .mapToDouble(entry -> entry.getValue() *
                        sinc(time / (1 / frequency - Math.floor(entry.getKey() * frequency))))
                .sum();
    }

    private double sinc(double x) {
        x *= Math.PI;
        return x == 0 ? 1 : Math.sin(x) / x;
    }
}

package backend.signal_operation.signal_reconstruction;

import backend.signal.DiscreteSignal;

public class Sinc implements ReconstructMethod {
    int numOfSamples;

    public Sinc(int numOfSamples) {
        this.numOfSamples = numOfSamples;
    }

    @Override
    public double reconstruct(DiscreteSignal signal, double time, double frequency) {
        return signal.getPoints().entrySet().stream()
                .filter(entry -> entry.getKey() < time + ((1 / frequency) * numOfSamples))
                .filter(entry -> entry.getKey() > time - ((1 / frequency) * numOfSamples))
                .mapToDouble(entry -> entry.getValue() *
                        sinc(time / (1 / frequency) - Math.round(entry.getKey() * frequency)))
                .sum();
    }

    private double sinc(double x) {
        x *= Math.PI;
        return x == 0 ? 1 : Math.sin(x) / x;
    }
}

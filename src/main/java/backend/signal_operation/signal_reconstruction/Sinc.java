package backend.signal_operation.signal_reconstruction;

import backend.signal.DiscreteSignal;

public class Sinc implements ReconstructMethod {
    int numOfSamples;

    public Sinc(int numOfSamples) {
        this.numOfSamples = numOfSamples % 2 == 1 ? numOfSamples - 1 : numOfSamples;
    }

    @Override
    public double reconstruct(DiscreteSignal signal, double time, double frequency) {
        int currentN = (int) Math.round(time * frequency);
        final int toStart = (numOfSamples / 2) - (currentN - signal.getN1());
        final int toEnd = (numOfSamples / 2) - (signal.getN2() - currentN);
        return signal.getPoints().entrySet().stream()
                .filter(entry -> Math.round(entry.getKey() * frequency) <= currentN + (numOfSamples / 2.0) +
                        (Math.max(toStart, 0)))
                .filter(entry -> Math.round(entry.getKey() * frequency) >= currentN - (numOfSamples / 2.0) -
                        Math.max(toEnd, 0))
                .mapToDouble(entry -> entry.getValue() *
                        sinc(time / (1 / frequency) - Math.round(entry.getKey() * frequency)))
                .sum();
    }

    private double sinc(double x) {
        x *= Math.PI;
        return x == 0 ? 1 : Math.sin(x) / x;
    }
}

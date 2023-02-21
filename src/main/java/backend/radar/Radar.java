package backend.radar;

import backend.signal.ContinuousSignal;

public class Radar {
    private final double X;
    private final double Y;
    private final ContinuousSignal probingSignal;
    private final double probingSignalF;
    private final int discreteBufferSize;
    private final double signalSpeed;
    private final double workTime;

    public Radar(
            double x,
            double y,
            ContinuousSignal probingSignal,
            double probingSignalF,
            int discreteBufferSize,
            double signalSpeed,
            double workTime
    ) {
        X = x;
        Y = y;
        this.probingSignal = probingSignal;
        this.probingSignalF = probingSignalF;
        this.discreteBufferSize = discreteBufferSize;
        this.signalSpeed = signalSpeed;
        this.workTime = workTime;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public ContinuousSignal getProbingSignal() {
        return probingSignal;
    }

    public double getProbingSignalF() {
        return probingSignalF;
    }

    public double getPeriod() {
        return 1 / probingSignalF;
    }

    public int getDiscreteBufferSize() {
        return discreteBufferSize;
    }

    public double getSignalSpeed() {
        return signalSpeed;
    }

    public double getWorkTime() {
        return workTime;
    }
}

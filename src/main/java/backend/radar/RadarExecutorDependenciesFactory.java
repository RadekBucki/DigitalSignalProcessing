package backend.radar;

import backend.signal.ContinuousSignal;

public class RadarExecutorDependenciesFactory {
    public MeasuredObject createMeasuredObject(double x, double y, double speedX, double speedY) {
        return new MeasuredObject(x, y, speedX, speedY);
    }
    public Radar createRadar(
            double x,
            double y,
            ContinuousSignal probingSignal,
            double probingSignalF,
            int discreteBufferSize,
            double signalSpeed,
            double workTime
    ) {
        return new Radar(x, y, probingSignal, probingSignalF, discreteBufferSize, signalSpeed, workTime);
    }
}

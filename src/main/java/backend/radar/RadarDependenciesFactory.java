package backend.radar;


import backend.radar.model.Environment;
import backend.radar.model.MeasuredObject;
import backend.radar.model.RadarConfig;
import backend.signal.ContinuousSignal;

public class RadarDependenciesFactory {
    public MeasuredObject createMeasuredObject(double x, double y, double speedX, double speedY) {
        return new MeasuredObject(x, y, speedX, speedY);
    }

    public Environment createEnvironment(double signalSpeed, double stepTime) {
        return new Environment(signalSpeed, stepTime);
    }

    public RadarConfig createRadarConfig(ContinuousSignal probingSignal, double probingSignalF, int discreteBufferSize,
                                         double workTime, double x, double y) {
        return new RadarConfig(probingSignal, probingSignalF, discreteBufferSize, workTime, 1 / probingSignalF, x, y);
    }
}

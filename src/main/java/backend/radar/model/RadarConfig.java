package backend.radar.model;

import backend.signal.ContinuousSignal;

public record RadarConfig(ContinuousSignal probingSignal, double probingSignalF, int discreteBufferSize,
                          double workTime, double period, double x, double y) {
}

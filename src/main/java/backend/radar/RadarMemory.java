package backend.radar;

import backend.signal.DiscreteSignal;

import java.util.ArrayList;
import java.util.List;

public class RadarMemory {
    private final List<Double> radarDistances = new ArrayList<>();
    private final List<Double> realDistances = new ArrayList<>();
    private final List<Double> distancesTimes = new ArrayList<>();
    private final List<DiscreteSignal> signalSentWindows = new ArrayList<>();
    private final List<DiscreteSignal> signalReceivedWindows = new ArrayList<>();
    private final List<DiscreteSignal> correlationsWindows = new ArrayList<>();

    public List<Double> getRadarDistances() {
        return radarDistances;
    }

    public void addToRadarDistances(Double distance) {
        radarDistances.add(distance);
    }

    public List<Double> getRealDistances() {
        return realDistances;
    }

    public void addToRealDistances(Double distance) {
        realDistances.add(distance);
    }

    public List<Double> getDistancesTimes() {
        return distancesTimes;
    }

    public void addToDistancesTimes(Double time) {
        distancesTimes.add(time);
    }

    public List<DiscreteSignal> getSignalSentWindows() {
        return signalSentWindows;
    }

    public void addToSignalSentWindows(DiscreteSignal signal) {
        signalSentWindows.add(signal);
    }

    public List<DiscreteSignal> getSignalReceivedWindows() {
        return signalReceivedWindows;
    }

    public void addToSignalReceivedWindows(DiscreteSignal signal) {
        signalReceivedWindows.add(signal);
    }

    public List<DiscreteSignal> getCorrelationsWindows() {
        return correlationsWindows;
    }

    public void addToCorrelationsWindows(DiscreteSignal signal) {
        correlationsWindows.add(signal);
    }
}

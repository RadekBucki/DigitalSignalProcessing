package backend.radar;

import backend.SignalFacade;
import backend.SignalFactory;
import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;
import backend.signal_operation.DiscreteSignalsCorrelationType;

import java.util.*;

public class Radar {
    private final double X;
    private final double Y;
    private final double probingSignalF;
    private final int discreteBufferSize;
    private final double signalSpeed;
    private final double workTime;
    private final double stepTime;
    private double first = -1;
    private final DiscreteSignal signalSent;
    private final DiscreteSignal signalReceived;
    private final ContinuousSignal probingSignal;
    private final List<Double> samplesSentButNotHit = new ArrayList<>();
    private final MeasuredObject measuredObject;
    private final SignalFacade facade;
    private final List<Double> radarDistances = new ArrayList<>();
    private final List<Double> realDistances = new ArrayList<>();
    private final List<Double> distancesTimes = new ArrayList<>();
    private final TreeMap<Double, Double> allRealDistances = new TreeMap<>();
    private final List<Double> hitsTime = new ArrayList<>();
    private final SignalFactory signalFactory;
    private final List<DiscreteSignal> signalSentWindows = new ArrayList<>();
    private final List<DiscreteSignal> signalReceivedWindows = new ArrayList<>();
    private final List<DiscreteSignal> correlationsWindows = new ArrayList<>();

    public Radar(double probingSignalF, int discreteBufferSize, double signalSpeed, double workTime, double stepTime,
                 ContinuousSignal probingSignal, double radarX, double radarY,MeasuredObject measuredObject,
                 SignalFacade facade, SignalFactory signalFactory) {
        this.probingSignalF = probingSignalF;
        this.discreteBufferSize = discreteBufferSize;
        this.signalSpeed = signalSpeed;
        this.workTime = workTime;
        this.stepTime = stepTime;
        this.probingSignal = probingSignal;
        this.measuredObject = measuredObject;
        this.X = radarX;
        this.Y = radarY;
        this.signalFactory = signalFactory;
        this.facade = facade;
        signalSent = (DiscreteSignal) signalFactory.createDiscreteSignal(0, workTime, probingSignalF, 0);
        signalReceived = (DiscreteSignal) signalFactory.createDiscreteSignal(0, workTime, probingSignalF, 0);
        startWorking();
        calculateCorrelations();
    }

    private void startWorking() {
        for (double time = 0; time < workTime; time += 1 / probingSignalF) {
            double timeRounded = Math.round(time * 10000) / 10000.0;
            double probingSignalTime = Math.floor(timeRounded / probingSignal.getD());
            signalSent.addPoint(timeRounded, probingSignal.calculatePointValue(timeRounded - (probingSignalTime * probingSignal.getD())));
            samplesSentButNotHit.add(timeRounded);

            //check hit
            double firstToHit = Collections.min(samplesSentButNotHit);
            while (Math.abs(firstToHit - timeRounded) * signalSpeed >= measuredObject.calculateRealDistance(X, Y)) {
                double timeReceived = Math.round((timeRounded + Math.abs(firstToHit - timeRounded)) * 10000) / 10000.0;
                if (first == -1) {
                    first = timeReceived;
                }
                signalReceived.addPoint(timeReceived, signalSent.getPoints().get(firstToHit));
                samplesSentButNotHit.remove(firstToHit);
                hitsTime.add(timeRounded);
                firstToHit = Collections.min(samplesSentButNotHit);
            }
            if (!signalReceived.getPoints().containsKey(timeRounded) && timeRounded >= first && first != -1) {
                double timeBack = timeRounded;
                do {
                    timeBack = Math.round((timeBack - (1 / probingSignalF)) * 10000) / 10000.0;
                } while (!signalReceived.getPoints().containsKey(timeRounded));
                signalReceived.addPoint(timeRounded, signalReceived.getPoints().get(timeBack));
            }
            allRealDistances.put(timeRounded, measuredObject.calculateRealDistance(X, Y));
            measuredObject.move(1 / probingSignalF);
        }
    }

    private void calculateCorrelations() {
        TreeMap<Double, Double> pointsSentWindow = new TreeMap<>();
        TreeMap<Double, Double> pointsReceivedWindow = new TreeMap<>();
        int correlationNumber = 0;
        for (double time = first; time < workTime; time += 1 / probingSignalF) {
            double timeRounded = Math.round(time * 10000) / 10000.0;
            pointsSentWindow.put(timeRounded, signalSent.getPoints().get(timeRounded));
            pointsReceivedWindow.put(timeRounded, signalReceived.getPoints().get(timeRounded));
            if (pointsSentWindow.size() >= discreteBufferSize) {
                DiscreteSignal signalSentWindow = (DiscreteSignal) signalFactory.createDiscreteSignal(pointsSentWindow);
                DiscreteSignal signalReceivedWindow = (DiscreteSignal) signalFactory.createDiscreteSignal(pointsReceivedWindow);
                DiscreteSignal correlation = facade.discreteSignalsCorrelation(signalReceivedWindow, signalSentWindow, DiscreteSignalsCorrelationType.DIRECT);

                signalSentWindows.add(signalSentWindow);
                signalReceivedWindows.add(signalReceivedWindow);
                correlationsWindows.add(correlation);

                correlationNumber++;
                double centerKey = Math.round((Collections.max(correlation.getPoints().keySet()) + Collections.min(correlation.getPoints().keySet())) / 2 * 10000) / 10000.0;
                double maxKey = correlation.getPoints().entrySet().stream().filter(e -> e.getKey() > centerKey).max(Map.Entry.comparingByValue()).orElseThrow().getKey();
                radarDistances.add(Math.abs(maxKey - centerKey) * signalSpeed / 2);
                realDistances.add(
                        allRealDistances.get(
                                hitsTime.get((int) (Math.floor(((discreteBufferSize / 2.0) +
                                        (stepTime * probingSignalF * correlationNumber
                                                * hitsTime.size() / signalSent.getPoints().size())) * 10000) / 10000.0) - 1)
                        )
                );
                distancesTimes.add(
                        hitsTime.get((int) (Math.floor(((discreteBufferSize / 2.0) +
                                (stepTime * probingSignalF * correlationNumber
                                        * hitsTime.size() / signalSent.getPoints().size())) * 10000) / 10000.0) - 1)
                );
                for (int i = 0; i < stepTime * probingSignalF; i++) {
                    pointsSentWindow.pollFirstEntry();
                    pointsReceivedWindow.pollFirstEntry();
                }
            }
        }
    }

    public List<Double> getRadarDistances() {
        return radarDistances;
    }

    public List<Double> getRealDistances() {
        return realDistances;
    }

    public List<Double> getDistancesTimes() {
        return distancesTimes;
    }

    public List<DiscreteSignal> getSignalSentWindows() {
        return signalSentWindows;
    }

    public List<DiscreteSignal> getSignalReceivedWindows() {
        return signalReceivedWindows;
    }

    public List<DiscreteSignal> getCorrelationsWindows() {
        return correlationsWindows;
    }
}

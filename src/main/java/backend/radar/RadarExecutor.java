package backend.radar;

import backend.SignalFacade;
import backend.SignalFactory;
import backend.signal.DiscreteSignal;
import backend.signal_operation.DiscreteSignalsCorrelationType;

import java.util.*;

public class RadarExecutor {
    protected static final double POINTS_DECIMAL_PLACES_DIVISION = 10000.0;
    private final Radar radar;
    private final double stepTime;
    private double first = -1;
    private final DiscreteSignal signalSent;
    private final DiscreteSignal signalReceived;
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

    public RadarExecutor(Radar radar, double stepTime, MeasuredObject measuredObject, SignalFacade facade, SignalFactory signalFactory) {
        this.radar = radar;
        this.stepTime = stepTime;
        this.measuredObject = measuredObject;
        this.signalFactory = signalFactory;
        this.facade = facade;
        signalSent = (DiscreteSignal) signalFactory.createDiscreteSignal(
                0,
                radar.getWorkTime(),
                radar.getProbingSignalF(),
                0
        );
        signalReceived = (DiscreteSignal) signalFactory.createDiscreteSignal(
                0,
                radar.getWorkTime(),
                radar.getProbingSignalF(),
                0
        );
        startWorking();
        calculateCorrelations();
    }

    private void startWorking() {
        for (double time = 0; time < radar.getWorkTime(); time += radar.getPeriod()) {
            double timeRounded = Math.round(time * POINTS_DECIMAL_PLACES_DIVISION) / POINTS_DECIMAL_PLACES_DIVISION;
            double probingSignalTime = Math.floor(timeRounded / radar.getProbingSignal().getD());
            signalSent.addPoint(timeRounded, radar.getProbingSignal().calculatePointValue(timeRounded - (probingSignalTime * radar.getProbingSignal().getD())));
            samplesSentButNotHit.add(timeRounded);

            //check hit
            double firstToHit = Collections.min(samplesSentButNotHit);
            while (Math.abs(firstToHit - timeRounded) * radar.getSignalSpeed() >= measuredObject.calculateRealDistance(radar)) {
                double timeReceived = Math.round((timeRounded + Math.abs(firstToHit - timeRounded)) * POINTS_DECIMAL_PLACES_DIVISION) / POINTS_DECIMAL_PLACES_DIVISION;
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
                    timeBack = Math.round((timeBack - (radar.getPeriod())) * POINTS_DECIMAL_PLACES_DIVISION) / POINTS_DECIMAL_PLACES_DIVISION;
                } while (!signalReceived.getPoints().containsKey(timeRounded));
                signalReceived.addPoint(timeRounded, signalReceived.getPoints().get(timeBack));
            }
            allRealDistances.put(timeRounded, measuredObject.calculateRealDistance(radar));
            measuredObject.move(radar.getPeriod());
        }
    }

    private void calculateCorrelations() {
        TreeMap<Double, Double> pointsSentWindow = new TreeMap<>();
        TreeMap<Double, Double> pointsReceivedWindow = new TreeMap<>();
        int correlationNumber = 0;
        for (double time = first; time < radar.getWorkTime(); time += radar.getPeriod()) {
            double timeRounded = Math.round(time * POINTS_DECIMAL_PLACES_DIVISION) / POINTS_DECIMAL_PLACES_DIVISION;
            pointsSentWindow.put(timeRounded, signalSent.getPoints().get(timeRounded));
            pointsReceivedWindow.put(timeRounded, signalReceived.getPoints().get(timeRounded));
            if (pointsSentWindow.size() >= radar.getDiscreteBufferSize()) {
                DiscreteSignal signalSentWindow = (DiscreteSignal) signalFactory.createDiscreteSignal(pointsSentWindow);
                DiscreteSignal signalReceivedWindow = (DiscreteSignal) signalFactory.createDiscreteSignal(pointsReceivedWindow);
                DiscreteSignal correlation = facade.discreteSignalsCorrelation(signalReceivedWindow, signalSentWindow, DiscreteSignalsCorrelationType.DIRECT);

                signalSentWindows.add(signalSentWindow);
                signalReceivedWindows.add(signalReceivedWindow);
                correlationsWindows.add(correlation);

                correlationNumber++;
                double centerKey = Math.round((Collections.max(correlation.getPoints().keySet()) + Collections.min(correlation.getPoints().keySet())) / 2 * POINTS_DECIMAL_PLACES_DIVISION) / POINTS_DECIMAL_PLACES_DIVISION;
                double maxKey = correlation.getPoints().entrySet().stream().filter(e -> e.getKey() > centerKey).max(Map.Entry.comparingByValue()).orElseThrow().getKey();
                radarDistances.add(Math.abs(maxKey - centerKey) * radar.getSignalSpeed() / 2);
                realDistances.add(
                        allRealDistances.get(
                                hitsTime.get((int) (Math.floor(((radar.getDiscreteBufferSize() / 2.0) +
                                        (stepTime * radar.getProbingSignalF() * correlationNumber
                                                * hitsTime.size() / signalSent.getPoints().size())) * POINTS_DECIMAL_PLACES_DIVISION) / POINTS_DECIMAL_PLACES_DIVISION) - 1)
                        )
                );
                distancesTimes.add(
                        hitsTime.get((int) (Math.floor(((radar.getDiscreteBufferSize() / 2.0) +
                                (stepTime * radar.getProbingSignalF() * correlationNumber
                                        * hitsTime.size() / signalSent.getPoints().size())) * POINTS_DECIMAL_PLACES_DIVISION) / POINTS_DECIMAL_PLACES_DIVISION) - 1)
                );
                for (int i = 0; i < stepTime * radar.getProbingSignalF(); i++) {
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

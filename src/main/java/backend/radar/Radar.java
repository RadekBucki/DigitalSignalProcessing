package backend.radar;

import backend.SignalFacade;
import backend.SignalFactory;
import backend.radar.model.Environment;
import backend.radar.model.MeasuredObject;
import backend.radar.model.RadarConfig;
import backend.signal.DiscreteSignal;
import backend.signal_operation.DiscreteSignalsCorrelationType;

import java.util.*;

import static backend.Rounder.floor;
import static backend.Rounder.round;

public class Radar {
    private final RadarConfig config;
    private final Environment environment;
    private final MeasuredObject measuredObject;
    private final SignalFacade facade;
    private final SignalFactory signalFactory;

    private double first = -1;
    private final DiscreteSignal signalSent;
    private final DiscreteSignal signalReceived;
    private final List<Double> samplesSentButNotHit = new ArrayList<>();
    private final TreeMap<Double, Double> allRealDistances = new TreeMap<>();
    private final List<Double> hitsTime = new ArrayList<>();
    private final RadarMemory radarMemory = new RadarMemory();

    public Radar(
            RadarConfig config,
            Environment environment,
            MeasuredObject measuredObject,
            SignalFacade facade,
            SignalFactory signalFactory
    ) {
        this.measuredObject = measuredObject;
        this.environment = environment;
        this.config = config;
        this.signalFactory = signalFactory;
        this.facade = facade;
        signalSent = (DiscreteSignal) signalFactory.createDiscreteSignal(
                0,
                config.workTime(),
                config.probingSignalF(),
                0
        );
        signalReceived = (DiscreteSignal) signalFactory.createDiscreteSignal(
                0,
                config.workTime(),
                config.probingSignalF(),
                0
        );
    }

    private void startWorking() {
        for (double time = 0; time < config.workTime(); time += config.period()) {
            double timeRounded = round(time);
            double probingSignalTime = Math.floor(timeRounded / config.probingSignal().getD());
            signalSent.addPoint(
                    timeRounded,
                    config.probingSignal().calculatePointValue(
                            timeRounded - (probingSignalTime * config.probingSignal().getD())
                    )
            );
            samplesSentButNotHit.add(timeRounded);

            //check hit
            double firstToHit = Collections.min(samplesSentButNotHit);
            while (Math.abs(firstToHit - timeRounded) * environment.signalSpeed()
                    >= measuredObject.calculateRealDistance(config.x(), config.y())) {
                double timeReceived = round(timeRounded + Math.abs(firstToHit - timeRounded));
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
                    timeBack = round(timeBack - (config.period()));
                } while (!signalReceived.getPoints().containsKey(timeBack));
                signalReceived.addPoint(timeRounded, signalReceived.getPoints().get(timeBack));
            }
            allRealDistances.put(timeRounded, measuredObject.calculateRealDistance(config.x(), config.y()));
            measuredObject.move(config.period());
        }
    }

    private void calculateCorrelations() {
        TreeMap<Double, Double> pointsSentWindow = new TreeMap<>();
        TreeMap<Double, Double> pointsReceivedWindow = new TreeMap<>();
        int correlationNumber = 0;
        for (double time = first; time < config.workTime(); time += config.period()) {
            double timeRounded = round(time);
            pointsSentWindow.put(timeRounded, signalSent.getPoints().get(timeRounded));
            pointsReceivedWindow.put(timeRounded, signalReceived.getPoints().get(timeRounded));
            if (pointsSentWindow.size() < config.discreteBufferSize()) {
                continue;
            }
            DiscreteSignal signalSentWindow = (DiscreteSignal) signalFactory.createDiscreteSignal(pointsSentWindow);
            DiscreteSignal signalReceivedWindow = (DiscreteSignal) signalFactory.createDiscreteSignal(
                    pointsReceivedWindow
            );
            DiscreteSignal correlation = facade.discreteSignalsCorrelation(
                    signalReceivedWindow,
                    signalSentWindow,
                    DiscreteSignalsCorrelationType.DIRECT
            );

            radarMemory.addToSignalSentWindows(signalSentWindow);
            radarMemory.addToSignalReceivedWindows(signalReceivedWindow);
            radarMemory.addToCorrelationsWindows(correlation);

            correlationNumber++;
            double centerKey = round(
                    (
                            Collections.max(correlation.getPoints().keySet()) +
                                    Collections.min(correlation.getPoints().keySet())
                    ) / 2
            );
            double maxKey = correlation.getPoints()
                    .entrySet()
                    .stream()
                    .filter(e -> e.getKey() > centerKey)
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow()
                    .getKey();
            radarMemory.addToRadarDistances(Math.abs(maxKey - centerKey) * environment.signalSpeed() / 2);
            double hitTime = hitsTime.get((int) (floor(
                    (config.discreteBufferSize() / 2.0) +
                            (environment.stepTime() * config.probingSignalF() * correlationNumber
                                    * hitsTime.size() / signalSent.getPoints().size())
            )) - 1);
            radarMemory.addToRealDistances(allRealDistances.get(hitTime));
            radarMemory.addToDistancesTimes(hitTime);
            for (int i = 0; i < environment.stepTime() * config.probingSignalF(); i++) {
                pointsSentWindow.pollFirstEntry();
                pointsReceivedWindow.pollFirstEntry();
            }
        }
    }

    public RadarMemory getRadarMemory() {
        startWorking();
        calculateCorrelations();
        return radarMemory;
    }
}

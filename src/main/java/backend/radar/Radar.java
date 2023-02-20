package backend.radar;

import backend.SignalFacade;
import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;
import backend.signal_operation.DiscreteSignalsCorrelationType;
import frontend.chart.ChartGenerator;
import org.jfree.chart.ChartUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class Radar {
    private final double X = 0;
    private final double Y = 0;
    private final double probingSignalF;
    private final int discreteBufferSize;
    private final double signalSpeed;
    private final double workTime;
    private final double stepTime;
    private double nextStepTime;
    private final DiscreteSignal signalSent;
    private final DiscreteSignal signalReceived;
    private final ContinuousSignal probingSignal;
    private final List<Double> samplesSentButNotHit = new ArrayList<>();
    private final MeasuredObject measuredObject = new MeasuredObject(10, 10, 1, 1);
    private final SignalFacade facade = new SignalFacade();
    private List<Double> radarDistances = new ArrayList<>();
    private List<Double> realDistances = new ArrayList<>();

    public Radar(double probingSignalF, int discreteBufferSize, double signalSpeed, double workTime, double stepTime,
                 ContinuousSignal probingSignal) {
        this.probingSignalF = probingSignalF;
        this.discreteBufferSize = discreteBufferSize;
        this.signalSpeed = signalSpeed;
        this.workTime = workTime;
        this.stepTime = stepTime;
        this.probingSignal = probingSignal;
        this.nextStepTime = stepTime;
        signalSent = new DiscreteSignal(0, workTime, probingSignalF);
        signalReceived = new DiscreteSignal(0, workTime, probingSignalF);
    }

    public void startWorking() {
        for (double time = 0; time < workTime; time += 1 / probingSignalF) {
            double timeRounded = Math.round(time * 10000) / 10000.0;
            if (signalSent.getPoints().size() > discreteBufferSize) {
                signalSent.removePoint(Collections.min(signalSent.getPoints().keySet()));
            }
            double probingSignalTime = Math.floor(timeRounded / probingSignal.getD());
            signalSent.addPoint(timeRounded, probingSignal.calculatePointValue(timeRounded - (probingSignalTime * probingSignal.getD())));
            samplesSentButNotHit.add(timeRounded);

            //check hit
            double firstToHit = Collections.min(samplesSentButNotHit);
            while (Math.abs(firstToHit - timeRounded) * signalSpeed >= measuredObject.calculateRealDistance(X, Y)) {
                double timeReceived = Math.round((timeRounded + Math.abs(firstToHit - timeRounded)) * 10000) / 10000.0;
                if (signalReceived.getPoints().size() > discreteBufferSize) {
                    signalReceived.removePoint(Collections.min(signalReceived.getPoints().keySet()));
                }
                signalReceived.addPoint(timeReceived, signalSent.getPoints().get(firstToHit));
                samplesSentButNotHit.remove(firstToHit);
                firstToHit = Collections.min(samplesSentButNotHit);
            }

            //calculate step
            if (timeRounded >= nextStepTime && signalSent.getPoints().size() == signalReceived.getPoints().size()) {
                DiscreteSignal correlation = facade.discreteSignalsCorrelation(signalSent, signalReceived, DiscreteSignalsCorrelationType.DIRECT);
                try {
                    ChartUtilities.saveChartAsPNG(
                            new File("chart.png"),
                            ChartGenerator.generateAmplitudeTimeChart(
                                    correlation.getPoints(),
                                    correlation instanceof DiscreteSignal
                            ),
                            400,
                            220
                    );
                } catch (Exception e) {
                    System.out.println("DUPA");
                }

                double centerKey = Math.round((Collections.max(correlation.getPoints().keySet()) + Collections.min(correlation.getPoints().keySet())) / 2 * 10000) / 10000.0;
                double maxKey = correlation.getPoints().entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow().getKey();
                radarDistances.add(Math.abs(maxKey - centerKey) * signalSpeed);
                realDistances.add(measuredObject.calculateRealDistance(X, Y));
                do {
                    nextStepTime += stepTime;
                } while (nextStepTime < timeRounded);
            }

            measuredObject.move(1 / probingSignalF);
        }
    }
}

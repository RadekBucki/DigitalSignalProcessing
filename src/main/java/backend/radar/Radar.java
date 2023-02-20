package backend.radar;

import backend.SignalFacade;
import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;
import backend.signal_operation.DiscreteSignalsCorrelationType;
import frontend.chart.ChartGenerator;
import org.jfree.chart.ChartUtilities;

import java.io.File;
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
    private double first = -1;
    private final DiscreteSignal signalSent;
    private final DiscreteSignal signalReceived;
    private final ContinuousSignal probingSignal;
    private final List<Double> samplesSentButNotHit = new ArrayList<>();
    private final MeasuredObject measuredObject = new MeasuredObject(20, 10, -1, 0);
    private final SignalFacade facade = new SignalFacade();
    private List<Double> radarDistances = new ArrayList<>();
    private List<Double> realDistances = new ArrayList<>();
    private TreeMap<Double, Double> allRealDistances = new TreeMap<>();
    private List<Double> hitsTime = new ArrayList<>();

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

        TreeMap<Double, Double> pointsSentWindow = new TreeMap<>();
        TreeMap<Double, Double> pointsReceivedWindow = new TreeMap<>();
        int correlationNumber = 0;
        for (double time = first; time < workTime; time += 1 / probingSignalF) {
            double timeRounded = Math.round(time * 10000) / 10000.0;
            pointsSentWindow.put(timeRounded, signalSent.getPoints().get(timeRounded));
            pointsReceivedWindow.put(timeRounded, signalReceived.getPoints().get(timeRounded));
            if (pointsSentWindow.size() >= discreteBufferSize) {
                DiscreteSignal signalSentWindow = new DiscreteSignal(pointsSentWindow);
                DiscreteSignal signalReceivedWindow = new DiscreteSignal(pointsReceivedWindow);
                DiscreteSignal correlation = facade.discreteSignalsCorrelation(signalReceivedWindow, signalSentWindow, DiscreteSignalsCorrelationType.DIRECT);

                try {
                    ChartUtilities.saveChartAsPNG(
                            new File("chart1.png"),
                            ChartGenerator.generateAmplitudeTimeChart(
                                    signalSentWindow.getPoints(),
                                    signalSentWindow instanceof DiscreteSignal
                            ),
                            400,
                            220
                    );
                } catch (Exception ignored) {
                    //ignored
                }

                try {
                    ChartUtilities.saveChartAsPNG(
                            new File("chart2.png"),
                            ChartGenerator.generateAmplitudeTimeChart(
                                    signalReceivedWindow.getPoints(),
                                    signalReceivedWindow instanceof DiscreteSignal
                            ),
                            400,
                            220
                    );
                } catch (Exception ignored) {
                    //ignored
                }

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
                } catch (Exception ignored) {
                    //ignored
                }

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

                for (int i = 0; i < stepTime * probingSignalF; i++) {
                    pointsSentWindow.pollFirstEntry();
                    pointsReceivedWindow.pollFirstEntry();
                }
            }
        }
    }
}

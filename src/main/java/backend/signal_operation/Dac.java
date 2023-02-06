package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;
import backend.signal_operation.signal_reconstruction.ReconstructMethod;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class Dac {
    protected static final double POINTS_DECIMAL_PLACES_DIVISION = 10000;
    private final ReconstructMethodFactory reconstructMethodFactory;
    private final SignalFactory signalFactory;

    public Dac(ReconstructMethodFactory reconstructMethodFactory, SignalFactory signalFactory) {
        this.reconstructMethodFactory = reconstructMethodFactory;
        this.signalFactory = signalFactory;
    }

    public ContinuousSignal reconstructZeroOrderHold(DiscreteSignal discreteSignal) {
        return reconstruct(discreteSignal, reconstructMethodFactory.createZeroOrderHold());
    }

    public ContinuousSignal reconstructFirstOrderHold(DiscreteSignal discreteSignal) {
        return reconstruct(discreteSignal, reconstructMethodFactory.createFirstOrderHold());
    }

    public ContinuousSignal reconstructSinc(DiscreteSignal discreteSignal, int numOfSamples) {
        return reconstruct(discreteSignal, reconstructMethodFactory.createSinc(numOfSamples));
    }

    private ContinuousSignal reconstruct(DiscreteSignal discreteSignal, ReconstructMethod reconstructMethod) {
        double f = discreteSignal.getF();
        double t1 = discreteSignal.getN1() / discreteSignal.getF();
        double t2 = discreteSignal.getN2() / discreteSignal.getF();
        Map<Double, Double> points = new LinkedHashMap<>();
        for (double i = t1; i <= t2; i += 1.0 / POINTS_DECIMAL_PLACES_DIVISION) {
            double iRounded = Math.round(i * POINTS_DECIMAL_PLACES_DIVISION) / POINTS_DECIMAL_PLACES_DIVISION;
            points.put(iRounded, reconstructMethod.reconstruct(discreteSignal, iRounded, f));
        }
        return (ContinuousSignal) signalFactory.createContinuousSignal(points);
    }

    public Map<String, Double> calculateStats(ContinuousSignal continuousSignal1, ContinuousSignal continuousSignal2) {
        return Map.of(
                "MSE", calculateMse(continuousSignal1, continuousSignal2),
                "SNR", calculateSnr(continuousSignal1, continuousSignal2),
                "PSNR", calculatePsnr(continuousSignal1, continuousSignal2),
                "MD", calculateMd(continuousSignal1, continuousSignal2)
        );
    }

    private double calculateMse(ContinuousSignal continuousSignal1, ContinuousSignal continuousSignal2) {
        return continuousSignal1.getPoints()
                .entrySet()
                .stream()
                .mapToDouble(
                        entry -> {
                            double difference = entry.getValue()
                                    - continuousSignal2.getPoints().getOrDefault(entry.getKey(), 0.0);
                            return difference * difference;
                        }
                )
                .average()
                .orElse(0.0);
    }

    private double calculateSnr(ContinuousSignal continuousSignal1, ContinuousSignal continuousSignal2) {
        return 10 * Math.log10(
                continuousSignal1.getPoints()
                        .values()
                        .stream()
                        .mapToDouble(value -> value * value)
                        .sum()
                        / (calculateMse(continuousSignal1, continuousSignal2) * continuousSignal1.getPoints().size())
        );
    }

    private double calculatePsnr(ContinuousSignal continuousSignal1, ContinuousSignal continuousSignal2) {
        return 10 * Math.log10(
                continuousSignal1.getPoints()
                        .values()
                        .stream()
                        .max(Double::compareTo)
                        .orElse(0.0) / calculateMse(continuousSignal1, continuousSignal2)
        );
    }

    public double calculateMd(ContinuousSignal continuousSignal1, ContinuousSignal continuousSignal2) {
        return continuousSignal1.getPoints()
                .entrySet()
                .stream()
                .mapToDouble(entry -> Math.abs(
                        entry.getValue() - continuousSignal2.getPoints().getOrDefault(entry.getKey(), 0.0)
                ))
                .max()
                .orElse(0.0) / calculateMse(continuousSignal1, continuousSignal2);
    }
}

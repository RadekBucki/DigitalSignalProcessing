package backend.signal_operation;

import backend.SignalFactory;
import backend.SignalOperationFactory;
import backend.signal.DiscreteSignal;
import org.apache.commons.math3.complex.Complex;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DiscreteCosineTransform {

    private final SignalFactory signalFactory;
    private final SignalOperationFactory signalOperationFactory;

    public DiscreteCosineTransform(SignalFactory signalFactory, SignalOperationFactory signalOperationFactory) {
        this.signalFactory = signalFactory;
        this.signalOperationFactory = signalOperationFactory;
    }

    public DiscreteSignal execute(DiscreteSignal signal, TransformType transformType) {
        if (transformType == TransformType.DIRECT) {
            return executeDirect(signal);
        } else {
            return executeFast(signal);
        }
    }

    private DiscreteSignal executeDirect(DiscreteSignal signal) {
        Map<Double, Double> transformPoints = new TreeMap<>();
        List<Double> pointsTimes = signal.getPoints().keySet().stream().toList();
        List<Double> pointsValues = signal.getPoints().values().stream().toList();
        int N = pointsValues.size();
        for (int i = 0; i < N; i++) {
            double sum = 0;
            for (int j = 0; j < N; j++) {
                sum += pointsValues.get(j) * Math.cos((Math.PI * (2 * j + 1) * i) / (2 * N));
            }
            transformPoints.put(pointsTimes.get(i), sum * calculateCm(N, i));
        }
        return (DiscreteSignal) signalFactory.createDiscreteSignal(transformPoints);
    }

    private DiscreteSignal executeFast(DiscreteSignal signal) {
        List<Double> pointsTimes = signal.getPoints().keySet().stream().toList();
        List<Double> pointsValues = signal.getPoints().values().stream().toList();
        List<Double> yNPointsValues = IntStream.range(0, pointsValues.size())
                .filter(i -> i % 2 == 0)
                .mapToDouble(pointsValues::get)
                .boxed().collect(Collectors.toList());
        yNPointsValues.addAll(
                IntStream.range(0, pointsValues.size())
                        .map(i -> pointsValues.size() - i - 1)
                        .filter(i -> i % 2 == 1)
                        .mapToDouble(pointsValues::get)
                        .boxed().toList()
        );
        Map<Double, Double> result = IntStream.range(0, pointsTimes.size())
                .boxed()
                .collect(Collectors.toMap(pointsTimes::get, yNPointsValues::get));
        List<Double> pointsValuesAfterDFT = signalOperationFactory.createDiscreteFourierTransform()
                .execute((DiscreteSignal) signalFactory.createDiscreteSignal(result), TransformType.DIRECT)
                .getPoints().values().stream().toList();
        Map<Double, Double> transformPoints = new TreeMap<>();
        int N = pointsValues.size();
        for (int i = 0; i < N; i++) {
            transformPoints.put(pointsTimes.get(i), calculateExpPiM(N, i).multiply(calculateCm(N, i))
                    .multiply(pointsValuesAfterDFT.get(i)).getReal());
        }
        return (DiscreteSignal) signalFactory.createDiscreteSignal(transformPoints);
    }

    private double calculateCm(int N, int m) {
        if (m == 0) {
            return 1.0 / N;
        }
        return 2.0 / N;
    }

    private Complex calculateExpPiM(int N, int m) {
        Complex i = new Complex(0, 1);
        return i.multiply((-Math.PI * m) / (2 * N)).exp();
    }
}

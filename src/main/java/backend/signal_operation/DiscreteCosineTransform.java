package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.DiscreteSignal;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DiscreteCosineTransform {

    SignalFactory signalFactory;

    public DiscreteCosineTransform(SignalFactory signalFactory) {
        this.signalFactory = signalFactory;
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
        return null; //TODO: TO IMPLEMENT
    }

    private double calculateCm(int N, int m) {
        if (m == 0) {
            return 1.0/N;
        }
        return 2.0/N;
    }
}

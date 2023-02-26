package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.DiscreteSignal;
import org.apache.commons.math3.complex.Complex;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DiscreteFourierTransformWithDecimationInTimeDomain {

    SignalFactory signalFactory;

    public DiscreteFourierTransformWithDecimationInTimeDomain(SignalFactory signalFactory) {
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
        List<Double> pointsValues = signal.getPoints().values().stream().toList();
        List<Double> pointsTimes = signal.getPoints().keySet().stream().toList();
        Map<Double, Complex> transformPoints = new TreeMap<>();
        int N = pointsValues.size();
        for (int i = 0; i < N; i++) {
            Complex sum = new Complex(0, 0);
            for (int j = 0; j < N; j++) {
                sum = sum.add(
                        calculateWn(N).pow(i * j).multiply(pointsValues.get(j))
                );
            }
            transformPoints.put(pointsTimes.get(i), sum.divide(N));
        }
        return signalFactory.createDiscreteFourierTransformedSignal(transformPoints);
    }

    private DiscreteSignal executeFast(DiscreteSignal signal) {
        return null; //TODO: TO IMPLEMENT
    }

    private Complex calculateWn(int N) {
        Complex i = new Complex(0, 1);
        return i.multiply(2 * Math.PI / N).exp();
    }
}

package backend.signal_operation;

import backend.signal.DiscreteSignal;
import org.apache.commons.math3.complex.Complex;

import java.util.List;

public class DiscreteFourierTransformWithDecimationInTimeDomain {
    public DiscreteSignal execute(DiscreteSignal signal, TransformType transformType) {
        if (transformType == TransformType.DIRECT) {
            return executeDirect(signal);
        } else {
            return executeFast(signal);
        }
    }

    private DiscreteSignal executeDirect(DiscreteSignal signal) {
        List<Double> pointsValues = signal.getPoints().values().stream().toList();
        int N = pointsValues.size();
        for (int i = 0; i < N; i++) {
            Complex sum = new Complex(0, 0);
            for (int j = 0; j < N; j++) {
                sum = sum.add(
                        calculateWn(N).pow(i * j).multiply(pointsValues.get(j))
                );
            }
        }
        return null;
    }

    private DiscreteSignal executeFast(DiscreteSignal signal) {
        return null; //TODO: TO IMPLEMENT
    }

    private Complex calculateWn(int N) {
        Complex i = new Complex(0, 1);
        return i.multiply(2 * Math.PI / N).exp();
    }
}

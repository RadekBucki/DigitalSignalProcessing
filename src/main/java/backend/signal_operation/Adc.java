package backend.signal_operation;

import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;
import backend.signal_operation.signal_quantization.QuantizationMethod;

import java.util.ArrayList;
import java.util.List;

public class Adc {
    private final QuantizationMethodFactory quantizationMethodFactory = new QuantizationMethodFactory();

    public DiscreteSignal sampling(ContinuousSignal continuousSignal, double samplingFrequency) {
        DiscreteSignal discreteSignal = new DiscreteSignal(continuousSignal.getA(), continuousSignal.getD(),
                samplingFrequency);
        double ts = 1 / samplingFrequency;
        for (double i = continuousSignal.getT1(); i < continuousSignal.getT2(); i += ts) {
            discreteSignal.addPoint(i, continuousSignal.calculatePointValue(i));
        }
        return discreteSignal;
    }

    public DiscreteSignal quantizationWithTruncation(DiscreteSignal discreteSignal, int numOfLevels) {
        QuantizationMethod quantizationMethod = quantizationMethodFactory.createQuantizationWithTruncation();
        return quantization(quantizationMethod, discreteSignal, numOfLevels);
    }

    public DiscreteSignal quantizationWithRounding(DiscreteSignal discreteSignal, int numOfLevels) {
        QuantizationMethod quantizationMethod = quantizationMethodFactory.createQuantizationWithRounding();
        return quantization(quantizationMethod, discreteSignal, numOfLevels);
    }

    private DiscreteSignal quantization(QuantizationMethod method, DiscreteSignal discreteSignal,
                                        int numOfLevels) {
        List<Double> levels = new ArrayList<>();
        double levelsDiff = 2 * discreteSignal.getA() / numOfLevels;
        for (double i = -discreteSignal.getA(); i < discreteSignal.getA(); i += levelsDiff) {
            levels.add(i);
        }
        DiscreteSignal discreteQuantizedSignal = new DiscreteSignal(discreteSignal.getA(), discreteSignal.getD(),
                discreteSignal.getF());
        discreteSignal.getPoints().entrySet().stream()
                .peek(entry -> entry.setValue(method.quantize(levels, entry.getValue())))
                .forEach(entryQuantized -> discreteQuantizedSignal.addPoint(entryQuantized.getKey(), entryQuantized.getValue()));
        return discreteQuantizedSignal;
    }
}

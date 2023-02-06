package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.AbstractSignal;
import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;
import backend.signal_operation.signal_quantization.QuantizationMethod;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class Adc {
    private final QuantizationMethodFactory quantizationMethodFactory;
    private final SignalFactory signalFactory;
    public Adc(QuantizationMethodFactory quantizationMethodFactory, SignalFactory signalFactory) {
        this.quantizationMethodFactory = quantizationMethodFactory;
        this.signalFactory = signalFactory;
    }

    public AbstractSignal sampling(ContinuousSignal continuousSignal, double samplingFrequency) {
        AbstractSignal discreteSignal = signalFactory.createDiscreteSignal(continuousSignal.getA(),
                continuousSignal.getD(), samplingFrequency, continuousSignal.getT1());
        DoubleStream.iterate(continuousSignal.getT1(), d -> d + (1 / samplingFrequency))
                .limit((long) (continuousSignal.getT2() * samplingFrequency - continuousSignal.getT1() * samplingFrequency + 1))
                .forEach(d -> discreteSignal.addPoint(d, continuousSignal.calculatePointValue(d)));
        return discreteSignal;
    }

    public AbstractSignal quantizationWithTruncation(DiscreteSignal discreteSignal, int numOfLevels) {
        QuantizationMethod quantizationMethod = quantizationMethodFactory.createQuantizationWithTruncation();
        return quantization(quantizationMethod, discreteSignal, numOfLevels);
    }

    public AbstractSignal quantizationWithRounding(DiscreteSignal discreteSignal, int numOfLevels) {
        QuantizationMethod quantizationMethod = quantizationMethodFactory.createQuantizationWithRounding();
        return quantization(quantizationMethod, discreteSignal, numOfLevels);
    }

    private AbstractSignal quantization(QuantizationMethod method, DiscreteSignal discreteSignal,
                                        int numOfLevels) {
        List<Double> levels = IntStream.range(0, numOfLevels)
                .mapToDouble(i -> -discreteSignal.getA() + i * 2 * discreteSignal.getA() / (numOfLevels - 1))
                .boxed()
                .collect(Collectors.toList());
        AbstractSignal discreteQuantizedSignal = signalFactory.createDiscreteSignal(discreteSignal.getA(),
                discreteSignal.getD(), discreteSignal.getF(), discreteSignal.getN1() / discreteSignal.getF());
        discreteSignal.getPoints().entrySet().stream()
                .peek(entry -> entry.setValue(method.quantize(levels, entry.getValue())))
                .forEach(entryQuantized ->
                        discreteQuantizedSignal.addPoint(entryQuantized.getKey(), entryQuantized.getValue()));
        return discreteQuantizedSignal;
    }
}

package backend;

import backend.signal.AbstractSignal;
import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;
import backend.signal.serialize.SignalSerializer;
import backend.signal_operation.*;
import backend.signal_operation.pass.Pass;
import backend.signal_operation.window.Window;

import java.util.List;
import java.util.Map;

public class SignalFacade {
    private final SignalFactory signalFactory = new SignalFactory();
    private final SignalOperationFactory signalOperationFactory = new SignalOperationFactory(signalFactory);
    private final WindowFactory windowFactory = new WindowFactory();
    private final PassFactory passFactory = new PassFactory();

    public AbstractSignal add(AbstractSignal signal1, AbstractSignal signal2) {
        return signalOperationFactory.createSignalAdd(signalFactory).execute(signal1, signal2);
    }

    public AbstractSignal subtract(AbstractSignal signal1, AbstractSignal signal2) {
        return signalOperationFactory.createSignalSubtract(signalFactory).execute(signal1, signal2);
    }

    public AbstractSignal multiply(AbstractSignal signal1, AbstractSignal signal2) {
        return signalOperationFactory.createSignalMultiply(signalFactory).execute(signal1, signal2);
    }

    public AbstractSignal divide(AbstractSignal signal1, AbstractSignal signal2) {
        return signalOperationFactory.createSignalDivide(signalFactory).execute(signal1, signal2);
    }

    public AbstractSignal getSignal(Class<?> name, List<Double> parameters) {
        return signalFactory.getSignal(name, parameters);
    }

    public ContinuousSignal reconstructZeroOrderHold(DiscreteSignal discreteSignal, Integer numberOfSamples) {
        return signalOperationFactory.createDac().reconstructZeroOrderHold(discreteSignal);
    }

    public ContinuousSignal reconstructFirstMethodHold(DiscreteSignal discreteSignal, Integer numberOfSamples) {
        return signalOperationFactory.createDac().reconstructFirstOrderHold(discreteSignal);
    }

    public ContinuousSignal reconstructSinc(DiscreteSignal discreteSignal, Integer numberOfSamples) {
        return signalOperationFactory.createDac().reconstructSinc(discreteSignal, numberOfSamples);
    }

    public Map<String, Double> calculateDacStats(ContinuousSignal continuousSignal1, ContinuousSignal continuousSignal2) {
        return signalOperationFactory.createDac().calculateStats(continuousSignal1, continuousSignal2);
    }

    public Class<AbstractSignal> getDefaultSignal() {
        return signalFactory.getDefaultSignal();
    }

    public List<Class<? extends AbstractSignal>> getPossibleSignals() {
        return signalFactory.getPossibleSignals();
    }

    public AbstractSignal readSignal(String filePath) {
        return SignalSerializer.read(filePath);
    }

    public void writeSignal(AbstractSignal signal, String filePath) {
        SignalSerializer.write(signal, filePath);
    }

    public AbstractSignal sampling(ContinuousSignal continuousSignal, double samplingFrequency) {
        return signalOperationFactory.createAdc()
                .sampling(continuousSignal, samplingFrequency);
    }

    public AbstractSignal quantizationWithTruncate(DiscreteSignal continuousSignal, int numOfLevels) {
        return signalOperationFactory.createAdc()
                .quantizationWithTruncation(continuousSignal, numOfLevels);
    }

    public AbstractSignal quantizationWithRounding(DiscreteSignal continuousSignal, int numOfLevels) {
        return signalOperationFactory.createAdc()
                .quantizationWithRounding(continuousSignal, numOfLevels);
    }

    public DiscreteSignal convolution(DiscreteSignal signal1, DiscreteSignal signal2) {
        return signalOperationFactory.createConvolution().execute(signal1, signal2);
    }

    public DiscreteSignal filter(DiscreteSignal signal, PassType passType, WindowType windowType, int M, double f0) {
        Window window = switch (windowType) {
            case RECTANGULAR -> windowFactory.createRectangularWindow();
            case BLACKMAN -> windowFactory.createBlackmanWindow(M);
            case HAMMING -> windowFactory.createHammingWindow(M);
            case HANNING -> windowFactory.createHanningWindow(M);
        };
        Pass pass = switch (passType) {
            case LOW_PASS -> passFactory.createLowPass(M, f0, signal.getF(), window);
            case BAND_PASS -> passFactory.createBandPass(M, f0, signal.getF(), window);
            case HIGH_PASS -> passFactory.createHighPass(M, f0, signal.getF(), window);
        };
        return signalOperationFactory.createFilter().execute(signal, pass);
    }

    public DiscreteSignal discreteSignalsCorrelation(
            DiscreteSignal discreteSignal1,
            DiscreteSignal discreteSignal,
            DiscreteSignalsCorrelationType discreteSignalsCorrelationType
    ) {
        return signalOperationFactory.createDiscreteSignalsCorrelation().execute(discreteSignal1, discreteSignal, discreteSignalsCorrelationType);
    }
}

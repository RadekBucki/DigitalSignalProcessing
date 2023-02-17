package backend;

import backend.signal_operation.*;

public class SignalOperationFactory {
    private final QuantizationMethodFactory quantizationMethodFactory = new QuantizationMethodFactory();
    private final SignalFactory signalFactory;
    private final ReconstructMethodFactory reconstructMethodFactory = new ReconstructMethodFactory();

    public SignalOperationFactory(SignalFactory signalFactory) {
        this.signalFactory = signalFactory;
    }

    public SignalAdd createSignalAdd(SignalFactory signalFactory) {
        return new SignalAdd(signalFactory);
    }
    public SignalSubtract createSignalSubtract(SignalFactory signalFactory) {
        return new SignalSubtract(signalFactory);
    }
    public SignalMultiply createSignalMultiply(SignalFactory signalFactory) {
        return new SignalMultiply(signalFactory);
    }
    public SignalDivide createSignalDivide(SignalFactory signalFactory) {
        return new SignalDivide(signalFactory);
    }
    public Adc createAdc() {
        return new Adc(quantizationMethodFactory, signalFactory);
    }
    public Dac createDac() {
        return new Dac(reconstructMethodFactory, signalFactory);
    }
    public Convolution createConvolution() {
        return new Convolution(signalFactory);
    }

    public Filter createFilter() {
        return new Filter(signalFactory, createConvolution());
    }

    public DiscreteSignalsCorrelation createDiscreteSignalsCorrelation() {
        return new DiscreteSignalsCorrelation(signalFactory, createConvolution());
    }
}

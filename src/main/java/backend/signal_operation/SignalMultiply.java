package backend.signal_operation;

import backend.SignalFactory;

import java.util.function.Function;

public class SignalMultiply extends AbstractSignalOperation {
    public SignalMultiply(SignalFactory signalFactory) {
        super(signalFactory);
    }

    @Override
    protected Double operation(double signal1Amplitude, double signal2Amplitude) {
        return signal1Amplitude * signal2Amplitude;
    }

    @Override
    protected Function<Double, Double> operation(Function<Double, Double> f1, Function<Double, Double> f2) {
        return x -> f1.apply(x) * f2.apply(x);
    }
}

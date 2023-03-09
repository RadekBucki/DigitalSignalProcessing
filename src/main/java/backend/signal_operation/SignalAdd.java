package backend.signal_operation;

import backend.SignalFactory;

import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

public class SignalAdd extends AbstractSignalOperation {
    public SignalAdd(SignalFactory signalFactory) {
        super(signalFactory);
    }

    @Override
    protected Double operation(double signal1Amplitude, double signal2Amplitude) {
        return signal1Amplitude + signal2Amplitude;
    }

    @Override
    protected DoubleUnaryOperator operation(DoubleUnaryOperator f1, DoubleUnaryOperator f2) {
        return x -> f1.applyAsDouble(x) + f2.applyAsDouble(x);
    }
}

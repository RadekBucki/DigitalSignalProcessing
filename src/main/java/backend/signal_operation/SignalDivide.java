package backend.signal_operation;

import backend.SignalFactory;

import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

public class SignalDivide extends AbstractSignalOperation {
    public SignalDivide(SignalFactory signalFactory) {
        super(signalFactory);
    }
    @Override
    protected Double operation(double signal1Amplitude, double signal2Amplitude) {
        if (Math.abs(signal1Amplitude - signal2Amplitude) < 0.0001) {
            return 1.0;
        }
        if (signal2Amplitude == 0) {
            return 0.0;
        }
        return signal1Amplitude / signal2Amplitude;
    }

    @Override
    protected DoubleUnaryOperator operation(DoubleUnaryOperator f1, DoubleUnaryOperator f2) {
        return x -> {
            if (f2.applyAsDouble(x) != 0) {
                return f1.applyAsDouble(x) / f2.applyAsDouble(x);
            }
            if (Math.abs(f1.applyAsDouble(x) - f2.applyAsDouble(x)) < 0.0001) {
                return 1.0;
            }
            return 0.0;
        };
    }
}

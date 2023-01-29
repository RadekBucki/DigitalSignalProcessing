package backend;

import backend.signal_operation.SignalAdd;
import backend.signal_operation.SignalDivide;
import backend.signal_operation.SignalMultiply;
import backend.signal_operation.SignalSubtract;

public class SignalOperationFactory {
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
}

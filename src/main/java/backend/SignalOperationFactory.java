package backend;

import backend.signal_operation.SignalAdd;
import backend.signal_operation.SignalDivide;
import backend.signal_operation.SignalMultiply;
import backend.signal_operation.SignalSubtract;

public class SignalOperationFactory {
    public SignalAdd createSignalAdd() {
        return new SignalAdd();
    }
    public SignalSubtract createSignalSubtract() {
        return new SignalSubtract();
    }
    public SignalMultiply createSignalMultiply() {
        return new SignalMultiply();
    }
    public SignalDivide createSignalDivide() {
        return new SignalDivide();
    }
}

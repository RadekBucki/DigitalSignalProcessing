package backend.signal_operation;

import backend.SignalFactory;

public class SignalMultiply extends AbstractSignalOperation {
    public SignalMultiply(SignalFactory signalFactory) {
        super(signalFactory);
    }

    @Override
    protected Double operation(double signal1Amplitude, double signal2Amplitude) {
        return signal1Amplitude * signal2Amplitude;
    }
}

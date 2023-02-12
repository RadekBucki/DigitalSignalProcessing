package backend.signal_operation;

import backend.SignalFactory;

public class SignalDivide extends AbstractSignalOperation {
    public SignalDivide(SignalFactory signalFactory) {
        super(signalFactory);
    }
    @Override
    protected Double operation(double signal1Amplitude, double signal2Amplitude) {
        if (signal1Amplitude - signal2Amplitude < 0.0001) {
            return 1.0;
        }
        if (signal2Amplitude == 0) {
            return 0.0;
        }
        return signal1Amplitude / signal2Amplitude;
    }
}

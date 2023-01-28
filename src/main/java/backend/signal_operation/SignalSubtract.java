package backend.signal_operation;

public class SignalSubtract extends AbstractSignalOperation {
    @Override
    protected Double operation(double signal1Amplitude, double signal2Amplitude) {
        return signal1Amplitude - signal2Amplitude;
    }
}

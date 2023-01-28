package backend;

import backend.signal.AbstractSignal;

public class SignalFacade {
    private final SignalOperationFactory factory = new SignalOperationFactory();

    public AbstractSignal add(AbstractSignal signal1, AbstractSignal signal2) {
        return factory.createSignalAdd().execute(signal1, signal2);
    }

    public AbstractSignal subtract(AbstractSignal signal1, AbstractSignal signal2) {
        return factory.createSignalSubtract().execute(signal1, signal2);
    }

    public AbstractSignal multiply(AbstractSignal signal1, AbstractSignal signal2) {
        return factory.createSignalMultiply().execute(signal1, signal2);
    }

    public AbstractSignal divide(AbstractSignal signal1, AbstractSignal signal2) {
        return factory.createSignalDivide().execute(signal1, signal2);
    }
}

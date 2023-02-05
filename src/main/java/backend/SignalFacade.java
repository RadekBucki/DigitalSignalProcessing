package backend;

import backend.signal.AbstractSignal;
import backend.signal.serialize.SignalSerializer;

import java.util.List;

public class SignalFacade {
    private final SignalOperationFactory signalOperationFactory = new SignalOperationFactory();
    private final SignalFactory signalFactory = new SignalFactory();

    public AbstractSignal add(AbstractSignal signal1, AbstractSignal signal2) {
        return signalOperationFactory.createSignalAdd(signalFactory).execute(signal1, signal2);
    }

    public AbstractSignal subtract(AbstractSignal signal1, AbstractSignal signal2) {
        return signalOperationFactory.createSignalSubtract(signalFactory).execute(signal1, signal2);
    }

    public AbstractSignal multiply(AbstractSignal signal1, AbstractSignal signal2) {
        return signalOperationFactory.createSignalMultiply(signalFactory).execute(signal1, signal2);
    }

    public AbstractSignal divide(AbstractSignal signal1, AbstractSignal signal2) {
        return signalOperationFactory.createSignalDivide(signalFactory).execute(signal1, signal2);
    }
    public AbstractSignal getSignal(Class<?> name, List<Double> parameters) {
        return signalFactory.getSignal(name, parameters);
    }

    public Class<AbstractSignal> getDefaultSignal() {
        return signalFactory.getDefaultSignal();
    }
    public List<Class<? extends AbstractSignal>> getPossibleSignals() {
        return signalFactory.getPossibleSignals();
    }
    public AbstractSignal readSignal(String filePath) {
        return SignalSerializer.read(filePath);
    }
    public void writeSignal(AbstractSignal signal, String filePath) {
        SignalSerializer.write(signal, filePath);
    }
}

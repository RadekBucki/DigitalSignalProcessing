package backend;

import backend.signal_read_write.SignalReadWriteFactory;
import backend.signal_read_write.SignalReadWriteType;
import backend.signal.AbstractSignal;

import java.util.List;

public class SignalFacade {
    private final SignalOperationFactory signalOperationFactory = new SignalOperationFactory();
    private final SignalFactory signalFactory = new SignalFactory();
    private final SignalReadWriteFactory signalReadWriteFactory = new SignalReadWriteFactory();

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
    public AbstractSignal readSignal(SignalReadWriteType type, String filePath) {
        return signalReadWriteFactory.getSignalReadWrite(type).read(filePath);
    }
    public void writeSignal(SignalReadWriteType type, AbstractSignal signal, String filePath) {
        signalReadWriteFactory.getSignalReadWrite(type).write(signal, filePath);
    }
}

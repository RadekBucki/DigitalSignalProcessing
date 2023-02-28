package backend;

import backend.signal_serialize.SignalSerializeFactory;
import backend.signal_serialize.SignalSerializeType;
import backend.signal.AbstractSignal;

import java.util.List;

public class SignalFacade {
    private final SignalOperationFactory signalOperationFactory = new SignalOperationFactory();
    private final SignalFactory signalFactory = new SignalFactory();
    private final SignalSerializeFactory signalReadWriteFactory = new SignalSerializeFactory();

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
    public AbstractSignal readSignal(SignalSerializeType type, String filePath) {
        return signalReadWriteFactory.createSignalSerializer(type).read(filePath);
    }
    public void writeSignal(SignalSerializeType type, AbstractSignal signal, String filePath) {
        signalReadWriteFactory.createSignalSerializer(type).write(signal, filePath);
    }
}

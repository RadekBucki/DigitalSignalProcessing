package backend.signal_read_write;

public class SignalReadWriteFactory {

    public SignalReadWrite getSignalReadWrite(SignalReadWriteType type) {
        return switch (type) {
            case JSON -> new SignalJsonizer();
            case BINARY -> new SignalSerializer();
        };
    }
}

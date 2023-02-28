package backend.signal_serialize;

public class SignalSerializeFactory {

    public SignalSerialize createSignalSerializer(SignalSerializeType type) {
        return switch (type) {
            case JSON -> createJsonSerializer();
            case BINARY -> createBinarySerializer();
        };
    }
    public SignalSerialize createJsonSerializer() {
        return new SignalJsonSerializer();
    }
    public SignalSerialize createBinarySerializer() {
        return new SignalBinarySerializer();
    }
}

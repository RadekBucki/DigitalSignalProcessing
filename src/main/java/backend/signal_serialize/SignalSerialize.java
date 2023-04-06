package backend.signal_serialize;

import backend.signal.AbstractSignal;

public interface SignalSerialize {
    void write(AbstractSignal signal, String filePath);
    AbstractSignal read(String filePath);
}

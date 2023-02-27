package backend.signal_read_write;

import backend.signal.AbstractSignal;

public interface SignalReadWrite {
    void write(AbstractSignal signal, String filePath);
    AbstractSignal read(String filePath);
}

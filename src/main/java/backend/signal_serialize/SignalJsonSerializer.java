package backend.signal_serialize;

import backend.signal.AbstractSignal;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class SignalJsonSerializer implements SignalSerialize {

    public AbstractSignal read(String filePath) {
        File file = new File(filePath);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(file, AbstractSignal.class);
        } catch (IOException ignored) {
            // ignored
        }
        return null;
    }

    public void write(AbstractSignal signal, String filePath) {
        File file = new File(filePath);
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(file, signal);
        } catch (IOException ignored) {
            // ignored
        }
    }
}

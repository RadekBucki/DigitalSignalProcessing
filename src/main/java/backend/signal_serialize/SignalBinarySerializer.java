package backend.signal_serialize;

import backend.signal.AbstractSignal;

import java.io.*;

public class SignalBinarySerializer implements SignalSerialize {

    public void write(AbstractSignal signal, String filePath) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))){
            out.writeObject(signal);
        } catch (IOException ignored) {
            // ignored
        }
    }

    public AbstractSignal read(String filePath) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))){
            return (AbstractSignal) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}

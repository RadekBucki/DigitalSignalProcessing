package backend.signal.serialize;

import backend.signal.AbstractSignal;

import java.io.*;

public class SignalSerializer {
    private SignalSerializer() {
    }

    public static void write(AbstractSignal signal, String filePath) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))){
            out.writeObject(signal);
        } catch (IOException ignored) {
            // ignored
        }
    }

    public static AbstractSignal read(String filePath) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))){
            return (AbstractSignal) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}

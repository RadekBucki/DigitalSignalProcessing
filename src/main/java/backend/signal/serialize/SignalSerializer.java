package backend.signal.serialize;

import backend.signal.AbstractSignal;

import java.io.*;

public class SignalSerializer {
    private SignalSerializer() {
    }

    public static void write(AbstractSignal signal, String filePath) {
        try {
            FileOutputStream file = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(signal);

            out.close();
            file.close();
        } catch (IOException ignored) {
            // ignored
        }
    }

    public static AbstractSignal read(String filePath) {
        try {
            FileInputStream file = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(file);

            AbstractSignal signal = (AbstractSignal) in.readObject();

            in.close();
            file.close();

            return signal;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}

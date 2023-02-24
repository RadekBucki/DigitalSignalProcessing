package backend.signal_operation;

import backend.signal_operation.pass.BandPass;
import backend.signal_operation.pass.HighPass;
import backend.signal_operation.pass.LowPass;
import backend.signal_operation.pass.Pass;
import backend.signal_operation.window.Window;

public class PassFactory {
    public Pass createLowPass(int M, double f0, double f, Window window) {
        return new LowPass(M, f0, f, window);
    }

    public Pass createBandPass(int M, double f0, double f, Window window) {
        return new BandPass(M, f0, f, window);
    }

    public Pass createHighPass(int M, double f0, double f, Window window) {
        return new HighPass(M, f0, f, window);
    }
}

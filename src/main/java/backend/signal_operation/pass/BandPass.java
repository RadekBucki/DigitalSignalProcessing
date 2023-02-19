package backend.signal_operation.pass;

import backend.signal_operation.window.Window;

public class BandPass extends LowPass {

    public BandPass(int M, double f0, double f, Window window) {
        super(M, f0, f, window);
    }

    @Override
    public double pass(int n) {
        return super.pass(n) * 2 * Math.sin(Math.PI * n / 2);
    }
}

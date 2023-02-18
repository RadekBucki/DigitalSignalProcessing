package backend.signal_operation.pass;

import backend.signal_operation.window.Window;

public class HighPass extends LowPass {

    public HighPass(int M, double f0, double f, Window window) {
        super(M, f/2 - f0, f, window);
    }

    @Override
    public double pass(int n) {
        return super.pass(n) * ((n % 2 == 0) ? 1 : -1);
    }
}

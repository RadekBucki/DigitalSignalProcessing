package backend.signal_operation.pass;

import backend.signal_operation.window.Window;

public class HighPass implements Pass {
    private final int M;
    private final double K;
    private final Window window;

    public HighPass(int M, double f0, double f, Window window) {
        this.M = M;
        this.K = f / f0;
        this.window = window;
    }

    @Override
    public double pass(int n) {
        return 0; //TODO: implementation
    }
}

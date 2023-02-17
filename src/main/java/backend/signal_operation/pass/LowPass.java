package backend.signal_operation.pass;

import backend.signal_operation.window.Window;

public class LowPass implements Pass {
    private final int M;
    private final double K;
    private final Window window;

    public LowPass(int M, double f0, double f, Window window) {
        this.M = M;
        this.K = f / f0;
        this.window = window;
    }

    @Override
    public double pass(int n) {
        int center = (M - 1) / 2;
        return Math.sin(2 * Math.PI * (n - center) / K) / (Math.PI * (n - center)) * window.window(n);
    }
}

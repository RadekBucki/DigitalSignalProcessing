package backend.signal_operation.window;

public class HanningWindow implements Window {
    private final int M;

    public HanningWindow(int m) {
        M = m;
    }

    @Override
    public double window(int n) {
        return 0.5 - 0.5 * Math.cos(2 * Math.PI * n / M);
    }
}

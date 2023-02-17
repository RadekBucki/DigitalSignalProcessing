package backend.signal_operation.window;

public class BlackmanWindow implements Window{
    private final int M;

    public BlackmanWindow(int m) {
        M = m;
    }

    @Override
    public double window(int n) {
        return 0.42 - 0.5 * Math.cos(2 * Math.PI * n / M);
    }
}

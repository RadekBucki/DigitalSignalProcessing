package backend.signal_operation.window;

public class HammingWindow implements Window{
    @Override
    public double window(int n, int M) {
        return 0.53836 - 0.46164 * Math.cos(2 * Math.PI * n / M);
    }
}

package backend.signal_operation.window;

public class HanningWindow implements Window{
    @Override
    public double window(int n, int M) {
        return 0.5 - 0.5 * Math.cos(2 * Math.PI * n / M);
    }
}

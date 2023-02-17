package backend.signal_operation.window;

public class BlackmanWindow implements Window{
    @Override
    public double window(int n, int M) {
        return 0.42 - 0.5 * Math.cos(2 * Math.PI * n / M);
    }
}

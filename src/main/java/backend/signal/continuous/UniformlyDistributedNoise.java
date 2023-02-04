package backend.signal.continuous;

public class UniformlyDistributedNoise extends Noise {
    public UniformlyDistributedNoise(double A, double d, double t1) {
        super(A, d, t1);
        calculateAllPoints();
    }

    @Override
    public double calculatePointValue(double x) {
        return Math.random() * 2 * A - A;
    }
}

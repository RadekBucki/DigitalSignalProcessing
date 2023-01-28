package backend.Signal;

public class ImpulseNoise extends DiscreteSignal {
    private double t1;
    private double d;
    private double p;

    public ImpulseNoise(double a, double t, double t1, double d, double p) {
        super(a, t);
        this.t1 = t1;
        this.d = d;
        this.p = p;
    }
}

package backend.signal;

public class UnitImpulse extends DiscreteSignal {
    private double ns;
    private double n1;
    private double l;

    public UnitImpulse(double a, double t, double ns, double n1, double l) {
        super(a, t);
        this.ns = ns;
        this.n1 = n1;
        this.l = l;
    }
}

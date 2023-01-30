package backend.signal.discrete;

import backend.signal.DiscreteSignal;

public class UnitImpulse extends DiscreteSignal {
    private final int ns;
    private final int n1;

    public UnitImpulse(double A, double d, double f, int ns, int n1) {
        super(A, d, f);
        this.ns = ns;
        this.n1 = n1;
        for (int i = n1; i < n1 + Math.floor(d * f); i++) {
            double time = i / f;
            if (i == ns) {
                points.put(time, A);
            } else {
                points.put(time, 0.0);
            }
        }
    }
}

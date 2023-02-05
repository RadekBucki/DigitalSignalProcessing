package backend.signal.discrete;

import backend.signal.DiscreteSignal;

public class UnitImpulse extends DiscreteSignal {
    private final int ns;

    public UnitImpulse(double A, double d, double f, int n1, int ns) {
        super(A, d, f);
        this.ns = ns;
        super.n1 = n1;
        super.n2 = (int) (n1 + Math.floor(d * f));
        for (int i = n1; i <= n2; i++) {
            double time = i / f;
            if (i == ns) {
                points.put(time, A);
            } else {
                points.put(time, 0.0);
            }
        }
    }
}

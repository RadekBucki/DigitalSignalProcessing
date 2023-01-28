package backend.signal.discrete;

import backend.signal.DiscreteSignal;

public class UnitImpulse extends DiscreteSignal {
    private int ns;
    private int n1;

    public UnitImpulse(double A, double f, int ns, int n1, double d) {
        super(A, d, f);
        this.ns = ns;
        this.n1 = n1;
        for (int i = n1; i < ns; i++) {
            double time = i / f;
            if (i == 0) {
                points.put(time, 1.0);
            } else {
                points.put(time, 0.0);
            }
        }
    }
}

package backend.signal.discrete;

import backend.signal.DiscreteSignal;

public class UnitImpulse extends DiscreteSignal {
    private int ns;
    private int n1;

    public UnitImpulse(double a, double t, int ns, int n1, double d) {
        super(a, t, d);
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

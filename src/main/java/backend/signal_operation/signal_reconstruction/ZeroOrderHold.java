package backend.signal_operation.signal_reconstruction;

import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;

public class ZeroOrderHold implements ReconstructMethod {
    @Override
    public ContinuousSignal reconstruct(DiscreteSignal discreteSignal) {
        return null;
    }
}

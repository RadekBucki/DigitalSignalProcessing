package backend.signal_operation.signal_reconstruction;

import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;

public interface ReconstructMethod {
    public ContinuousSignal reconstruct(DiscreteSignal discreteSignal);
}

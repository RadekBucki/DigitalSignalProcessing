package backend.signal_operation.signal_reconstruction;

import backend.signal.DiscreteSignal;

public interface ReconstructMethod {
    double reconstruct(DiscreteSignal signal, double time, double frequency);
}

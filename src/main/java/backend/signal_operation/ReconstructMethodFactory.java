package backend.signal_operation;

import backend.signal_operation.signal_reconstruction.FirstOrderHold;
import backend.signal_operation.signal_reconstruction.ReconstructMethod;
import backend.signal_operation.signal_reconstruction.Sinc;
import backend.signal_operation.signal_reconstruction.ZeroOrderHold;

public class ReconstructMethodFactory {
    public ReconstructMethod createZeroOrderHold() {
        return new ZeroOrderHold();
    }
    public ReconstructMethod createFirstOrderHold() {
        return new FirstOrderHold();
    }
    public ReconstructMethod createSinc(int numOfSamples) {
        return new Sinc(numOfSamples);
    }
}

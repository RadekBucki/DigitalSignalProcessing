package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.DiscreteSignal;

public class DiscreteFourierTransformWithDecimationInTimeDomain {
    private final SignalFactory signalFactory;

    public DiscreteFourierTransformWithDecimationInTimeDomain(SignalFactory signalFactory) {
        this.signalFactory = signalFactory;
    }

    public DiscreteSignal execute(DiscreteSignal signal, TransformType type) {
        if (type == TransformType.DIRECT) {
            return executeDirect(signal);
        } else {
            return executeFast(signal);
        }
    }

    private DiscreteSignal executeFast(DiscreteSignal signal) {
        return null;
    }

    private DiscreteSignal executeDirect(DiscreteSignal signal) {
        return null;
    }
}

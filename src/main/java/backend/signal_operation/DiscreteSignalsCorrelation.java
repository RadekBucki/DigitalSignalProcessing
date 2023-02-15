package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.DiscreteSignal;

public class DiscreteSignalsCorrelation {
    SignalFactory signalFactory;
    public DiscreteSignalsCorrelation(SignalFactory signalFactory) {
        this.signalFactory = signalFactory;
    }

    public DiscreteSignal execute(DiscreteSignal discreteSignal1, DiscreteSignal discreteSignal, DiscreteSignalsCorrelationType discreteSignalsCorrelationType) {
        if (discreteSignalsCorrelationType == DiscreteSignalsCorrelationType.DIRECT) {
            return executeDirect(discreteSignal1, discreteSignal);
        } else {
            return executeUsingWeave(discreteSignal1, discreteSignal);
        }
    }

    private DiscreteSignal executeUsingWeave(DiscreteSignal discreteSignal1, DiscreteSignal discreteSignal) {
        return null;
    }

    private DiscreteSignal executeDirect(DiscreteSignal discreteSignal1, DiscreteSignal discreteSignal) {
        return null;
    }
}

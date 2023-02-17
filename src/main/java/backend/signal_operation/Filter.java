package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.DiscreteSignal;
import backend.signal_operation.pass.Pass;

public class Filter {

    private final SignalFactory signalFactory;

    public Filter(SignalFactory signalFactory) {
        this.signalFactory = signalFactory;
    }

    public DiscreteSignal execute(DiscreteSignal signal, Pass pass) {
        return (DiscreteSignal) signalFactory.createDiscreteSignal(null); //TODO: Iterate over signal and use pass
    }
}

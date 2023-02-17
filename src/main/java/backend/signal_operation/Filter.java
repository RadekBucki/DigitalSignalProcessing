package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.DiscreteSignal;
import backend.signal_operation.pass.Pass;

import java.util.Map;
import java.util.stream.Collectors;

public class Filter {

    private final SignalFactory signalFactory;

    public Filter(SignalFactory signalFactory) {
        this.signalFactory = signalFactory;
    }

    public DiscreteSignal execute(DiscreteSignal signal, Pass pass) {
        return (DiscreteSignal) signalFactory.createDiscreteSignal(signal.getPoints()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> pass.pass((int) (e.getKey() * signal.getF())))));
    }
}

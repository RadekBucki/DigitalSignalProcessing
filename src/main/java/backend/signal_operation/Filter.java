package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.DiscreteSignal;
import backend.signal_operation.pass.Pass;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Filter {
    private final SignalFactory signalFactory;
    private final Convolution convolution;
    private final Pass pass;

    public Filter(Pass pass, SignalFactory signalFactory, Convolution convolution) {
        this.signalFactory = signalFactory;
        this.convolution = convolution;
        this.pass = pass;
    }

    public DiscreteSignal execute(DiscreteSignal signal) {
        DiscreteSignal filter = (DiscreteSignal) signalFactory.createDiscreteSignal(signal.getPoints()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> pass.pass((int) (e.getKey() * signal.getF())),
                        (u, v) -> {
                            throw new IllegalStateException(String.format("Duplicate key %s", u));
                        },
                        TreeMap::new)));
        return convolution.execute(signal, filter);
    }
}

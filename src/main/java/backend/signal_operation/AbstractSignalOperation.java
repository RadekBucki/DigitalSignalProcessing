package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.AbstractSignal;
import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import static backend.Rounder.DECIMAL_PLACES_DIVISION;


public abstract class AbstractSignalOperation {
    private final SignalFactory signalFactory;

    protected AbstractSignalOperation(SignalFactory signalFactory) {
        this.signalFactory = signalFactory;
    }

    public AbstractSignal execute(AbstractSignal signal1, AbstractSignal signal2) {
        LinkedHashMap<Double, Double> resultPoints = new LinkedHashMap<>();
        Iterator<Map.Entry<Double, Double>> signal1Iterator = signal1.getPoints().entrySet().iterator();
        Map<Double, Double> signal2Points = signal2.getPoints();
        while (signal1Iterator.hasNext()) {
            Map.Entry<Double, Double> signal1Entry = signal1Iterator.next();
            Double signal1EntryKey = signal1Entry.getKey();
            if (signal2Points.containsKey(signal1EntryKey)) {
                resultPoints.put(
                        signal1EntryKey,
                        operation(signal1Entry.getValue(), signal2Points.get(signal1EntryKey))
                );
                signal2Points.remove(signal1EntryKey);
            } else {
                resultPoints.put(
                        signal1Entry.getKey(),
                        signal1Entry.getValue()
                );
            }
        }
        resultPoints.putAll(signal2Points);

        if (signal1 instanceof DiscreteSignal && signal2 instanceof DiscreteSignal) {
            return signalFactory.createDiscreteSignal(resultPoints);
        }

        int t1Rounded = (int) (Collections.min(resultPoints.keySet()) * DECIMAL_PLACES_DIVISION);
        int t2Rounded = (int) (Collections.max(resultPoints.keySet()) * DECIMAL_PLACES_DIVISION);
        for (int i = t1Rounded; i <= t2Rounded; i++) {
            resultPoints.putIfAbsent(i / DECIMAL_PLACES_DIVISION, 0.0);
        }

        ContinuousSignal signal = (ContinuousSignal) signalFactory.createContinuousSignal(resultPoints);
        signal.setFunction(operation(signal1::calculatePointValue, signal2::calculatePointValue));
        return signal;
    }
    protected abstract Double operation(double signal1Amplitude, double signal2Amplitude);
    protected abstract Function<Double, Double> operation(Function<Double, Double> f1, Function<Double, Double> f2);
}

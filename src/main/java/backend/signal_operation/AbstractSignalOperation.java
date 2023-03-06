package backend.signal_operation;

import backend.Rounder;
import backend.SignalFactory;
import backend.signal.AbstractSignal;
import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;


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

        DoubleUnaryOperator signal1Function = signal1::calculatePointValue;
        DoubleUnaryOperator signal2Function = signal2::calculatePointValue;
        if (signal1 instanceof ContinuousSignal continuousSignal) {
            signal1Function = continuousSignal.getFunction();
        }
        if (signal2 instanceof ContinuousSignal continuousSignal) {
            signal2Function = continuousSignal.getFunction();
        }

        ContinuousSignal signal = (ContinuousSignal) signalFactory.createContinuousSignal(resultPoints);
        signal.setFunction(operation(signal1Function, signal2Function));
        return signal;
    }
    protected abstract Double operation(double signal1Amplitude, double signal2Amplitude);
    protected abstract DoubleUnaryOperator operation(DoubleUnaryOperator f1, DoubleUnaryOperator f2);
}

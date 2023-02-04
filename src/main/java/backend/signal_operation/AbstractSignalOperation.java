package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.AbstractSignal;
import backend.signal.DiscreteSignal;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractSignalOperation {
    private final SignalFactory signalFactory;

    protected AbstractSignalOperation(SignalFactory signalFactory) {
        this.signalFactory = signalFactory;
    }

    public AbstractSignal execute(AbstractSignal signal1, AbstractSignal signal2) {
        LinkedHashMap<Double, Double> resultPoints = new LinkedHashMap<>();
        Iterator<Map.Entry<Double, Double>> signal1Iterator = signal1.getAmplitudeFromTimeChartData().entrySet().iterator();
        Map<Double, Double> signal2Points = signal2.getAmplitudeFromTimeChartData();
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
        return signalFactory.createContinuousSignal(resultPoints);
    }
    protected abstract Double operation(double signal1Amplitude, double signal2Amplitude);
}

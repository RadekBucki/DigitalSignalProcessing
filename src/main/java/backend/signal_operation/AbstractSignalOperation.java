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
        Iterator<Map.Entry<Double, Double>> signal2Iterator = signal2.getAmplitudeFromTimeChartData().entrySet().iterator();
        while (signal1Iterator.hasNext()) {
            Map.Entry<Double, Double> signal1Entry = signal1Iterator.next();
            Map.Entry<Double, Double> signal2Entry = signal2Iterator.next();
            if (signal1Entry.getKey().equals(signal2Entry.getKey())) {
                resultPoints.put(
                        signal1Entry.getKey(),
                        operation(signal1Entry.getValue(), signal2Entry.getValue())
                );
            } else {
                resultPoints.put(
                        signal1Entry.getKey(),
                        signal1Entry.getValue()
                );
                resultPoints.put(
                        signal2Entry.getKey(),
                        signal2Entry.getValue()
                );
            }
        }

        if (signal1 instanceof DiscreteSignal && signal2 instanceof DiscreteSignal) {
            return signalFactory.createDiscreteSignal(resultPoints);
        }
        return signalFactory.createContinuousSignal(resultPoints);
    }
    protected abstract Double operation(double signal1Amplitude, double signal2Amplitude);
}

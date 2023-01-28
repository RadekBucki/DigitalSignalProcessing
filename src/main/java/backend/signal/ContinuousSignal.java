package backend.signal;

import java.util.HashMap;

public class ContinuousSignal extends AbstractSignal {
    public HashMap<Double, Double> getAmplitudeFromTimeChartData() {
        return new HashMap<>();
    }

    public ContinuousSignal(HashMap<Double, Double> points) {
    }
}

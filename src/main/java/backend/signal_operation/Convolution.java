package backend.signal_operation;

import backend.signal.DiscreteSignal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Convolution {
    public DiscreteSignal execute(DiscreteSignal signal1, DiscreteSignal signal2) {
        Map<Double, Double> points = new LinkedHashMap<>();
        ArrayList<Double> signal1Points = new ArrayList<>(signal1.getPoints().values());
        ArrayList<Double> signal2Points = new ArrayList<>(signal2.getPoints().values());
        for (int n = 0; n < signal1.getPoints().size() + signal2.getPoints().size() - 1; n++) {
            double value = 0;
            for (int k = 0; k < signal1.getPoints().size(); k++) {
                if ((n - k) >= 0 && (n - k) < signal2.getPoints().size()) {
                    value = value + signal1Points.get(k) * signal2Points.get(n - k);
                }
            }
            points.put(n / signal1.getF(), value);
        }
        return new DiscreteSignal(points);
    }
}

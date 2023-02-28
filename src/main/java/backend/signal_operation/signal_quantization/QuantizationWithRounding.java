package backend.signal_operation.signal_quantization;

import java.util.Comparator;
import java.util.List;

public class QuantizationWithRounding implements QuantizationMethod{
    @Override
    public double quantize(List<Double> levels, double sample) {
        return levels.stream().min(Comparator.comparingDouble(lev -> Math.abs(lev - sample))).orElseThrow();
    }
}

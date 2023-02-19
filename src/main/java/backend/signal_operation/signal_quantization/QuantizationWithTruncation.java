package backend.signal_operation.signal_quantization;

import java.util.List;

public class QuantizationWithTruncation implements QuantizationMethod {
    @Override
    public double quantize(List<Double> levels, double sample) {
        return levels.stream().filter(level -> level <= sample).mapToDouble(Double::doubleValue).max().orElseThrow();
    }
}

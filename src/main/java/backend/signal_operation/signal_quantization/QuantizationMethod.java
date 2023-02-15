package backend.signal_operation.signal_quantization;

import java.util.List;

public interface QuantizationMethod {
    double quantize(List<Double> levels, double sample);
}

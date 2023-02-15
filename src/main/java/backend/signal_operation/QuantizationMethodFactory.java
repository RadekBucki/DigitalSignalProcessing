package backend.signal_operation;

import backend.signal_operation.signal_quantization.QuantizationMethod;
import backend.signal_operation.signal_quantization.QuantizationWithRounding;
import backend.signal_operation.signal_quantization.QuantizationWithTruncation;

public class QuantizationMethodFactory {
    public QuantizationMethod createQuantizationWithTruncation() {
        return new QuantizationWithTruncation();
    }

    public QuantizationMethod createQuantizationWithRounding() {
        return new QuantizationWithRounding();
    }
}

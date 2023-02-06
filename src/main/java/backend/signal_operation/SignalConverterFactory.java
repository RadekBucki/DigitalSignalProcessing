package backend.signal_operation;

public class SignalConverterFactory {
    public Adc createAdc() {
        return new Adc();
    }

    public Adc createDac() {
        return null;
    }
}

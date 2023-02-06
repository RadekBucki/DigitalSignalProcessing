package backend.signal_operation;

import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;

import java.util.List;

public class Dac {
    ReconstructMethodFactory reconstructMethodFactory;

    public Dac(ReconstructMethodFactory reconstructMethodFactory) {
        this.reconstructMethodFactory = reconstructMethodFactory;
    }

    public ContinuousSignal reconstruct(DiscreteSignal discreteSignal) {
        return null;
    }
    public List<Double> calculateStats(ContinuousSignal continuousSignal1, ContinuousSignal continuousSignal2) {
        return null;
    }
    public double calculateMse(ContinuousSignal continuousSignal1, ContinuousSignal continuousSignal2) {
        return 0.0;
    }
    public double calculateSnr(ContinuousSignal continuousSignal1, ContinuousSignal continuousSignal2) {
        return 0.0;
    }
    public double calculatePsnr(ContinuousSignal continuousSignal1, ContinuousSignal continuousSignal2) {
        return 0.0;
    }
    public double calculateMd(ContinuousSignal continuousSignal1, ContinuousSignal continuousSignal2) {
        return 0.0;
    }
}

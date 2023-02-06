package backend.signal_operation;

import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;
import backend.signal_operation.signal_reconstruction.ReconstructMethod;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Dac {
    ReconstructMethodFactory reconstructMethodFactory;

    public Dac(ReconstructMethodFactory reconstructMethodFactory) {
        this.reconstructMethodFactory = reconstructMethodFactory;
    }

    public ContinuousSignal reconstructZeroOrderHold(DiscreteSignal discreteSignal) {
        return reconstruct(discreteSignal, reconstructMethodFactory.createZeroOrderHold());
    }

    public ContinuousSignal reconstructFirstOrderHold(DiscreteSignal discreteSignal) {
        return reconstruct(discreteSignal, reconstructMethodFactory.createFirstOrderHold());
    }

    public ContinuousSignal reconstructSinc(DiscreteSignal discreteSignal) {
        return reconstruct(discreteSignal, reconstructMethodFactory.createSinc());
    }

    private ContinuousSignal reconstruct(DiscreteSignal discreteSignal, ReconstructMethod reconstructMethod) {
        return new ContinuousSignal(
                IntStream.range(discreteSignal.getN1(), discreteSignal.getN2())
                        .mapToDouble(i -> discreteSignal.getD() * i)
                        .boxed()
                        .collect(Collectors.toMap(
                                t -> t,
                                t -> reconstructMethod.reconstruct(discreteSignal, t)
                        ))
        );
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

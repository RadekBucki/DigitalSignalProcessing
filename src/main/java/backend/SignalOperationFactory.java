package backend;

import backend.radar.Radar;
import backend.radar.RadarDependenciesFactory;
import backend.signal.ContinuousSignal;
import backend.signal_operation.*;
import backend.signal_operation.pass.Pass;
import backend.signal_operation.window.Window;

public class SignalOperationFactory {
    RadarDependenciesFactory radarDependenciesFactory = new RadarDependenciesFactory();
    private final QuantizationMethodFactory quantizationMethodFactory = new QuantizationMethodFactory();
    private final SignalFactory signalFactory;
    private final ReconstructMethodFactory reconstructMethodFactory = new ReconstructMethodFactory();
    private final WindowFactory windowFactory = new WindowFactory();
    private final PassFactory passFactory = new PassFactory();

    public SignalOperationFactory(SignalFactory signalFactory) {
        this.signalFactory = signalFactory;
    }

    public SignalAdd createSignalAdd(SignalFactory signalFactory) {
        return new SignalAdd(signalFactory);
    }
    public SignalSubtract createSignalSubtract(SignalFactory signalFactory) {
        return new SignalSubtract(signalFactory);
    }
    public SignalMultiply createSignalMultiply(SignalFactory signalFactory) {
        return new SignalMultiply(signalFactory);
    }
    public SignalDivide createSignalDivide(SignalFactory signalFactory) {
        return new SignalDivide(signalFactory);
    }
    public Adc createAdc() {
        return new Adc(quantizationMethodFactory, signalFactory);
    }
    public Dac createDac() {
        return new Dac(reconstructMethodFactory, signalFactory);
    }
    public Convolution createConvolution() {
        return new Convolution(signalFactory);
    }

    public Filter createFilter(PassType passType, WindowType windowType, int M, double f0, double f) {
        Window window = switch (windowType) {
            case RECTANGULAR -> windowFactory.createRectangularWindow();
            case BLACKMAN -> windowFactory.createBlackmanWindow(M);
            case HAMMING -> windowFactory.createHammingWindow(M);
            case HANNING -> windowFactory.createHanningWindow(M);
        };
        Pass pass = switch (passType) {
            case LOW_PASS -> passFactory.createLowPass(M, f0, f, window);
            case BAND_PASS -> passFactory.createBandPass(M, f0, f, window);
            case HIGH_PASS -> passFactory.createHighPass(M, f0, f, window);
        };
        return new Filter(pass, signalFactory, createConvolution());
    }

    public DiscreteSignalsCorrelation createDiscreteSignalsCorrelation() {
        return new DiscreteSignalsCorrelation(signalFactory, createConvolution());
    }

    public Radar createRadar(double probingSignalF, int discreteBufferSize, double signalSpeed,
                             double workTime, double stepTime, ContinuousSignal continuousSignal,
                             double radarX, double radarY, double objectX, double objectY,
                             double objectSpeedX, double objectSpeedY, SignalFacade facade) {
        return new Radar(
                radarDependenciesFactory.createRadarConfig(continuousSignal, probingSignalF, discreteBufferSize,
                        workTime, radarX, radarY),
                radarDependenciesFactory.createEnvironment(signalSpeed, stepTime),
                radarDependenciesFactory.createMeasuredObject(objectX, objectY, objectSpeedX, objectSpeedY),
                facade,
                signalFactory
        );
    }

    public DiscreteFalcoTransform createDiscreteFalcoTransform() {
        return new DiscreteFalcoTransform(signalFactory, createConvolution());
    }
}

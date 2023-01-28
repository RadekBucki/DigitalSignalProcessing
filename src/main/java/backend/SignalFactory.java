package backend;

import backend.signal.AbstractSignal;
import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;
import backend.signal.continuous.*;

import java.util.Map;

public class SignalFactory {
    public AbstractSignal createUniformlyDistributedNoise(double A, double t1, double d) {
        return new UniformlyDistributedNoise(A, t1, d);
    }
    public AbstractSignal createGaussianNoise(double A, double t1, double d) {
        return new GaussianNoise(A, t1, d);
    }
    public AbstractSignal createSinusoidalSignal(double A, double t1, double d, double T) {
        return new SinusoidalSignal(A, t1, d, T);
    }
    public AbstractSignal createOneHalfRectifiedSinusoidalSignal(double A, double t1, double d, double T) {
        return new OneHaltRectifiedSinusoidalSignal(A, t1, d, T);
    }
    public AbstractSignal createTwoHalfRectifiedSinusoidalSignal(double A, double t1, double d, double T) {
        return new TwoHalfRectifiedSinusoidalSignal(A, t1, d, T);
    }
    public AbstractSignal createRectangularSignal(double A, double t1, double d, double T, double kw) {
        return new RectangularSignal(A, t1, d, T, kw);
    }
    public AbstractSignal createSymmetricalRectangularSignal(double A, double t1, double d, double T, double kw) {
        return new SymmetricalRectangularSignal(A, t1, d, T, kw);
    }
    public AbstractSignal createTriangleSignal(double A, double t1, double d, double T, double kw) {
        return new TriangleSignal(A, t1, d, T, kw);
    }
    public AbstractSignal createUnitJump(double A, double t1, double d, double ts) {
        return new UnitJump(A, t1, d, ts);
    }
    public AbstractSignal createContinuousSignal(Map<Double, Double> points) {
        return new ContinuousSignal(points);
    }
    public AbstractSignal createDiscreteSignal(Map<Double, Double> points) {
        return new DiscreteSignal(points);
    }
}

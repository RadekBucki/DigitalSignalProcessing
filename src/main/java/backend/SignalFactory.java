package backend;

import backend.signal.AbstractSignal;
import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;
import backend.signal.continuous.*;
import backend.signal.discrete.ImpulseNoise;
import backend.signal.discrete.UnitImpulse;

import java.util.List;
import java.util.Map;

public class SignalFactory {
    private static final Class<AbstractSignal> DEFAULT_SIGNAL = AbstractSignal.class;
    private static final List<Class<? extends AbstractSignal>> POSSIBLE_SIGNALS = List.of(
            GaussianNoise.class,
            OneHaltRectifiedSinusoidalSignal.class,
            RectangularSignal.class,
            SinusoidalSignal.class,
            SymmetricalRectangularSignal.class,
            TriangleSignal.class,
            TwoHalfRectifiedSinusoidalSignal.class,
            UniformlyDistributedNoise.class,
            UnitJump.class,
            ImpulseNoise.class,
            UnitImpulse.class
    );
    public Class<AbstractSignal> getDefaultSignal() {
        return DEFAULT_SIGNAL;
    }
    public List<Class<? extends AbstractSignal>> getPossibleSignals() {
        return POSSIBLE_SIGNALS;
    }
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

    public AbstractSignal createImpulseNoise(double A, double f, double t1, double d, double p) {
        return new ImpulseNoise(A, f, t1, d, p);
    }

    public AbstractSignal createUnitImpulse(double A, double f, int ns, int n1, double d) {
        return new UnitImpulse(A, f, ns, n1, d);
    }

    public AbstractSignal createContinuousSignal(Map<Double, Double> points) {
        return new ContinuousSignal(points);
    }

    public AbstractSignal createDiscreteSignal(Map<Double, Double> points) {
        return new DiscreteSignal(points);
    }
    public AbstractSignal getSignal(Class<?> name, List<Double> parameters) {
        if (name == GaussianNoise.class) {
            return createGaussianNoise(
                    parameters.get(0),
                    parameters.get(1),
                    parameters.get(2)
            );
        } else if (name == OneHaltRectifiedSinusoidalSignal.class) {
            return createOneHalfRectifiedSinusoidalSignal(
                    parameters.get(0),
                    parameters.get(1),
                    parameters.get(2),
                    parameters.get(3)
            );
        } else if (name == RectangularSignal.class) {
            return createRectangularSignal(
                    parameters.get(0),
                    parameters.get(1),
                    parameters.get(2),
                    parameters.get(3),
                    parameters.get(4)
            );
        } else if (name == SinusoidalSignal.class) {
            return createSinusoidalSignal(
                    parameters.get(0),
                    parameters.get(1),
                    parameters.get(2),
                    parameters.get(3)
            );
        } else if (name == SymmetricalRectangularSignal.class) {
            return createSymmetricalRectangularSignal(
                    parameters.get(0),
                    parameters.get(1),
                    parameters.get(2),
                    parameters.get(3),
                    parameters.get(4)
            );
        } else if (name == TriangleSignal.class) {
            return createTriangleSignal(
                    parameters.get(0),
                    parameters.get(1),
                    parameters.get(2),
                    parameters.get(3),
                    parameters.get(4)
            );
        } else if (name == TwoHalfRectifiedSinusoidalSignal.class) {
            return createTwoHalfRectifiedSinusoidalSignal(
                    parameters.get(0),
                    parameters.get(1),
                    parameters.get(2),
                    parameters.get(3)
            );
        } else if (name == UniformlyDistributedNoise.class) {
            return createUniformlyDistributedNoise(
                    parameters.get(0),
                    parameters.get(1),
                    parameters.get(2)
            );
        } else if (name == UnitJump.class ) {
            return createUnitJump(
                    parameters.get(0),
                    parameters.get(1),
                    parameters.get(2),
                    parameters.get(3)
            );
        } else if (name == ImpulseNoise.class) {
            return createImpulseNoise(
                    parameters.get(0),
                    parameters.get(1),
                    parameters.get(2),
                    parameters.get(3),
                    parameters.get(4)
            );
        } else if (name == UnitImpulse.class) {
            return createUnitImpulse(
                    parameters.get(0),
                    parameters.get(1),
                    parameters.get(2).intValue(),
                    parameters.get(3).intValue(),
                    parameters.get(4)
            );
        }
        return null;
    }

}

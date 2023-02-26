package backend;

import backend.signal.AbstractSignal;
import backend.signal.ContinuousSignal;
import backend.signal.DiscreteFourierTransformedSignal;
import backend.signal.DiscreteSignal;
import backend.signal.continuous.*;
import backend.signal.discrete.ImpulseNoise;
import backend.signal.discrete.UnitImpulse;
import org.apache.commons.math3.complex.Complex;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    public AbstractSignal createUniformlyDistributedNoise(double A, double d, double t1) {
        return new UniformlyDistributedNoise(A, d, t1);
    }

    public AbstractSignal createGaussianNoise(double A, double d, double t1) {
        return new GaussianNoise(A, d, t1);
    }

    public AbstractSignal createSinusoidalSignal(double A, double d, double t1, double T) {
        return new SinusoidalSignal(A, d, t1, T);
    }

    public AbstractSignal createOneHalfRectifiedSinusoidalSignal(double A, double d, double t1, double T) {
        return new OneHaltRectifiedSinusoidalSignal(A, d, t1, T);
    }

    public AbstractSignal createTwoHalfRectifiedSinusoidalSignal(double A, double d, double t1, double T) {
        return new TwoHalfRectifiedSinusoidalSignal(A, d, t1, T);
    }

    public AbstractSignal createRectangularSignal(double A, double d, double t1, double T, double kw) {
        return new RectangularSignal(A, d, t1, T, kw);
    }

    public AbstractSignal createSymmetricalRectangularSignal(double A, double d, double t1, double T, double kw) {
        return new SymmetricalRectangularSignal(A, d, t1, T, kw);
    }

    public AbstractSignal createTriangleSignal(double A, double d, double t1, double T, double kw) {
        return new TriangleSignal(A, d, t1, T, kw);
    }

    public AbstractSignal createUnitJump(double A, double d, double t1, double ts) {
        return new UnitJump(A, d, t1, ts);
    }

    public AbstractSignal createImpulseNoise(double A, double d, double f, double p, double t1) {
        return new ImpulseNoise(A, d, f, p, t1);
    }

    public AbstractSignal createUnitImpulse(double A, double d, double f, int n1, int ns) {
        return new UnitImpulse(A, d, f, n1, ns);
    }

    public AbstractSignal createContinuousSignal(Map<Double, Double> points) {
        return new ContinuousSignal(new TreeMap<>(points));
    }
    public AbstractSignal createDiscreteSignal(double A, double d, double f, double t1) {
        return new DiscreteSignal(A, d, f, t1);
    }
    public AbstractSignal createDiscreteSignal(Map<Double, Double> points) {
        return new DiscreteSignal(new TreeMap<>(points));
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
                    parameters.get(2),
                    parameters.get(3).intValue(),
                    parameters.get(4).intValue()
            );
        }
        return null;
    }

    public DiscreteSignal createDiscreteFourierTransformedSignal(Map<Double, Complex> points) {
        return new DiscreteFourierTransformedSignal(points);
    }

}

package backend.signal;

import backend.signal.continuous.*;
import backend.signal.discrete.ImpulseNoise;
import backend.signal.discrete.UnitImpulse;
import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DiscreteSignal.class, name = "DiscreteSignal"),
        @JsonSubTypes.Type(value = ContinuousSignal.class, name = "ContinuousSignal"),
        @JsonSubTypes.Type(value = ContinuousSignal.class, name = "ContinuousSignal"),
        @JsonSubTypes.Type(value = GaussianNoise.class, name = "GaussianNoise"),
        @JsonSubTypes.Type(value = OneHaltRectifiedSinusoidalSignal.class, name = "OneHaltRectifiedSinusoidalSignal"),
        @JsonSubTypes.Type(value = RectangularSignal.class, name = "RectangularSignal"),
        @JsonSubTypes.Type(value = SinusoidalSignal.class, name = "SinusoidalSignal"),
        @JsonSubTypes.Type(value = SymmetricalRectangularSignal.class, name = "SymmetricalRectangularSignal"),
        @JsonSubTypes.Type(value = TwoHalfRectifiedSinusoidalSignal.class, name = "TwoHalfRectifiedSinusoidalSignal"),
        @JsonSubTypes.Type(value = UniformlyDistributedNoise.class, name = "UniformlyDistributedNoise"),
        @JsonSubTypes.Type(value = UnitJump.class, name = "UnitJump"),
        @JsonSubTypes.Type(value = ImpulseNoise.class, name = "ImpulseNoise"),
        @JsonSubTypes.Type(value = UnitImpulse.class, name = "UnitImpulse")
})
@JsonIgnoreProperties(value = {"average", "absoluteAverage", "averagePower", "variance", "effectiveValue", "function"})
public abstract class AbstractSignal implements Serializable {
    protected static final double POINTS_DECIMAL_PLACES_DIVISION = 10000;
    protected Map<Double, Double> points = new LinkedHashMap<>();
    protected double d;
    protected double A;

    protected AbstractSignal(double A, double d) {
        this.A = A;
        this.d = d;
    }

    protected AbstractSignal(Map<Double, Double> points) {
        this.points = points;
    }

    @JsonCreator
    public AbstractSignal(
            @JsonProperty("a") double A,
            @JsonProperty("d") double d,
            @JsonProperty("points") Map<Double, Double> points
    ) {
        this.A = A;
        this.d = d;
        this.points = points;
    }
    public abstract double getAverage();
    public abstract double getAbsoluteAverage();
    public abstract double getAveragePower();
    public abstract double getVariance();
    public abstract double getEffectiveValue();
    public Map<Double, Double> getPoints() {
        return new LinkedHashMap<>(points);
    }

    public double getD() {
        return d;
    }

    public double getA() {
        return A;
    }
    public abstract double calculatePointValue(double x);

    public static double getPointsDecimalPlacesDivision() {
        return POINTS_DECIMAL_PLACES_DIVISION;
    }
}

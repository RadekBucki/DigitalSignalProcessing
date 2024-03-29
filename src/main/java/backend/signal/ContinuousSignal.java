package backend.signal;

import backend.Rounder;
import backend.simpson.DSPSimpsonIntegrator;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

import static backend.Rounder.DECIMAL_PLACES_DIVISION;
import static backend.Rounder.round;

public class ContinuousSignal extends AbstractSignal {
    private final DSPSimpsonIntegrator si = new DSPSimpsonIntegrator();
    protected double t1;
    protected Double t2;
    private transient DoubleUnaryOperator function;

    public ContinuousSignal(double A, double d, double t1) {
        super(A, d);
        this.t1 = t1;
        this.t2 = t1 + d;
    }

    public ContinuousSignal(Map<Double, Double> points) {
        super(points);
        this.t1 = Collections.min(points.keySet());
        this.t2 = Collections.max(points.keySet());
        this.A = Collections.max(points.values());
        this.d = t2 - t1;
    }

    @JsonCreator
    public ContinuousSignal(
            @JsonProperty("a") double A,
            @JsonProperty("d") double d,
            @JsonProperty("t1") double t1,
            @JsonProperty("t2") Double t2,
            @JsonProperty("points") Map<Double, Double> points
    ) {
        super(A, d, points);
        this.t1 = t1;
        this.t2 = t2;
    }

    public void calculateAllPoints() {
        double jump = Rounder.DECIMAL_PLACES_DIVISION / d;
        int t1Rounded = (int) (t1 * jump);
        int t2Rounded = (int) ((t1 + d) * jump);
        for (int i = t1Rounded; i <= t2Rounded; i++) {
            double pointX = i / jump;
            points.put(pointX, calculatePointValue(pointX));
        }
    }

    @Override
    public double calculatePointValue(double x) {
        if (function != null) {
            return function.applyAsDouble(x);
        }
        double value = points.get(Rounder.round(x));
        return Math.abs(value) < 0.000001 ? 0 : value;
    }

    @Override
    public double getAverage() {
        return (1 / (t2 - t1)) * si.integrate(Integer.MAX_VALUE, this::calculatePointValue, t1, t2);
    }

    @Override
    public double getAbsoluteAverage() {
        return (1 / (t2 - t1)) * si.integrate(Integer.MAX_VALUE, x -> Math.abs(calculatePointValue(x)), t1, t2);
    }

    @Override
    public double getAveragePower() {
        return (1 / (t2 - t1)) * si.integrate(Integer.MAX_VALUE,
                x -> calculatePointValue(x) * calculatePointValue(x), t1, t2);
    }

    @Override
    public double getVariance() {
        double average = getAverage();
        return (1 / (t2 - t1)) * si.integrate(Integer.MAX_VALUE,
                x -> (calculatePointValue(x) - average) * (calculatePointValue(x) - average), t1, t2);
    }

    @Override
    public double getEffectiveValue() {
        return Math.sqrt((1 / (t2 - t1)) * si.integrate(Integer.MAX_VALUE,
                x -> calculatePointValue(x) * calculatePointValue(x), t1, t2));
    }

    public double getT1() {
        return t1;
    }

    public Double getT2() {
        return t2;
    }

    public DoubleUnaryOperator getFunction() {
        return function;
    }

    public void setFunction(DoubleUnaryOperator function) {
        this.function = function;
    }

    public DoubleUnaryOperator createFunction(DoubleUnaryOperator calcPoint) {
        return x -> {
            if (x > t2 || x < t1) {
                return 0.0;
            }
            return calcPoint.applyAsDouble(x);
        };
    }
}

package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.DiscreteSignal;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.sqrt;

public class DiscreteFalcoTransform {
    private static final List<Double> H_DB4 = List.of(
            (1 + sqrt(3) / (4 * sqrt(2))),
            (3 + sqrt(3) / (4 * sqrt(2))),
            (3 - sqrt(3) / (4 * sqrt(2))),
            (1 - sqrt(3) / (4 * sqrt(2)))
    );
    private static final List<Double> G_DB4 = List.of(
            H_DB4.get(3) / 2,
            -H_DB4.get(2) / 2,
            H_DB4.get(1) / 2,
            -H_DB4.get(0) / 2
    );
    private static final List<Double> H_DB6 = List.of(
            (1 + sqrt(10) + (2 * sqrt(5))) / (16 * sqrt(2)),
            (5 + sqrt(10) + (2 * sqrt(5))) / (16 * sqrt(2)),
            (10 - 2 * sqrt(10) + (2 * sqrt(5))) / (16 * sqrt(2)),
            (10 - 2 * sqrt(10) - (2 * sqrt(5))) / (16 * sqrt(2)),
            (5 + sqrt(10) - (2 * sqrt(5))) / (16 * sqrt(2)),
            (1 + sqrt(10) - (2 * sqrt(5))) / (16 * sqrt(2))
    );

    private static final List<Double> G_DB6 = List.of(
            H_DB6.get(5) / 4,
            -H_DB6.get(4) / 4,
            H_DB6.get(3) / 4,
            -H_DB6.get(2) / 4,
            H_DB6.get(1) / 4,
            -H_DB6.get(0) / 4
    );

    private static final List<Double> H_DB8 = List.of(
            (1 + sqrt(2) + sqrt(6) + sqrt(10) + (2 * sqrt(5))) / (32 * sqrt(2)),
            (5 + sqrt(2) + sqrt(6) + sqrt(10) - (2 * sqrt(5))) / (32 * sqrt(2)),
            (10 - 2 * sqrt(2) + sqrt(6) + 2 * sqrt(10) + (2 * sqrt(5))) / (32 * sqrt(2)),
            (10 - 2 * sqrt(2) + sqrt(6) - 2 * sqrt(10) - (2 * sqrt(5))) / (32 * sqrt(2)),
            (5 + sqrt(2) - sqrt(6) - sqrt(10) + (2 * sqrt(5))) / (32 * sqrt(2)),
            (1 + sqrt(2) - sqrt(6) - sqrt(10) - (2 * sqrt(5))) / (32 * sqrt(2)),
            (-sqrt(2) + sqrt(6) - sqrt(10) + (2 * sqrt(5))) / (32 * sqrt(2)),
            (-sqrt(2) - sqrt(6) + sqrt(10) + (2 * sqrt(5))) / (32 * sqrt(2))
    );

    private static final List<Double> G_DB8 = List.of(
            H_DB8.get(7) / 8,
            -H_DB8.get(6) / 8,
            H_DB8.get(5) / 8,
            -H_DB8.get(4) / 8,
            H_DB8.get(3) / 8,
            -H_DB8.get(2) / 8,
            H_DB8.get(1) / 8,
            -H_DB8.get(0) / 8
    );

    SignalFactory signalFactory;

    public DiscreteFalcoTransform(SignalFactory signalFactory) {
        this.signalFactory = signalFactory;
    }

    public List<DiscreteSignal> execute(DiscreteSignal signal, Level level) {
        List<Double> g;
        List<Double> h;
        switch (level) {
            case DB4 -> {
                g = G_DB4;
                h = H_DB4;
            }
            case DB6 -> {
                g = G_DB6;
                h = H_DB6;
            }
            case DB8 -> {
                g = G_DB8;
                h = H_DB8;
            }
            default -> throw new IllegalStateException("Unexpected value: " + level);
        }

        AtomicInteger iForH = new AtomicInteger(0);
        AtomicInteger iForG = new AtomicInteger(0);

        return List.of(
                (DiscreteSignal) signalFactory.createDiscreteSignal(
                        getTransformedPoints(signal.getPoints(), h).entrySet().stream()
                                .filter(entry -> iForH.getAndIncrement() % 2 == 0)
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                ),
                (DiscreteSignal) signalFactory.createDiscreteSignal(
                        getTransformedPoints(signal.getPoints(), g).entrySet().stream()
                                .filter(entry -> iForG.getAndIncrement() % 2 == 1)
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                )
        );
    }

    private Map<Double, Double> getTransformedPoints(Map<Double, Double> points, List<Double> filter) {
        return points.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> IntStream.of(0, filter.size())
                                .mapToDouble(i -> entry.getValue() * filter.get(i) * (filter.size() - i))
                                .sum()
                ));
    }
}

package backend.signal_operation;

import backend.SignalFactory;
import backend.signal.DiscreteSignal;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DiscreteWalshHadamardTransform {

    private final SignalFactory signalFactory;

    public DiscreteWalshHadamardTransform(SignalFactory signalFactory) {
        this.signalFactory = signalFactory;
    }

    public DiscreteSignal execute(DiscreteSignal signal, TransformType transformType) {
        if (transformType == TransformType.DIRECT) {
            return executeDirect(signal);
        } else {
            return executeFast(signal);
        }
    }

    private DiscreteSignal executeDirect(DiscreteSignal signal) {
        List<Double> pointsTimes = signal.getPoints().keySet().stream().toList();
        List<Double> pointsValues = signal.getPoints().values().stream().toList();
        double[][] normalizeFactor = {{1 / Math.pow(Math.sqrt(2), pointsValues.size())}};
        RealMatrix walshHadamardMatrix = calculateWalshHadamardMatrix(pointsValues.size()).multiply(MatrixUtils.createRealMatrix(normalizeFactor));
        RealMatrix signalMatrix = MatrixUtils.createColumnRealMatrix(pointsValues.stream().mapToDouble(Double::doubleValue).toArray());
        List<Double> resultPointsValues = Arrays.stream(walshHadamardMatrix.multiply(signalMatrix).getColumn(0)).boxed().toList();
        return (DiscreteSignal) signalFactory.createDiscreteSignal(
                IntStream.range(0, pointsTimes.size())
                        .boxed()
                        .collect(Collectors.toMap(pointsTimes::get, resultPointsValues::get))
        );
    }

    private DiscreteSignal executeFast(DiscreteSignal signal) {
        return null; //TODO: TO IMPLEMENT
    }

    private RealMatrix calculateWalshHadamardMatrix(int m) {
        double[][] startMatrixData = {{1}};
        RealMatrix previousMatrix = MatrixUtils.createRealMatrix(startMatrixData);
        RealMatrix matrix = MatrixUtils.createRealMatrix(startMatrixData);
        for (int i = 0; i < m; i++) {
            matrix = MatrixUtils.createRealMatrix(2 * previousMatrix.getRowDimension(), 2 * previousMatrix.getColumnDimension());
            for (int j = 0; j < previousMatrix.getRowDimension(); j++) {
                for (int k = 0; k < previousMatrix.getColumnDimension(); k++) {
                    matrix.setEntry(j, k, previousMatrix.getEntry(j, k));
                    matrix.setEntry(j + previousMatrix.getRowDimension(), k, previousMatrix.getEntry(j, k));
                    matrix.setEntry(j, k + previousMatrix.getColumnDimension(), previousMatrix.getEntry(j, k));
                    matrix.setEntry(j + previousMatrix.getRowDimension(), k + previousMatrix.getColumnDimension(), -previousMatrix.getEntry(j, k));
                }
            }
            previousMatrix = matrix;
        }
        return matrix;
    }
}

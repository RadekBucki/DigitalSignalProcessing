package frontend.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.text.NumberFormat;
import java.util.Map;
import java.util.Optional;

public class ChartGenerator {
    private static final float POINT_SIZE = 3.0f;
    private ChartGenerator() {
    }

    public static JFreeChart generateAmplitudeTimeChart(Map<Double, Double> points, boolean isDiscrete) {
        return generateAmplitudeTimeChart(points, isDiscrete, "Amplitude / time function" , "Time");
    }
    public static JFreeChart generateAmplitudeTimeChart(
            Map<Double, Double> points, boolean isDiscrete, String title, String xLabel
    ) {
        XYSeries errorFunctionSeries = new XYSeries("Amplitude / time function");
        for (Map.Entry<Double, Double> entry : points.entrySet()) {
            errorFunctionSeries.add(entry.getKey(), entry.getValue());
        }

        XYSeriesCollection seriesCollection = new XYSeriesCollection();
        seriesCollection.addSeries(errorFunctionSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                title, xLabel, "Amplitude", seriesCollection,
                PlotOrientation.VERTICAL, false, true, false
        );
        chart.setBackgroundPaint(new Color(0xF4, 0xF4, 0xF4));
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        Shape shape = new Ellipse2D.Double(-POINT_SIZE/2, -POINT_SIZE/2, POINT_SIZE, POINT_SIZE);
        renderer.setSeriesShape(0, shape);

        Optional<Map.Entry<Double, Double>> maxEntry = points.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());
        Optional<Map.Entry<Double, Double>> minEntry = points.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue());
        if (maxEntry.isPresent() && minEntry.isPresent()) {
            double chartMargin = Math.max(Math.abs(minEntry.get().getValue()), Math.abs(maxEntry.get().getValue())) / 10.0;
            if (chartMargin == 0) {
                chartMargin = 1;
            }
            plot.getRangeAxis().setRange(minEntry.get().getValue() - chartMargin,
                    maxEntry.get().getValue() + chartMargin);
        }

        ValueMarker yZeroMarker = new ValueMarker(0);
        yZeroMarker.setPaint(Color.darkGray);
        yZeroMarker.setAlpha(0.5F);
        plot.addRangeMarker(yZeroMarker);

        changeVisibility(renderer, 0, !isDiscrete);
        formatAxis(renderer, 1);
        formatAxis(renderer, 2);

        plot.setRenderer(renderer);

        return chart;
    }

    public static JFreeChart generateHistogram(Map<Double, Double> points, int binsNumber) {

        double[] values = points.values().stream().mapToDouble(Double::doubleValue).toArray();

        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("key", values, binsNumber);
        dataset.setType(HistogramType.RELATIVE_FREQUENCY);

        JFreeChart histogram = ChartFactory.createHistogram("Histogram", "Value",
                "Frequency", dataset, PlotOrientation.VERTICAL, false, false, false);
        histogram.setBackgroundPaint(new Color(0xF4, 0xF4, 0xF4));

        NumberAxis axis = (NumberAxis) histogram.getXYPlot().getRangeAxis();
        axis.setNumberFormatOverride(NumberFormat.getPercentInstance());

        return histogram;
    }

    private static void formatAxis(XYLineAndShapeRenderer renderer, int series) {
        changeVisibility(renderer, series, true);
        renderer.setSeriesStroke(series, new BasicStroke(0.5f));
    }

    private static void changeVisibility(XYLineAndShapeRenderer renderer, int series, boolean displayLine) {
        renderer.setSeriesLinesVisible(series, displayLine);
        renderer.setSeriesShapesVisible(series, !displayLine);
    }
}
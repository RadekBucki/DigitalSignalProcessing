package frontend.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.Map;
import java.util.Optional;

public class ChartGenerator {
    private ChartGenerator() {
    }

    public static JFreeChart generatePlot(Map<Double, Double> points, boolean isDiscrete) {
        XYSeries errorFunctionSeries = new XYSeries("Amplitude / time function");
        for (Map.Entry<Double,Double> entry : points.entrySet()) {
            errorFunctionSeries.add(entry.getKey(), entry.getValue());
        }

        XYSeriesCollection seriesCollection = new XYSeriesCollection();
        seriesCollection.addSeries(errorFunctionSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Amplitude / time function", "Time", "Amplitude", seriesCollection,
                PlotOrientation.VERTICAL, false, true, false
        );
        chart.setBackgroundPaint(new Color(0xF4, 0xF4, 0xF4));
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        Optional<Map.Entry<Double, Double>> maxEntry = points.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());
        Optional<Map.Entry<Double, Double>> minEntry = points.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue());
        if (maxEntry.isPresent() && minEntry.isPresent()) {
            double chartMargin = Math.max(Math.abs(minEntry.get().getValue()), Math.abs(maxEntry.get().getValue())) / 10.0;
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

    private static void formatAxis(XYLineAndShapeRenderer renderer, int series) {
        changeVisibility(renderer, series, true);
        renderer.setSeriesStroke(series, new BasicStroke(0.5f));
    }

    private static void changeVisibility(XYLineAndShapeRenderer renderer, int series, boolean displayLine) {
        renderer.setSeriesLinesVisible(series, displayLine);
        renderer.setSeriesShapesVisible(series, !displayLine);
    }
}
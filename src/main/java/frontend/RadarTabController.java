package frontend;

import backend.SignalFacade;
import backend.radar.RadarExecutor;
import backend.signal.AbstractSignal;
import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;
import frontend.chart.ChartGenerator;
import frontend.units.TextFormatterFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.jfree.chart.ChartUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class RadarTabController implements Initializable {

    @FXML
    private Button startRadarButton;
    @FXML
    private ComboBox<String> signal1ComboBox;
    @FXML
    private TextField samplingFrequency;
    @FXML
    private TextField bufferSize;
    @FXML
    private TextField workTime;
    @FXML
    private TextField radarX;
    @FXML
    private TextField radarY;
    @FXML
    private TextField timeBetweenReports;
    @FXML
    private TextField signalSpeed;
    @FXML
    private TextField objectX;
    @FXML
    private TextField objectY;
    @FXML
    private TextField objectXspeed;
    @FXML
    private TextField objectYspeed;
    @FXML
    private VBox rightPanel;
    @FXML
    private VBox statsPanel;
    @FXML
    private ImageView signalSentGraph;
    @FXML
    private ImageView signalReceivedGraph;
    @FXML
    private ImageView correlationGraph;
    @FXML
    private Label radarDistance;
    @FXML
    private Label realDistance;
    @FXML
    private Label timeDistance;
    @FXML
    private Button previousWindowButton;
    @FXML
    private Button nextWindowButton;

    private final SignalFacade signalFacade = new SignalFacade();
    private final TextFormatterFactory formatterFactory = new TextFormatterFactory();
    private final Map<String, AbstractSignal> signals = new LinkedHashMap<>();
    private RadarExecutor r;
    private int currentWindow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bufferSize.setTextFormatter(formatterFactory.createIntegerTextFormatter(
                bufferSize, startRadarButton, this::shouldRadarStartButtonBeDisabled
        ));
        setDecimalTextFormatter(samplingFrequency);
        setDecimalTextFormatter(signalSpeed);
        setDecimalTextFormatter(workTime);
        setDecimalTextFormatter(timeBetweenReports);
        setDecimalTextFormatter(radarX);
        setDecimalTextFormatter(radarY);
        setDecimalTextFormatter(objectX);
        setDecimalTextFormatter(objectY);
        setDecimalTextFormatter(objectXspeed);
        setDecimalTextFormatter(objectYspeed);
    }

    public void addOrUpdateSignal(String name, AbstractSignal signal) {
        if (signal == null) {
            signals.remove(name);
            return;
        }
        if (signal instanceof DiscreteSignal || Collections.min(signal.getPoints().keySet()) != 0.0) {
            return;
        }
        signals.put(name, signal);
        signal1ComboBox.getItems().setAll(signals.keySet());
    }

    public void onUpdateRadarInitData() {
        startRadarButton.setDisable(
                signal1ComboBox.getValue() == null
        );
    }

    public void startRadar() {
        r = signalFacade.startRadar(Double.parseDouble(samplingFrequency.getText()),
                Integer.parseInt(bufferSize.getText()),
                Double.parseDouble(signalSpeed.getText()),
                Double.parseDouble(workTime.getText()),
                Double.parseDouble(timeBetweenReports.getText()),
                (ContinuousSignal) signals.get(signal1ComboBox.getValue()),
                Double.parseDouble(radarX.getText()),
                Double.parseDouble(radarY.getText()),
                Double.parseDouble(objectX.getText()),
                Double.parseDouble(objectY.getText()),
                Double.parseDouble(objectXspeed.getText()),
                Double.parseDouble(objectYspeed.getText()));

        currentWindow = 0;
        updateData();
        previousWindowButton.setDisable(true);
        rightPanel.setVisible(true);
        statsPanel.setVisible(true);
    }

    private boolean shouldRadarStartButtonBeDisabled() {
        return signal1ComboBox.getValue() == null ||
                samplingFrequency.getText().isEmpty() ||
                bufferSize.getText().isEmpty() ||
                signalSpeed.getText().isEmpty() ||
                workTime.getText().isEmpty() ||
                timeBetweenReports.getText().isEmpty() ||
                radarX.getText().isEmpty() ||
                radarY.getText().isEmpty() ||
                objectX.getText().isEmpty() ||
                objectY.getText().isEmpty() ||
                objectXspeed.getText().isEmpty() ||
                objectYspeed.getText().isEmpty();
    }

    private void setDecimalTextFormatter(TextField textField) {
        textField.setTextFormatter(formatterFactory.createDecimalTextFormatter(
                textField, startRadarButton, this::shouldRadarStartButtonBeDisabled
        ));
    }

    public void previousWindow() {
        currentWindow--;
        nextWindowButton.setDisable(false);
        if (currentWindow == 0) {
            previousWindowButton.setDisable(true);
        }
        updateData();
    }

    public void nextWindow() {
        currentWindow++;
        previousWindowButton.setDisable(false);
        if (currentWindow + 1 == r.getRadarDistances().size()) {
            nextWindowButton.setDisable(true);
        }
        updateData();
    }

    private void updateData() {
        DiscreteSignal signalSent = r.getSignalSentWindows().get(currentWindow);
        DiscreteSignal signalReceived = r.getSignalReceivedWindows().get(currentWindow);
        DiscreteSignal correlation = r.getCorrelationsWindows().get(currentWindow);
        try {
            ChartUtilities.saveChartAsPNG(
                    new File("radarChart1.png"),
                    ChartGenerator.generateAmplitudeTimeChart(
                            signalSent.getPoints(), true
                    ),
                    400,
                    220
            );
            FileInputStream input = new FileInputStream("radarChart1.png");
            signalSentGraph.setImage(new Image(input));

            ChartUtilities.saveChartAsPNG(
                    new File("radarChart2.png"),
                    ChartGenerator.generateAmplitudeTimeChart(
                            signalReceived.getPoints(), true
                    ),
                    400,
                    220
            );
            input = new FileInputStream("radarChart2.png");
            signalReceivedGraph.setImage(new Image(input));

            ChartUtilities.saveChartAsPNG(
                    new File("radarChartCorrelation.png"),
                    ChartGenerator.generateAmplitudeTimeChart(
                            correlation.getPoints(), true
                    ),
                    400,
                    220
            );
            input = new FileInputStream("radarChartCorrelation.png");
            correlationGraph.setImage(new Image(input));
        } catch (Exception ignored) {
            //ignored
        }

        radarDistance.setText(String.valueOf(r.getRadarDistances().get(currentWindow)));
        realDistance.setText(String.valueOf(r.getRealDistances().get(currentWindow)));
        timeDistance.setText(String.valueOf(r.getDistancesTimes().get(currentWindow)));
    }
}

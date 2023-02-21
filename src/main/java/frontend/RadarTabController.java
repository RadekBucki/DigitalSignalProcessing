package frontend;

import backend.SignalFacade;
import backend.signal.AbstractSignal;
import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

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

    private final SignalFacade signalFacade = new SignalFacade();
    private final TextFormatterFactory formatterFactory = new TextFormatterFactory();
    private final Map<String, AbstractSignal> signals = new LinkedHashMap<>();

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
        signalFacade.startRadar(Double.parseDouble(samplingFrequency.getText()),
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
}

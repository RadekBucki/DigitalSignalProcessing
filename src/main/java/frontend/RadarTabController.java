package frontend;

import backend.signal.AbstractSignal;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class RadarTabController implements Initializable {

    @FXML
    private Button applyOperationButton;
    @FXML
    private ComboBox<String> signal1ComboBox;
    @FXML
    private ComboBox<String> signal2ComboBox;

    private final Map<String, AbstractSignal> signals = new LinkedHashMap<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void addOrUpdateSignal(String name, AbstractSignal signal) {
        if (signal == null) {
            signals.remove(name);
        } else {
            signals.put(name, signal);
        }

        signal1ComboBox.getItems().setAll(signals.keySet());
        signal2ComboBox.getItems().setAll(signals.keySet());
    }

    public void onUpdateMathOperationsComboBox() {
        applyOperationButton.setDisable(
                        signal1ComboBox.getValue() == null ||
                        signal2ComboBox.getValue() == null
        );
    }

    public void mathOperation() {

    }
}

package frontend;

import backend.SignalFacade;
import backend.signal.AbstractSignal;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

public class SignalOperationFormController implements Initializable {
    public static final String SIGNAL_OPERATION_FORM_RESOURCE = "SignalOperationForm.fxml";
    @FXML
    private ComboBox<String> signalOperationComboBox;
    @FXML
    private ComboBox<String> signal1ComboBox;
    @FXML
    private ComboBox<String> signal2ComboBox;
    private final SignalFacade signalFacade = new SignalFacade();

    private final Map<BiFunction<AbstractSignal, AbstractSignal, AbstractSignal>, String> signalOperations = Map.of(
            signalFacade::add, "Add",
            signalFacade::subtract, "Subtract",
            signalFacade::multiply, "Multiply",
            signalFacade::divide, "Divide"
    );
    private final Map<String, AbstractSignal> signals = new LinkedHashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signalOperationComboBox.getItems().addAll(signalOperations.values());
    }

    public void addOrUpdateSignal(String name, AbstractSignal signal) {
        signals.put(name, signal);
        signal1ComboBox.getItems().setAll(signals.keySet());
        signal2ComboBox.getItems().setAll(signals.keySet());
    }
}

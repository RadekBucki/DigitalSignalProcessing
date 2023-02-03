package frontend;

import backend.SignalFacade;
import backend.signal.AbstractSignal;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

public class SignalOperationTabController implements Initializable {
    public static final String RESOURCE = "SignalOperationTab.fxml";
    @FXML
    private Button applyOperationButton; // TODO: Disable button if data is not relevant
    @FXML
    private ComboBox<String> signalOperationComboBox;
    @FXML
    private ComboBox<String> signal1ComboBox;
    @FXML
    private ComboBox<String> signal2ComboBox;
    private final SignalFacade signalFacade = new SignalFacade();

    private final Map<String, BiFunction<AbstractSignal, AbstractSignal, AbstractSignal>>  signalOperations = Map.of(
            "Add", signalFacade::add,
            "Subtract", signalFacade::subtract,
            "Multiply", signalFacade::multiply,
            "Divide", signalFacade::divide
    );
    private final Map<String, AbstractSignal> signals = new LinkedHashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signalOperationComboBox.getItems().addAll(signalOperations.keySet());
    }

    public void addOrUpdateSignal(String name, AbstractSignal signal) {
        signals.put(name, signal);
        signal1ComboBox.getItems().setAll(signals.keySet());
        signal2ComboBox.getItems().setAll(signals.keySet());
    }

    public void applyOperation() {
        AbstractSignal signal = signalOperations.get(signalOperationComboBox.getValue()).apply(
            signals.get(signal1ComboBox.getValue()),
            signals.get(signal2ComboBox.getValue())
        );
        // TODO: Create new tab and set signal
    }
}

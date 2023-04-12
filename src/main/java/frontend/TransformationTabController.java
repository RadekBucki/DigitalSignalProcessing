package frontend;

import backend.SignalFacade;
import backend.signal.AbstractSignal;
import backend.signal.DiscreteSignal;
import backend.signal_operation.Level;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class TransformationTabController implements Initializable {
    private final Map<String, AbstractSignal> signals = new LinkedHashMap<>();
    private final SignalFacade signalFacade = new SignalFacade();

    private Consumer<AbstractSignal> createSignalTab = null;

    @FXML
    public ComboBox<String> falcoSignalComboBox;
    @FXML
    public ComboBox<Level> falcoLevelComboBox;
    @FXML
    public Button applyFalcoButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        falcoLevelComboBox.getItems().setAll(Level.values());
    }

    public void setCreateSignalTab(Consumer<AbstractSignal> createSignalTab) {
        this.createSignalTab = createSignalTab;
    }

    public void addOrUpdateSignal(String name, AbstractSignal signal) {
        if (signal == null) {
            signals.remove(name);
            return;
        }
        signals.put(name, signal);

        List<String> discreteSignals = signals.entrySet()
                .stream()
                .filter(entry -> entry.getValue() instanceof DiscreteSignal)
                .map(Map.Entry::getKey)
                .toList();
        falcoSignalComboBox.getItems().setAll(discreteSignals);
    }

    public void discreteFalcoTransformOperation() {
        signalFacade.discreteFalcoTransform(
                (DiscreteSignal) signals.get(falcoSignalComboBox.getValue()),
                falcoLevelComboBox.getValue()
        ).forEach(createSignalTab);
    }

    public void onUpdateDiscreteFalcoTransformOperationsComboBox() {
        applyFalcoButton.setDisable(falcoSignalComboBox.getValue() == null || falcoLevelComboBox.getValue() == null);
    }
}

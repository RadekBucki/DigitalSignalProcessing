package frontend;

import backend.SignalFacade;
import backend.signal.AbstractSignal;
import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class SignalOperationTabController implements Initializable {
    @FXML
    private Button applyOperationButton;
    @FXML
    private ComboBox<String> signalOperationComboBox;
    @FXML
    private ComboBox<String> signal1ComboBox;
    @FXML
    private ComboBox<String> signal2ComboBox;
    @FXML
    private ComboBox<String> signalACDCComboBox;
    @FXML
    private Button samplingOperationButton;
    @FXML
    private Button quantizationOperationButton;
    @FXML
    private Button reconstructOperationButton;

    private final SignalFacade signalFacade = new SignalFacade();

    private final Map<String, BiFunction<AbstractSignal, AbstractSignal, AbstractSignal>>  signalOperations = Map.of(
            "Add", signalFacade::add,
            "Subtract", signalFacade::subtract,
            "Multiply", signalFacade::multiply,
            "Divide", signalFacade::divide
    );
    private final Map<String, AbstractSignal> signals = new LinkedHashMap<>();

    private Consumer<AbstractSignal> createSignalTab = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signalOperationComboBox.getItems().addAll(signalOperations.keySet());
    }

    public void addOrUpdateSignal(String name, AbstractSignal signal) {
        signals.put(name, signal);
        signal1ComboBox.getItems().setAll(signals.keySet());
        signal2ComboBox.getItems().setAll(signals.keySet());
        signalACDCComboBox.getItems().setAll(signals.keySet());
    }

    public void applyOperation() {
        AbstractSignal signal = signalOperations.get(signalOperationComboBox.getValue()).apply(
            signals.get(signal1ComboBox.getValue()),
            signals.get(signal2ComboBox.getValue())
        );
        createSignalTab.accept(signal);
    }

    public void onUpdateComboBox() {
        applyOperationButton.setDisable(
                signalOperationComboBox.getValue() == null ||
                signal1ComboBox.getValue() == null ||
                signal2ComboBox.getValue() == null
        );
    }

    public void onUpdateComboBoxAcDc() {
        if (signals.get(signalACDCComboBox.getValue()) instanceof DiscreteSignal) {
            samplingOperationButton.setDisable(true);
            quantizationOperationButton.setDisable(false);
            reconstructOperationButton.setDisable(false);
        } else {
            samplingOperationButton.setDisable(false);
            quantizationOperationButton.setDisable(true);
            reconstructOperationButton.setDisable(true);
        }
    }

    public void setCreateSignalTab(Consumer<AbstractSignal> createSignalTab) {
        this.createSignalTab = createSignalTab;
    }

    public void samplingOperation() {
        AbstractSignal signal = signalFacade.sampling((ContinuousSignal) signals.get(signalACDCComboBox.getValue()), 10);
        samplingOperationButton.setDisable(true);
        quantizationOperationButton.setDisable(true);
        reconstructOperationButton.setDisable(true);
        createSignalTab.accept(signal);
    }

    public void quantizationOperation() {
    }

    public void reconstructOperation() {
    }
}

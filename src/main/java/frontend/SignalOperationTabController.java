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
import javafx.scene.control.TextFormatter;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class SignalOperationTabController implements Initializable {
    @FXML
    public TextField numOfSamples;
    @FXML
    private ComboBox<String> reconstructionTypeComboBox;
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
    private TextField samplingFrequency;
    @FXML
    private Button quantizationOperationButton;
    @FXML
    private TextField numOfLevelsQuantization;
    @FXML
    private ComboBox<String> quantizationTypeComboBox;
    @FXML
    private Button reconstructOperationButton;

    private final SignalFacade signalFacade = new SignalFacade();

    private final Map<String, BiFunction<AbstractSignal, AbstractSignal, AbstractSignal>>  signalOperations = Map.of(
            "Add", signalFacade::add,
            "Subtract", signalFacade::subtract,
            "Multiply", signalFacade::multiply,
            "Divide", signalFacade::divide
    );

    private final Map<String, BiFunction<DiscreteSignal, Integer, AbstractSignal>>  quantizationTypes = Map.of(
            "With Truncation", signalFacade::quantizationWithTruncate,
            "With Rounding", signalFacade::quantizationWithRounding
    );

    private final Map<String, BiFunction<DiscreteSignal, Integer, ContinuousSignal>> reconstructionTypes = Map.of(
            "Zero Order Hold", signalFacade::reconstructZeroOrderHold,
            "First Order Hold", signalFacade::reconstructFirstMethodHold,
            "Sinc", signalFacade::reconstructSinc
    );
    private final Map<String, AbstractSignal> signals = new LinkedHashMap<>();

    private Consumer<AbstractSignal> createSignalTab = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signalOperationComboBox.getItems().addAll(signalOperations.keySet());
        quantizationTypeComboBox.getItems().addAll(quantizationTypes.keySet());
        samplingFrequency.setTextFormatter(new TextFormatter<>(text -> {
            String newText = text.getControlNewText().replace(",", ".");
            if (!newText.matches("-?(\\d*[.])?\\d*")) {
                samplingFrequency.clear();
                return null;
            }
            samplingOperationButton.setDisable(shouldSamplingButtonBeDisabled());
            return text;
        }));
        numOfLevelsQuantization.setTextFormatter(new TextFormatter<>(text -> {
            String newText = text.getControlNewText().replace(",", ".");
            if (!newText.matches("\\d*")) {
                samplingFrequency.clear();
                return null;
            }
            quantizationOperationButton.setDisable(shouldQuantizationButtonBeDisabled());
            return text;
        }));
        numOfSamples.setTextFormatter(new TextFormatter<>(text -> {
            String newText = text.getControlNewText().replace(",", ".");
            if (!newText.matches("\\d*")) {
                numOfSamples.clear();
                return null;
            }
            reconstructOperationButton.setDisable(shouldReconstructButtonBeDisabled());
            return text;
        }));
        reconstructionTypeComboBox.getItems().addAll(reconstructionTypes.keySet());
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

    public void setCreateSignalTab(Consumer<AbstractSignal> createSignalTab) {
        this.createSignalTab = createSignalTab;
    }

    public void samplingOperation() {
        AbstractSignal signal = signalFacade.sampling((ContinuousSignal) signals.get(signalACDCComboBox.getValue()),
                Double.parseDouble(samplingFrequency.getText()));
        samplingFrequency.clear();
        createSignalTab.accept(signal);
    }

    public boolean shouldSamplingButtonBeDisabled() {
        return !(signals.get(signalACDCComboBox.getValue()) instanceof ContinuousSignal) ||
                samplingFrequency.getText().isEmpty();
    }

    public void quantizationOperation() {
        AbstractSignal signal = quantizationTypes.get(quantizationTypeComboBox.getValue())
                .apply((DiscreteSignal) signals.get(signalACDCComboBox.getValue()),
                        Integer.valueOf(numOfLevelsQuantization.getText()));
        numOfLevelsQuantization.clear();
        createSignalTab.accept(signal);
    }

    public boolean shouldQuantizationButtonBeDisabled() {
        return !(signals.get(signalACDCComboBox.getValue()) instanceof DiscreteSignal) ||
                numOfLevelsQuantization.getText().isEmpty() ||
                quantizationTypeComboBox.getValue() == null;
    }

    private boolean shouldReconstructButtonBeDisabled() {
        return !(signals.get(signalACDCComboBox.getValue()) instanceof DiscreteSignal) ||
                numOfSamples.getText().isEmpty() ||
                reconstructionTypeComboBox.getValue() == null;
    }

    public void reconstructOperation() {
        AbstractSignal signal = reconstructionTypes.get(reconstructionTypeComboBox.getValue())
                .apply(
                        (DiscreteSignal) signals.get(signalACDCComboBox.getValue()),
                        Integer.valueOf(numOfSamples.getText())
                );
        createSignalTab.accept(signal);
    }
}

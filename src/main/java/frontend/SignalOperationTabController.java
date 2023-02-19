package frontend;

import backend.SignalFacade;
import backend.signal.AbstractSignal;
import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;
import backend.signal_operation.DiscreteSignalsCorrelationType;
import backend.signal_operation.PassType;
import backend.signal_operation.WindowType;
import frontend.alert.AlertBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SignalOperationTabController implements Initializable {
    @FXML
    private HBox reconstructionOperationHBox;
    @FXML
    private HBox quantizationOperationHBox;
    @FXML
    private HBox samplingOperationHBox;
    @FXML
    private TextField numOfSamples;
    @FXML
    private ComboBox<String> reconstructionSourceSignalComboBox;
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
    @FXML
    private ComboBox<String> signal1ConvolutionCorrelationComboBox;
    @FXML
    private ComboBox<String> signal2ConvolutionCorrelationComboBox;
    @FXML
    private Button convolutionOperationButton;
    @FXML
    public ComboBox<String> correlationTypeComboBox;
    @FXML
    public Button correlationOperationButton;
    @FXML
    public ComboBox<String> signalFilterComboBox;
    @FXML
    public ComboBox<String> windowComboBox;
    @FXML
    public ComboBox<String> passComboBox;
    @FXML
    public TextField rankOfFilter;
    @FXML
    public TextField cutOffFrequency;
    @FXML
    public Button filterOperationButton;

    private final SignalFacade signalFacade = new SignalFacade();

    private final Map<String, BiFunction<AbstractSignal, AbstractSignal, AbstractSignal>> signalOperations = Map.of(
            "Add", signalFacade::add,
            "Subtract", signalFacade::subtract,
            "Multiply", signalFacade::multiply,
            "Divide", signalFacade::divide
    );

    private final Map<String, BiFunction<DiscreteSignal, Integer, AbstractSignal>> quantizationTypes = Map.of(
            "With Truncation", signalFacade::quantizationWithTruncate,
            "With Rounding", signalFacade::quantizationWithRounding
    );

    private final Map<String, BiFunction<DiscreteSignal, Integer, ContinuousSignal>> reconstructionTypes = Map.of(
            "Zero Order Hold", signalFacade::reconstructZeroOrderHold,
            "First Order Hold", signalFacade::reconstructFirstMethodHold,
            "Sinc", signalFacade::reconstructSinc
    );
    private final Map<String, DiscreteSignalsCorrelationType> discreteSignalsCorrelationTypes = Map.of(
            "Direct", DiscreteSignalsCorrelationType.DIRECT,
            "Using convolution", DiscreteSignalsCorrelationType.USING_CONVOLUTION
    );
    private final Map<String, WindowType> windowTypes = Map.of(
            "Rectangular", WindowType.RECTANGULAR,
            "Blackman", WindowType.BLACKMAN,
            "Hamming", WindowType.HAMMING,
            "Hanning", WindowType.HANNING
    );
    private final Map<String, PassType> passTypes = Map.of(
            "Low pass", PassType.LOW_PASS,
            "High pass", PassType.HIGH_PASS,
            "Band pass", PassType.BAND_PASS
    );
    private final Map<String, AbstractSignal> signals = new LinkedHashMap<>();

    private Consumer<AbstractSignal> createSignalTab = null;

    @SuppressWarnings("DuplicatedCode")
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
        correlationTypeComboBox.getItems().setAll(discreteSignalsCorrelationTypes.keySet());
        windowComboBox.getItems().setAll(windowTypes.keySet());
        passComboBox.getItems().setAll(passTypes.keySet());
        rankOfFilter.setTextFormatter(new TextFormatter<>(text -> {
            String newText = text.getControlNewText().replace(",", ".");
            if (!newText.matches("-?(\\d*[.])?\\d*")) {
                rankOfFilter.clear();
                return null;
            }
            filterOperationButton.setDisable(shouldFilterButtonBeDisabled());
            return text;
        }));
        cutOffFrequency.setTextFormatter(new TextFormatter<>(text -> {
            String newText = text.getControlNewText().replace(",", ".");
            if (!newText.matches("-?(\\d*[.])?\\d*")) {
                cutOffFrequency.clear();
                return null;
            }
            filterOperationButton.setDisable(shouldFilterButtonBeDisabled());
            return text;
        }));
    }

    public void addOrUpdateSignal(String name, AbstractSignal signal) {
        if (signal == null) {
            signals.remove(name);
        } else {
            signals.put(name, signal);
        }

        Set<String> discreteSignals = signals.entrySet()
                .stream()
                .filter(v -> v.getValue() instanceof DiscreteSignal)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).keySet();

        Set<String> continuousSignals = signals.entrySet()
                .stream()
                .filter(v -> v.getValue() instanceof ContinuousSignal)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).keySet();

        signal1ComboBox.getItems().setAll(signals.keySet());
        signal2ComboBox.getItems().setAll(signals.keySet());
        signalACDCComboBox.getItems().setAll(signals.keySet());

        signal1ConvolutionCorrelationComboBox.getItems().setAll(discreteSignals);
        signal2ConvolutionCorrelationComboBox.getItems().setAll(discreteSignals);
        signalFilterComboBox.getItems().setAll(discreteSignals);

        reconstructionSourceSignalComboBox.getItems().setAll(continuousSignals);
    }

    public void mathOperation() {
        AbstractSignal signal = signalOperations.get(signalOperationComboBox.getValue()).apply(
                signals.get(signal1ComboBox.getValue()),
                signals.get(signal2ComboBox.getValue())
        );
        createSignalTab.accept(signal);
    }

    public void samplingOperation() {
        AbstractSignal signal = signalFacade.sampling((ContinuousSignal) signals.get(signalACDCComboBox.getValue()),
                Double.parseDouble(samplingFrequency.getText()));
        samplingFrequency.clear();
        createSignalTab.accept(signal);
    }

    public void quantizationOperation() {
        AbstractSignal signal = quantizationTypes.get(quantizationTypeComboBox.getValue())
                .apply((DiscreteSignal) signals.get(signalACDCComboBox.getValue()),
                        Integer.valueOf(numOfLevelsQuantization.getText()));
        numOfLevelsQuantization.clear();
        createSignalTab.accept(signal);
    }

    public void reconstructOperation() {
        ContinuousSignal signal = reconstructionTypes.get(reconstructionTypeComboBox.getValue())
                .apply(
                        (DiscreteSignal) signals.get(signalACDCComboBox.getValue()),
                        numOfSamples.getText().isEmpty() ? 0 : Integer.parseInt(numOfSamples.getText())
                );
        if (reconstructionSourceSignalComboBox.getValue() != null) {
            AlertBox.show(
                    "Signal mapping statistics",
                    signalFacade.calculateDacStats(
                                    signal,
                                    (ContinuousSignal) signals.get(reconstructionSourceSignalComboBox.getValue())
                            ).entrySet()
                            .stream()
                            .map(entry -> entry.getKey() + ": " + entry.getValue())
                            .collect(Collectors.joining("\n")),
                    Alert.AlertType.INFORMATION
            );
        }
        numOfSamples.clear();
        createSignalTab.accept(signal);
    }

    public void convolutionOperation() {
        AbstractSignal signal = signalFacade.convolution((DiscreteSignal) signals.get(signal1ConvolutionCorrelationComboBox.getValue()),
                (DiscreteSignal) signals.get(signal2ConvolutionCorrelationComboBox.getValue()));
        createSignalTab.accept(signal);
    }

    public void correlationOperation() {
        AbstractSignal signal = signalFacade.discreteSignalsCorrelation(
                (DiscreteSignal) signals.get(signal1ConvolutionCorrelationComboBox.getValue()),
                (DiscreteSignal) signals.get(signal2ConvolutionCorrelationComboBox.getValue()),
                discreteSignalsCorrelationTypes.get(correlationTypeComboBox.getValue())
        );
        createSignalTab.accept(signal);
    }

    public void filterOperation() {
        AbstractSignal signal = signalFacade.filter(
                (DiscreteSignal) signals.get(signalFilterComboBox.getValue()),
                passTypes.get(passComboBox.getValue()),
                windowTypes.get(windowComboBox.getValue()),
                Integer.parseInt(rankOfFilter.getText()),
                Double.parseDouble(cutOffFrequency.getText())
        );
        createSignalTab.accept(signal);
    }

    public void setCreateSignalTab(Consumer<AbstractSignal> createSignalTab) {
        this.createSignalTab = createSignalTab;
    }

    private boolean shouldSamplingButtonBeDisabled() {
        return !(signals.get(signalACDCComboBox.getValue()) instanceof ContinuousSignal) ||
                samplingFrequency.getText().isEmpty();
    }

    private boolean shouldQuantizationButtonBeDisabled() {
        return !(signals.get(signalACDCComboBox.getValue()) instanceof DiscreteSignal) ||
                numOfLevelsQuantization.getText().isEmpty() ||
                quantizationTypeComboBox.getValue() == null;
    }

    private boolean shouldReconstructButtonBeDisabled() {
        return !(signals.get(signalACDCComboBox.getValue()) instanceof DiscreteSignal) ||
                reconstructionTypeComboBox.getValue() == null ||
                (numOfSamples.getText().isEmpty() && reconstructionTypeComboBox.getValue().equals("Sinc"));
    }

    private boolean shouldFilterButtonBeDisabled() {
        return signalFilterComboBox.getValue() == null ||
                windowComboBox.getValue() == null ||
                passComboBox.getValue() == null ||
                rankOfFilter.getText().isEmpty() ||
                cutOffFrequency.getText().isEmpty();
    }

    public void onUpdateMathOperationsComboBox() {
        applyOperationButton.setDisable(
                signalOperationComboBox.getValue() == null ||
                        signal1ComboBox.getValue() == null ||
                        signal2ComboBox.getValue() == null
        );
    }

    public void onUpdateSignalACDCComboBox() {
        AbstractSignal signal = signals.get(signalACDCComboBox.getValue());
        samplingOperationHBox.setDisable(!(signal instanceof ContinuousSignal));
        quantizationOperationHBox.setDisable(!(signal instanceof DiscreteSignal));
        reconstructionOperationHBox.setDisable(!(signal instanceof DiscreteSignal));
    }

    public void onUpdateReconstructionTypeComboBox() {
        numOfSamples.setDisable(!reconstructionTypeComboBox.getValue().equals("Sinc"));
        reconstructOperationButton.setDisable(shouldReconstructButtonBeDisabled());
    }

    public void onUpdateConvolutionCorrelationOperationsComboBox() {
        convolutionOperationButton.setDisable(
                signal1ConvolutionCorrelationComboBox.getValue() == null ||
                        signal2ConvolutionCorrelationComboBox.getValue() == null
        );
        correlationOperationButton.setDisable(
                signal1ConvolutionCorrelationComboBox.getValue() == null ||
                        signal2ConvolutionCorrelationComboBox.getValue() == null ||
                        correlationTypeComboBox.getValue() == null
        );
    }

    public void onUpdateFilterOperationInputFields() {
        filterOperationButton.setDisable(shouldFilterButtonBeDisabled());
    }
}

package frontend;

import backend.SignalFacade;
import backend.signal.AbstractSignal;
import backend.signal.DiscreteFourierTransformedSignal;
import backend.signal.DiscreteSignal;
import backend.signal_serialize.SignalSerializeType;
import frontend.chart.ChartGenerator;
import frontend.classes.ClassTranslator;
import frontend.fields.FieldMapper;
import frontend.fields.FieldReader;
import frontend.file.FileChoose;
import frontend.units.TextFormatterFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.jfree.chart.ChartUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SignalTabController implements Initializable {
    public static final String RESOURCE = "SignalTab.fxml";
    private final SignalFacade facade = new SignalFacade();
    private final TextFormatterFactory formatterFactory = new TextFormatterFactory();
    private AbstractSignal signal;
    private Class<?> selectedComboBoxKey;
    @FXML
    private Button load;
    @FXML
    private Button save;
    @FXML
    private ComboBox<SignalSerializeType> loadSaveFileTypeComboBox;
    @FXML
    private GridPane parametersGrid;
    @FXML
    private ComboBox<String> signalTypes;
    @FXML
    private Button generateButton;
    @FXML
    private TabPane rightPanel;
    @FXML
    private ImageView amplitudeTimeChart;
    @FXML
    private ImageView amplitudeTimeImaginaryChart;
    @FXML
    private ImageView histogram;
    @FXML
    private Slider binNumberSlider;
    @FXML
    private ImageView imaginaryHistogram;
    @FXML
    private Slider binNumberImaginarySlider;
    @FXML
    private GridPane statisticsGrid;
    @FXML
    private ImageView amplitudeTimeChartModule;
    @FXML
    private ImageView amplitudeTimeImaginaryChartPhase;
    @FXML
    private Tab moduleAndPhaseTab;
    private final List<BiConsumer<String, AbstractSignal>> signalConsumers = new ArrayList<>();
    private String tabName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createParametersTextFields(facade.getDefaultSignal());
        Map<String, Class<?>> signals = facade.getPossibleSignals().stream().collect(
                Collectors.toMap(ClassTranslator::translatePascalCaseClassToText, value -> value)
        );
        signalTypes.getItems().addAll(signals.keySet());
        signalTypes.setOnAction(event -> {
            generateButton.setDisable(true);
            signal = null;
            Class<?> selectedKey = signals.get(signalTypes.getValue());
            createParametersTextFields(selectedKey);
            selectedComboBoxKey = selectedKey;
        });
        statisticsGrid.getChildren().clear();
        binNumberSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                FileInputStream input = new FileInputStream(getHistogramFileName((int) binNumberSlider.getValue()));
                histogram.setImage(new Image(input));
            } catch (FileNotFoundException ignored) {
                //ignored
            }
        });
        loadSaveFileTypeComboBox.getItems().addAll(SignalSerializeType.values());
        binNumberImaginarySlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                FileInputStream input = new FileInputStream(getHistogramFileName((int) binNumberImaginarySlider.getValue()));
                imaginaryHistogram.setImage(new Image(input));
            } catch (FileNotFoundException ignored) {
                //ignored
            }
        });
    }

    private void createParametersTextFields(Class<?> classDefinition) {
        createParametersTextFields(classDefinition, null);
    }

    private void createParametersTextFields(AbstractSignal signal) {
        createParametersTextFields(signal.getClass(), signal);
    }
    private void createParametersTextFields(Class<?> classDefinition, AbstractSignal signal) {
        List<String> names = FieldReader.getFieldNames(classDefinition);
        parametersGrid.getChildren().clear();
        for (int i = 0; i < names.size(); i++) {
            Group group = new Group();
            TextField textField = createGroupNumericalTextField();
            if (signal != null) {
                try {
                    textField.setText(
                            String.valueOf(
                                classDefinition.getMethod(
                                    "get"
                                            + names.get(i).substring(0, 1).toUpperCase()
                                            + names.get(i).substring(1)
                                ).invoke(signal)
                            )
                    );
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
                    //ignored
                }
            }
            Label label = createGroupLabel(FieldMapper.map(names.get(i)), textField);
            group.getChildren().addAll(textField, label);
            parametersGrid.addRow(i, group);
        }
    }

    public void createSignalInstance() throws IOException {
        rightPanel.setVisible(false);
        List<Double> values = getParamsTextFieldsStream()
                .map(textField -> Double.parseDouble(textField.getText()))
                .toList();
        signal = facade.getSignal(selectedComboBoxKey, values);

        createRightPanel(signal);

        notifyAllConsumers(tabName, signal);
        save.setDisable(loadSaveFileTypeComboBox.getValue() == null);
        load.setDisable(true);
    }

    private TextField createGroupNumericalTextField() {
        TextField textField = new TextField();
        textField.setLayoutX(334);
        textField.setTextFormatter(formatterFactory.createDecimalTextFormatter(
                textField, generateButton, this::shouldGenerateButtonBeDisabled
        ));
        return textField;
    }

    private Label createGroupLabel(String text, TextField labelFor) {
        return createGroupLabel(text, labelFor, 0);
    }
    private Label createGroupLabel(String text, TextField labelFor, int layoutX) {
        Label label = new Label(text);
        label.setLabelFor(labelFor);
        label.setLayoutY(4);
        label.setLayoutX(layoutX);
        return label;
    }

    private Stream<TextField> getParamsTextFieldsStream() {
        return parametersGrid.getChildren()
                .stream()
                .map(group -> (TextField) ((Group) group).getChildren().get(0));
    }

    private boolean shouldGenerateButtonBeDisabled() {
        if (selectedComboBoxKey == null) {
            return true;
        }
        for (TextField textField : getParamsTextFieldsStream().toList()) {
            if (textField.getText().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void addSignalConsumer(BiConsumer<String, AbstractSignal> signalConsumer) {
        this.signalConsumers.add(signalConsumer);
    }

    public void setTabName(String name) {
        this.tabName = name;
    }

    private void createStatistics(Map<String, Supplier<Double>> statistics) {
        statisticsGrid.getChildren().clear();
        int row = 0;
        for (Map.Entry<String, Supplier<Double>> statistic: statistics.entrySet()) {
            statisticsGrid.addRow(row, new Group(
                    createGroupLabel(statistic.getKey(), null),
                    createGroupLabel(String.valueOf(statistic.getValue().get()), null, 300)
            ));
            row++;
        }
    }

    public void setSignal(AbstractSignal signal) throws IOException {
        this.signal = signal;
        signalTypes.getSelectionModel().select(ClassTranslator.translatePascalCaseClassToText(signal.getClass()));
        createParametersTextFields(signal);
        signalTypes.setDisable(true);
        parametersGrid.setDisable(true);
        createRightPanel(signal);
        notifyAllConsumers(tabName, signal);
        save.setDisable(loadSaveFileTypeComboBox.getValue() == null);
        load.setDisable(true);
    }

    private void createRightPanel(AbstractSignal signal) throws IOException {
        for (int i = 5; i <= 20; i++) {
            ChartUtilities.saveChartAsPNG(
                    new File(getHistogramFileName(i)),
                    ChartGenerator.generateHistogram(
                            signal.getPoints(), i
                    ),
                    400,
                    220
            );
        }
        FileInputStream input = new FileInputStream(getHistogramFileName((int) binNumberSlider.getValue()));
        histogram.setImage(new Image(input));

        if (signal instanceof DiscreteFourierTransformedSignal) {
            ChartUtilities.saveChartAsPNG(
                    new File("chart.png"),
                    ChartGenerator.generateAmplitudeTimeChart(
                            signal.getPoints(),
                            true,
                            "Amplitude / frequency function - real part",
                            "frequency"
                    ),
                    400,
                    220
            );
            input = new FileInputStream("chart.png");
            amplitudeTimeChart.setImage(new Image(input));

            ChartUtilities.saveChartAsPNG(
                    new File("chart_imaginary.png"),
                    ChartGenerator.generateAmplitudeTimeChart(
                            ((DiscreteFourierTransformedSignal) signal).getImaginaryPartPoints(),
                            true,
                            "Amplitude / frequency function - imaginary part",
                            "frequency"
                    ),
                    400,
                    220
            );
            input = new FileInputStream("chart_imaginary.png");
            amplitudeTimeImaginaryChart.setImage(new Image(input));

            ChartUtilities.saveChartAsPNG(
                    new File("chart_module.png"),
                    ChartGenerator.generateAmplitudeTimeChart(
                            ((DiscreteFourierTransformedSignal) signal).getModulePoints(),
                            true,
                            "Amplitude / frequency function - module",
                            "frequency"
                    ),
                    400,
                    220
            );
            input = new FileInputStream("chart_module.png");
            amplitudeTimeChartModule.setImage(new Image(input));

            ChartUtilities.saveChartAsPNG(
                    new File("chart_phase.png"),
                    ChartGenerator.generateAmplitudeTimeChart(
                            ((DiscreteFourierTransformedSignal) signal).getPhasePoints(),
                            true,
                            "Amplitude / frequency function - phase",
                            "frequency"
                    ),
                    400,
                    220
            );
            input = new FileInputStream("chart_phase.png");
            amplitudeTimeImaginaryChartPhase.setImage(new Image(input));

            for (int i = 5; i <= 20; i++) {
                ChartUtilities.saveChartAsPNG(
                        new File(getImaginaryHistogramFileName(i)),
                        ChartGenerator.generateHistogram(
                                ((DiscreteFourierTransformedSignal) signal).getImaginaryPartPoints(), i
                        ),
                        400,
                        220
                );
            }
            input = new FileInputStream(getImaginaryHistogramFileName((int) binNumberImaginarySlider.getValue()));
            imaginaryHistogram.setImage(new Image(input));
            binNumberImaginarySlider.setVisible(true);
        } else {
            rightPanel.getTabs().remove(moduleAndPhaseTab);
            ChartUtilities.saveChartAsPNG(
                    new File("chart.png"),
                    ChartGenerator.generateAmplitudeTimeChart(
                            signal.getPoints(),
                            signal instanceof DiscreteSignal
                    ),
                    400,
                    220
            );
            input = new FileInputStream("chart.png");
            amplitudeTimeChart.setImage(new Image(input));
        }

        try {
            createStatistics(Map.of(
                    "Average", signal::getAverage,
                    "Absolute Average", signal::getAbsoluteAverage,
                    "Average Power", signal::getAveragePower,
                    "Variance", signal::getVariance,
                    "Effective value", signal::getEffectiveValue
            ));
        } catch (Exception ignored) {
            //ignored
        }

        rightPanel.setVisible(true);
    }

    public void loadSignal(ActionEvent actionEvent)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        String path = FileChoose.openChooser(
                "Choose file",
                actionEvent,
                loadSaveFileTypeComboBox.getValue()
        );
        if (path.isEmpty()) {
            return;
        }
        AbstractSignal loadedSignal = facade.readSignal(loadSaveFileTypeComboBox.getValue(), path);
        if (loadedSignal == null) {
            return;
        }
        setSignal(loadedSignal);
        load.setDisable(true);
    }

    public void saveSignal(ActionEvent actionEvent)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String path = FileChoose.saveChooser(
                "Choose file",
                actionEvent,
                loadSaveFileTypeComboBox.getValue()
        );
        if (path.isEmpty()) {
            return;
        }
        facade.writeSignal(loadSaveFileTypeComboBox.getValue(), signal, path);
    }

    private String getHistogramFileName(int number) {
        return "histogram" + number + ".png";
    }

    private String getImaginaryHistogramFileName(int number) {
        return "histogram_imaginary" + number + ".png";
    }

    private void notifyAllConsumers(String t, AbstractSignal signal) {
        for (BiConsumer<String, AbstractSignal> consumer : signalConsumers) {
            consumer.accept(t, signal);
        }
    }

    public void onUpdateLoadSaveFileTypeComboBox() {
        if (loadSaveFileTypeComboBox.getValue() == null) {
            return;
        }
        load.setDisable(signal != null);
        save.setDisable(signal == null);
    }
}
package frontend;

import backend.SignalFacade;
import backend.signal.AbstractSignal;
import backend.signal.DiscreteSignal;
import frontend.chart.ChartGenerator;
import frontend.classes.ClassTranslator;
import frontend.fields.FieldMapper;
import frontend.fields.FieldReader;
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
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainFormController implements Initializable {
    public static final String MAIN_FORM_RESOURCE = "MainForm.fxml";
    public static final String MAIN_FORM_TITLE = "Digital Signal Processing";
    private final SignalFacade facade = new SignalFacade();
    private AbstractSignal signal;
    private Class<?> selectedComboBoxKey;
    @FXML
    private GridPane parametersGrid;
    @FXML
    private ComboBox<String> signalTypes;
    @FXML
    private Button generateButton;
    @FXML
    private ImageView amplitudeTimeChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createParametersTextFields(facade.getDefaultSignal());
        Map<Class<?>, String> signals = facade.getPossibleSignals().stream().collect(
                Collectors.toMap(key -> key, ClassTranslator::translatePascalCaseClassToText)
        );
        signalTypes.getItems().addAll(signals.values());
        signalTypes.setOnAction(event -> {
            generateButton.setDisable(true);
            signal = null;
            Class<?> selectedKey = signals.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().equals(signalTypes.getValue()))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);
            createParametersTextFields(selectedKey);
            selectedComboBoxKey = selectedKey;
        });
    }

    public void createParametersTextFields(Class<?> classDefinition) {
        List<String> names = FieldReader.getFieldNames(classDefinition);
        parametersGrid.getChildren().clear();
        for (int i = 0; i < names.size(); i++) {
            Group group = new Group();
            TextField textField = createGroupNumericalTextField();
            Label label = createGroupLabel(FieldMapper.map(names.get(i)), textField);
            group.getChildren().addAll(textField, label);
            parametersGrid.addRow(i, group);
        }
    }

    public void createSignalInstance() throws IOException {
        List<Double> values = getParamsTextFieldsStream()
                .map(textField -> Double.parseDouble(textField.getText()))
                .toList();
        signal = facade.getSignal(selectedComboBoxKey, values);
        ChartUtilities.saveChartAsPNG(new File("chart.png"),
                ChartGenerator.generatePlot(signal.getAmplitudeFromTimeChartData(), signal instanceof DiscreteSignal),
                400, 220);
        FileInputStream input = new FileInputStream("chart.png");
        amplitudeTimeChart.setImage(new Image(input));
    }

    private TextField createGroupNumericalTextField() {
        TextField textField = new TextField();
        textField.setLayoutX(334);
        textField.setTextFormatter(new TextFormatter<>(text -> {
            String newText = text.getControlNewText().replace(",", ".");
            if (!newText.matches("-?([0-9]*[.])?[0-9]*")) {
                textField.clear();
                return null;
            }
            generateButton.setDisable(shouldGenerateButtonBeDisabled());
            return text;
        }));
        return textField;
    }

    private Label createGroupLabel(String text, TextField labelFor) {
        Label label = new Label(text);
        label.setLabelFor(labelFor);
        label.setLayoutY(4);
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
}
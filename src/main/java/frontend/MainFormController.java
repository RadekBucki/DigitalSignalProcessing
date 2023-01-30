package frontend;

import backend.SignalFacade;
import backend.signal.AbstractSignal;
import frontend.classes.ClassesTranslator;
import frontend.fields.FieldsMapper;
import frontend.fields.FieldsReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainFormController implements Initializable {
    public static final String MAIN_FORM_RESOURCE = "MainForm.fxml";
    public static final String MAIN_FORM_TITLE = "Digital Signal Processing";
    private final SignalFacade facade = new SignalFacade();

    public final List<TextField> list = new ArrayList<>();
    AbstractSignal signal;
    Class<?> selectedComboBoxKey;
    @FXML
    public GridPane parametersGrid;
    @FXML
    public ComboBox<String> signalTypes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createParametersTextFields(facade.getDefaultSignal());
        Map<Class<?>, String> signals = facade.getPossibleSignals().stream().collect(
                Collectors.toMap(
                        key -> key,
                        ClassesTranslator::translatePascalCaseClassToText
                )
        );
        signalTypes.getItems().addAll(signals.values());
        signalTypes.setOnAction(event -> {
            String selectedValue = signalTypes.getValue();
            for (Map.Entry<Class<?>, String> entry : signals.entrySet()) {
                if (selectedValue.equals(entry.getValue())) {
                    createParametersTextFields(entry.getKey());
                    selectedComboBoxKey = entry.getKey();
                    break;
                }
            }
        });
    }

    public void createParametersTextFields(Class<?> classInstance) {
        list.clear();
        List<String> names = FieldsReader.getFieldNames(classInstance);
        parametersGrid.getChildren().clear();
        for (int i = 0; i < names.size(); i++) {
            Group group = new Group();

            TextField textField = new TextField();
            textField.setLayoutX(334);
            textField.setTextFormatter(new TextFormatter<>(c -> {
                String newText = c.getControlNewText().replace(",", ".");
                if (!newText.matches("-?([0-9]*[.])?[0-9]*")) {
                    textField.clear();
                    return null;
                }
                return c;
            }));

            Label label = new Label(FieldsMapper.map(names.get(i)));
            label.setLabelFor(textField);
            label.setLayoutY(4);

            group.getChildren().addAll(textField, label);
            parametersGrid.addRow(i, group);
            list.add(textField);
        }
    }

    public void createSignalInstance() {
        List<Double> values = list.stream().map(
                textField -> Double.parseDouble(textField.getText())
        ).toList();
        signal = facade.getSignal(selectedComboBoxKey, values);
    }
}
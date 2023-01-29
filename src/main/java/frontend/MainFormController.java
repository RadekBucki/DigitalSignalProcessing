package frontend;

import backend.signal.continuous.UnitJump;
import frontend.fields.FieldsMapper;
import frontend.fields.FieldsReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {
    public static final String MAIN_FORM_RESOURCE = "MainForm.fxml";
    public static final String MAIN_FORM_TITLE = "Digital Signal Processing";
    @FXML
    public GridPane parametersGrid;

    public final List<TextField> list = new ArrayList<>();

    public MainFormController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> names = FieldsReader.getFieldNames(new UnitJump(1.0, 1.0, 1.0, 1.0));
        parametersGrid.getChildren().clear();
        for (int i = 0; i < names.size(); i++) {
            Group group = new Group();

            TextField textField = new TextField();
            textField.setLayoutX(334);

            Label label = new Label(FieldsMapper.map(names.get(i)));
            label.setLabelFor(textField);
            label.setLayoutY(4);

            group.getChildren().addAll(textField, label);
            parametersGrid.addRow(i, group);
            list.add(textField);
        }
    }
}
package frontend;

import backend.SignalFacade;
import backend.signal.AbstractSignal;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

public class SignalOperationFormController implements Initializable {
    public static final String SIGNAL_OPERATION_FORM_RESOURCE = "SignalOperationForm.fxml";
    @FXML
    private ComboBox<String> signalOperationComboBox;
    private final SignalFacade signalFacade = new SignalFacade();

    Map<BiFunction<AbstractSignal, AbstractSignal, AbstractSignal>, String> signalOperations = Map.of(
            signalFacade::add, "Add",
            signalFacade::subtract, "Subtract",
            signalFacade::multiply, "Multiply",
            signalFacade::divide, "Divide"
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signalOperationComboBox.getItems().addAll(signalOperations.values());
    }
}

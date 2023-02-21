package frontend.units;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.BooleanSupplier;

public class TextFormatterFactory {

    public TextFormatter createIntegerTextFormatter(TextField textField, Button button, BooleanSupplier shouldButtonDisabled) {
        return createTextFormatter(textField, button, shouldButtonDisabled, "\\d*");
    }

    public TextFormatter createDecimalTextFormatter(TextField textField, Button button, BooleanSupplier shouldButtonDisabled) {
        return createTextFormatter(textField, button, shouldButtonDisabled, "-?(\\d*[.])?\\d*");
    }
    private TextFormatter createTextFormatter(TextField textField, Button button, BooleanSupplier shouldButtonDisabled, String regex) {
        return new TextFormatter<>(text -> {
            String newText = text.getControlNewText().replace(",", ".");
            if (!newText.matches(regex)) {
                textField.clear();
                return null;
            }
            button.setDisable(shouldButtonDisabled.getAsBoolean());
            return text;
        });
    }
}

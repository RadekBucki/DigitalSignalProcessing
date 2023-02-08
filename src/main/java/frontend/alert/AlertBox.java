package frontend.alert;

import javafx.scene.control.Alert;
import javafx.stage.Modality;

public class AlertBox {
    /**
     * Method shows alert box with parameters.
     * @param title String
     * @param message String
     * @param alertType AlertType
     */
    public static void show(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.initModality(Modality.NONE);
        alert.show();
    }
}

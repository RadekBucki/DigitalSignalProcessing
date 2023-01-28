module cps {
    requires javafx.controls;
    requires javafx.fxml;
    requires jfreechart;
    requires java.datatransfer;
    requires java.desktop;

    opens frontend to javafx.fxml;
    exports frontend;
    exports backend;
    opens backend to javafx.fxml;
    exports backend.signal;
    opens backend.signal to javafx.fxml;
    exports backend.signal.discrete;
    opens backend.signal.discrete to javafx.fxml;
}
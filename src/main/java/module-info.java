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
    exports backend.signal_operation;
    exports frontend.file;
    exports backend.signal.discrete;
    opens backend.signal.discrete to javafx.fxml;
    opens frontend.file to javafx.fxml;
    exports frontend.chart;
    opens frontend.chart to javafx.fxml;
    exports backend.signal.continuous;
    opens backend.signal.continuous to javafx.fxml;
    opens backend.signal to javafx.fxml;
}
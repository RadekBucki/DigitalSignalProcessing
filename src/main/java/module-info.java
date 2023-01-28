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
    opens frontend.file to javafx.fxml;
    exports frontend.chart;
    opens frontend.chart to javafx.fxml;
}
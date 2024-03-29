module cps {
    requires javafx.controls;
    requires javafx.fxml;
    requires jfreechart;
    requires commons.math3;
    requires java.datatransfer;
    requires java.desktop;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    opens frontend to javafx.fxml;
    exports frontend;
    exports backend;
    opens backend to javafx.fxml;
    exports backend.signal;
    exports backend.signal_operation;
    exports backend.signal_operation.signal_reconstruction;
    exports frontend.file;
    exports backend.signal.discrete;
    opens backend.signal.discrete to javafx.fxml;
    opens frontend.file to javafx.fxml;
    exports frontend.chart;
    opens frontend.chart to javafx.fxml;
    exports backend.signal.continuous;
    opens backend.signal.continuous to javafx.fxml;
    opens backend.signal to javafx.fxml, com.fasterxml.jackson.databind;
    exports frontend.fields;
    opens frontend.fields to javafx.fxml;
    exports frontend.classes;
    opens frontend.classes to javafx.fxml;
    exports backend.signal_operation.window;
    exports backend.signal_operation.pass;
    exports backend.radar;
    exports frontend.units;
    opens frontend.units to javafx.fxml;
    exports backend.radar.model;
    exports backend.signal_serialize;
}
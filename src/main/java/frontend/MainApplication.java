package frontend;
import backend.SignalFacade;
import backend.signal.ContinuousSignal;
import backend.signal.DiscreteSignal;
import backend.signal.discrete.ImpulseNoise;
import backend.signal.discrete.UnitImpulse;
import frontend.chart.ChartGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jfree.chart.ChartUtilities;

import java.io.File;
import java.io.IOException;

/**
 * =========================================================
 * CPS (2023)
 * Kamil Bednarek 236500, Radosław Bucki 236507
 * =========================================================
 */

public class MainApplication extends Application{
    private static final String MAIN_APPLICATION_FXML = "MainApplication.fxml";
    public static final String TITLE = "Digital Signal Processing";
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(MAIN_APPLICATION_FXML));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }
}
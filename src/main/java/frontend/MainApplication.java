package frontend;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static frontend.MainFormController.*;
import static frontend.SignalOperationFormController.SIGNAL_OPERATION_FORM_RESOURCE;

/**
 * =========================================================
 * CPS (2023)
 * Kamil Bednarek 236500, Radosław Bucki 236507
 * =========================================================
 */

public class MainApplication extends Application {
    private static final int DEFAULT_CARD_NUMBER = 2;
    private static final String MAIN_APPLICATION_STYLE = "MainApplication.css";
    private final TabPane tabPane = new TabPane();
    @Override
    public void start(Stage stage) throws IOException {
        tabPane.getTabs().add(createSignalOperationTab());
        for (int i = 0; i < DEFAULT_CARD_NUMBER; i++) {
            Tab tab = createTab(i + 1);
            tabPane.getTabs().add(tab);
        }
        tabPane.getTabs().add(createAddTabButton());
        tabPane.getSelectionModel().select(tabPane.getTabs().get(1));

        Scene scene = new Scene(new VBox(createTitleLabel(), tabPane));
        scene.getStylesheets()
                .add(
                        Objects.requireNonNull(getClass().getResource(MAIN_APPLICATION_STYLE))
                                .toExternalForm()
                );
        stage.setTitle(MAIN_FORM_TITLE);
        stage.setScene(scene);
        stage.show();
    }

    private Tab createTab(int count) throws IOException {
        Tab tab = new Tab();
        tab.setText("Signal " + count);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MAIN_FORM_RESOURCE));
        tab.setContent(fxmlLoader.load());
        tab.setClosable(true);
        return tab;
    }

    private Tab createAddTabButton() {
        Tab addTab = new Tab();
        addTab.setText("+");
        addTab.setClosable(false);
        addTab.getStyleClass().add("highlighting-tab");
        addTab.setOnSelectionChanged(event -> {
            if (!addTab.isSelected()) {
                return;
            }
            Tab tab;
            try {
                tab = createTab(tabPane.getTabs().size() - 1);
            } catch (IOException e) {
                return;
            }
            tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab);
            tabPane.getSelectionModel().select(tab);
        });
        return addTab;
    }

    private Tab createSignalOperationTab() throws IOException {
        Tab tab = new Tab();
        tab.setText("Signal Operation");
        tab.setClosable(false);
        tab.getStyleClass().add("highlighting-tab");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(SIGNAL_OPERATION_FORM_RESOURCE));
        tab.setContent(fxmlLoader.load());
        return tab;
    }

    private Label createTitleLabel() {
        Label label = new Label(MAIN_FORM_TITLE);
        label.setFont(new Font(24.0));
        return label;
    }
    public static void main(String[] args) {
        launch();
    }
}
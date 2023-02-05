package frontend;
import backend.signal.AbstractSignal;
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

/**
 * =========================================================
 * CPS (2023)
 * Kamil Bednarek 236500, Radosław Bucki 236507
 * =========================================================
 */

public class MainApplication extends Application{
    private static final int DEFAULT_CARD_NUMBER = 2;
    private static final String MAIN_APPLICATION_STYLE = "MainApplication.css";
    public static final String TITLE = "Digital Signal Processing";
    private final TabPane tabPane = new TabPane();
    private SignalOperationTabController signalOperationTabController = null;
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws IOException {
        tabPane.getTabs().add(createSignalOperationTab());
        for (int i = 0; i < DEFAULT_CARD_NUMBER; i++) {
            Tab tab = createSignalTab(i + 1);
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
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }

    private Tab createSignalTab(int count) throws IOException {
        Tab tab = new Tab();
        String tabName = "Signal " + count;
        tab.setText(tabName);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(SignalTabController.RESOURCE));
        tab.setContent(fxmlLoader.load());
        tab.setClosable(true);
        SignalTabController signalTabController = fxmlLoader.getController();
        signalTabController.setSignalConsumer(signalOperationTabController::addOrUpdateSignal);
        signalTabController.setTabName(tabName);
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
                tab = createSignalTab(tabPane.getTabs().size() - 1);
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(SignalOperationTabController.RESOURCE));
        tab.setContent(fxmlLoader.load());
        signalOperationTabController = fxmlLoader.getController();
        signalOperationTabController.setCreateSignalTab(this::createSignalTabFromAbstractSignal);
        return tab;
    }

    private Label createTitleLabel() {
        Label label = new Label(TITLE);
        label.setFont(new Font(24.0));
        return label;
    }

    private void createSignalTabFromAbstractSignal(AbstractSignal signal) {
        try {
            Tab tab = new Tab();
            String tabName = "Signal " + (tabPane.getTabs().size() - 1);
            tab.setText(tabName);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(SignalTabController.RESOURCE));
            tab.setClosable(true);
            tab.setContent(fxmlLoader.load());
            SignalTabController signalTabController = fxmlLoader.getController();
            signalTabController.setSignalConsumer(signalOperationTabController::addOrUpdateSignal);
            signalTabController.setTabName(tabName);
            signalTabController.setSignal(signal);
            tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab);
            tabPane.getSelectionModel().select(tab);
        } catch (IOException ignored) {
            // ignored
        }
    }
}
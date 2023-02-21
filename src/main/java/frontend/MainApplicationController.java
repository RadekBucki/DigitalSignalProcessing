package frontend;

import backend.signal.AbstractSignal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainApplicationController implements Initializable {
    private static final int DEFAULT_CARD_NUMBER = 2;
    private int nextTabNumber = 1;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab plusTab;
    @FXML
    private SignalOperationTabController signalOperationTabFxmlController;
    @FXML
    private RadarTabController radarTabFxmlController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            plusTab.setOnSelectionChanged(event -> {
                try {
                    Tab tab = createSignalTab();
                    tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab);
                    tabPane.getSelectionModel().select(tab);
                } catch (IOException ignored) {
                    //ignored
                }
            });
            for (int i = 0; i < DEFAULT_CARD_NUMBER; i++) {
                Tab tab = createSignalTab();
                tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab);
            }
            tabPane.getSelectionModel().select(tabPane.getTabs().get(1));
            signalOperationTabFxmlController.setCreateSignalTab(this::createSignalTabFromAbstractSignal);
        } catch (IOException ignored) {
            //ignored
        }
    }

    private Tab createSignalTab() throws IOException {
        Tab tab = new Tab();
        String tabName = "Signal " + nextTabNumber;
        tab.setText(tabName);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(SignalTabController.RESOURCE));
        tab.setContent(fxmlLoader.load());
        tab.setClosable(true);
        tab.setOnClosed((event) -> {
            signalOperationTabFxmlController.addOrUpdateSignal(tabName, null);
            radarTabFxmlController.addOrUpdateSignal(tabName, null);
        });
        SignalTabController signalTabController = fxmlLoader.getController();
        signalTabController.addSignalConsumer(signalOperationTabFxmlController::addOrUpdateSignal);
        signalTabController.addSignalConsumer(radarTabFxmlController::addOrUpdateSignal);
        signalTabController.setTabName(tabName);
        nextTabNumber++;
        return tab;
    }

    private void createSignalTabFromAbstractSignal(AbstractSignal signal) {
        try {
            Tab tab = new Tab();
            String tabName = "Signal " + nextTabNumber;
            tab.setText(tabName);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(SignalTabController.RESOURCE));
            tab.setClosable(true);
            tab.setContent(fxmlLoader.load());
            tab.setOnClosed((event) -> signalOperationTabFxmlController.addOrUpdateSignal(tabName, null));
            SignalTabController signalTabController = fxmlLoader.getController();
            signalTabController.addSignalConsumer(signalOperationTabFxmlController::addOrUpdateSignal);
            signalTabController.setTabName(tabName);
            signalTabController.setSignal(signal);
            tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab);
            tabPane.getSelectionModel().select(tab);
            nextTabNumber++;
        } catch (IOException ignored) {
            // ignored
        }
    }
}

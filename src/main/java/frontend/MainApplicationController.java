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
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab plusTab;
    @FXML
    private SignalOperationTabController signalOperationTabFxmlController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            plusTab.setOnSelectionChanged(event -> {
                try {
                    Tab tab = createSignalTab(tabPane.getTabs().size() - 1);
                    tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab);
                    tabPane.getSelectionModel().select(tab);
                } catch (IOException ignored) {
                    //ignored
                }
            });
            for (int i = 0; i < DEFAULT_CARD_NUMBER; i++) {
                Tab tab = createSignalTab(i + 1);
                tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab);
            }
            tabPane.getSelectionModel().select(tabPane.getTabs().get(1));
            signalOperationTabFxmlController.setCreateSignalTab(this::createSignalTabFromAbstractSignal);
        } catch (IOException ignored) {
            //ignored
        }
    }

    private Tab createSignalTab(int count) throws IOException {
        Tab tab = new Tab();
        String tabName = "Signal " + count;
        tab.setText(tabName);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(SignalTabController.RESOURCE));
        tab.setContent(fxmlLoader.load());
        tab.setClosable(true);
        SignalTabController signalTabController = fxmlLoader.getController();
        signalTabController.setSignalConsumer(signalOperationTabFxmlController::addOrUpdateSignal);
        signalTabController.setTabName(tabName);
        return tab;
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
            signalTabController.setSignalConsumer(signalOperationTabFxmlController::addOrUpdateSignal);
            signalTabController.setTabName(tabName);
            signalTabController.setSignal(signal);
            tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab);
            tabPane.getSelectionModel().select(tab);
        } catch (IOException ignored) {
            // ignored
        }
    }
}
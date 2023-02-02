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
import static frontend.MainFormController.*;

/**
 * =========================================================
 * CPS (2023)
 * Kamil Bednarek 236500, Radosław Bucki 236507
 * =========================================================
 */

public class MainApplication extends Application {
    private static final int DEFAULT_CARD_NUMBER = 2;
    private final TabPane tabPane = new TabPane();
    @Override
    public void start(Stage stage) throws IOException {
        Label label = new Label(MAIN_FORM_TITLE);
        label.setFont(new Font(24.0));
        VBox vBox = new VBox(label, tabPane);
        for (int i = 0; i < DEFAULT_CARD_NUMBER; i++) {
            Tab tab = createTab(i + 1);
            tabPane.getTabs().add(tab);
        }
        tabPane.getTabs().add(createAddTabButton());

        Scene scene = new Scene(vBox);
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
        addTab.setOnSelectionChanged(event -> {
            if (!addTab.isSelected()) {
                return;
            }
            Tab tab;
            try {
                tab = createTab(tabPane.getTabs().size());
            } catch (IOException e) {
                return;
            }
            tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab);
            tabPane.getSelectionModel().select(tab);
        });
        return addTab;
    }
    public static void main(String[] args) {
        launch();
    }
}
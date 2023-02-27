package frontend.file;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import backend.signal_read_write.SignalReadWriteType;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Window;
import javafx.stage.FileChooser;

public class FileChoose {
    private static String lastUsedDir = "";
    private static final Map<SignalReadWriteType, String> extensionMap = Map.of(
            SignalReadWriteType.JSON, "json",
            SignalReadWriteType.BINARY, "bin"
    );

    private FileChoose() {
    }

    /**
     * Method opens dialog to choose location to save.
     * @param windowTitle String
     * @param actionEvent ActionEvent
     * @return String
     * @throws NoSuchMethodException exception
     * @throws InvocationTargetException exception
     * @throws IllegalAccessException exception
     */
    public static String saveChooser(String windowTitle, ActionEvent actionEvent, SignalReadWriteType type)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return choose(
                windowTitle,
                actionEvent,
                FileChooser.class.getMethod("showSaveDialog", Window.class),
                type
        );
    }

    /**
     * Method opens dialog to choose file to open.
     * @param windowTitle String
     * @param actionEvent ActionEvent
     * @return String
     * @throws NoSuchMethodException exception
     * @throws InvocationTargetException exception
     * @throws IllegalAccessException exception
     */
    public static String openChooser(String windowTitle, ActionEvent actionEvent, SignalReadWriteType type)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return choose(
                windowTitle,
                actionEvent,
                FileChooser.class.getMethod("showOpenDialog", Window.class),
                type
        );
    }

    /**
     * Method opens given type of dialog.
     * @param windowTitle String
     * @param actionEvent ActionEvent
     * @return String
     */
    private static String choose(
            String windowTitle,
            ActionEvent actionEvent,
            Method showDialog,
            SignalReadWriteType type
    ) throws InvocationTargetException, IllegalAccessException {
        String extension = extensionMap.get(type);
        FileChooser chooser = new FileChooser();
        chooser.setTitle(windowTitle);
        if (!lastUsedDir.isEmpty()) {
            chooser.setInitialDirectory(new File(lastUsedDir));
        }
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Signal (*." + extension + ")", "*." + extension)
        );
        File chosenFile = (File) showDialog.invoke(
                chooser,
                ((Button) actionEvent.getSource())
                        .getScene()
                        .getWindow()
        );
        if (chosenFile == null) {
            return "";
        }
        lastUsedDir = chosenFile.getAbsoluteFile().getParentFile().getAbsolutePath();
        return chosenFile.getAbsolutePath();
    }
}


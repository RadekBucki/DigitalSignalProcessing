package frontend;

import backend.Signal.AbstractSignal;
import backend.Signal.UniformlyDistributedNoise;

public class MainFormController {
    public static final String MAIN_FORM_RESOURCE = "MainForm.fxml";
    public static final String MAIN_FORM_TITLE = "CPS";

    public MainFormController() {
        AbstractSignal signal = new UniformlyDistributedNoise(10, 1, 1);
        System.out.println("DUPA");
    }
}
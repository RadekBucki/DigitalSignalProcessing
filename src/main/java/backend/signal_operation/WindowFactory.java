package backend.signal_operation;

import backend.signal_operation.window.*;

public class WindowFactory {
    public Window createRectangularWindow() {
        return new RectangularWindow();
    }

    public Window createHammingWindow() {
        return new HammingWindow();
    }

    public Window createHanningWindow() {
        return new HanningWindow();
    }

    public Window createBlackmanWindow() {
        return new BlackmanWindow();
    }
}

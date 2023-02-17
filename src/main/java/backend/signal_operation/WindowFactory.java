package backend.signal_operation;

import backend.signal_operation.window.*;

public class WindowFactory {
    public Window createRectangularWindow() {
        return new RectangularWindow();
    }

    public Window createHammingWindow(int M) {
        return new HammingWindow(M);
    }

    public Window createHanningWindow(int M) {
        return new HanningWindow(M);
    }

    public Window createBlackmanWindow(int M) {
        return new BlackmanWindow(M);
    }
}

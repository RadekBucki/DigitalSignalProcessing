# DigitalSignalProcessing

## Task 1 - Signal and noise generation

```plantuml
package Backend {
    abstract class AbstractSignal {
    }
    class Signal1
    class Signal2
    
    AbstractSignal <|-- Signal1
    AbstractSignal <|-- Signal2
    
    class SignalFactory {
        + createSignal1(params): AbstractSignal
        + createSignal2(params): AbstractSignal
    }
    
    SignalFactory ..> AbstractSignal
    
    class SignalFacade {
        + add(AbstractSignal,AbstractSignal): AbstractSignal
        + subtract(AbstractSignal,AbstractSignal): AbstractSignal
        + multiply(AbstractSignal,AbstractSignal): AbstractSignal
        + divide(AbstractSignal,AbstractSignal): AbstractSignal
    }
    
    class SignalOperationFactory {
        + createSignalAdd(): SignalAdd
        + createSignalSubtract(): SignalSubtract
        + createSignalMultiply(): SignalMultiply
        + createSignalDivide(): SignalDivide
    }
    
    SignalFacade --> SignalOperationFactory
    
    class SignalAdd
    class SignalSubtract
    class SignalMultiply
    class SignalDivide
    
    SignalOperationFactory ..> SignalAdd
    SignalOperationFactory ..> SignalSubtract
    SignalOperationFactory ..> SignalMultiply
    SignalOperationFactory ..> SignalDivide
}

package Frontend {
    class MainApplication
    class MainFormController
    class FileChoose {
        + saveChooser()
        + openChooser()
        - choose()
    }
    class ChartGenerator {
        + generatePlot()
        - formatAxis()
        - changeVisibility()
    }
    class AmplitudeFromTimeChartGenerator
    class HistogramChartGenerator
    
    MainApplication ..> MainFormController
    
    MainFormController ..> FileChoose
    MainFormController ..> ChartGenerator
    
    ChartGenerator <|-- AmplitudeFromTimeChartGenerator
    ChartGenerator <|-- HistogramChartGenerator
}
MainFormController ...> SignalFacade
MainFormController ...> SignalFactory
```
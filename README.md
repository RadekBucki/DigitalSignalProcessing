# DigitalSignalProcessing

## Task 1 - Signal and noise generation

```plantuml
package Backend {
    class SignalFactory {
        + createSignal1(params): AbstractSignal
        + createSignal2(params): AbstractSignal
    }
    SignalFactory ...> AbstractSignal
    
    abstract class AbstractSignal {
    }
    class UniformlyDistributedNoise
    class GaussianNoise
    class SinusoidalSignal
    class OneHalfRectifiedSinusoidalSignal
    class TwoHalfRectifiedSinusoidalSignal
    class RectangularSignal
    class SymmetricalRectangularSignal
    class TriangleSignal
    class UnitJump
    class ImpulseNoise
    
    AbstractSignal <|-- UniformlyDistributedNoise
    AbstractSignal <|-- GaussianNoise
    AbstractSignal <|-- SinusoidalSignal
    AbstractSignal <|-- OneHalfRectifiedSinusoidalSignal
    AbstractSignal <|-- TwoHalfRectifiedSinusoidalSignal
    AbstractSignal <|-- RectangularSignal
    AbstractSignal <|-- SymmetricalRectangularSignal
    AbstractSignal <|-- TriangleSignal
    AbstractSignal <|-- UnitJump
    AbstractSignal <|-- ImpulseNoise
    
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
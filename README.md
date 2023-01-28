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
        - function: Function
        - A: double
        - t1: double
        - d: double
        + getAverage()
        + getAbsoluteAverage()
        + getEffectiveValue()
        + getVariance()
        + getMeanSpeed()
        + getAmplitudeFromTimeChartData()
        + getHistogramData()
    }
    
    class AbstractContinuousSignal {
    }
    class AbstractDiscreteSignal {
    }
    
    AbstractSignal <|-- AbstractContinuousSignal
    AbstractSignal <|-- AbstractDiscreteSignal
    
    class UniformlyDistributedNoise
    class GaussianNoise
    class SinusoidalSignal
    class OneHalfRectifiedSinusoidalSignal
    class TwoHalfRectifiedSinusoidalSignal
    class RectangularSignal
    class SymmetricalRectangularSignal
    class TriangleSignal
    class UnitJump
    class UnitImpulse
    class ImpulseNoise
    
    AbstractContinuousSignal <|-- UniformlyDistributedNoise
    AbstractContinuousSignal <|-- GaussianNoise
    AbstractContinuousSignal <|-- SinusoidalSignal
    AbstractContinuousSignal <|-- OneHalfRectifiedSinusoidalSignal
    AbstractContinuousSignal <|-- TwoHalfRectifiedSinusoidalSignal
    AbstractContinuousSignal <|-- RectangularSignal
    AbstractContinuousSignal <|-- SymmetricalRectangularSignal
    AbstractContinuousSignal <|-- TriangleSignal
    AbstractContinuousSignal <|-- UnitJump
    
    AbstractDiscreteSignal <|-- UnitImpulse
    AbstractDiscreteSignal <|-- ImpulseNoise
    
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
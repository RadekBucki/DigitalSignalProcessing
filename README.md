# DigitalSignalProcessing

## Task 1 - Signal and noise generation

```plantuml
package Backend {
    class SignalFactory {
        + createUniformlyDistributedNoise()(): AbstractSignal
        + createGaussianNoise(): AbstractSignal
        + createSinusoidalSignal(): AbstractSignal
        + createOneHalfRectifiedSinusoidalSignal(): AbstractSignal
        + createTwoHalfRectifiedSinusoidalSignal(): AbstractSignal
        + createRectangularSignal(): AbstractSignal
        + createSymmetricalRectangularSignal(): AbstractSignal
        + createTriangleSignal(): AbstractSignal
        + createUnitJump(): AbstractSignal
    }
    SignalFactory ...> AbstractSignal
    
    abstract class AbstractSignal {
        - function: Function
        - A: double
        + {abstract} getAverage()
        + {abstract} getAbsoluteAverage()
        + {abstract} getEffectiveValue()
        + {abstract} getVariance()
        + {abstract} getMeanSpeed()
        + getAmplitudeFromTimeChartData()
        + getHistogramData()
    }
    
    abstract class AbstractContinuousSignal {
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
    abstract class AbstractDiscreteSignal {
        - f: double
        + getAverage()
        + getAbsoluteAverage()
        + getEffectiveValue()
        + getVariance()
        + getMeanSpeed()
        + getAmplitudeFromTimeChartData()
        + getHistogramData()
    }
    
    AbstractSignal <|-- AbstractContinuousSignal
    AbstractSignal <|-- AbstractDiscreteSignal
    
    class UniformlyDistributedNoise
    class GaussianNoise
    class SinusoidalSignal {
        - T: double
    }
    class OneHalfRectifiedSinusoidalSignal {
        - T: double
    }
    class TwoHalfRectifiedSinusoidalSignal {
        - T: double
    }
    class RectangularSignal {
        - T: double
        - kw: double
    }
    class SymmetricalRectangularSignal {
        - T: double
        - kw: double
    }
    class TriangleSignal {
        - T: double
        - kw: double
    }
    class UnitJump {
        - ts: double
    }
    class UnitImpulse {
        - ns: double
        - n1: double
        - l: double
    }
    class ImpulseNoise {
        - t1: double
        - d: double
        - p: double
    }
    
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
    
    class SignalAdd {
        + add(AbstractSignal, AbstractSignal): AbstractSignal
    }
    class SignalSubtract {
        + subtract(AbstractSignal, AbstractSignal): AbstractSignal
    }
    class SignalMultiply {
        + multiply(AbstractSignal, AbstractSignal): AbstractSignal
    }
    class SignalDivide {
        + divide(AbstractSignal, AbstractSignal): AbstractSignal
    }
    
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
MainFormController ....> SignalFacade
MainFormController ....> SignalFactory
```
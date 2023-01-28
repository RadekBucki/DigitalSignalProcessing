# DigitalSignalProcessing

## Task 1 - Signal and noise generation

```plantuml
package backend {
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
    
    package signal {
        SignalFactory ....> AbstractSignal
        abstract class AbstractSignal {
            # points: double[][]
            # A: double
            + {abstract} getAverage()
            + {abstract} getAbsoluteAverage()
            + {abstract} getEffectiveValue()
            + {abstract} getVariance()
            + {abstract} getMeanSpeed()
            + getAmplitudeFromTimeChartData()
            + getHistogramData()
        }
        
        class ContinuousSignal {
            # t1: double
            # d: double
            + getAverage()
            + getAbsoluteAverage()
            + getEffectiveValue()
            + getVariance()
            + getMeanSpeed()
            + getAmplitudeFromTimeChartData()
            + getHistogramData()
        }
        class DiscreteSignal {
            # f: double
            + getAverage()
            + getAbsoluteAverage()
            + getEffectiveValue()
            + getVariance()
            + getMeanSpeed()
            + getAmplitudeFromTimeChartData()
            + getHistogramData()
        }
        
        AbstractSignal <|-- ContinuousSignal
        AbstractSignal <|-- DiscreteSignal
        
        package continuous {
            class UniformlyDistributedNoise
            class GaussianNoise
            class SinusoidalSignal {
                # T: double
            }
            class OneHalfRectifiedSinusoidalSignal {
                # T: double
            }
            class TwoHalfRectifiedSinusoidalSignal {
                # T: double
            }
            class RectangularSignal {
                # T: double
                # kw: double
            }
            class SymmetricalRectangularSignal {
                # T: double
                # kw: double
            }
            class TriangleSignal {
                # T: double
                # kw: double
            }
            class UnitJump {
                # ts: double
            }
        }
        package discrete {
            class UnitImpulse {
                # ns: double
                # n1: double
                # l: double
            }
            class ImpulseNoise {
                # t1: double
                # d: double
                # p: double
            }
        }
        
        ContinuousSignal <|-- UniformlyDistributedNoise
        ContinuousSignal <|-- GaussianNoise
        ContinuousSignal <|-- SinusoidalSignal
        ContinuousSignal <|-- OneHalfRectifiedSinusoidalSignal
        ContinuousSignal <|-- TwoHalfRectifiedSinusoidalSignal
        ContinuousSignal <|-- RectangularSignal
        ContinuousSignal <|-- SymmetricalRectangularSignal
        ContinuousSignal <|-- TriangleSignal
        ContinuousSignal <|-- UnitJump
        
        DiscreteSignal <|-- UnitImpulse
        DiscreteSignal <|-- ImpulseNoise
    }
    
    class SignalFacade {
        + add(AbstractSignal,AbstractSignal): AbstractSignal
        + subtract(AbstractSignal,AbstractSignal): AbstractSignal
        + multiply(AbstractSignal,AbstractSignal): AbstractSignal
        + divide(AbstractSignal,AbstractSignal): AbstractSignal
    }
    
    class SignalOperationFactory {
        + createSignalAdd(): AbstractSignalOperation
        + createSignalSubtract(): AbstractSignalOperation
        + createSignalMultiply(): AbstractSignalOperation
        + createSignalDivide(): AbstractSignalOperation
    }
    
    SignalFacade ---> SignalOperationFactory
    
    package signal_operation {
        abstract class AbstractSignalOperation {
            + execute(AbstractSignal, AbstractSignal): AbstractSignal
            # {abstract} operation(double, double): double
        }
        class SignalAdd {
            # operation(double, double): double
        }
        class SignalSubtract {
            # operation(double, double): double
        }
        class SignalMultiply {
            # operation(double, double): double
        }
        class SignalDivide {
            # operation(double, double): double
        }
    }
    
    AbstractSignalOperation <|-- SignalAdd
    AbstractSignalOperation <|-- SignalSubtract
    AbstractSignalOperation <|-- SignalMultiply
    AbstractSignalOperation <|-- SignalDivide
    
    SignalOperationFactory ..> AbstractSignalOperation
}

package frontend {
    class MainApplication
    class MainFormController
    package file {
        class FileChoose {
            + saveChooser()
            + openChooser()
            - choose()
        }
    }
    package chart {
        class ChartGenerator {
            + generatePlot()
            - formatAxis()
            - changeVisibility()
        }
        class AmplitudeFromTimeChartGenerator
        class HistogramChartGenerator
    }
    
    MainApplication ..> MainFormController
    
    MainFormController ..> FileChoose
    MainFormController ..> ChartGenerator
    
    ChartGenerator <|-- AmplitudeFromTimeChartGenerator
    ChartGenerator <|-- HistogramChartGenerator
}
MainFormController ....> SignalFacade
MainFormController ....> SignalFactory
```
# DigitalSignalProcessing

## Task 1 - Signal and noise generation

```plantuml
package backend {
    class SignalFactory {
        + createUniformlyDistributedNoise(): AbstractSignal
        + createGaussianNoise(): AbstractSignal
        + createSinusoidalSignal(): AbstractSignal
        + createOneHalfRectifiedSinusoidalSignal(): AbstractSignal
        + createTwoHalfRectifiedSinusoidalSignal(): AbstractSignal
        + createRectangularSignal(): AbstractSignal
        + createSymmetricalRectangularSignal(): AbstractSignal
        + createTriangleSignal(): AbstractSignal
        + createUnitJump(): AbstractSignal
        + createContinuousSignal(): AbstractSignal
        + createDiscreteSignal(): AbstractSignal
        + getSignal(Class,Double[]): AbstractSignal
    }
    
    package signal {
        SignalFactory ....> AbstractSignal
        abstract class AbstractSignal {
            # points: double[][]
            # A: double
            # d: double
            + {abstract} getAverage()
            + {abstract} getAbsoluteAverage()
            + {abstract} getEffectiveValue()
            + {abstract} getVariance()
            + {abstract} getMeanSpeed()
            + getAmplitudeFromTimeChartData()
            + getHistogramData()
            + getD()
            + getA()
        }
        
        class ContinuousSignal {
            # t1: double
            # t2: double
            + {abstract} calculatePointValue()
            + calculateAllPoints()
            + getAverage()
            + getAbsoluteAverage()
            + getAveragePower()
            + getVariance()
            + getEffectiveValue()
            + getAmplitudeFromTimeChartData()
            + getHistogramData()
            + getT1()
            + getT2()
        }
        class DiscreteSignal {
            # f: double
            # n1: int
            # n2: int
            + getAverage()
            + getAbsoluteAverage()
            + getAveragePower()
            + getVariance()
            + getEffectiveValue()
            + getAmplitudeFromTimeChartData()
            + getHistogramData()
            + getN1()
            + getN2()
        }
        
        AbstractSignal <|-- ContinuousSignal
        AbstractSignal <|-- DiscreteSignal
        
        package continuous {
            class UniformlyDistributedNoise {
                + calculatePointValue()
            }
            class ContinuousNoise {
                + getAverage()
                + getAbsoluteAverage()
                + getAveragePower()
                + getVariance()
                + getEffectiveValue()
            }
            class GaussianNoise {
                + calculatePointValue()
            }
            class SinusoidalSignal {
                # T: double
                + calculatePointValue()
            }
            class OneHalfRectifiedSinusoidalSignal {
                # T: double
                + calculatePointValue()
            }
            class TwoHalfRectifiedSinusoidalSignal {
                # T: double
                + calculatePointValue()
            }
            class RectangularSignal {
                # T: double
                # kw: double
                + calculatePointValue()
            }
            class SymmetricalRectangularSignal {
                # T: double
                # kw: double
                + calculatePointValue()
            }
            class TriangleSignal {
                # T: double
                # kw: double
                + calculatePointValue()
            }
            class UnitJump {
                # ts: double
                + calculatePointValue()
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
                # p: double
            }
        }
        
        ContinuousSignal <|-- SinusoidalSignal
        ContinuousSignal <|-- OneHalfRectifiedSinusoidalSignal
        ContinuousSignal <|-- TwoHalfRectifiedSinusoidalSignal
        ContinuousSignal <|-- RectangularSignal
        ContinuousSignal <|-- SymmetricalRectangularSignal
        ContinuousSignal <|-- TriangleSignal
        ContinuousSignal <|-- UnitJump
        ContinuousSignal <|-- ContinuousNoise
        
        ContinuousNoise <|-- GaussianNoise
        ContinuousNoise <|-- UniformlyDistributedNoise
        
        DiscreteSignal <|-- UnitImpulse
        DiscreteSignal <|-- ImpulseNoise
    }
    
    class SignalFacade {
        + add(AbstractSignal,AbstractSignal): AbstractSignal
        + subtract(AbstractSignal,AbstractSignal): AbstractSignal
        + multiply(AbstractSignal,AbstractSignal): AbstractSignal
        + divide(AbstractSignal,AbstractSignal): AbstractSignal
        + getSignalFactory(): SignalFactory
        + getSignal(Class,Double[]): AbstractSignal
        + getDefaultSignal(): AbstractSignal
        + getPossibleSignals(): AbstractSignal[]
    }
    
    class SignalOperationFactory {
        + createSignalAdd(): AbstractSignalOperation
        + createSignalSubtract(): AbstractSignalOperation
        + createSignalMultiply(): AbstractSignalOperation
        + createSignalDivide(): AbstractSignalOperation
    }
    
    SignalFacade ---> SignalOperationFactory
    SignalFacade ---> SignalFactory
    
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
    
    AbstractSignalOperation ---> SignalFactory
    
    SignalOperationFactory ..> AbstractSignalOperation
}

package frontend {
    class MainApplication {
        - createSignalTab()
        - createAddTabButton()
        - createSignalOperationTab()
        - createTitleLabel()
    }
    class SignalTabController {
        - createGroupLabel()
        - shouldGenerateButtonBeDisabled()
        - parametersTextFields()
        + createSignalInstance()
    }
    class SignalOperationTabController {
        + addOrUpdateSignal()
        + applyOperation()
    }
    package file {
        class FileChoose {
            + {static} saveChooser()
            + {static} openChooser()
            - {static} choose()
        }
    }
    package chart {
        class ChartGenerator {
            + {static} generatePlot()
            - {static} formatAxis()
            - {static} changeVisibility()
        }
        class AmplitudeFromTimeChartGenerator
        class HistogramChartGenerator
    }
    package classes {
        class ClassTranslator {
            + {static} translatePascalCaseClassToText(Class): String
        }
    }
    package fields {
        class FieldReader {
            + {static} getFieldNames(Class): String[]
        }
        class FieldMapper {
            + {static} getFieldNames(String): String
        }
    }
    
    MainApplication o---> SignalTabController
    MainApplication o--> SignalOperationTabController
    
    SignalTabController ..> FileChoose
    SignalTabController ..> ChartGenerator
    SignalTabController ..> ClassTranslator
    SignalTabController ..> FieldMapper
    SignalTabController ..> FieldReader
    
    ChartGenerator <|-- AmplitudeFromTimeChartGenerator
    ChartGenerator <|-- HistogramChartGenerator
}
SignalTabController ....> SignalFacade
SignalOperationTabController ....> SignalFacade
```
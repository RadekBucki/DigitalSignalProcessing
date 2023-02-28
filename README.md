# DigitalSignalProcessing

## Task 1 - Signal and noise generation
## Frontend
```plantuml
package frontend {
    class MainApplication {
    }
    class MainApplicationController {
        - createSignalTab()
        - createSignalTabFromAbstractSignal()
    }
    class SignalTabController {
        - createGroupLabel()
        - shouldGenerateButtonBeDisabled()
        - parametersTextFields()
        + createSignalInstance()
        + saveSignal()
        + loadSignal()
        + setSignal()
        + onUpdateLoadSaveFileTypeComboBox()
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
            + {static} generateAmplitudeTimeChart()
            + {static} generateHistogram()
            - {static} formatAxis()
            - {static} changeVisibility()
        }
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
    
    MainApplicationController o---> SignalTabController
    MainApplicationController o--> SignalOperationTabController
    MainApplication o--> MainApplicationController
    
    SignalTabController ..> FileChoose
    SignalTabController ..> ChartGenerator
    SignalTabController ..> ClassTranslator
    SignalTabController ..> FieldMapper
    SignalTabController ..> FieldReader
}
package backend {
    class SignalFacade {
        + add(AbstractSignal,AbstractSignal): AbstractSignal
        + subtract(AbstractSignal,AbstractSignal): AbstractSignal
        + multiply(AbstractSignal,AbstractSignal): AbstractSignal
        + divide(AbstractSignal,AbstractSignal): AbstractSignal
        + getSignalFactory(): SignalFactory
        + getSignal(Class,Double[]): AbstractSignal
        + getDefaultSignal(): AbstractSignal
        + getPossibleSignals(): AbstractSignal[]
        + writeSignal(AbstractSignal, String)
        + readSignal(String): AbstractSignal
    }
    package signal_serialize {
       
        enum SignalSerializeType {
            JSON
            BINARY
        }
    }
}
SignalTabController ....> SignalFacade
SignalTabController ....> SignalSerializeType
SignalOperationTabController ....> SignalFacade
```
## Backend
### Polymorphic signal hierarchy
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
    
    class Rounder {
        + round(double): double
        + floor(double): double
    }
    AbstractSignal ..> Rounder
    
    package signal {
        SignalFactory ....> AbstractSignal
        abstract class AbstractSignal {
            # points: double[][]
            # A: double
            # d: double
            + {abstract} calculatePointValue()
            + {abstract} getAverage()
            + {abstract} getAbsoluteAverage()
            + {abstract} getEffectiveValue()
            + {abstract} getVariance()
            + {abstract} getMeanSpeed()
            + getPoints()
            + getHistogramData()
            + getD()
            + getA()
        }
        
        class ContinuousSignal {
            # t1: double
            # t2: double
            - function: Function
            + calculatePointValue()
            + calculateAllPoints()
            + getAverage()
            + getAbsoluteAverage()
            + getAveragePower()
            + getVariance()
            + getEffectiveValue()
            + getT1()
            + getT2()
            + getFunction()
            + setFunction()
        }
        class DiscreteSignal {
            # f: double
            # n1: int
            # n2: int
            + calculatePointValue()
            + getAverage()
            + getAbsoluteAverage()
            + getAveragePower()
            + getVariance()
            + getEffectiveValue()
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
}
```
### Signal operations
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
    class SignalFacade {
        + add(AbstractSignal,AbstractSignal): AbstractSignal
        + subtract(AbstractSignal,AbstractSignal): AbstractSignal
        + multiply(AbstractSignal,AbstractSignal): AbstractSignal
        + divide(AbstractSignal,AbstractSignal): AbstractSignal
        + getSignalFactory(): SignalFactory
        + getSignal(Class,Double[]): AbstractSignal
        + getDefaultSignal(): AbstractSignal
        + getPossibleSignals(): AbstractSignal[]
        + writeSignal(AbstractSignal, String)
        + readSignal(String): AbstractSignal
    }
    
    class SignalOperationFactory {
        + createSignalAdd(): AbstractSignalOperation
        + createSignalSubtract(): AbstractSignalOperation
        + createSignalMultiply(): AbstractSignalOperation
        + createSignalDivide(): AbstractSignalOperation
    }
    
    SignalFacade ---> SignalOperationFactory
    SignalFacade ---> SignalFactory
    
    class Rounder {
        + round(double): double
        + floor(double): double
    }
    AbstractSignalOperation ..> Rounder
    
    package signal_operation {
        abstract class AbstractSignalOperation {
            + execute(AbstractSignal, AbstractSignal): AbstractSignal
            # {abstract} operation(double, double): double
            # {abstract} operation(Function, Function): Function
        }
        class SignalAdd {
            # operation(double, double): double
            # operation(Function, Function): Function
        }
        class SignalSubtract {
            # operation(double, double): double
            # operation(Function, Function): Function
        }
        class SignalMultiply {
            # operation(double, double): double
            # operation(Function, Function): Function
        }
        class SignalDivide {
            # operation(double, double): double
            # operation(Function, Function): Function
        }
    }
    
    AbstractSignalOperation <|-- SignalAdd
    AbstractSignalOperation <|-- SignalSubtract
    AbstractSignalOperation <|-- SignalMultiply
    AbstractSignalOperation <|-- SignalDivide
    
    AbstractSignalOperation ---> SignalFactory
    
    SignalOperationFactory ..> AbstractSignalOperation
    
    package signal_serialize {
        interface SignalSerializer {
            + write(AbstractSignal, String)
            + read(String): AbstractSignal
        }
        class SignalBinarySerializer implements SignalSerializer {
            + write(AbstractSignal, String)
            + read(String): AbstractSignal
        }
        class SignalJsonSerializer implements SignalSerializer {
            + write(AbstractSignal, String)
            + read(String): AbstractSignal
        }
        enum SignalSerializeType {
            JSON
            BINARY
        }
        class SignalSerializerFactory {
            + createSignalSerializer(SignalSerializeType): SignalSerializer
            + createJsonSerializer(): SignalSerializer
            + createBinarySerializer(): SSignalSerializer
        }
        SignalSerializerFactory ..> SignalSerializer
        SignalSerializerFactory ..> SignalSerializeType
    }
    SignalFacade ...> SignalSerializerFactory
}
```

## Task 2 - Quantization, sampling and reconstruction

```plantuml
package backend {    
    class SignalFacade {
        + sampling(ContinuousSignal, double): AbstractSignal
        + quantizationWithTruncation(DiscreteSignal, int): AbstractSignal
        + quantizationWithRounding(DiscreteSignal, int): AbstractSignal
        + reconstructZeroOrderHold((DiscreteSignal): ContinuousSignal
        + reconstructFirstOrderHold((DiscreteSignal): ContinuousSignal
        + reconstructSinc((DiscreteSignal): ContinuousSignal
        + calculateDacStats(ContinuousSignal, ContinuousSignal): double[]
    }
    
    class SignalOperationFactory {
        + createAdc(): Adc
        + createDac(): Dac
    }
    
    package signal_operation {
        class Dac {
            + calculateStats(ContinuousSignal, ContinuousSignal): double[]
            + reconstructZeroOrderHold((DiscreteSignal): ContinuousSignal
            + reconstructFirstOrderHold((DiscreteSignal): ContinuousSignal
            + reconstructSinc((DiscreteSignal): ContinuousSignal
            - reconstruct(DiscreteSignal, ReconstructMethod): ContinuousSignal
            - calculateMSE(ContinuousSignal, ContinuousSignal): double
            - calculateSNR(ContinuousSignal, ContinuousSignal): double
            - calculatePSNR(ContinuousSignal, ContinuousSignal): double
            - calculateMD(ContinuousSignal, ContinuousSignal): double
        }
        class ReconstructMethodFactory {
            + createZeroOrderHold(): ReconstructMethod
            + createFirstOrderHold(): ReconstructMethod
            + createSinc(): ReconstructMethod
        }
    
        package signal_reconstruction {
            interface ReconstructMethod {
                + reconstruct(DiscreteSignal): ContinuousSignal
            }
            class ZeroOrderHold {
                + reconstruct(DiscreteSignal): ContinuousSignal
            }
            class FirstOrderHold {
                + reconstruct(DiscreteSignal): ContinuousSignal
            }
            class Sinc {
                + reconstruct(DiscreteSignal): ContinuousSignal
            }
        }
        
        class Adc {
            + sampling(ContinuousSignal, double): AbstractSignal
            + quantizationWithTruncation(DiscreteSignal, int): AbstractSignal
            + quantizationWithRounding(DiscreteSignal, int): AbstractSignal
            - quantization(QuantizationMethod,DiscreteSignal,int)
        }
    
        class QuantizationMethodFactory {
            + createQuantizationWithTruncation(): QuantizationMethod
            + createQuantizationWithRounding(): QuantizationMethod
        }
        
        package signal_quantization {
            interface QuantizationMethod {
                + quantize(double[], double): double
            }
            class QuantizationWithTruncation {
                + quantize(double[], double): double
            }
            class QuantizationWithRounding {
                + quantize(double[], double): double
            }
        }
    }
    
    QuantizationMethod <|.. QuantizationWithTruncation
    QuantizationMethod <|.. QuantizationWithRounding
    Adc ---> QuantizationMethodFactory
    QuantizationMethodFactory ..> QuantizationMethod
    
    ReconstructMethod <|.. ZeroOrderHold
    ReconstructMethod <|.. FirstOrderHold
    ReconstructMethod <|.. Sinc
    
    SignalFacade ---> SignalOperationFactory
    SignalOperationFactory ..> Dac
    SignalOperationFactory ..> Adc
    Dac ---> ReconstructMethodFactory
    ReconstructMethodFactory ..> ReconstructMethod
}

package frontend {
    class SignalOperationTabController {
        + addOrUpdateSignal()
        + mathOperation()
        + samplingOperation()
        + quantizationOperation()
        + reconstructOperation()
        - shouldSamplingButtonBeDisabled()
        - shouldQuantizationButtonBeDisabled()
        - shouldReconstructButtonBeDisabled()
        - shouldReconstructButtonBeDisabled()
        + onUpdateMathOperationsComboBox()
        + onUpdateSignalACDCComboBox()
        + onUpdateReconstructionTypeComboBox()
    }
    package alert {
        class AlertBox {
            + show(String,String,AlertType)
        }
    }
    SignalOperationTabController .> AlertBox
}
SignalOperationTabController ....> SignalFacade
```
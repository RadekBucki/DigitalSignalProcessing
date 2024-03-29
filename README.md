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
        + {static} round(double): double
        + {static} floor(double): double
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
        + {static} round(double): double
        + {static} floor(double): double
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
        + onUpdateConvolutionOperationsComboBox()
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
# Task 3 - convolution, filtering, correlation
## Frontend
```plantuml
package backend {
    class SignalFacade {
        + convolution(DiscreteSignal, DiscreteSignal): DiscreteSignal
        + filter(DiscreteSignal, PassType, WindowType, int, double): DiscreteSignal
        + discreteSignalsCorrelation(DiscreteSignal, DiscreteSignal, DiscreteSignalsCorrelationType): double
        + startRadar(double, int, double, double, double, ContinuousSignal, double, double, double, double, double, double): Radar
    }
    package signal_operation {
        enum DiscreteSignalsCorrelationType {
            + DIRECT
            + USING_CONVOLUTION
        }
        enum WindowType {
            + RECTANGULAR
            + BLACKMAN
            + HAMMING
            + HANNING
        }
        enum PassType {
            + LOW_PASS
            + HIGH_PASS
            + BAND_PASS
        }
    }
}

package frontend {
    class SignalOperationTabController {
        + convolutionOperation()
        + correlationOperation()
        + filterOperation()
        + onUpdateConvolutionCorrelationOperationsComboBox()
        + shouldFilterButtonBeDisabled()
        + onUpdateFilterOperationInputFields()
    }
    class RadarTabController {
        + addOrUpdateSignal()
        + onUpdateRadarInitData()
        + startRadar()
        - shouldRadarStartButtonBeDisabled(): boolean
        - updateData()
    }
    package utils {
        class TextFormatterFactory {
            + createIntegerTextFormatter(TextField, Button, Function)
            + createDecimalTextFormatter(TextField, Button, Function)
            - createTextFormatter(TextField, Button, Function, String)
        }
    }
    SignalOperationTabController ..> TextFormatterFactory
    RadarTabController ..> TextFormatterFactory
}
SignalOperationTabController ....> SignalFacade
RadarTabController ....> SignalFacade
SignalOperationTabController ..> DiscreteSignalsCorrelationType
SignalOperationTabController ..> WindowType
SignalOperationTabController ..> PassType
```
## Backend
```plantuml
package backend {
    class SignalFacade {
        + convolution(DiscreteSignal, DiscreteSignal): DiscreteSignal
        + filter(DiscreteSignal, PassType, WindowType, int, double): DiscreteSignal
        + discreteSignalsCorrelation(DiscreteSignal, DiscreteSignal, DiscreteSignalsCorrelationType): double
        + startRadar(double, int, double, double, double, ContinuousSignal, double, double, double, double, double, double): Radar
    }
    class SignalOperationFactory {
        + createConvolution(): Convolution
        + createFilter(PassType, WindowType, int, double, double): Filter
        + createDiscreteSignalsCorrelation(): DiscreteSignalsCorrelation
        + createRadar(double, int, double, double, double, ContinuousSignal, double, double, double, double, double, double, SignalFacade): Radar
    }
    package radar {
        package model {
            class Environment {
                + signalSpeed: double
                + stepTime: double
            }
             class MeasuredObject {
                - X: double
                - Y: double
                - speedX: double
                - speedY: double
                + move(double)
                + calculateRealDistance(double)
            }
            class RadarConfig {
                + probingSignal: ContinuousSignal
                + probingSignalF: double
                + discreteBufferSize: int
                + workTime: double
                + period: double
                + x: double
                + y: double
            }
        }
        class RadarMemory {
            - radarDistances: double[]
            - realDistances: double[]
            - distancesTimes: double[]
            - signalSentWindows: DiscreteSignal[]
            - signalReceivedWindows: DiscreteSignal[]
            - correlationWindows: DiscreteSignal[]
            + getRadarDistances(): double[]
            + addToRadarDistances(double)
            + getRealDistances(): double[]
            + addToRealDistances(double)
            + getDistancesTimes(): double[]
            + addToDistancesTimes(double)
            + getSignalSentWindows(): DiscreteSignal[]
            + addToSignalSentWindows(DiscreteSignal)
            + getSignalReceivedWindows(): DiscreteSignal[]
            + addToSignalReceivedWindows(DiscreteSignal)
            + getCorrelationWindows(): DiscreteSignal[]
            + addToCorrelationWindows(DiscreteSignal)
        }
        
        class RadarDependenciesFactory {
            + createEnvironment(double, double): Environment
            + createMeasuredObject(double, double, double, double): MeasuredObject
            + createRadarConfig(ContinuousSignal, double, int, double, double, double, double): RadarConfig
        }
        RadarDependenciesFactory ..> Environment
        RadarDependenciesFactory ..> MeasuredObject
        RadarDependenciesFactory ..> RadarConfig
        SignalOperationFactory ..> RadarDependenciesFactory
        
        class Radar {
            - radarConfig: RadarConfig
            - environment: Environment
            - measuredObject: MeasuredObject
            - first: double
            - signalSent: DiscreteSignal
            - signalReceived: DiscreteSignal
            - samplesSentButNotHit: double[]
            - allRealDistances: double[][]
            - hitsTime: double[]
            - startWorking()
            - calculateCorrelation()
            + getRadarMemory(): RadarMemory
        }
        SignalOperationFactory ..> Radar
        Radar o--> RadarMemory
    }
    package signal_operation {
        class Convolution {
            + execute(DiscreteSignal, DiscreteSignal): DiscreteSignal
        }
        class Filter {
            + execute(DiscreteSignal): DiscreteSignal
        }
        Filter ..> Pass
        Filter o-> Convolution
        Pass ..> Window
        class PassFactory {
            + createLowPass(int, double, double, Window): Pass
            + createHighPass(int, double, double, Window): Pass
            + createBandPass(int, double, double, Window): Pass
        }
        PassFactory ..> Pass
        class WindowFactory {
            + createRectangularWindow(): Window
            + createHammingWindow(int): Window
            + createHanningWindow(int): Window
            + createBlackmanWindow(int): Window
        }
        WindowFactory ..> Window
        package pass {
            interface Pass {
                + pass(int): double
            }
            class LowPass {
                + pass(int): double
            }
            class HighPass {
                + pass(int): double
            }
            class BandPass {
                + pass(int): double
            }
            Pass <|.. LowPass
            LowPass <|.. HighPass
            LowPass <|.. BandPass
        }
        package window {
            interface Window {
                + window(int): double
            }
            class RectangularWindow {
                + window(int): double
            }
            class HammingWindow {
                + window(int): double
            }
            class HanningWindow {
                + window(int): double
            }
            class BlackmanWindow {
                + window(int): double
            }
            Window <|.. RectangularWindow
            Window <|.. HammingWindow
            Window <|.. HanningWindow
            Window <|.. BlackmanWindow
        }
        class DiscreteSignalsCorrelation {
            + execute(DiscreteSignal, DiscreteSignal, DiscreteSignalsCorrelationType): DiscreteSignal
            - executeDirect(DiscreteSignal, DiscreteSignal): DiscreteSignal
            - executeUsingConvolution(DiscreteSignal, DiscreteSignal): DiscreteSignal
        }
        DiscreteSignalsCorrelation ..> DiscreteSignalsCorrelationType
        DiscreteSignalsCorrelation o-> Convolution
        enum DiscreteSignalsCorrelationType {
            + DIRECT
            + USING_CONVOLUTION
        }
        enum WindowType {
            + RECTANGULAR
            + BLACKMAN
            + HAMMING
            + HANNING
        }
        enum PassType {
            + LOW_PASS
            + HIGH_PASS
            + BAND_PASS
        }
    }
    SignalFacade --> SignalOperationFactory
    SignalOperationFactory --> PassFactory
    SignalOperationFactory --> WindowFactory
    SignalOperationFactory ..> WindowType
    SignalOperationFactory ..> PassType
    SignalOperationFactory ..> Convolution
    SignalOperationFactory ..> Filter
    SignalOperationFactory ..> DiscreteSignalsCorrelation
}
```
# Task 4 - Fourier and falco transforms, fast algorithms
## Backend
```plantuml
package backend {
    class SignalFacade {
        + discreteFourierTransformWithDecimationInTimeDomain(DiscreteSignal,TransformType): DiscreteFourierTransformedSignal
        + discreteFalcoTransform(DiscreteSignal,Level): DiscreteSignal[]
    }
    class SignalOperationFactory {
        + createDiscreteFourierTransformWithDecimationInTimeDomain(): DiscreteFourierTransformWithDecimationInTimeDomain
        + createDiscreteFalcoTransform(): DiscreteFalcoTransform
    }
    SignalFacade --> SignalOperationFactory
    package signal_operation {
        enum TransformType {
            + DIRECT
            + FAST
        }
        class DiscreteFourierTransformWithDecimationInTimeDomain {
            + execute(DiscreteSignal,TransformType): DiscreteSignal
            - executeDirect(DiscreteSignal): DiscreteSignal
            - executeFast(DiscreteSignal): DiscreteSignal
        }
        DiscreteFourierTransformWithDecimationInTimeDomain ..> TransformType
        
        enum Level {
            + DB4
            + DB6
            + DB8
        }
        class DiscreteFalcoTransform {
            + execute(DiscreteSignal,Level): DiscreteSignal
            - getTransformedPoints(double[],double[]: double[][]
        }
        DiscreteFalcoTransform ..> Level
    }
    SignalOperationFactory ..> DiscreteFourierTransformWithDecimationInTimeDomain
    SignalOperationFactory ..> DiscreteFalcoTransform
    class SignalFactory {
        + createDiscreteFourierTransformedSignal(double[]): DiscreteSignal
    }
    package signal {
        SignalFactory ..> DiscreteFourierTransformedSignal
        SignalFacade --> SignalFactory
        class DiscreteSignal extends AbstractSignal
        class DiscreteFourierTransformedSignal extends DiscreteSignal {
            # imaginaryPartPoints: double[][]
            + getRealPartPoints(): double[][]
            + getImaginaryPartPoints(): double[][]
            + getModulePoints(): double[][]
            + getPhasePoints(): double[][]
        }
    }
}
```
## Frontend
```plantuml
package frontend {
    class TransformTabController {
        + discreteFourierTransformOperation()
        + onUpdateDiscreteFourierTransformOperationsComboBox()
    }
}
package backend {
    class SignalFacade {
        + discreteFourierTransformWithDecimationInTimeDomain(DiscreteSignal,TransformType): DiscreteSignal
        + discreteFalcoTransform(DiscreteSignal,Level): DiscreteSignal[]
    }
    package signal_operation {
        enum TransformType {
            + DIRECT
            + FAST
        }
        enum Level {
            + DB4
            + DB6
            + DB8
        }
    }
}
TransformTabController ....> SignalFacade
TransformTabController ..> TransformType
TransformTabController ..> Level
```
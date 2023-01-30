package frontend.fields;

import java.util.Map;

public class FieldsMapper {
    private FieldsMapper() {
    }

    private static final Map<String, String> fieldsMap = Map.of(
            "A", "Amplitude",
            "d", "Duration",
            "t1", "Start time",
            "f", "Frequency",
            "T", "Basic period",
            "kw", "Fill factor",
            "ts", "Sampling time",
            "p", "Probability of an impulse",
            "n1", "First sample number",
            "ns", "Number of the sample for which the amplitude jump occurs"
    );
    public static String map(String value) {
        return fieldsMap.getOrDefault(value, value);
    }
}

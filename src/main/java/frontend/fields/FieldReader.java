package frontend.fields;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class FieldReader {
    private FieldReader() {
    }
    public static List<String> getFieldNames(Class<?> currentClass) {
        List<String> fieldNames = new ArrayList<>();
        while (currentClass != null) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (
                        (Modifier.isPrivate(field.getModifiers()) || Modifier.isProtected(field.getModifiers()))
                                && !Modifier.isStatic(field.getModifiers())
                                && field.getType().isPrimitive()
                ) {
                    fieldNames.add(0, field.getName());
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return fieldNames;
    }
}

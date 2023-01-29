package frontend.fields;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class FieldsReader {
    public static List<String> getFieldNames(Object object) {
        List<String> fieldNames = new ArrayList<>();
        Class<?> currentClass = object.getClass();
        while (currentClass != null) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (
                        (Modifier.isPrivate(field.getModifiers()) || Modifier.isProtected(field.getModifiers()))
                                && !Modifier.isStatic(field.getModifiers())
                                && field.getType().isPrimitive()
                ) {
                    fieldNames.add(field.getName());
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return fieldNames;
    }
}

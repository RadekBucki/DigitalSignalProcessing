package frontend.classes;

import org.reflections.Reflections;

import java.util.*;
import java.util.function.Function;

public class ClassesReader {
    private static final String ROOT = "backend";
    public static Map<Class<?>, String> getChildClasses(Class<?> parent, Function<String, String> translator) {
        Map<Class<?>, String> classNames = new HashMap<>();

        Reflections reflections = new Reflections(ROOT);
        Set<Class<?>> allTypes = reflections.getSubTypesOf((Class<Object>) parent);
        for (Class<?> type : allTypes) {
            classNames.put(type, translator.apply(type.getSimpleName()));
        }
        return classNames;
    }
}

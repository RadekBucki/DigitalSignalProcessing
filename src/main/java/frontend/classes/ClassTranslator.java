package frontend.classes;

public class ClassTranslator {
    private ClassTranslator() {
    }
    public static String translatePascalCaseClassToText(Class<?> input) {
        String text = input.getSimpleName().replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}

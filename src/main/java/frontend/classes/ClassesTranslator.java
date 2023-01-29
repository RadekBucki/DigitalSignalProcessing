package frontend.classes;

public class ClassesTranslator {
    public static String translatePascalCaseToText(String input) {
        String text = input.replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}

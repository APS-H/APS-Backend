package apsh.backend.util;

public class StringUtil {

    public static String extractNumber(String str) {
        StringBuilder sb = new StringBuilder();
        for (char ch : str.toCharArray()) {
            if (Character.isDigit(ch)) sb.append(ch);
        }
        return sb.toString();
    }

    public static Integer lastDigit(String str) {
        for (int i = str.length() - 1; i >= 0; i--) {
            char ch  = str.charAt(i);
            if (Character.isDigit(ch)) return (int) ch;
        }
        return -1;
    }

}

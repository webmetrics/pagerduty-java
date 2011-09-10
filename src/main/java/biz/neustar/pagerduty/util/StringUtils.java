package biz.neustar.pagerduty.util;

public class StringUtils {
    public static String join(String... strings) {
        StringBuilder sb = new StringBuilder(strings.length * 16);

        for (int i = 0; i < strings.length; i++) {
            String s = strings[i];
            if (i > 0) {
                sb.append(",");
            }
            sb.append(s);
        }

        return sb.toString();
    }
}

package biz.neustar.pagerduty.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Version {
    private static String VERSION = "DEVELOPMENT";

    static {
        InputStream is = Version.class.getResourceAsStream("/META-INF/maven/biz.neustar/pagerduty/pom.properties");
        if (is != null) {
            Properties props = new Properties();
            try {
                props.load(is);
                VERSION = props.getProperty("version");
            } catch (IOException e) {
                // ignore
            }
        }
    }

    public static String get() {
        return VERSION;
    }
}

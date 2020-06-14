package kma.cs.sample.desktop;

import java.io.IOException;
import java.util.Properties;

public class PropertiesProvider {

    private static final Properties PROPERTIES = new Properties();
    static {
        try {
            PROPERTIES.load(PropertiesProvider.class.getResourceAsStream("/application.properties"));
        } catch (final IOException ex) {
            throw new RuntimeException("Can't load properties", ex);
        }
    }

    public static String getString(final String propertyName) {
        return PROPERTIES.getProperty(propertyName);
    }

}

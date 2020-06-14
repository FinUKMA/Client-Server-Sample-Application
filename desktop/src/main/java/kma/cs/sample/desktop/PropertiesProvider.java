package kma.cs.sample.desktop;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesProvider {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{.*?\\}");
    private static final Properties PROPERTIES = new Properties();
    static {
        try {
            PROPERTIES.load(PropertiesProvider.class.getResourceAsStream("/application.properties"));
        } catch (final IOException ex) {
            throw new RuntimeException("Can't load properties", ex);
        }
    }

    // https://coderanch.com/t/436990/java/Placeholders-property-files
    private static String getProperty(final String key) {
        final String raw = PROPERTIES.getProperty(key);
        if (raw == null)
        {
            return raw;
        }
        final Matcher matcher = PLACEHOLDER_PATTERN.matcher(raw);
        final StringBuilder value = new StringBuilder(raw.length());
        int index = 0;
        while (matcher.find())
        {
            final int start = matcher.start();
            final int end = matcher.end();
            final String template = raw.substring(start + 1, end - 1); // + 1 and - 1 to cut off the { and }
            final String templateVal = getProperty(template); // recursive call
            if (templateVal == null)
            {
                // don't replace
                // index is the end of the last template, so add anything from there to the end of the current template
                value.append(raw, index, end);
            }
            else
            {
                // index is the end of the last template, so add anything between that template and the current template
                value.append(raw, index, start);
                // add the substituted value
                value.append(templateVal);
            }
            index = end;
        }
        // end the remained
        value.append(raw.substring(index));
        return value.toString();
    }

    public static String getString(final String propertyName) {
        return getProperty(propertyName);
    }

}

package kma.cs.sample.backend.dao;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DaoUtils {

    public Object[] toArray(final Object... args) {
        return Arrays.stream(args)
            .toArray(Object[]::new);
    }

    public static String like(final String fieldName, final String query) {
        if (StringUtils.isBlank(query)) {
            return null;
        }
        return fieldName + " LIKE '%" + query + "%'";
    }

    public static <T> String gte(final String fieldName, final T value) {
        if (value == null) {
            return null;
        }
        return fieldName + " >= " + value;
    }

    public static <T> String lte(final String fieldName, final T value) {
        if (value == null) {
            return null;
        }
        return fieldName + " <= " + value;
    }

}

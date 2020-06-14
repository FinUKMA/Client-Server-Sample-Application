package kma.cs.sample.backend.dao;

import java.util.Arrays;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DaoUtils {

    public Object[] toArray(final Object... args) {
        return Arrays.stream(args)
            .toArray(Object[]::new);
    }

}

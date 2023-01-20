package com.github.dcysteine.nesql.server.common.util;

import com.google.common.base.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

/** Contains utility methods for working with URL parameters. */
public class ParamUtil {
    // Static class.
    private ParamUtil() {}

    @Nullable
    public static <T> Specification<T> buildStringSpec(
            Optional<String> param, Function<String, Specification<T>> function) {
        return param
                .map(Strings::emptyToNull)
                .map(function)
                .orElse(null);
    }

    @Nullable
    public static <T, R> Specification<T> buildSpec(
            Optional<R> param, Function<R, Specification<T>> function) {
        return param
                .map(function)
                .orElse(null);
    }
}

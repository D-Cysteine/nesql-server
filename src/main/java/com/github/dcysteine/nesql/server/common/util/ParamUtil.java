package com.github.dcysteine.nesql.server.common.util;

import com.google.common.base.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

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

    /**
     * We ignore parameters that aren't set, and false boolean parameters are unset, so the only
     * possible actual value for a boolean parameter is true.
     */
    @Nullable
    public static <T> Specification<T> buildBooleanSpec(
            Optional<Boolean> param, Supplier<Specification<T>> supplier) {
        return param
                .filter(bool -> bool)
                .map(bool -> supplier.get())
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

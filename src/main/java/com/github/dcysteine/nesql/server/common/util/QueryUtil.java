package com.github.dcysteine.nesql.server.common.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

/**
 * Contains helper methods for constructing specifications.
 *
 * <p>As a reference, see
 * <a href="http://www.hsqldb.org/doc/2.0/guide/builtinfunctions-chapt.htm">here</a>.
 */
public class QueryUtil {
    // Static class.
    private QueryUtil() {}

    /**
     * Creates a predicate that returns true for partial regex matches.
     *
     * <p>The built-in HSQLDB {@code REGEXP_MATCHES} function requires the entire string to match.
     */
    public static Predicate regexMatch(
            CriteriaBuilder builder, Expression<String> string, Expression<String> regex) {
        return builder.greaterThan(
                builder.function("REGEXP_INSTR", Integer.class, string, regex), 0);
    }
}

package com.github.dcysteine.nesql.server.plugin.thaumcraft.spec;

import com.github.dcysteine.nesql.server.common.util.QueryUtil;
import com.github.dcysteine.nesql.sql.base.item.Item_;
import com.github.dcysteine.nesql.sql.thaumcraft.Aspect;
import com.github.dcysteine.nesql.sql.thaumcraft.Aspect_;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class AspectSpec {
    public static final Sort DEFAULT_SORT =
            Sort.by(Sort.Direction.DESC, Aspect_.PRIMAL).and(Sort.by(Aspect_.NAME, Aspect_.ID));

    // Static class.
    private AspectSpec() {}

    /** Matches by regex. */
    public static Specification<Aspect> buildAspectNameSpec(String aspectName) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(Aspect_.NAME),
                        builder.literal(aspectName));
    }

    /** Matches by regex. */
    public static Specification<Aspect> buildComponentNameSpec(String componentName) {
        return (root, query, builder) -> {
            Subquery<String> aspectQuery = query.subquery(String.class);
            Root<Aspect> aspectRoot = aspectQuery.from(Aspect.class);
            aspectQuery.select(aspectRoot.get(Aspect_.ID))
                    .where(
                            QueryUtil.regexMatch(
                                    builder,
                                    aspectRoot.get(Aspect_.NAME),
                                    builder.literal(componentName)));

            return builder.equal(
                    root.get(Aspect_.COMPONENTS).get(Aspect_.ID),
                    builder.any(aspectQuery));
        };
    }

    /** Matches by regex. */
    public static Specification<Aspect> buildComponentOfNameSpec(String componentOfName) {
        return (root, query, builder) -> {
            Subquery<String> aspectQuery = query.subquery(String.class);
            Root<Aspect> aspectRoot = aspectQuery.from(Aspect.class);
            aspectQuery.select(aspectRoot.get(Aspect_.ID))
                    .where(
                            QueryUtil.regexMatch(
                                    builder,
                                    aspectRoot.get(Aspect_.NAME),
                                    builder.literal(componentOfName)));

            return builder.equal(
                    root.get(Aspect_.COMPONENT_OF).get(Aspect_.ID),
                    builder.any(aspectQuery));
        };
    }
}
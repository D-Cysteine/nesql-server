package com.github.dcysteine.nesql.server.plugin.base.spec;

import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid_;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class FluidSpec {
    // Static class.
    private FluidSpec() {}

    public static Sort DEFAULT_SORT = Sort.by(Fluid_.FLUID_ID, Fluid_.NBT, Fluid_.ID);

    /** Matches by regex. */
    public static Specification<Fluid> buildLocalizedNameSpec(String localizedName) {
        return (root, query, builder) -> {
            return builder.isTrue(
                    builder.function(
                            "regexp_like",
                            Boolean.class,
                            root.get(Fluid_.LOCALIZED_NAME),
                            builder.literal(localizedName)));
        };
    }

    /** Matches by regex. */
    public static Specification<Fluid> buildInternalNameSpec(String internalName) {
        return (root, query, builder) -> {
            return builder.isTrue(
                    builder.function(
                            "regexp_like",
                            Boolean.class,
                            root.get(Fluid_.INTERNAL_NAME),
                            builder.literal(internalName)));
        };
    }

    /** Matches by Forge fluid ID. */
    public static Specification<Fluid> buildFluidIdSpec(int fluidId) {
        return (root, query, builder) -> {
            return builder.equal(root.get(Fluid_.FLUID_ID), fluidId);
        };
    }

    /** Matches by regex. */
    public static Specification<Fluid> buildNbtSpec(String nbt) {
        return (root, query, builder) -> {
            return builder.isTrue(
                    builder.function(
                            "regexp_like",
                            Boolean.class,
                            root.get(Fluid_.NBT),
                            builder.literal(nbt)));
        };
    }

    public static Specification<Fluid> buildNullNbtSpec() {
        return (root, query, builder) -> {
            return builder.isNull(root.get(Fluid_.NBT));
        };
    }
}

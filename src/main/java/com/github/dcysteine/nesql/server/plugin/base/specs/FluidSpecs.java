package com.github.dcysteine.nesql.server.plugin.base.specs;

import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid_;
import org.springframework.data.jpa.domain.Specification;

public class FluidSpecs {
    // Static class.
    private FluidSpecs() {}

    /** Matches by regex. */
    public static Specification<Fluid> buildLocalizedNameSpec(String localizedName) {
        return (root, query, builder) -> {
            return builder.like(root.get(Fluid_.LOCALIZED_NAME), localizedName);
        };
    }

    /** Matches by regex. */
    public static Specification<Fluid> buildInternalNameSpec(String internalName) {
        return (root, query, builder) -> {
            return builder.like(root.get(Fluid_.INTERNAL_NAME), internalName);
        };
    }

    /** Matches by Minecraft item ID. */
    public static Specification<Fluid> buildFluidIdSpec(int fluidId) {
        return (root, query, builder) -> {
            return builder.equal(root.get(Fluid_.FLUID_ID), fluidId);
        };
    }
}

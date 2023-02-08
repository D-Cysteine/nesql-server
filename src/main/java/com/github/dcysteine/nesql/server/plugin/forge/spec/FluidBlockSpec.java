package com.github.dcysteine.nesql.server.plugin.forge.spec;

import com.github.dcysteine.nesql.sql.base.fluid.Fluid_;
import com.github.dcysteine.nesql.sql.base.item.Item_;
import com.github.dcysteine.nesql.sql.forge.FluidBlock;
import com.github.dcysteine.nesql.sql.forge.FluidBlock_;
import org.springframework.data.jpa.domain.Specification;

public class FluidBlockSpec {
    // Static class.
    private FluidBlockSpec() {}

    public static Specification<FluidBlock> buildItemIdSpec(String itemId) {
        return (root, query, builder) ->
                builder.equal(root.get(FluidBlock_.BLOCK).get(Item_.ID), itemId);
    }

    public static Specification<FluidBlock> buildFluidIdSpec(String fluidId) {
        return (root, query, builder) ->
                builder.equal(root.get(FluidBlock_.FLUID).get(Fluid_.ID), fluidId);
    }
}
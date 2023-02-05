package com.github.dcysteine.nesql.server.plugin.base.spec;

import com.github.dcysteine.nesql.server.common.util.QueryUtil;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup_;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack_;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid_;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class FluidGroupSpec {
    // Static class.
    private FluidGroupSpec() {}

    /** Matches by regex. */
    public static Specification<FluidGroup> buildFluidNameSpec(String localizedName) {
        return (root, query, builder) -> {
            Subquery<Fluid> fluidQuery = query.subquery(Fluid.class);
            Root<Fluid> fluidRoot = fluidQuery.from(Fluid.class);
            fluidQuery.select(fluidRoot)
                    .where(
                            QueryUtil.regexMatch(
                                    builder,
                                    fluidRoot.get(Fluid_.LOCALIZED_NAME),
                                    builder.literal(localizedName)));

            Subquery<Fluid> fluidWitnessQuery = query.subquery(Fluid.class);
            Root<Fluid> fluidWitnessRoot = fluidWitnessQuery.from(Fluid.class);
            fluidWitnessQuery.select(fluidWitnessRoot)
                    .where(
                            builder.and(
                                    builder.in(fluidWitnessRoot).value(fluidQuery),
                                    builder.in(fluidWitnessRoot)
                                            .value(
                                                    root
                                                            .get(FluidGroup_.FLUID_STACKS)
                                                            .get(FluidStack_.FLUID))));

            return builder.exists(fluidWitnessQuery);
        };
    }

    public static Specification<FluidGroup> buildFluidIdSpec(String fluidId) {
        return (root, query, builder) ->
                builder.in(builder.literal(fluidId))
                        .value(
                                root
                                        .get(FluidGroup_.FLUID_STACKS)
                                        .get(FluidStack_.FLUID)
                                        .get(Fluid_.ID));
    }

    public static Specification<FluidGroup> buildAmountSpec(int amount) {
        return (root, query, builder) ->
                builder.in(builder.literal(amount))
                        .value(
                                root
                                        .get(FluidGroup_.FLUID_STACKS)
                                        .get(FluidStack_.AMOUNT));
    }

    public static Specification<FluidGroup> buildMinSizeSpec(int size) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(
                        builder.size(root.get(FluidGroup_.FLUID_STACKS)), size);
    }

    public static Specification<FluidGroup> buildMaxSizeSpec(int size) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(
                        builder.size(root.get(FluidGroup_.FLUID_STACKS)), size);
    }
}
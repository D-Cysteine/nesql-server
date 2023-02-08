package com.github.dcysteine.nesql.server.plugin.forge.spec;

import com.github.dcysteine.nesql.server.common.util.QueryUtil;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack_;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid_;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.Item_;
import com.github.dcysteine.nesql.sql.forge.FluidContainer;
import com.github.dcysteine.nesql.sql.forge.FluidContainer_;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class FluidContainerSpec {
    public static final Sort DEFAULT_SORT =
            Sort.unsorted()
                    .and(
                            Sort.sort(FluidContainer.class)
                                    .by(FluidContainer::getFluidStack)
                                    .by(FluidStack::getFluid)
                                    .by(Fluid::getId))
                    .and(
                            Sort.sort(FluidContainer.class)
                                    .by(FluidContainer::getFluidStack)
                                    .by(FluidStack::getAmount))
                    .and(
                            Sort.sort(FluidContainer.class)
                                    .by(FluidContainer::getEmptyContainer)
                                    .by(Item::getId));

    // Static class.
    private FluidContainerSpec() {}

    /** Matches by regex. */
    public static Specification<FluidContainer> buildFilledItemNameSpec(String localizedName) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(FluidContainer_.CONTAINER).get(Item_.LOCALIZED_NAME),
                        builder.literal(localizedName));
    }

    /** Matches by regex. */
    public static Specification<FluidContainer> buildFilledItemModIdSpec(String modId) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(FluidContainer_.CONTAINER).get(Item_.MOD_ID),
                        builder.literal(modId));
    }

    public static Specification<FluidContainer> buildFilledItemIdSpec(String itemId) {
        return (root, query, builder) ->
                builder.equal(root.get(FluidContainer_.CONTAINER).get(Item_.ID), itemId);
    }

    public static Specification<FluidContainer> buildEmptyItemIdSpec(String itemId) {
        return (root, query, builder) ->
                builder.equal(root.get(FluidContainer_.EMPTY_CONTAINER).get(Item_.ID), itemId);
    }

    /** Matches by regex. */
    public static Specification<FluidContainer> buildFluidNameSpec(String localizedName) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root
                                .get(FluidContainer_.FLUID_STACK)
                                .get(FluidStack_.FLUID)
                                .get(Fluid_.LOCALIZED_NAME),
                        builder.literal(localizedName));
    }

    /** Matches by regex. */
    public static Specification<FluidContainer> buildFluidModIdSpec(String modId) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root
                                .get(FluidContainer_.FLUID_STACK)
                                .get(FluidStack_.FLUID)
                                .get(Fluid_.MOD_ID),
                        builder.literal(modId));
    }

    public static Specification<FluidContainer> buildFluidIdSpec(String fluidId) {
        return (root, query, builder) ->
                builder.equal(
                        root
                                .get(FluidContainer_.FLUID_STACK)
                                .get(FluidStack_.FLUID)
                                .get(Fluid_.ID),
                        fluidId);
    }

    public static Specification<FluidContainer> buildMinAmountSpec(int amount) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(
                        root.get(FluidContainer_.FLUID_STACK).get(FluidStack_.AMOUNT), amount);
    }

    public static Specification<FluidContainer> buildMaxAmountSpec(int amount) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(
                        root.get(FluidContainer_.FLUID_STACK).get(FluidStack_.AMOUNT), amount);
    }
}
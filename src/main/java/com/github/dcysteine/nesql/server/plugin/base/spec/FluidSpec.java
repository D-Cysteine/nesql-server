package com.github.dcysteine.nesql.server.plugin.base.spec;

import com.github.dcysteine.nesql.server.common.util.QueryUtil;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup_;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack_;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid_;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe_;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class FluidSpec {
    public static final Sort DEFAULT_SORT =
            Sort.by(Fluid_.MOD_ID, Fluid_.INTERNAL_NAME, Fluid_.NBT, Fluid_.ID);

    // Static class.
    private FluidSpec() {}

    /** Matches by regex. */
    public static Specification<Fluid> buildLocalizedNameSpec(String localizedName) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(Fluid_.LOCALIZED_NAME),
                        builder.literal(localizedName));
    }

    /** Matches by regex. */
    public static Specification<Fluid> buildModIdSpec(String modId) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(Fluid_.MOD_ID),
                        builder.literal(modId));
    }

    /** Matches by regex. */
    public static Specification<Fluid> buildInternalNameSpec(String internalName) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(Fluid_.INTERNAL_NAME),
                        builder.literal(internalName));
    }

    /** Matches by Forge fluid ID. */
    public static Specification<Fluid> buildFluidIdSpec(int fluidId) {
        return (root, query, builder) ->  builder.equal(root.get(Fluid_.FLUID_ID), fluidId);
    }

    /** Matches by regex. */
    public static Specification<Fluid> buildNbtSpec(String nbt) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(Fluid_.NBT),
                        builder.literal(nbt));
    }

    /** Finds fluids that belong to the specified fluid group. */
    public static Specification<Fluid> buildFluidGroupSpec(String fluidGroupId) {
        return (root, query, builder) -> {
            Subquery<Fluid> directFluidsQuery = query.subquery(Fluid.class);
            Root<FluidGroup> directFluidGroupRoot = directFluidsQuery.from(FluidGroup.class);
            directFluidsQuery.select(
                            directFluidGroupRoot.get(FluidGroup_.FLUID_STACKS)
                                    .get(FluidStack_.FLUID))
                    .where(builder.equal(directFluidGroupRoot.get(FluidGroup_.ID), fluidGroupId));

            return builder.in(root).value(directFluidsQuery);
        };
    }

    /** Finds fluids that are inputs to the specified recipe. */
    public static Specification<Fluid> buildRecipeInputSpec(String recipeId) {
        return (root, query, builder) -> {
            Subquery<Fluid> directFluidsQuery = query.subquery(Fluid.class);
            Root<Recipe> directRecipeRoot = directFluidsQuery.from(Recipe.class);
            directFluidsQuery.select(
                            directRecipeRoot.get(Recipe_.FLUID_INPUTS)
                                    .get(FluidGroup_.FLUID_STACKS)
                                    .get(FluidStack_.FLUID))
                    .where(builder.equal(directRecipeRoot.get(Recipe_.ID), recipeId));

            return builder.in(root).value(directFluidsQuery);
        };
    }
}

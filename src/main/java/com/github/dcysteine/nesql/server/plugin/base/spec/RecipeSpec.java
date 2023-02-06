package com.github.dcysteine.nesql.server.plugin.base.spec;

import com.github.dcysteine.nesql.server.common.util.QueryUtil;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup_;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid_;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup_;
import com.github.dcysteine.nesql.sql.base.item.Item_;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType_;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe_;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class RecipeSpec {
    // Static class.
    private RecipeSpec() {}

    public static final Sort DEFAULT_SORT =
            Sort.unsorted().and(
                            Sort.sort(Recipe.class)
                                    .by(Recipe::getRecipeType)
                                    .by(RecipeType::getCategory))
                    .and(
                            Sort.sort(Recipe.class)
                                    .by(Recipe::getRecipeType)
                                    .by(RecipeType::getType));

    /** Matches by regex. */
    public static Specification<Recipe> buildRecipeCategorySpec(String recipeCategory) {
        // TODO add subquery to find matching recipe types first, for speed?
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(Recipe_.RECIPE_TYPE).get(RecipeType_.CATEGORY),
                        builder.literal(recipeCategory));
    }

    /** Matches by regex. */
    public static Specification<Recipe> buildRecipeTypeSpec(String recipeType) {
        // TODO add subquery to find matching recipe types first, for speed?
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(Recipe_.RECIPE_TYPE).get(RecipeType_.TYPE),
                        builder.literal(recipeType));
    }

    public static Specification<Recipe> buildRecipeTypeIdSpec(String recipeTypeId) {
        return (root, query, builder) ->
                builder.equal(root.get(Recipe_.RECIPE_TYPE).get(RecipeType_.ID), recipeTypeId);
    }

    /** Matches by regex. */
    public static Specification<Recipe> buildInputItemNameSpec(String localizedName) {
        return (root, query, builder) -> {
            Subquery<String> itemQuery = query.subquery(String.class);
            Root<Item> itemRoot = itemQuery.from(Item.class);
            itemQuery.select(itemRoot.get(Item_.ID))
                    .where(
                            QueryUtil.regexMatch(
                                    builder,
                                    itemRoot.get(Item_.LOCALIZED_NAME),
                                    builder.literal(localizedName)));

            return builder.equal(
                    root.get(Recipe_.ITEM_INPUTS_ITEMS).get(Item_.ID),
                    builder.any(itemQuery));
        };
    }

    public static Specification<Recipe> buildInputItemIdSpec(String itemId) {
        return (root, query, builder) ->
                builder.equal(root.get(Recipe_.ITEM_INPUTS_ITEMS).get(Item_.ID), itemId);
    }

    public static Specification<Recipe> buildInputItemGroupIdSpec(String itemGroupId) {
        return (root, query, builder) ->
                builder.equal(root.get(Recipe_.UNIQUE_ITEM_INPUTS).get(ItemGroup_.ID), itemGroupId);
    }

    /** Matches by regex. */
    public static Specification<Recipe> buildInputFluidNameSpec(String localizedName) {
        return (root, query, builder) -> {
            Subquery<String> fluidQuery = query.subquery(String.class);
            Root<Fluid> fluidRoot = fluidQuery.from(Fluid.class);
            fluidQuery.select(fluidRoot.get(Fluid_.ID))
                    .where(
                            QueryUtil.regexMatch(
                                    builder,
                                    fluidRoot.get(Fluid_.LOCALIZED_NAME),
                                    builder.literal(localizedName)));

            return builder.equal(
                    root.get(Recipe_.FLUID_INPUTS_FLUIDS).get(Fluid_.ID),
                    builder.any(fluidQuery));
        };
    }

    public static Specification<Recipe> buildInputFluidIdSpec(String fluidId) {
        return (root, query, builder) ->
                builder.equal(root.get(Recipe_.FLUID_INPUTS_FLUIDS).get(Fluid_.ID), fluidId);
    }

    public static Specification<Recipe> buildInputFluidGroupIdSpec(String fluidGroupId) {
        return (root, query, builder) ->
                builder.equal(
                        root.get(Recipe_.UNIQUE_FLUID_INPUTS).get(FluidGroup_.ID), fluidGroupId);
    }

    /** Matches by regex. */
    public static Specification<Recipe> buildOutputItemNameSpec(String localizedName) {
        return (root, query, builder) -> {
            Subquery<String> itemQuery = query.subquery(String.class);
            Root<Item> itemRoot = itemQuery.from(Item.class);
            itemQuery.select(itemRoot.get(Item_.ID))
                    .where(
                            QueryUtil.regexMatch(
                                    builder,
                                    itemRoot.get(Item_.LOCALIZED_NAME),
                                    builder.literal(localizedName)));

            return builder.equal(
                    root.get(Recipe_.UNIQUE_ITEM_OUTPUTS).get(Item_.ID),
                    builder.any(itemQuery));
        };
    }

    public static Specification<Recipe> buildOutputItemIdSpec(String itemId) {
        return (root, query, builder) ->
                builder.equal(root.get(Recipe_.UNIQUE_ITEM_OUTPUTS).get(Item_.ID), itemId);
    }

    /** Matches by regex. */
    public static Specification<Recipe> buildOutputFluidNameSpec(String localizedName) {
        return (root, query, builder) -> {
            Subquery<String> fluidQuery = query.subquery(String.class);
            Root<Fluid> fluidRoot = fluidQuery.from(Fluid.class);
            fluidQuery.select(fluidRoot.get(Fluid_.ID))
                    .where(
                            QueryUtil.regexMatch(
                                    builder,
                                    fluidRoot.get(Fluid_.LOCALIZED_NAME),
                                    builder.literal(localizedName)));

            return builder.equal(
                    root.get(Recipe_.UNIQUE_FLUID_OUTPUTS).get(Fluid_.ID),
                    builder.any(fluidQuery));
        };
    }

    public static Specification<Recipe> buildOutputFluidIdSpec(String fluidId) {
        return (root, query, builder) ->
                builder.equal(root.get(Recipe_.UNIQUE_FLUID_OUTPUTS).get(Fluid_.ID), fluidId);
    }
}

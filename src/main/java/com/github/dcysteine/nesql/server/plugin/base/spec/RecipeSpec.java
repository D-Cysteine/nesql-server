package com.github.dcysteine.nesql.server.plugin.base.spec;

import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup_;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStackWithProbability_;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack_;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid_;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup_;
import com.github.dcysteine.nesql.sql.base.item.ItemStackWithProbability_;
import com.github.dcysteine.nesql.sql.base.item.ItemStack_;
import com.github.dcysteine.nesql.sql.base.item.Item_;
import com.github.dcysteine.nesql.sql.base.item.WildcardItemStack_;
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
        return (root, query, builder) ->
                builder.isTrue(
                        builder.function(
                                "regexp_like",
                                Boolean.class,
                                root.get(Recipe_.RECIPE_TYPE).get(RecipeType_.CATEGORY),
                                builder.literal(recipeCategory)));
    }

    /** Matches by regex. */
    public static Specification<Recipe> buildRecipeTypeSpec(String recipeType) {
        return (root, query, builder) ->
                builder.isTrue(
                        builder.function(
                                "regexp_like",
                                Boolean.class,
                                root.get(Recipe_.RECIPE_TYPE).get(RecipeType_.TYPE),
                                builder.literal(recipeType)));
    }

    public static Specification<Recipe> buildRecipeTypeIdSpec(String recipeTypeId) {
        return (root, query, builder) ->
                builder.equal(root.get(Recipe_.RECIPE_TYPE).get(RecipeType_.ID), recipeTypeId);
    }

    /**
     * This is it: the most complex query in the server (so far). Be careful with this query!
     * If you pass it a regex that matches even just a moderate amount of items, the query will take
     * forever to finish. If you want to use such a regex, be sure to also apply other restrictions
     * to narrow down the scope.
     */
    public static Specification<Recipe> buildInputItemNameSpec(String localizedName) {
        return (root, query, builder) -> {
            Subquery<Item> itemQuery = query.subquery(Item.class);
            Root<Item> itemRoot = itemQuery.from(Item.class);
            itemQuery.select(itemRoot)
                    .where(
                            builder.isTrue(
                                    builder.function(
                                            "regexp_like",
                                            Boolean.class,
                                            itemRoot.get(Item_.LOCALIZED_NAME),
                                            builder.literal(localizedName))));


            // We have to split the two item inclusion paths (direct and wildcard) into separate
            // subqueries, as otherwise we'll only select item groups that have at least one direct
            // and one wildcard item stack, due to how inner joins work.
            // We don't use left joins because they are super slow.
            Subquery<ItemGroup> directItemGroupsQuery = query.subquery(ItemGroup.class);
            Root<ItemGroup> directItemGroupsRoot = directItemGroupsQuery.from(ItemGroup.class);
            Subquery<Item> directItemWitnessQuery = directItemGroupsQuery.subquery(Item.class);
            Root<Item> directItemWitnessRoot = directItemWitnessQuery.from(Item.class);
            directItemWitnessQuery.select(directItemWitnessRoot)
                    .where(
                            builder.and(
                                    builder.in(directItemWitnessRoot).value(itemQuery),
                                    builder.in(directItemWitnessRoot)
                                            .value(
                                                    directItemGroupsRoot
                                                            .get(ItemGroup_.ITEM_STACKS)
                                                            .get(ItemStack_.ITEM))));
            directItemGroupsQuery.select(directItemGroupsRoot)
                    .where(builder.exists(directItemWitnessQuery));

            Subquery<ItemGroup> wildcardItemGroupsQuery = query.subquery(ItemGroup.class);
            Root<ItemGroup> wildcardItemGroupsRoot = wildcardItemGroupsQuery.from(ItemGroup.class);
            Subquery<Item> wildcardItemWitnessQuery = wildcardItemGroupsQuery.subquery(Item.class);
            Root<Item> wildcardItemWitnessRoot = wildcardItemWitnessQuery.from(Item.class);
            wildcardItemWitnessQuery.select(wildcardItemWitnessRoot)
                    .where(
                            builder.and(
                                    builder.in(wildcardItemWitnessRoot).value(itemQuery),
                                    builder.in(wildcardItemWitnessRoot.get(Item_.ITEM_ID))
                                            .value(
                                                    wildcardItemGroupsRoot
                                                            .get(ItemGroup_.WILDCARD_ITEM_STACKS)
                                                            .get(WildcardItemStack_.ITEM_ID))));
            wildcardItemGroupsQuery.select(wildcardItemGroupsRoot)
                    .where(builder.exists(wildcardItemWitnessQuery));

            Subquery<ItemGroup> directItemGroupWitnessQuery = query.subquery(ItemGroup.class);
            Root<ItemGroup> directItemGroupWitnessRoot =
                    directItemGroupWitnessQuery.from(ItemGroup.class);
            directItemGroupWitnessQuery.select(directItemGroupWitnessRoot)
                    .where(
                            builder.and(
                                    builder.in(directItemGroupWitnessRoot)
                                            .value(directItemGroupsQuery),
                                    builder.in(directItemGroupWitnessRoot.get(ItemGroup_.ID))
                                            .value(
                                                    root.get(Recipe_.ITEM_INPUTS)
                                                            .get(ItemGroup_.ID))));

            Subquery<ItemGroup> wildcardItemGroupWitnessQuery = query.subquery(ItemGroup.class);
            Root<ItemGroup> wildcardItemGroupWitnessRoot =
                    wildcardItemGroupWitnessQuery.from(ItemGroup.class);
            wildcardItemGroupWitnessQuery.select(wildcardItemGroupWitnessRoot)
                    .where(
                            builder.and(
                                    builder.in(wildcardItemGroupWitnessRoot)
                                            .value(wildcardItemGroupsQuery),
                                    builder.in(wildcardItemGroupWitnessRoot.get(ItemGroup_.ID))
                                            .value(
                                                    root.get(Recipe_.ITEM_INPUTS)
                                                            .get(ItemGroup_.ID))));

            return builder.or(
                    builder.exists(directItemGroupWitnessQuery),
                    builder.exists(wildcardItemGroupWitnessQuery));
        };
    }

    public static Specification<Recipe> buildInputItemIdSpec(String itemId) {
        return (root, query, builder) -> {
            Subquery<Integer> minecraftItemIdQuery = query.subquery(Integer.class);
            Root<Item> minecraftItemIdRoot = minecraftItemIdQuery.from(Item.class);
            minecraftItemIdQuery.select(minecraftItemIdRoot.get(Item_.ITEM_ID))
                    .where(builder.equal(minecraftItemIdRoot.get(Item_.ID), itemId));

            // We have to split the two item inclusion paths (direct and wildcard) into separate
            // subqueries, as otherwise we'll only select item groups that have at least one direct
            // and one wildcard item stack, due to how inner joins work.
            // We don't use left joins because they are super slow.
            Subquery<ItemGroup> directItemGroupsQuery = query.subquery(ItemGroup.class);
            Root<ItemGroup> directItemGroupsRoot = directItemGroupsQuery.from(ItemGroup.class);
            directItemGroupsQuery.select(directItemGroupsRoot)
                    .where(
                            builder.in(builder.literal(itemId))
                                    .value(
                                            directItemGroupsRoot
                                                    .get(ItemGroup_.ITEM_STACKS)
                                                    .get(ItemStack_.ITEM)
                                                    .get(Item_.ID)));

            Subquery<ItemGroup> wildcardItemGroupsQuery = query.subquery(ItemGroup.class);
            Root<ItemGroup> wildcardItemGroupsRoot = wildcardItemGroupsQuery.from(ItemGroup.class);
            wildcardItemGroupsQuery.select(wildcardItemGroupsRoot)
                    .where(
                            builder.in(minecraftItemIdQuery)
                                    .value(
                                            wildcardItemGroupsRoot
                                                    .get(ItemGroup_.WILDCARD_ITEM_STACKS)
                                                    .get(WildcardItemStack_.ITEM_ID)));

            Subquery<ItemGroup> directItemGroupWitnessQuery = query.subquery(ItemGroup.class);
            Root<ItemGroup> directItemGroupWitnessRoot =
                    directItemGroupWitnessQuery.from(ItemGroup.class);
            directItemGroupWitnessQuery.select(directItemGroupWitnessRoot)
                    .where(
                            builder.and(
                                    builder.in(directItemGroupWitnessRoot)
                                            .value(directItemGroupsQuery),
                                    builder.in(directItemGroupWitnessRoot.get(ItemGroup_.ID))
                                            .value(
                                                    root.get(Recipe_.ITEM_INPUTS)
                                                            .get(ItemGroup_.ID))));

            Subquery<ItemGroup> wildcardItemGroupWitnessQuery = query.subquery(ItemGroup.class);
            Root<ItemGroup> wildcardItemGroupWitnessRoot =
                    wildcardItemGroupWitnessQuery.from(ItemGroup.class);
            wildcardItemGroupWitnessQuery.select(wildcardItemGroupWitnessRoot)
                    .where(
                            builder.and(
                                    builder.in(wildcardItemGroupWitnessRoot)
                                            .value(wildcardItemGroupsQuery),
                                    builder.in(wildcardItemGroupWitnessRoot.get(ItemGroup_.ID))
                                            .value(
                                                    root.get(Recipe_.ITEM_INPUTS)
                                                            .get(ItemGroup_.ID))));

            return builder.or(
                    builder.exists(directItemGroupWitnessQuery),
                    builder.exists(wildcardItemGroupWitnessQuery));
        };
    }

    public static Specification<Recipe> buildInputItemGroupIdSpec(String itemGroupId) {
        return (root, query, builder) -> {
            // Unfortunately, we have to do this to ensure distinct query results.
            // This is necessary because Recipe.ITEM_INPUTS can contain duplicates.
            // We cannot simply call query.distinct() because that breaks sorting, for some reason.
            Subquery<ItemGroup> itemGroupWitnessQuery = query.subquery(ItemGroup.class);
            Root<ItemGroup> itemGroupWitnessRoot = itemGroupWitnessQuery.from(ItemGroup.class);
            itemGroupWitnessQuery.select(itemGroupWitnessRoot)
                    .where(
                            builder.and(
                                    builder.equal(
                                            itemGroupWitnessRoot.get(ItemGroup_.ID), itemGroupId),
                                    builder.in(itemGroupWitnessRoot.get(ItemGroup_.ID))
                                            .value(
                                                    root.get(Recipe_.ITEM_INPUTS)
                                                            .get(ItemGroup_.ID))));

            return builder.exists(itemGroupWitnessQuery);
        };
    }

    /** Matches by regex. */
    public static Specification<Recipe> buildInputFluidNameSpec(String localizedName) {
        return (root, query, builder) -> {
            Subquery<Fluid> fluidQuery = query.subquery(Fluid.class);
            Root<Fluid> fluidRoot = fluidQuery.from(Fluid.class);
            fluidQuery.select(fluidRoot)
                    .where(
                            builder.isTrue(
                                    builder.function(
                                            "regexp_like",
                                            Boolean.class,
                                            fluidRoot.get(Fluid_.LOCALIZED_NAME),
                                            builder.literal(localizedName))));


            Subquery<FluidGroup> fluidGroupsQuery = query.subquery(FluidGroup.class);
            Root<FluidGroup> fluidGroupsRoot = fluidGroupsQuery.from(FluidGroup.class);
            Subquery<Fluid> fluidWitnessQuery = fluidGroupsQuery.subquery(Fluid.class);
            Root<Fluid> fluidWitnessRoot = fluidWitnessQuery.from(Fluid.class);
            fluidWitnessQuery.select(fluidWitnessRoot)
                    .where(
                            builder.and(
                                    builder.in(fluidWitnessRoot).value(fluidQuery),
                                    builder.in(fluidWitnessRoot)
                                            .value(
                                                    fluidGroupsRoot
                                                            .get(FluidGroup_.FLUID_STACKS)
                                                            .get(FluidStack_.FLUID))));
            fluidGroupsQuery.select(fluidGroupsRoot)
                    .where(builder.exists(fluidWitnessQuery));

            Subquery<FluidGroup> fluidGroupWitnessQuery = query.subquery(FluidGroup.class);
            Root<FluidGroup> fluidGroupWitnessRoot =
                    fluidGroupWitnessQuery.from(FluidGroup.class);
            fluidGroupWitnessQuery.select(fluidGroupWitnessRoot)
                    .where(
                            builder.and(
                                    builder.in(fluidGroupWitnessRoot)
                                            .value(fluidGroupsQuery),
                                    builder.in(fluidGroupWitnessRoot.get(FluidGroup_.ID))
                                            .value(
                                                    root.get(Recipe_.FLUID_INPUTS)
                                                            .get(FluidGroup_.ID))));

            return builder.exists(fluidGroupWitnessQuery);
        };
    }

    public static Specification<Recipe> buildInputFluidIdSpec(String fluidId) {
        return (root, query, builder) -> {
            Subquery<FluidGroup> fluidGroupsQuery = query.subquery(FluidGroup.class);
            Root<FluidGroup> fluidGroupsRoot = fluidGroupsQuery.from(FluidGroup.class);
            fluidGroupsQuery.select(fluidGroupsRoot)
                    .where(
                            builder.in(builder.literal(fluidId))
                                    .value(
                                            fluidGroupsRoot
                                                    .get(FluidGroup_.FLUID_STACKS)
                                                    .get(FluidStack_.FLUID)
                                                    .get(Fluid_.ID)));

            Subquery<FluidGroup> fluidGroupWitnessQuery = query.subquery(FluidGroup.class);
            Root<FluidGroup> fluidGroupWitnessRoot =
                    fluidGroupWitnessQuery.from(FluidGroup.class);
            fluidGroupWitnessQuery.select(fluidGroupWitnessRoot)
                    .where(
                            builder.and(
                                    builder.in(fluidGroupWitnessRoot)
                                            .value(fluidGroupsQuery),
                                    builder.in(fluidGroupWitnessRoot.get(FluidGroup_.ID))
                                            .value(
                                                    root.get(Recipe_.FLUID_INPUTS)
                                                            .get(FluidGroup_.ID))));

            return builder.exists(fluidGroupWitnessQuery);
        };
    }

    public static Specification<Recipe> buildInputFluidGroupIdSpec(String fluidGroupId) {
        return (root, query, builder) -> {
            // Unfortunately, we have to do this to ensure distinct query results.
            // This is necessary because Recipe.ITEM_INPUTS can contain duplicates.
            // We cannot simply call query.distinct() because that breaks sorting, for some reason.
            Subquery<FluidGroup> fluidGroupWitnessQuery = query.subquery(FluidGroup.class);
            Root<FluidGroup> fluidGroupWitnessRoot = fluidGroupWitnessQuery.from(FluidGroup.class);
            fluidGroupWitnessQuery.select(fluidGroupWitnessRoot)
                    .where(
                            builder.and(
                                    builder.equal(
                                            fluidGroupWitnessRoot.get(FluidGroup_.ID),
                                            fluidGroupId),
                                    builder.in(fluidGroupWitnessRoot.get(FluidGroup_.ID))
                                            .value(
                                                    root.get(Recipe_.FLUID_INPUTS)
                                                            .get(FluidGroup_.ID))));

            return builder.exists(fluidGroupWitnessQuery);
        };
    }

    /** Matches by regex. */
    public static Specification<Recipe> buildOutputItemNameSpec(String localizedName) {
        return (root, query, builder) -> {
            Subquery<Item> itemQuery = query.subquery(Item.class);
            Root<Item> itemRoot = itemQuery.from(Item.class);
            itemQuery.select(itemRoot)
                    .where(
                            builder.isTrue(
                                    builder.function(
                                            "regexp_like",
                                            Boolean.class,
                                            itemRoot.get(Item_.LOCALIZED_NAME),
                                            builder.literal(localizedName))));


            Subquery<Item> itemWitnessQuery = query.subquery(Item.class);
            Root<Item> itemWitnessRoot = itemWitnessQuery.from(Item.class);
            itemWitnessQuery.select(itemWitnessRoot)
                    .where(
                            builder.and(
                                    builder.in(itemWitnessRoot).value(itemQuery),
                                    builder.in(itemWitnessRoot)
                                            .value(
                                                    root
                                                            .get(Recipe_.ITEM_OUTPUTS)
                                                            .get(ItemStackWithProbability_.ITEM))));

            return builder.exists(itemWitnessQuery);
        };
    }

    public static Specification<Recipe> buildOutputItemIdSpec(String itemId) {
        return (root, query, builder) -> {
            // Unfortunately, we have to do this to ensure distinct query results.
            // This is necessary because Recipe.ITEM_INPUTS can contain duplicates.
            // We cannot simply call query.distinct() because that breaks sorting, for some reason.
            Subquery<Item> itemWitnessQuery = query.subquery(Item.class);
            Root<Item> itemWitnessRoot = itemWitnessQuery.from(Item.class);
            itemWitnessQuery.select(itemWitnessRoot)
                    .where(
                            builder.and(
                                    builder.equal(
                                            itemWitnessRoot.get(Item_.ID), itemId),
                                    builder.in(itemWitnessRoot)
                                            .value(
                                                    root.get(Recipe_.ITEM_OUTPUTS)
                                                            .get(ItemStackWithProbability_.ITEM))));

            return builder.exists(itemWitnessQuery);
        };
    }

    /** Matches by regex. */
    public static Specification<Recipe> buildOutputFluidNameSpec(String localizedName) {
        return (root, query, builder) -> {
            Subquery<Fluid> fluidQuery = query.subquery(Fluid.class);
            Root<Fluid> fluidRoot = fluidQuery.from(Fluid.class);
            fluidQuery.select(fluidRoot)
                    .where(
                            builder.isTrue(
                                    builder.function(
                                            "regexp_like",
                                            Boolean.class,
                                            fluidRoot.get(Fluid_.LOCALIZED_NAME),
                                            builder.literal(localizedName))));


            Subquery<Fluid> fluidWitnessQuery = query.subquery(Fluid.class);
            Root<Fluid> fluidWitnessRoot = fluidWitnessQuery.from(Fluid.class);
            fluidWitnessQuery.select(fluidWitnessRoot)
                    .where(
                            builder.and(
                                    builder.in(fluidWitnessRoot).value(fluidQuery),
                                    builder.in(fluidWitnessRoot)
                                            .value(
                                                    root
                                                            .get(Recipe_.FLUID_OUTPUTS)
                                                            .get(FluidStackWithProbability_.FLUID))));

            return builder.exists(fluidWitnessQuery);
        };
    }

    public static Specification<Recipe> buildOutputFluidIdSpec(String fluidId) {
        return (root, query, builder) -> {
            // Unfortunately, we have to do this to ensure distinct query results.
            // This is necessary because Recipe.FLUID_INPUTS can contain duplicates.
            // We cannot simply call query.distinct() because that breaks sorting, for some reason.
            Subquery<Fluid> fluidWitnessQuery = query.subquery(Fluid.class);
            Root<Fluid> fluidWitnessRoot = fluidWitnessQuery.from(Fluid.class);
            fluidWitnessQuery.select(fluidWitnessRoot)
                    .where(
                            builder.and(
                                    builder.equal(
                                            fluidWitnessRoot.get(Fluid_.ID), fluidId),
                                    builder.in(fluidWitnessRoot)
                                            .value(
                                                    root.get(Recipe_.FLUID_OUTPUTS)
                                                            .get(FluidStackWithProbability_.FLUID))));

            return builder.exists(fluidWitnessQuery);
        };
    }
}

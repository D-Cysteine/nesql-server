package com.github.dcysteine.nesql.server.plugin.base.spec;

import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup_;
import com.github.dcysteine.nesql.sql.base.item.ItemStack_;
import com.github.dcysteine.nesql.sql.base.item.Item_;
import com.github.dcysteine.nesql.sql.base.item.WildcardItemStack_;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe_;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class RecipeSpec {
    // Static class.
    private RecipeSpec() {}

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

    /** Searches by {@code Item} entity ID. */
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
}

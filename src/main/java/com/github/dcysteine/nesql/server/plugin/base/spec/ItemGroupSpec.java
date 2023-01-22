package com.github.dcysteine.nesql.server.plugin.base.spec;

import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup_;
import com.github.dcysteine.nesql.sql.base.item.ItemStack_;
import com.github.dcysteine.nesql.sql.base.item.Item_;
import com.github.dcysteine.nesql.sql.base.item.WildcardItemStack_;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class ItemGroupSpec {
    // Static class.
    private ItemGroupSpec() {}

    /** Matches by regex. */
    public static Specification<ItemGroup> buildItemNameSpec(String localizedName) {
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

            return builder.or(
                    builder.in(root).value(directItemGroupsQuery),
                    builder.in(root).value(wildcardItemGroupsQuery));
        };
    }

    public static Specification<ItemGroup> buildItemIdSpec(String itemId) {
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

            return builder.or(
                    builder.in(root).value(directItemGroupsQuery),
                    builder.in(root).value(wildcardItemGroupsQuery));
        };
    }

    public static Specification<ItemGroup> buildStackSizeSpec(int stackSize) {
        return (root, query, builder) -> {
            Subquery<ItemGroup> directItemGroupsQuery = query.subquery(ItemGroup.class);
            Root<ItemGroup> directItemGroupsRoot = directItemGroupsQuery.from(ItemGroup.class);
            directItemGroupsQuery.select(directItemGroupsRoot)
                    .where(
                            builder.in(builder.literal(stackSize))
                                    .value(
                                            directItemGroupsRoot
                                                    .get(ItemGroup_.ITEM_STACKS)
                                                    .get(ItemStack_.STACK_SIZE)));

            Subquery<ItemGroup> wildcardItemGroupsQuery = query.subquery(ItemGroup.class);
            Root<ItemGroup> wildcardItemGroupsRoot = wildcardItemGroupsQuery.from(ItemGroup.class);
            wildcardItemGroupsQuery.select(wildcardItemGroupsRoot)
                    .where(
                            builder.in(builder.literal(stackSize))
                                    .value(
                                            wildcardItemGroupsRoot
                                                    .get(ItemGroup_.WILDCARD_ITEM_STACKS)
                                                    .get(WildcardItemStack_.STACK_SIZE)));

            return builder.or(
                    builder.in(root).value(directItemGroupsQuery),
                    builder.in(root).value(wildcardItemGroupsQuery));
        };
    }

    public static Specification<ItemGroup> buildMinSizeSpec(int size) {
        return (root, query, builder) -> {
            Subquery<Integer> wildcardItemsQuery = query.subquery(Integer.class);
            Root<Item> wildcardItemsRoot = wildcardItemsQuery.from(Item.class);
            wildcardItemsQuery.select(builder.toInteger(builder.count(wildcardItemsRoot)))
                    .where(
                            builder.in(wildcardItemsRoot.get(Item_.ITEM_ID))
                                    .value(
                                            root
                                                    .get(ItemGroup_.WILDCARD_ITEM_STACKS)
                                                    .get(WildcardItemStack_.ITEM_ID)));

            return builder.greaterThanOrEqualTo(
                    builder.sum(
                            builder.size(root.get(ItemGroup_.ITEM_STACKS)),
                            wildcardItemsQuery),
                    size);
        };
    }

    public static Specification<ItemGroup> buildMaxSizeSpec(int size) {
        return (root, query, builder) -> {
            Subquery<Integer> wildcardItemsQuery = query.subquery(Integer.class);
            Root<Item> wildcardItemsRoot = wildcardItemsQuery.from(Item.class);
            wildcardItemsQuery.select(builder.toInteger(builder.count(wildcardItemsRoot)))
                    .where(
                            builder.in(wildcardItemsRoot.get(Item_.ITEM_ID))
                                    .value(
                                            root
                                                    .get(ItemGroup_.WILDCARD_ITEM_STACKS)
                                                    .get(WildcardItemStack_.ITEM_ID)));

            return builder.lessThanOrEqualTo(
                    builder.sum(
                            builder.size(root.get(ItemGroup_.ITEM_STACKS)),
                            wildcardItemsQuery),
                    size);
        };
    }

    public static Specification<ItemGroup> buildNoWildcardSpec() {
        return (root, query, builder) -> builder.isEmpty(root.get(ItemGroup_.WILDCARD_ITEM_STACKS));
    }

    public static Specification<ItemGroup> buildHasWildcardSpec() {
        return (root, query, builder) ->
                builder.isNotEmpty(root.get(ItemGroup_.WILDCARD_ITEM_STACKS));
    }
}
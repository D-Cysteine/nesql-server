package com.github.dcysteine.nesql.server.plugin.base.spec;

import com.github.dcysteine.nesql.server.common.util.QueryUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup_;
import com.github.dcysteine.nesql.sql.base.item.ItemStack_;
import com.github.dcysteine.nesql.sql.base.item.Item_;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class ItemGroupSpec {
    // Static class.
    private ItemGroupSpec() {}

    /** Matches by regex. */
    public static Specification<ItemGroup> buildItemNameSpec(String localizedName) {
        return (root, query, builder) -> {
            Subquery<String> itemQuery = query.subquery(String.class);
            Root<Item> itemRoot = itemQuery.from(Item.class);
            itemQuery.select(itemRoot.get(Item_.ID))
                    .where(
                            QueryUtil.regexMatch(
                                    builder,
                                    itemRoot.get(Item_.LOCALIZED_NAME),
                                    builder.literal(localizedName)));

            // This works for now, but will stop working if we ever add sorting.
            // We may need to add a new field in the row schema if we want to add sorting.
            query.distinct(true);
            return builder.equal(
                    root.get(ItemGroup_.ITEM_STACKS).get(ItemStack_.ITEM).get(Item_.ID),
                    builder.any(itemQuery));
        };
    }

    /** Matches by regex. */
    public static Specification<ItemGroup> buildItemModIdSpec(String modId) {
        return (root, query, builder) -> {
            Subquery<String> itemQuery = query.subquery(String.class);
            Root<Item> itemRoot = itemQuery.from(Item.class);
            itemQuery.select(itemRoot.get(Item_.ID))
                    .where(
                            QueryUtil.regexMatch(
                                    builder,
                                    itemRoot.get(Item_.MOD_ID),
                                    builder.literal(modId)));

            // This works for now, but will stop working if we ever add sorting.
            // We may need to add a new field in the row schema if we want to add sorting.
            query.distinct(true);
            return builder.equal(
                    root.get(ItemGroup_.ITEM_STACKS).get(ItemStack_.ITEM).get(Item_.ID),
                    builder.any(itemQuery));
        };
    }

    public static Specification<ItemGroup> buildItemIdSpec(String itemId) {
        return (root, query, builder) -> {
            // This works for now, but will stop working if we ever add sorting.
            // We may need to add a new field in the row schema if we want to add sorting.
            query.distinct(true);
            return builder.equal(
                    root.get(ItemGroup_.ITEM_STACKS).get(ItemStack_.ITEM).get(Item_.ID),
                    itemId);
        };
    }

    public static Specification<ItemGroup> buildStackSizeSpec(int stackSize) {
        return (root, query, builder) ->
                builder.in(builder.literal(stackSize))
                        .value(root.get(ItemGroup_.ITEM_STACKS).get(ItemStack_.STACK_SIZE));
    }

    public static Specification<ItemGroup> buildMinSizeSpec(int size) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(builder.size(root.get(ItemGroup_.ITEM_STACKS)), size);
    }

    public static Specification<ItemGroup> buildMaxSizeSpec(int size) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(builder.size(root.get(ItemGroup_.ITEM_STACKS)), size);
    }
}
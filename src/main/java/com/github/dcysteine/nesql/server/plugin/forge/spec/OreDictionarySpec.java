package com.github.dcysteine.nesql.server.plugin.forge.spec;

import com.github.dcysteine.nesql.server.common.util.QueryUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup_;
import com.github.dcysteine.nesql.sql.base.item.ItemStack_;
import com.github.dcysteine.nesql.sql.base.item.Item_;
import com.github.dcysteine.nesql.sql.forge.OreDictionary;
import com.github.dcysteine.nesql.sql.forge.OreDictionary_;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class OreDictionarySpec {
    public static final Sort DEFAULT_SORT = Sort.by(OreDictionary_.NAME, OreDictionary_.ID);

    // Static class.
    private OreDictionarySpec() {}

    /** Matches by regex. */
    public static Specification<OreDictionary> buildItemNameSpec(String localizedName) {
        return (root, query, builder) -> {
            Subquery<String> itemQuery = query.subquery(String.class);
            Root<Item> itemRoot = itemQuery.from(Item.class);
            itemQuery.select(itemRoot.get(Item_.ID))
                    .where(
                            QueryUtil.regexMatch(
                                    builder,
                                    itemRoot.get(Item_.LOCALIZED_NAME),
                                    builder.literal(localizedName)));

            Subquery<String> itemGroupQuery = query.subquery(String.class);
            Root<ItemGroup> itemGroupRoot = itemGroupQuery.from(ItemGroup.class);
            itemGroupQuery.select(itemGroupRoot.get(ItemGroup_.ID))
                    .where(
                            builder.and(
                                    builder.equal(
                                            itemGroupRoot.get(ItemGroup_.ID),
                                            root
                                                    .get(OreDictionary_.ITEM_GROUP)
                                                    .get(ItemGroup_.ID)),
                                    builder.equal(
                                            itemGroupRoot
                                                    .get(ItemGroup_.ITEM_STACKS)
                                                    .get(ItemStack_.ITEM)
                                                    .get(Item_.ID),
                                            builder.any(itemQuery))));

            return builder.exists(itemGroupQuery);
        };
    }

    /** Matches by regex. */
    public static Specification<OreDictionary> buildItemModIdSpec(String modId) {
        return (root, query, builder) -> {
            Subquery<String> itemQuery = query.subquery(String.class);
            Root<Item> itemRoot = itemQuery.from(Item.class);
            itemQuery.select(itemRoot.get(Item_.ID))
                    .where(
                            QueryUtil.regexMatch(
                                    builder,
                                    itemRoot.get(Item_.MOD_ID),
                                    builder.literal(modId)));

            Subquery<String> itemGroupQuery = query.subquery(String.class);
            Root<ItemGroup> itemGroupRoot = itemGroupQuery.from(ItemGroup.class);
            itemGroupQuery.select(itemGroupRoot.get(ItemGroup_.ID))
                    .where(
                            builder.and(
                                    builder.equal(
                                            itemGroupRoot.get(ItemGroup_.ID),
                                            root
                                                    .get(OreDictionary_.ITEM_GROUP)
                                                    .get(ItemGroup_.ID)),
                                    builder.equal(
                                            itemGroupRoot
                                                    .get(ItemGroup_.ITEM_STACKS)
                                                    .get(ItemStack_.ITEM)
                                                    .get(Item_.ID),
                                            builder.any(itemQuery))));

            return builder.exists(itemGroupQuery);
        };
    }

    public static Specification<OreDictionary> buildItemIdSpec(String itemId) {
        return (root, query, builder) -> {
            Subquery<String> itemGroupQuery = query.subquery(String.class);
            Root<ItemGroup> itemGroupRoot = itemGroupQuery.from(ItemGroup.class);
            itemGroupQuery.select(itemGroupRoot.get(ItemGroup_.ID))
                    .where(
                            builder.and(
                                    builder.equal(
                                            itemGroupRoot.get(ItemGroup_.ID),
                                            root
                                                    .get(OreDictionary_.ITEM_GROUP)
                                                    .get(ItemGroup_.ID)),
                                    builder.equal(
                                            itemGroupRoot
                                                    .get(ItemGroup_.ITEM_STACKS)
                                                    .get(ItemStack_.ITEM)
                                                    .get(Item_.ID),
                                            itemId)));

            return builder.exists(itemGroupQuery);
        };
    }

    /** Matches by regex. */
    public static Specification<OreDictionary> buildOreNameSpec(String oreName) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(OreDictionary_.NAME),
                        builder.literal(oreName));
    }

    public static Specification<OreDictionary> buildMinSizeSpec(int size) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(
                        builder.size(
                                root.get(OreDictionary_.ITEM_GROUP).get(ItemGroup_.ITEM_STACKS)),
                        size);
    }

    public static Specification<OreDictionary> buildMaxSizeSpec(int size) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(
                        builder.size(
                                root.get(OreDictionary_.ITEM_GROUP).get(ItemGroup_.ITEM_STACKS)),
                        size);
    }

    /** Finds Ore Dictionary entries by base item group. */
    public static Specification<OreDictionary> buildBaseItemGroupIdSpec(String itemGroupId) {
        return (root, query, builder) -> {
            Subquery<String> baseItemGroupQuery = query.subquery(String.class);
            Root<ItemGroup> baseItemGroupRoot = baseItemGroupQuery.from(ItemGroup.class);
            baseItemGroupQuery.select(
                            baseItemGroupRoot
                                    .get(ItemGroup_.BASE_ITEM_GROUP)
                                    .get(ItemGroup_.ID))
                    .where(builder.equal(baseItemGroupRoot.get(ItemGroup_.ID), itemGroupId));

            return builder.equal(
                    root.get(OreDictionary_.ITEM_GROUP).get(ItemGroup_.ID), baseItemGroupQuery);
        };
    }
}
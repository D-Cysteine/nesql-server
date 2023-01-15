package com.github.dcysteine.nesql.server.plugin.base.specs;

import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.Item_;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecs {
    // Static class.
    private ItemSpecs() {}

    public static Sort DEFAULT_SORT =
            Sort.by(Item_.ITEM_ID, Item_.ITEM_DAMAGE, Item_.NBT, Item_.ID);

    /** Matches by regex. */
    public static Specification<Item> buildLocalizedNameSpec(String localizedName) {
        return (root, query, builder) -> {
            return builder.isTrue(
                    builder.function(
                            "regexp_like",
                            Boolean.class,
                            root.get(Item_.LOCALIZED_NAME),
                            builder.literal(localizedName)));
        };
    }

    /** Matches by regex. */
    public static Specification<Item> buildInternalNameSpec(String internalName) {
        return (root, query, builder) -> {
            return builder.isTrue(
                    builder.function(
                            "regexp_like",
                            Boolean.class,
                            root.get(Item_.INTERNAL_NAME),
                            builder.literal(internalName)));
        };
    }

    /** Matches by regex. */
    public static Specification<Item> buildModIdSpec(String modId) {
        return (root, query, builder) -> {
            return builder.isTrue(
                    builder.function(
                            "regexp_like",
                            Boolean.class,
                            root.get(Item_.MOD_ID),
                            builder.literal(modId)));
        };
    }

    /** Matches by Minecraft item ID. */
    public static Specification<Item> buildItemIdSpec(int itemId) {
        return (root, query, builder) -> {
            return builder.equal(root.get(Item_.ITEM_ID), itemId);
        };
    }

    public static Specification<Item> buildItemDamageSpec(int itemDamage) {
        return (root, query, builder) -> {
            return builder.equal(root.get(Item_.ITEM_DAMAGE), itemDamage);
        };
    }

    /** Matches by regex. */
    public static Specification<Item> buildNbtSpec(String nbt) {
        return (root, query, builder) -> {
            return builder.isTrue(
                    builder.function(
                            "regexp_like",
                            Boolean.class,
                            root.get(Item_.NBT),
                            builder.literal(nbt)));
        };
    }

    public static Specification<Item> buildNullNbtSpec() {
        return (root, query, builder) -> {
            return builder.isNull(root.get(Item_.NBT));
        };
    }

    /** Matches by regex. */
    public static Specification<Item> buildTooltipSpec(String tooltip) {
        return (root, query, builder) -> {
            return builder.isTrue(
                    builder.function(
                            "regexp_like",
                            Boolean.class,
                            root.get(Item_.TOOLTIP),
                            builder.literal(tooltip)));
        };
    }
}

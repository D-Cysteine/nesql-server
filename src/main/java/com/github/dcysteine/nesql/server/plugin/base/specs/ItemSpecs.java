package com.github.dcysteine.nesql.server.plugin.base.specs;

import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.Item_;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecs {
    // Static class.
    private ItemSpecs() {}

    /** Matches by regex. */
    public static Specification<Item> buildLocalizedNameSpec(String localizedName) {
        return (root, query, builder) -> {
            return builder.like(root.get(Item_.LOCALIZED_NAME), localizedName);
        };
    }

    /** Matches by regex. */
    public static Specification<Item> buildInternalNameSpec(String internalName) {
        return (root, query, builder) -> {
            return builder.like(root.get(Item_.INTERNAL_NAME), internalName);
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
}

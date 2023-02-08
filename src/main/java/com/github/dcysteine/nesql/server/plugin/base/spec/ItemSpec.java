package com.github.dcysteine.nesql.server.plugin.base.spec;

import com.github.dcysteine.nesql.server.common.util.QueryUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup_;
import com.github.dcysteine.nesql.sql.base.item.ItemStack_;
import com.github.dcysteine.nesql.sql.base.item.Item_;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe_;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpec {
    public static final Sort DEFAULT_SORT =
            Sort.by(Item_.MOD_ID, Item_.INTERNAL_NAME, Item_.ITEM_DAMAGE, Item_.NBT, Item_.ID);

    // Static class.
    private ItemSpec() {}

    /** Matches by regex. */
    public static Specification<Item> buildLocalizedNameSpec(String localizedName) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(Item_.LOCALIZED_NAME),
                        builder.literal(localizedName));
    }

    /** Matches by regex. */
    public static Specification<Item> buildModIdSpec(String modId) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(Item_.MOD_ID),
                        builder.literal(modId));
    }

    /** Matches by regex. */
    public static Specification<Item> buildInternalNameSpec(String internalName) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(Item_.INTERNAL_NAME),
                        builder.literal(internalName));
    }

    /** Matches by Minecraft item ID. */
    public static Specification<Item> buildItemIdSpec(int itemId) {
        return (root, query, builder) -> builder.equal(root.get(Item_.ITEM_ID), itemId);
    }

    public static Specification<Item> buildItemDamageSpec(int itemDamage) {
        return (root, query, builder) -> builder.equal(root.get(Item_.ITEM_DAMAGE), itemDamage);
    }

    /** Matches by regex. */
    public static Specification<Item> buildTooltipSpec(String tooltip) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(Item_.TOOLTIP),
                        builder.literal(tooltip));
    }

    /** Matches by regex. */
    public static Specification<Item> buildNbtSpec(String nbt) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(Item_.NBT),
                        builder.literal(nbt));
    }

    /** Finds items that belong to the specified item group. */
    public static Specification<Item> buildItemGroupSpec(String itemGroupId) {
        return (root, query, builder) -> {
            Subquery<Item> itemsQuery = query.subquery(Item.class);
            Root<ItemGroup> itemGroupRoot = itemsQuery.from(ItemGroup.class);
            itemsQuery.select(itemGroupRoot.get(ItemGroup_.ITEM_STACKS).get(ItemStack_.ITEM))
                    .where(builder.equal(itemGroupRoot.get(ItemGroup_.ID), itemGroupId));

            return builder.in(root).value(itemsQuery);
        };
    }

    /** Finds items that are inputs to the specified recipe. */
    public static Specification<Item> buildRecipeInputSpec(String recipeId) {
        return (root, query, builder) -> {
            Subquery<Item> itemsQuery = query.subquery(Item.class);
            Root<Recipe> directRecipeRoot = itemsQuery.from(Recipe.class);
            itemsQuery.select(
                            directRecipeRoot
                                    .get(Recipe_.ITEM_INPUTS)
                                    .get(ItemGroup_.ITEM_STACKS)
                                    .get(ItemStack_.ITEM))
                    .where(builder.equal(directRecipeRoot.get(Recipe_.ID), recipeId));

            return builder.in(root).value(itemsQuery);
        };
    }
}

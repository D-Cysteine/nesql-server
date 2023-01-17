package com.github.dcysteine.nesql.server.plugin.base.spec;

import com.github.dcysteine.nesql.sql.base.item.Item;
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

    /** This is it: the most complex query in the server (so far). Hope it isn't too slow... */
    public static Specification<Recipe> buildInputItemNameSpec(String localizedName) {
        return (root, query, builder) -> {
            // We have to split the two item inclusion paths (direct and wildcard) into separate
            // subqueries, as otherwise we'll only select item groups that have at least one direct
            // and one wildcard item stack, due to how joins work.
            Subquery<String> directRecipeIdQuery = query.subquery(String.class);
            Root<Recipe> directRecipeRoot = directRecipeIdQuery.from(Recipe.class);
            directRecipeIdQuery.select(
                            directRecipeRoot.get(Recipe_.ITEM_INPUTS)
                                    .get(ItemGroup_.ITEM_STACKS)
                                    .get(ItemStack_.ITEM)
                                    .get(Item_.ID))
                    .where(builder.equal(directRecipeRoot.get(Recipe_.ID), root.get(Recipe_.ID)));

            Subquery<Integer> wildcardRecipeIdQuery = query.subquery(Integer.class);
            Root<Recipe> wildcardRecipeRoot = wildcardRecipeIdQuery.from(Recipe.class);
            wildcardRecipeIdQuery.select(
                            wildcardRecipeRoot.get(Recipe_.ITEM_INPUTS)
                                    .get(ItemGroup_.WILDCARD_ITEM_STACKS)
                                    .get(WildcardItemStack_.ITEM_ID))
                    .where(builder.equal(wildcardRecipeRoot.get(Recipe_.ID), root.get(Recipe_.ID)));

            Subquery<String> itemIdQuery = query.subquery(String.class);
            Root<Item> itemIdRoot = itemIdQuery.from(Item.class);
            itemIdQuery.select(itemIdRoot.get(Item_.ID))
                    .where(
                            builder.function(
                                    "regexp_like",
                                    Boolean.class,
                                    itemIdRoot.get(Item_.LOCALIZED_NAME),
                                    builder.literal(localizedName)));

            Subquery<String> itemQuery = query.subquery(String.class);
            Root<Item> itemRoot = itemQuery.from(Item.class);
            itemQuery.select(itemRoot.get(Item_.ID))
                    .where(
                            builder.and(
                                    builder.in(itemRoot.get(Item_.ID)).value(itemIdQuery),
                                    builder.or(
                                            builder.in(itemRoot.get(Item_.ID))
                                                    .value(directRecipeIdQuery),
                                            builder.in(itemRoot.get(Item_.ITEM_ID))
                                                    .value(wildcardRecipeIdQuery))));

            return builder.exists(itemQuery);
        };
    }

    /** Searches by {@code Item} entity ID. */
    public static Specification<Recipe> buildInputItemIdSpec(String itemId) {
        return (root, query, builder) -> {
            // We have to split the two item inclusion paths (direct and wildcard) into separate
            // subqueries, as otherwise we'll only select item groups that have at least one direct
            // and one wildcard item stack, due to how joins work.
            Subquery<String> directRecipeIdQuery = query.subquery(String.class);
            Root<Recipe> directRecipeRoot = directRecipeIdQuery.from(Recipe.class);
            directRecipeIdQuery.select(
                            directRecipeRoot.get(Recipe_.ITEM_INPUTS)
                                    .get(ItemGroup_.ITEM_STACKS)
                                    .get(ItemStack_.ITEM)
                                    .get(Item_.ID))
                    .where(builder.equal(directRecipeRoot.get(Recipe_.ID), root.get(Recipe_.ID)));

            Subquery<Integer> wildcardRecipeIdQuery = query.subquery(Integer.class);
            Root<Recipe> wildcardRecipeRoot = wildcardRecipeIdQuery.from(Recipe.class);
            wildcardRecipeIdQuery.select(
                            wildcardRecipeRoot.get(Recipe_.ITEM_INPUTS)
                                    .get(ItemGroup_.WILDCARD_ITEM_STACKS)
                                    .get(WildcardItemStack_.ITEM_ID))
                    .where(builder.equal(wildcardRecipeRoot.get(Recipe_.ID), root.get(Recipe_.ID)));

            Subquery<String> itemQuery = query.subquery(String.class);
            Root<Item> itemRoot = itemQuery.from(Item.class);
            itemQuery.select(itemRoot.get(Item_.ID))
                    .where(
                            builder.and(
                                    builder.equal(itemRoot.get(Item_.ID), itemId),
                                    builder.or(
                                            builder.in(itemRoot.get(Item_.ID))
                                                    .value(directRecipeIdQuery),
                                            builder.in(itemRoot.get(Item_.ITEM_ID))
                                                    .value(wildcardRecipeIdQuery))));

            return builder.exists(itemQuery);
        };
    }
}

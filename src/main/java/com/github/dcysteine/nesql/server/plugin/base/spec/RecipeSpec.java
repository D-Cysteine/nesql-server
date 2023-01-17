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

    /** Searches by {@code Item} entity ID. */
    public static Specification<Recipe> buildInputItemSpec(String itemId) {
        return (root, query, builder) -> {
            Subquery<Integer> minecraftItemIdQuery = query.subquery(Integer.class);
            Root<Item> itemRoot = minecraftItemIdQuery.from(Item.class);
            minecraftItemIdQuery.select(itemRoot.get(Item_.ITEM_ID))
                    .where(builder.equal(itemRoot.get(Item_.ID), itemId));

            // We have to split the two item inclusion paths (direct and wildcard) into separate
            // subqueries, as otherwise we'll only select item groups that have at least one direct
            // and one wildcard item stack, due to how joins work.
            Subquery<String> directRecipeIdQuery = query.subquery(String.class);
            Root<Recipe> directRecipeRoot = directRecipeIdQuery.from(Recipe.class);
            directRecipeIdQuery.select(directRecipeRoot.get(Recipe_.ID))
                    .where(
                            builder.in(builder.literal(itemId))
                                    .value(
                                            directRecipeRoot.get(Recipe_.ITEM_INPUTS)
                                                    .get(ItemGroup_.ITEM_STACKS)
                                                    .get(ItemStack_.ITEM)
                                                    .get(Item_.ID)));

            Subquery<String> wildcardRecipeIdQuery = query.subquery(String.class);
            Root<Recipe> wildcardRecipeRoot = wildcardRecipeIdQuery.from(Recipe.class);
            wildcardRecipeIdQuery.select(wildcardRecipeRoot.get(Recipe_.ID))
                    .where(
                            builder.in(minecraftItemIdQuery)
                                    .value(
                                            wildcardRecipeRoot.get(Recipe_.ITEM_INPUTS)
                                                    .get(ItemGroup_.WILDCARD_ITEM_STACKS)
                                                    .get(WildcardItemStack_.ITEM_ID)));

            return builder.or(
                    builder.in(root.get(Recipe_.ID)).value(directRecipeIdQuery),
                    builder.in(root.get(Recipe_.ID)).value(wildcardRecipeIdQuery));
        };
    }
}

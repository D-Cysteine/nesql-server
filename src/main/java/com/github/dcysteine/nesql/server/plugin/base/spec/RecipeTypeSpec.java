package com.github.dcysteine.nesql.server.plugin.base.spec;

import com.github.dcysteine.nesql.server.common.util.QueryUtil;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType_;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class RecipeTypeSpec {
    // Static class.
    private RecipeTypeSpec() {}

    public static final Sort DEFAULT_SORT = Sort.by(RecipeType_.CATEGORY, RecipeType_.TYPE);

    /** Matches by regex. */
    public static Specification<RecipeType> buildRecipeCategorySpec(String recipeCategory) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(RecipeType_.CATEGORY),
                        builder.literal(recipeCategory));
    }

    /** Matches by regex. */
    public static Specification<RecipeType> buildRecipeTypeSpec(String recipeType) {
        return (root, query, builder) ->
                QueryUtil.regexMatch(
                        builder,
                        root.get(RecipeType_.TYPE),
                        builder.literal(recipeType));
    }
}

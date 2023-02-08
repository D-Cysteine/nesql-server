package com.github.dcysteine.nesql.server.plugin.base.spec;

import com.github.dcysteine.nesql.server.common.util.QueryUtil;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType_;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe_;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class RecipeTypeSpec {
    public static final Sort DEFAULT_SORT = Sort.by(RecipeType_.CATEGORY, RecipeType_.TYPE);

    // Static class.
    private RecipeTypeSpec() {}

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

    public static Specification<RecipeType> buildMinRecipeCountSpec(long recipeCount) {
        return (root, query, builder) -> {
            Subquery<Long> recipeQuery = query.subquery(Long.class);
            Root<Recipe> recipeRoot = recipeQuery.from(Recipe.class);
            recipeQuery.select(builder.count(recipeRoot))
                    .where(builder.equal(recipeRoot.get(Recipe_.RECIPE_TYPE), root));

            return builder.greaterThanOrEqualTo(recipeQuery, recipeCount);
        };
    }

    public static Specification<RecipeType> buildMaxRecipeCountSpec(long recipeCount) {
        return (root, query, builder) -> {
            Subquery<Long> recipeQuery = query.subquery(Long.class);
            Root<Recipe> recipeRoot = recipeQuery.from(Recipe.class);
            recipeQuery.select(builder.count(recipeRoot))
                    .where(builder.equal(recipeRoot.get(Recipe_.RECIPE_TYPE), root));

            return builder.lessThanOrEqualTo(recipeQuery, recipeCount);
        };
    }
}

package com.github.dcysteine.nesql.server.plugin.gregtech;

import com.github.dcysteine.nesql.sql.base.recipe.Recipe_;
import com.github.dcysteine.nesql.sql.gregtech.GregTechRecipe;
import com.github.dcysteine.nesql.sql.gregtech.GregTechRecipe_;
import org.springframework.data.jpa.domain.Specification;

public class GregTechRecipeSpec {
    // Static class.
    private GregTechRecipeSpec() {}

    public static Specification<GregTechRecipe> buildRecipeIdSpec(String recipeId) {
        return (root, query, builder) ->
                builder.equal(root.get(GregTechRecipe_.RECIPE).get(Recipe_.ID), recipeId);
    }
}
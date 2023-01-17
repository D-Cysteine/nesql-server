package com.github.dcysteine.nesql.server.plugin.base.display.recipe;

import com.github.dcysteine.nesql.server.display.Icon;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayDeps;
import com.github.dcysteine.nesql.server.util.StringUtil;
import com.github.dcysteine.nesql.server.util.UrlBuilder;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DisplayRecipeType implements Comparable<DisplayRecipeType> {
    public static DisplayRecipeType create(RecipeType recipeType, BaseDisplayDeps deps) {
        return new AutoValue_DisplayRecipeType(
                recipeType, buildIcon(recipeType, deps),
                StringUtil.prettyPrintDimension(recipeType.getItemInputDimension()),
                StringUtil.prettyPrintDimension(recipeType.getFluidInputDimension()),
                StringUtil.prettyPrintDimension(recipeType.getItemOutputDimension()),
                StringUtil.prettyPrintDimension(recipeType.getFluidOutputDimension()));
    }

    public static Icon buildIcon(RecipeType recipeType, BaseDisplayDeps deps) {
        return Icon.builder()
                .setDescription(
                        String.format("%s: %s", recipeType.getCategory(), recipeType.getType()))
                .setUrl(UrlBuilder.buildRecipeTypeUrl(recipeType))
                .setImage(recipeType.getIcon().getImageFilePath())
                .build();
    }

    public abstract RecipeType getRecipeType();
    public abstract Icon getIcon();
    public abstract String getItemInputDimension();
    public abstract String getFluidInputDimension();
    public abstract String getItemOutputDimension();
    public abstract String getFluidOutputDimension();

    @Override
    public int compareTo(DisplayRecipeType other) {
        return getRecipeType().compareTo(other.getRecipeType());
    }
}
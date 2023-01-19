package com.github.dcysteine.nesql.server.plugin.base.display.recipe;

import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.util.StringUtil;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayService;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class DisplayRecipeType implements Comparable<DisplayRecipeType> {
    public static DisplayRecipeType create(RecipeType recipeType, BaseDisplayService service) {
        return new AutoValue_DisplayRecipeType(
                recipeType, buildIcon(recipeType, service),
                StringUtil.prettyPrintDimension(recipeType.getItemInputDimension()),
                StringUtil.prettyPrintDimension(recipeType.getFluidInputDimension()),
                StringUtil.prettyPrintDimension(recipeType.getItemOutputDimension()),
                StringUtil.prettyPrintDimension(recipeType.getFluidOutputDimension()),
                service.getAdditionalInfo(recipeType));
    }

    public static Icon buildIcon(RecipeType recipeType, BaseDisplayService service) {
        return Icon.builder()
                .setDescription(
                        String.format("%s: %s", recipeType.getCategory(), recipeType.getType()))
                .setUrl(Table.RECIPE_TYPE.getViewUrl(recipeType))
                .setImage(recipeType.getIcon().getImageFilePath())
                .build();
    }

    public abstract RecipeType getRecipeType();
    public abstract Icon getIcon();
    public abstract String getItemInputDimension();
    public abstract String getFluidInputDimension();
    public abstract String getItemOutputDimension();
    public abstract String getFluidOutputDimension();
    public abstract ImmutableList<InfoPanel> getAdditionalInfo();

    @Override
    public int compareTo(DisplayRecipeType other) {
        return getRecipeType().compareTo(other.getRecipeType());
    }
}
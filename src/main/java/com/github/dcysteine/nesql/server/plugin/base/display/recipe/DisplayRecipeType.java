package com.github.dcysteine.nesql.server.plugin.base.display.recipe;

import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.server.common.util.StringUtil;
import com.github.dcysteine.nesql.server.plugin.base.spec.RecipeSpec;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.Map;

@AutoValue
public abstract class DisplayRecipeType implements Comparable<DisplayRecipeType> {
    private static final Map<RecipeType, Long> cachedRecipeCounts = new HashMap<>();

    private static long getRecipeCount(RecipeType recipeType, DisplayService service) {
        return cachedRecipeCounts.computeIfAbsent(
                recipeType,
                rt ->
                        service.getRecipeRepository()
                                .count(RecipeSpec.buildRecipeTypeIdSpec(rt.getId())));
    }
    public static DisplayRecipeType create(RecipeType recipeType, DisplayService service) {
        long recipeCount = getRecipeCount(recipeType, service);

        return new AutoValue_DisplayRecipeType(
                recipeType, buildIcon(recipeType, service), recipeCount,
                StringUtil.prettyPrintDimension(recipeType.getItemInputDimension()),
                StringUtil.prettyPrintDimension(recipeType.getFluidInputDimension()),
                StringUtil.prettyPrintDimension(recipeType.getItemOutputDimension()),
                StringUtil.prettyPrintDimension(recipeType.getFluidOutputDimension()),
                service.buildAdditionalInfo(RecipeType.class, recipeType));
    }

    public static Icon buildIcon(RecipeType recipeType, DisplayService service) {
        long recipeCount = getRecipeCount(recipeType, service);

        Icon.Builder builder = Icon.builder()
                .setDescription(
                        String.format("%s: %s", recipeType.getCategory(), recipeType.getType()))
                .setUrl(Table.RECIPE_TYPE.getViewUrl(recipeType))
                .setImage(recipeType.getIcon().getImageFilePath())
                .setBottomRight(NumberUtil.formatCompact(recipeCount));

        if (recipeType.isShapeless()) {
            builder.setTopLeft("*");
        }

        return builder.build();
    }

    public abstract RecipeType getRecipeType();
    public abstract Icon getIcon();
    public abstract long getRecipeCount();
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
package com.github.dcysteine.nesql.server.plugin.base.display.recipe;

import com.github.dcysteine.nesql.server.Main;
import com.github.dcysteine.nesql.server.common.Constants;
import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayService;
import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluidGroup;
import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluidStackWithProbability;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItemGroup;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItemStackWithProbability;
import com.github.dcysteine.nesql.sql.base.recipe.Dimension;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Packages up a {@link Recipe} for displaying on a page.
 *
 * <p>Inputs and outputs are returned as {@link ImmutableTable}s, to help with displaying them in
 * grids.
 */
@AutoValue
public abstract class DisplayRecipe implements Comparable<DisplayRecipe> {
    public static DisplayRecipe create(Recipe recipe, BaseDisplayService service) {
        RecipeType recipeType = recipe.getRecipeType();

        Map<Integer, Icon> displayItemInputs =
                Maps.transformValues(
                        recipe.getItemInputs(),
                        itemGroup -> DisplayItemGroup.buildIcon(itemGroup, service));
        ImmutableTable<Integer, Integer, Icon> itemInputs =
                buildIngredientsGrid(
                        recipeType.getItemInputDimension(), displayItemInputs, recipe);

        Map<Integer, Icon> displayFluidInputs =
                Maps.transformValues(
                        recipe.getFluidInputs(),
                        fluidGroup -> DisplayFluidGroup.buildIcon(fluidGroup, service));
        ImmutableTable<Integer, Integer, Icon> fluidInputs =
                buildIngredientsGrid(
                        recipeType.getFluidInputDimension(), displayFluidInputs, recipe);

        Map<Integer, Icon> displayItemOutputs =
                Maps.transformValues(
                        recipe.getItemOutputs(),
                        itemStack -> DisplayItemStackWithProbability.buildIcon(itemStack, service));
        ImmutableTable<Integer, Integer, Icon> itemOutputs =
                buildIngredientsGrid(
                        recipeType.getItemOutputDimension(), displayItemOutputs, recipe);

        Map<Integer, Icon> displayFluidOutputs =
                Maps.transformValues(
                        recipe.getFluidOutputs(),
                        fluidStack -> DisplayFluidStackWithProbability.buildIcon(fluidStack, service));
        ImmutableTable<Integer, Integer, Icon> fluidOutputs =
                buildIngredientsGrid(
                        recipeType.getFluidOutputDimension(), displayFluidOutputs, recipe);

        return new AutoValue_DisplayRecipe(
                recipe, buildIcon(recipe, service),
                itemInputs, fluidInputs, itemOutputs, fluidOutputs,
                service.getAdditionalInfo(recipe));
    }

    public static Icon buildIcon(Recipe recipe, BaseDisplayService service) {
        RecipeType recipeType = recipe.getRecipeType();
        int numItemOutputs = recipe.getItemOutputs().size();
        int numFluidOutputs = recipe.getFluidOutputs().size();

        String description = recipeType.getCategory() + " " + recipeType.getType();
        String url = Table.RECIPE.getViewUrl(recipe);
        Icon icon;
        if (numItemOutputs > 0) {
            Icon innerIcon =
                    DisplayItemStackWithProbability.buildIcon(recipe.getItemOutputs().get(0), service);
            if (numItemOutputs == 1) {
                description += String.format(" (%s)", innerIcon.getDescription());
            } else if (numFluidOutputs > 0) {
                description +=
                        String.format(
                                " (%s items, %s fluids)",
                                NumberUtil.formatInteger(numItemOutputs),
                                NumberUtil.formatInteger(numFluidOutputs));
            } else {
                description +=
                        String.format(" (%s items)", NumberUtil.formatInteger(numItemOutputs));
            }

            icon = innerIcon.toBuilder()
                    .setDescription(description)
                    .setUrl(url)
                    .setBottomLeftImage(recipe.getRecipeType().getIcon().getImageFilePath())
                    .build();
        } else if (numFluidOutputs > 0) {
            Icon innerIcon =
                    DisplayFluidStackWithProbability.buildIcon(
                            recipe.getFluidOutputs().get(0), service);
            if (numFluidOutputs == 1) {
                description += String.format(" (%s)", innerIcon.getDescription());
            } else {
                description +=
                        String.format(" (%s fluids)", NumberUtil.formatInteger(numFluidOutputs));
            }

            icon = innerIcon.toBuilder()
                    .setDescription(description)
                    .setUrl(url)
                    .setBottomLeftImage(recipe.getRecipeType().getIcon().getImageFilePath())
                    .build();
        } else {
            icon = Icon.builder()
                    .setDescription(description + " (empty)")
                    .setUrl(url)
                    .setImage(Constants.MISSING_IMAGE)
                    .setBottomLeftImage(recipe.getRecipeType().getIcon().getImageFilePath())
                    .build();
        }
        return icon;
    }

    public abstract Recipe getRecipe();
    public abstract Icon getIcon();
    public abstract ImmutableTable<Integer, Integer, Icon> getItemInputs();
    public abstract ImmutableTable<Integer, Integer, Icon> getFluidInputs();
    public abstract ImmutableTable<Integer, Integer, Icon> getItemOutputs();
    public abstract ImmutableTable<Integer, Integer, Icon> getFluidOutputs();
    public abstract ImmutableList<InfoPanel> getAdditionalInfo();

    private static ImmutableTable<Integer, Integer, Icon> buildIngredientsGrid(
            Dimension gridDimension, Map<Integer, Icon> ingredients, Recipe recipe) {
        ImmutableTable.Builder<Integer, Integer, Icon> builder = ImmutableTable.builder();
        int excessEntries = 0;
        int maxIndex = 0;
        for (Map.Entry<Integer, Icon> entry : ingredients.entrySet()) {
            int row = entry.getKey() / gridDimension.getWidth();
            int col = entry.getKey() % gridDimension.getWidth();
            builder.put(row, col, entry.getValue());

            if (row >= gridDimension.getHeight()) {
                excessEntries++;
                maxIndex = Math.max(maxIndex, entry.getKey());
            }
        }

        if (excessEntries > 0) {
            Main.Logger.error(
                    "Tried to build recipe ingredients grid with too many ingredients!\n"
                            + "Expected {}x{}; got {} excess, {} max index for recipe {}",
                    gridDimension.getHeight(), gridDimension.getWidth(),
                    excessEntries, maxIndex, recipe.getId());
        }

        return builder.build();
    }

    @Override
    public int compareTo(DisplayRecipe other) {
        return getRecipe().compareTo(other.getRecipe());
    }
}
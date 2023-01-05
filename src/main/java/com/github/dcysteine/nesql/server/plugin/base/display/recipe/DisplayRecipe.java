package com.github.dcysteine.nesql.server.plugin.base.display.recipe;

import com.github.dcysteine.nesql.server.display.Dimension;
import com.github.dcysteine.nesql.server.display.Icon;
import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluidGroup;
import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluidStackWithProbability;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItemGroup;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItemStackWithProbability;
import com.github.dcysteine.nesql.server.util.Constants;
import com.github.dcysteine.nesql.server.Main;
import com.github.dcysteine.nesql.server.util.UrlBuilder;
import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;

/**
 * Packages up a {@link Recipe} for displaying on a page.
 *
 * <p>Inputs and outputs are returned as {@link ImmutableTable}s, to help with displaying them in
 * grids.
 */
@AutoValue
public abstract class DisplayRecipe implements Comparable<DisplayRecipe> {
    public static DisplayRecipe create(Recipe recipe, ItemRepository itemRepository) {
        DisplayRecipeType displayRecipeType = DisplayRecipeType.create(recipe.getRecipeType());

        Map<Integer, Icon> displayItemInputs =
                Maps.transformValues(
                        recipe.getItemInputs(),
                        itemGroup -> DisplayItemGroup.buildIcon(itemGroup, itemRepository));
        ImmutableTable<Integer, Integer, Icon> itemInputs =
                buildIngredientsGrid(
                        displayRecipeType.getItemInputGrid(), displayItemInputs, recipe);

        Map<Integer, Icon> displayFluidInputs =
                Maps.transformValues(recipe.getFluidInputs(), DisplayFluidGroup::buildIcon);
        ImmutableTable<Integer, Integer, Icon> fluidInputs =
                buildIngredientsGrid(
                        displayRecipeType.getFluidInputGrid(), displayFluidInputs, recipe);

        Map<Integer, Icon> displayItemOutputs =
                Maps.transformValues(
                        recipe.getItemOutputs(), DisplayItemStackWithProbability::buildIcon);
        ImmutableTable<Integer, Integer, Icon> itemOutputs =
                buildIngredientsGrid(
                        displayRecipeType.getItemOutputGrid(), displayItemOutputs, recipe);

        Map<Integer, Icon> displayFluidOutputs =
                Maps.transformValues(
                        recipe.getFluidOutputs(), DisplayFluidStackWithProbability::buildIcon);
        ImmutableTable<Integer, Integer, Icon> fluidOutputs =
                buildIngredientsGrid(
                        displayRecipeType.getFluidOutputGrid(), displayFluidOutputs, recipe);

        return new AutoValue_DisplayRecipe(
                recipe, displayRecipeType, buildIcon(recipe),
                itemInputs, fluidInputs, itemOutputs, fluidOutputs);
    }

    public static Icon buildIcon(Recipe recipe) {
        DisplayRecipeType displayRecipeType = DisplayRecipeType.create(recipe.getRecipeType());

        int numItemOutputs = recipe.getItemOutputs().size();
        int numFluidOutputs = recipe.getFluidOutputs().size();

        String description = displayRecipeType.getDescription();
        String url = UrlBuilder.buildRecipeUrl(recipe);
        Icon icon;
        if (numItemOutputs > 0) {
            Icon innerIcon =
                    DisplayItemStackWithProbability.buildIcon(recipe.getItemOutputs().get(0));
            if (numItemOutputs == 1) {
                description += String.format(" (%s)", innerIcon.getDescription());
            } else if (numFluidOutputs > 0) {
                description +=
                        String.format(" (%d items, %d fluids)", numItemOutputs, numFluidOutputs);
            } else {
                description += String.format(" (%d items)", numItemOutputs);
            }

            icon = innerIcon.toBuilder()
                    .setDescription(description)
                    .setUrl(url)
                    .build();
        } else if (numFluidOutputs > 0) {
            Icon innerIcon =
                    DisplayFluidStackWithProbability.buildIcon(recipe.getFluidOutputs().get(0));
            if (numFluidOutputs == 1) {
                description += String.format(" (%s)", innerIcon.getDescription());
            } else {
                description += String.format(" (%d fluids)", numFluidOutputs);
            }

            icon = innerIcon.toBuilder()
                    .setDescription(description)
                    .setUrl(url)
                    .build();
        } else {
            icon = Icon.builder()
                    .setDescription(description + " (empty)")
                    .setUrl(url)
                    .setImageFilePath(Constants.MISSING_IMAGE)
                    .build();
        }
        return icon;
    }

    public abstract Recipe getRecipe();
    public abstract DisplayRecipeType getDisplayRecipeType();
    public abstract Icon getIcon();
    public abstract ImmutableTable<Integer, Integer, Icon> getItemInputs();
    public abstract ImmutableTable<Integer, Integer, Icon> getFluidInputs();
    public abstract ImmutableTable<Integer, Integer, Icon> getItemOutputs();
    public abstract ImmutableTable<Integer, Integer, Icon> getFluidOutputs();

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
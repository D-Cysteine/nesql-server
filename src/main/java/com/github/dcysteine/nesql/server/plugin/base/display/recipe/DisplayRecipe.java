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

import java.util.Iterator;

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

        ImmutableList<Icon> displayItemInputs =
                recipe.getItemInputs().stream()
                        .map(itemGroup -> DisplayItemGroup.buildIcon(itemGroup, itemRepository))
                        .collect(ImmutableList.toImmutableList());
        ImmutableTable<Integer, Integer, Icon> itemInputs =
                buildIngredientsGrid(
                        displayRecipeType.getItemInputGrid(), displayItemInputs, recipe);

        ImmutableList<Icon> displayFluidInputs =
                recipe.getFluidInputs().stream()
                        .map(DisplayFluidGroup::buildIcon)
                        .collect(ImmutableList.toImmutableList());
        ImmutableTable<Integer, Integer, Icon> fluidInputs =
                buildIngredientsGrid(
                        displayRecipeType.getFluidInputGrid(), displayFluidInputs, recipe);

        ImmutableList<Icon> displayItemOutputs =
                recipe.getItemOutputs().stream()
                        .map(DisplayItemStackWithProbability::buildIcon)
                        .collect(ImmutableList.toImmutableList());
        ImmutableTable<Integer, Integer, Icon> itemOutputs =
                buildIngredientsGrid(
                        displayRecipeType.getItemOutputGrid(), displayItemOutputs, recipe);

        ImmutableList<Icon> displayFluidOutputs =
                recipe.getFluidOutputs().stream()
                        .map(DisplayFluidStackWithProbability::buildIcon)
                        .collect(ImmutableList.toImmutableList());
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
        if (numItemOutputs > 0 && numFluidOutputs > 0) {
            description += String.format(" (%d items, %d fluids)", numItemOutputs, numFluidOutputs);

            icon = DisplayItemStackWithProbability.buildIcon(recipe.getItemOutputs().get(0))
                    .toBuilder()
                    .setDescription(description)
                    .setUrl(url)
                    .build();
        } else if (numItemOutputs > 0) {
            description += String.format(" (%d items)", numItemOutputs);

            Icon itemIcon =
                    DisplayItemStackWithProbability.buildIcon(recipe.getItemOutputs().get(0));
            icon = itemIcon.toBuilder().setDescription(description).setUrl(url).build();
        } else if (numFluidOutputs > 0) {
            description += String.format(" (%d fluids)", numFluidOutputs);

            Icon fluidIcon =
                    DisplayFluidStackWithProbability.buildIcon(recipe.getFluidOutputs().get(0));
            icon = fluidIcon.toBuilder().setDescription(description).setUrl(url).build();
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
            Dimension gridDimension, Iterable<Icon> ingredients, Recipe recipe) {
        ImmutableTable.Builder<Integer, Integer, Icon> builder = ImmutableTable.builder();
        Iterator<Icon> iterator = ingredients.iterator();
        for (int row = 0; row < gridDimension.getHeight(); row++) {
            for (int col = 0; col < gridDimension.getHeight(); col++) {
                if (!iterator.hasNext()) {
                    break;
                }

                Icon icon = iterator.next();
                if (icon != null) {
                    builder.put(row, col, icon);
                }
            }
        }

        if (iterator.hasNext()) {
            int excess = 0;
            while (iterator.hasNext()) {
                excess++;
                iterator.next();
            }

            Main.Logger.error(
                    "Tried to build recipe ingredients grid with too many ingredients!\n"
                    + "Expected {}x{}; got {} excess for recipe {}",
                    gridDimension.getWidth(), gridDimension.getHeight(), excess, recipe.getId());
        }

        return builder.build();
    }

    @Override
    public int compareTo(DisplayRecipe other) {
        return getRecipe().compareTo(other.getRecipe());
    }
}
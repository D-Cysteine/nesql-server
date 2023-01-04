package com.github.dcysteine.nesql.server.plugin.base.display.recipe;

import com.github.dcysteine.nesql.server.display.Dimension;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DisplayRecipeType implements Comparable<DisplayRecipeType> {
    public static DisplayRecipeType create(RecipeType recipeType) {
        String description;
        Dimension itemInputGrid;
        Dimension fluidInputGrid;
        Dimension itemOutputGrid;
        Dimension fluidOutputGrid;
        switch (recipeType) {
            case MINECRAFT_SHAPED_CRAFTING -> {
                description = "Shaped Crafting Recipe";
                itemInputGrid = Dimension.create(3);
                fluidInputGrid = Dimension.create(0);
                itemOutputGrid = Dimension.create(1);
                fluidOutputGrid = Dimension.create(0);
            }

            case MINECRAFT_SHAPED_CRAFTING_OREDICT -> {
                description = "Shaped Crafting Recipe (Oredict)";
                itemInputGrid = Dimension.create(3);
                fluidInputGrid = Dimension.create(0);
                itemOutputGrid = Dimension.create(1);
                fluidOutputGrid = Dimension.create(0);
            }

            case MINECRAFT_SHAPELESS_CRAFTING -> {
                description = "Shapeless Crafting Recipe";
                itemInputGrid = Dimension.create(3);
                fluidInputGrid = Dimension.create(0);
                itemOutputGrid = Dimension.create(1);
                fluidOutputGrid = Dimension.create(0);
            }

            case MINECRAFT_SHAPELESS_CRAFTING_OREDICT -> {
                description = "Shapeless Crafting Recipe (Oredict)";
                itemInputGrid = Dimension.create(3);
                fluidInputGrid = Dimension.create(0);
                itemOutputGrid = Dimension.create(1);
                fluidOutputGrid = Dimension.create(0);
            }

            case MINECRAFT_FURNACE -> {
                description = "Furnace";
                itemInputGrid = Dimension.create(1);
                fluidInputGrid = Dimension.create(0);
                itemOutputGrid = Dimension.create(1);
                fluidOutputGrid = Dimension.create(0);
            }

            default ->
                    throw new IllegalArgumentException("Unrecognized recipe type: " + recipeType);
        }

        return new AutoValue_DisplayRecipeType(
                recipeType, description,
                itemInputGrid, fluidInputGrid, itemOutputGrid, fluidOutputGrid);
    }

    public abstract RecipeType getRecipeType();
    public abstract String getDescription();
    public abstract Dimension getItemInputGrid();
    public abstract Dimension getFluidInputGrid();
    public abstract Dimension getItemOutputGrid();
    public abstract Dimension getFluidOutputGrid();

    @Override
    public int compareTo(DisplayRecipeType other) {
        return getRecipeType().compareTo(other.getRecipeType());
    }
}

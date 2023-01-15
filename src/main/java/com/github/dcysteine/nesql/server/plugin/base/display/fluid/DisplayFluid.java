package com.github.dcysteine.nesql.server.plugin.base.display.fluid;

import com.github.dcysteine.nesql.server.display.Icon;
import com.github.dcysteine.nesql.server.plugin.base.display.recipe.DisplayRecipe;
import com.github.dcysteine.nesql.server.util.StringUtil;
import com.github.dcysteine.nesql.server.util.UrlBuilder;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroupRepository;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeRepository;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class DisplayFluid implements Comparable<DisplayFluid> {
    public static DisplayFluid create(
            Fluid fluid,
            FluidGroupRepository fluidGroupRepository,
            RecipeRepository recipeRepository) {
        ImmutableList<Icon> recipesWithInput =
                recipeRepository.findByFluidInput(fluid.getId()).stream()
                        .sorted()
                        .map(DisplayRecipe::buildIcon)
                        .collect(ImmutableList.toImmutableList());
        ImmutableList<Icon> recipesWithOutput =
                recipeRepository.findByFluidInput(fluid.getId()).stream()
                        .sorted()
                        .map(DisplayRecipe::buildIcon)
                        .collect(ImmutableList.toImmutableList());

        ImmutableList<Icon> fluidGroupsContaining =
                fluidGroupRepository.findByFluid(fluid.getId()).stream()
                        .sorted()
                        .map(DisplayFluidGroup::buildIcon)
                        .collect(ImmutableList.toImmutableList());

        return new AutoValue_DisplayFluid(
                fluid, buildIcon(fluid),
                recipesWithInput, recipesWithOutput, fluidGroupsContaining);
    }

    public static Icon buildIcon(Fluid fluid) {
        return Icon.builder()
                .setDescription(fluid.getLocalizedName())
                .setUrl(UrlBuilder.buildFluidUrl(fluid))
                .setImage(fluid.getImageFilePath())
                .build();
    }

    public abstract Fluid getFluid();
    public abstract Icon getIcon();
    public abstract ImmutableList<Icon> getFluidGroupsContaining();
    public abstract ImmutableList<Icon> getRecipesWithInput();
    public abstract ImmutableList<Icon> getRecipesWithOutput();

    @Override
    public int compareTo(DisplayFluid other) {
        return getFluid().compareTo(other.getFluid());
    }
}

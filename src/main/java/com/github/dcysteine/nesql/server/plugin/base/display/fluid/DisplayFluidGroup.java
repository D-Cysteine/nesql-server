package com.github.dcysteine.nesql.server.plugin.base.display.fluid;

import com.github.dcysteine.nesql.server.display.Icon;
import com.github.dcysteine.nesql.server.plugin.base.display.recipe.DisplayRecipe;
import com.github.dcysteine.nesql.server.util.Constants;
import com.github.dcysteine.nesql.server.util.NumberUtil;
import com.github.dcysteine.nesql.server.util.UrlBuilder;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class DisplayFluidGroup implements Comparable<DisplayFluidGroup> {
    public static DisplayFluidGroup create(FluidGroup fluidGroup) {
        ImmutableList<Icon> recipesWithInput =
                fluidGroup.getRecipesWithInput().stream()
                        .map(DisplayRecipe::buildIcon)
                        .collect(ImmutableList.toImmutableList());

        ImmutableList<Icon> fluidStacks =
                fluidGroup.getFluidStacks().stream()
                        .map(DisplayFluidStack::buildIcon)
                        .collect(ImmutableList.toImmutableList());

        return new AutoValue_DisplayFluidGroup(
                fluidGroup, buildIcon(fluidGroup), recipesWithInput, fluidStacks);
    }

    public static Icon buildIcon(FluidGroup fluidGroup) {
        String url = UrlBuilder.buildFluidGroupUrl(fluidGroup);
        Icon icon;
        if (!fluidGroup.getFluidStacks().isEmpty()) {
            int size = fluidGroup.getFluidStacks().size();
            String description =
                    String.format("Fluid Group (%s fluid stacks)", NumberUtil.formatInteger(size));
            Icon innerIcon = DisplayFluidStack.buildIcon(fluidGroup.getFluidStacks().first());
            if (size == 1) {
                description = String.format("Fluid Group (%s)", innerIcon.getDescription());
            }

            icon = innerIcon.toBuilder()
                    .setDescription(description)
                    .setUrl(url)
                    .build();
        } else {
            icon = Icon.builder()
                    .setDescription("Fluid Group (empty)")
                    .setUrl(url)
                    .setImageFilePath(Constants.MISSING_IMAGE)
                    .build();
        }
        return icon;
    }

    public abstract FluidGroup getFluidGroup();
    public abstract Icon getIcon();
    public abstract ImmutableList<Icon> getRecipesWithInput();
    public abstract ImmutableList<Icon> getFluidStacks();

    @Override
    public int compareTo(DisplayFluidGroup other) {
        return getFluidGroup().compareTo(other.getFluidGroup());
    }
}
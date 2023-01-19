package com.github.dcysteine.nesql.server.plugin.base.display.fluid;

import com.github.dcysteine.nesql.server.display.Icon;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayDeps;
import com.github.dcysteine.nesql.server.util.UrlBuilder;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class DisplayFluid implements Comparable<DisplayFluid> {
    public static DisplayFluid create(Fluid fluid, BaseDisplayDeps deps) {
        ImmutableList<Icon> recipesWithInput = ImmutableList.of();
        ImmutableList<Icon> recipesWithOutput = ImmutableList.of();
        ImmutableList<Icon> fluidGroupsContaining = ImmutableList.of();

        return new AutoValue_DisplayFluid(
                fluid, buildIcon(fluid, deps),
                recipesWithInput, recipesWithOutput, fluidGroupsContaining);
    }

    public static Icon buildIcon(Fluid fluid, BaseDisplayDeps deps) {
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

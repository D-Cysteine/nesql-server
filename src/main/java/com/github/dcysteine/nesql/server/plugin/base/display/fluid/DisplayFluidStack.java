package com.github.dcysteine.nesql.server.plugin.base.display.fluid;

import com.github.dcysteine.nesql.server.display.Icon;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayDeps;
import com.github.dcysteine.nesql.server.util.NumberUtil;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DisplayFluidStack implements Comparable<DisplayFluidStack> {
    public static DisplayFluidStack create(FluidStack fluidStack, BaseDisplayDeps deps) {
        return new AutoValue_DisplayFluidStack(fluidStack, buildIcon(fluidStack, deps));
    }

    public static Icon buildIcon(FluidStack fluidStack, BaseDisplayDeps deps) {
        return DisplayFluid.buildIcon(fluidStack.getFluid(), deps).toBuilder()
                .setBottomRight(NumberUtil.formatCompact(fluidStack.getAmount()))
                .build();
    }

    public abstract FluidStack getFluidStack();
    public abstract Icon getIcon();

    @Override
    public int compareTo(DisplayFluidStack other) {
        return getFluidStack().compareTo(other.getFluidStack());
    }
}

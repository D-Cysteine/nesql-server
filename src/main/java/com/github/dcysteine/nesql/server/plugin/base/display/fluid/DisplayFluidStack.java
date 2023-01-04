package com.github.dcysteine.nesql.server.plugin.base.display.fluid;

import com.github.dcysteine.nesql.server.display.Icon;
import com.github.dcysteine.nesql.server.util.NumberUtil;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DisplayFluidStack implements Comparable<DisplayFluidStack> {
    public static DisplayFluidStack create(FluidStack fluidStack) {
        return new AutoValue_DisplayFluidStack(fluidStack, buildIcon(fluidStack));
    }

    public static Icon buildIcon(FluidStack fluidStack) {
        return DisplayFluid.buildIcon(fluidStack.getFluid()).toBuilder()
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

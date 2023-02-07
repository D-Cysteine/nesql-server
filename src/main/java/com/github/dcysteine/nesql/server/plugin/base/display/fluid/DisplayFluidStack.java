package com.github.dcysteine.nesql.server.plugin.base.display.fluid;

import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DisplayFluidStack implements Comparable<DisplayFluidStack> {
    public static DisplayFluidStack create(FluidStack fluidStack, DisplayService service) {
        return new AutoValue_DisplayFluidStack(fluidStack, buildIcon(fluidStack, service));
    }

    public static Icon buildIcon(FluidStack fluidStack, DisplayService service) {
        Icon innerIcon = DisplayFluid.buildIcon(fluidStack.getFluid(), service);
        String description =
                String.format(
                        "%s (%s)",
                        innerIcon.getDescription(),
                        NumberUtil.formatInteger(fluidStack.getAmount()));

        return innerIcon.toBuilder()
                .setDescription(description)
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

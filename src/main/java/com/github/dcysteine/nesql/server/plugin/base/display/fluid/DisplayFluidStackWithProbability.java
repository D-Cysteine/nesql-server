package com.github.dcysteine.nesql.server.plugin.base.display.fluid;

import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayService;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStackWithProbability;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DisplayFluidStackWithProbability
        implements Comparable<DisplayFluidStackWithProbability> {
    public static DisplayFluidStackWithProbability create(
            FluidStackWithProbability fluidStack, BaseDisplayService service) {
        return new AutoValue_DisplayFluidStackWithProbability(
                fluidStack, buildIcon(fluidStack, service));
    }

    public static Icon buildIcon(FluidStackWithProbability fluidStack, BaseDisplayService service) {
        Icon icon = DisplayFluidStack.buildIcon(fluidStack.withoutProbability(), service);
        if (NumberUtil.fuzzyEquals(fluidStack.getProbability(), 1.0d)) {
            return icon;
        } else {
            return icon.toBuilder()
                    .setTopLeft(NumberUtil.formatPercentage(fluidStack.getProbability()))
                    .build();
        }
    }

    public abstract FluidStackWithProbability getFluidStack();
    public abstract Icon getIcon();

    @Override
    public int compareTo(DisplayFluidStackWithProbability other) {
        return getFluidStack().compareTo(other.getFluidStack());
    }
}

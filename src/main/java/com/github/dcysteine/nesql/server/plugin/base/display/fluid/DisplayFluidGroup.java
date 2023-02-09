package com.github.dcysteine.nesql.server.plugin.base.display.fluid;

import com.github.dcysteine.nesql.server.common.Constants;
import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.Optional;

@AutoValue
public abstract class DisplayFluidGroup implements Comparable<DisplayFluidGroup> {
    public static DisplayFluidGroup create(FluidGroup fluidGroup, DisplayService service) {
        ImmutableList<Icon> fluidStacks =
                fluidGroup.getFluidStacks().stream()
                        .sorted()
                        .map(fluidStack -> DisplayFluidStack.buildIcon(fluidStack, service))
                        .collect(ImmutableList.toImmutableList());
        int size = fluidStacks.size();

        Optional<Icon> onlyFluidStackIcon = Optional.empty();
        if (size == 1) {
            onlyFluidStackIcon = Optional.of(Iterables.getOnlyElement(fluidStacks));
        }

        return new AutoValue_DisplayFluidGroup(
                fluidGroup, buildIcon(fluidGroup, service), onlyFluidStackIcon,
                size, fluidStacks, service.buildAdditionalInfo(FluidGroup.class, fluidGroup));
    }

    public static Icon buildIcon(FluidGroup fluidGroup, DisplayService service) {
        String url = Table.FLUID_GROUP.getViewUrl(fluidGroup);

        ImmutableList<FluidStack> fluidStacks =
                fluidGroup.getFluidStacks().stream()
                        .sorted()
                        .collect(ImmutableList.toImmutableList());
        int size = fluidStacks.size();
        if (size > 0) {
            String description =
                    String.format("Fluid Group (%s fluid stacks)", NumberUtil.formatInteger(size));
            Icon innerIcon = DisplayFluidStack.buildIcon(fluidStacks.get(0), service);
            if (size == 1) {
                description = String.format("Fluid Group (%s)", innerIcon.getDescription());
            }

            return innerIcon.toBuilder()
                    .setDescription(description)
                    .setUrl(url)
                    .setTopLeft(NumberUtil.formatCompact(size))
                    .build();
        } else {
            return Icon.builder()
                    .setDescription("Fluid Group (empty)")
                    .setUrl(url)
                    .setImage(Constants.MISSING_IMAGE)
                    .setTopLeft("0")
                    .build();
        }
    }

    public abstract FluidGroup getFluidGroup();
    public abstract Icon getIcon();

    /** Will be set if and only if this fluid group contains exactly one fluid stack. */
    public abstract Optional<Icon> getOnlyFluidStackIcon();

    public abstract int getSize();
    public abstract ImmutableList<Icon> getFluidStacks();
    public abstract ImmutableList<InfoPanel> getAdditionalInfo();

    @Override
    public int compareTo(DisplayFluidGroup other) {
        return getFluidGroup().compareTo(other.getFluidGroup());
    }
}
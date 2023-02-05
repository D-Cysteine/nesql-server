package com.github.dcysteine.nesql.server.plugin.base.display.fluid;

import com.github.dcysteine.nesql.server.common.Constants;
import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayService;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.Optional;

@AutoValue
public abstract class DisplayFluidGroup implements Comparable<DisplayFluidGroup> {
    public static DisplayFluidGroup create(FluidGroup fluidGroup, BaseDisplayService service) {
        ImmutableList<Icon> fluidStacks =
                fluidGroup.getFluidStacks().stream()
                        .sorted()
                        .distinct()
                        .map(fluidStack -> DisplayFluidStack.buildIcon(fluidStack, service))
                        .collect(ImmutableList.toImmutableList());
        int size = fluidStacks.size();

        Optional<Icon> onlyFluidStackIcon = Optional.empty();
        if (size == 1) {
            onlyFluidStackIcon = Optional.of(Iterables.getOnlyElement(fluidStacks));
        }

        return new AutoValue_DisplayFluidGroup(
                fluidGroup, buildIcon(fluidGroup, service), onlyFluidStackIcon,
                fluidGroup.getFluidStacks().size(), fluidStacks,
                service.getAdditionalInfo(fluidGroup));
    }

    public static Icon buildIcon(FluidGroup fluidGroup, BaseDisplayService service) {
        String url = Table.FLUID_GROUP.getViewUrl(fluidGroup);

        if (!fluidGroup.getFluidStacks().isEmpty()) {
            int size = fluidGroup.getFluidStacks().size();
            String description =
                    String.format("Fluid Group (%s fluid stacks)", NumberUtil.formatInteger(size));
            Icon innerIcon =
                    DisplayFluidStack.buildIcon(
                            fluidGroup.getFluidStacks().iterator().next(), service);
            if (size == 1) {
                description = String.format("Fluid Group (%s)", innerIcon.getDescription());
            }

            return innerIcon.toBuilder()
                    .setDescription(description)
                    .setUrl(url)
                    .setTopLeft(NumberUtil.formatInteger(size))
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
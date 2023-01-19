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

@AutoValue
public abstract class DisplayFluidGroup implements Comparable<DisplayFluidGroup> {
    public static DisplayFluidGroup create(FluidGroup fluidGroup, BaseDisplayService service) {
        return new AutoValue_DisplayFluidGroup(
                fluidGroup, buildIcon(fluidGroup, service), service.getAdditionalInfo(fluidGroup));
    }

    public static Icon buildIcon(FluidGroup fluidGroup, BaseDisplayService service) {
        String url = Table.FLUID_GROUP.getViewUrl(fluidGroup);
        Icon icon;
        if (!fluidGroup.getFluidStacks().isEmpty()) {
            int size = fluidGroup.getFluidStacks().size();
            String description =
                    String.format("Fluid Group (%s fluid stacks)", NumberUtil.formatInteger(size));
            Icon innerIcon = DisplayFluidStack.buildIcon(fluidGroup.getFluidStacks().first(), service);
            if (size == 1) {
                description = String.format("Fluid Group (%s)", innerIcon.getDescription());
            }

            icon = innerIcon.toBuilder()
                    .setDescription(description)
                    .setUrl(url)
                    .setTopLeft(NumberUtil.formatInteger(size))
                    .build();
        } else {
            icon = Icon.builder()
                    .setDescription("Fluid Group (empty)")
                    .setUrl(url)
                    .setImage(Constants.MISSING_IMAGE)
                    .setTopLeft("0")
                    .build();
        }
        return icon;
    }

    public abstract FluidGroup getFluidGroup();
    public abstract Icon getIcon();
    public abstract ImmutableList<InfoPanel> getAdditionalInfo();

    @Override
    public int compareTo(DisplayFluidGroup other) {
        return getFluidGroup().compareTo(other.getFluidGroup());
    }
}
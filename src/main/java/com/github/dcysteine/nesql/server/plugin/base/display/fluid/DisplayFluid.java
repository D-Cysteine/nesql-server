package com.github.dcysteine.nesql.server.plugin.base.display.fluid;

import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.common.util.StringUtil;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class DisplayFluid implements Comparable<DisplayFluid> {
    public static DisplayFluid create(Fluid fluid, DisplayService service) {
        ImmutableList<String> nbt = ImmutableList.of();
        if (fluid.hasNbt()) {
            nbt = ImmutableList.copyOf(StringUtil.prettyPrintNbt(fluid.getNbt()).split("\n"));
        }

        return new AutoValue_DisplayFluid(
                fluid, buildIcon(fluid, service), nbt,
                service.buildAdditionalInfo(Fluid.class, fluid));
    }

    public static Icon buildIcon(Fluid fluid, DisplayService service) {
        return Icon.builder()
                .setDescription(fluid.getLocalizedName())
                .setUrl(Table.FLUID.getViewUrl(fluid))
                .setImage(fluid.getImageFilePath())
                .build();
    }

    public abstract Fluid getFluid();
    public abstract Icon getIcon();
    public abstract ImmutableList<String> getNbt();
    public abstract ImmutableList<InfoPanel> getAdditionalInfo();

    @Override
    public int compareTo(DisplayFluid other) {
        return getFluid().compareTo(other.getFluid());
    }
}

package com.github.dcysteine.nesql.server.plugin.thaumcraft.display;

import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.sql.thaumcraft.Aspect;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class DisplayAspect implements Comparable<DisplayAspect> {
    public static DisplayAspect create(Aspect aspect, DisplayService service) {
        ImmutableList<Icon> componentIcons =
                aspect.getComponents().stream()
                        .sorted()
                        .map(component -> DisplayAspect.buildIcon(component, service))
                        .collect(ImmutableList.toImmutableList());
        ImmutableList<Icon> componentOfIcons =
                aspect.getComponentOf().stream()
                        .sorted()
                        .map(componentOf -> DisplayAspect.buildIcon(componentOf, service))
                        .collect(ImmutableList.toImmutableList());

        return new AutoValue_DisplayAspect(
                aspect, buildIcon(aspect, service), componentIcons, componentOfIcons,
                service.buildAdditionalInfo(Aspect.class, aspect));
    }

    public static Icon buildIcon(Aspect aspect, DisplayService service) {
        Icon.Builder builder =
                Icon.builder()
                        .setDescription(aspect.getName())
                        .setUrl(Table.ASPECT.getViewUrl(aspect))
                        .setImage(aspect.getIcon().getImageFilePath());

        if (aspect.isPrimal()) {
            builder.setTopLeft("P");
        }

        return builder.build();
    }

    public abstract Aspect getAspect();
    public abstract Icon getIcon();
    public abstract ImmutableList<Icon> getComponents();
    public abstract ImmutableList<Icon> getComponentOf();
    public abstract ImmutableList<InfoPanel> getAdditionalInfo();

    @Override
    public int compareTo(DisplayAspect other) {
        return getAspect().compareTo(other.getAspect());
    }
}

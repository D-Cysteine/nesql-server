package com.github.dcysteine.nesql.server.plugin.thaumcraft.display;

import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItem;
import com.github.dcysteine.nesql.sql.thaumcraft.AspectEntry;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DisplayAspectEntry implements Comparable<DisplayAspectEntry> {
    /** We delegate view to {@code Item}, so this method is not currently used. */
    public static DisplayAspectEntry create(AspectEntry aspectEntry, DisplayService service) {
        return new AutoValue_DisplayAspectEntry(aspectEntry, buildIcon(aspectEntry, service));
    }

    public static Icon buildIcon(AspectEntry aspectEntry, DisplayService service) {
        String description =
                String.format(
                        "%s (%s: %s)",
                        aspectEntry.getItem().getLocalizedName(),
                        aspectEntry.getAspect().getName(),
                        NumberUtil.formatInteger(aspectEntry.getAmount()));

        return DisplayItem.buildIcon(aspectEntry.getItem(), service).toBuilder()
                .setDescription(description)
                .setBottomLeftImage(aspectEntry.getAspect().getIcon().getImageFilePath())
                .setBottomRight(NumberUtil.formatCompact(aspectEntry.getAmount()))
                .build();
    }

    /** Alternate icon that is used for {@code Item} info panels. */
    public static Icon buildAspectIcon(AspectEntry aspectEntry, DisplayService service) {
        return DisplayAspect.buildIcon(aspectEntry.getAspect(), service).toBuilder()
                .setBottomRight(NumberUtil.formatCompact(aspectEntry.getAmount()))
                .build();
    }

    public abstract AspectEntry getAspectEntry();
    public abstract Icon getIcon();

    @Override
    public int compareTo(DisplayAspectEntry other) {
        return getAspectEntry().compareTo(other.getAspectEntry());
    }
}

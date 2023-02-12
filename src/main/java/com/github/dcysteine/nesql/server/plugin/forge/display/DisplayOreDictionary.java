package com.github.dcysteine.nesql.server.plugin.forge.display;

import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItemGroup;
import com.github.dcysteine.nesql.sql.forge.OreDictionary;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DisplayOreDictionary implements Comparable<DisplayOreDictionary> {
    /** We delegate view to {@code ItemGroup}, so this method is not currently used. */
    public static DisplayOreDictionary create(OreDictionary oreDictionary, DisplayService service) {
        return new AutoValue_DisplayOreDictionary(oreDictionary, buildIcon(oreDictionary, service));
    }

    public static Icon buildIcon(OreDictionary oreDictionary, DisplayService service) {
        return DisplayItemGroup.buildIcon(oreDictionary.getItemGroup(), service).toBuilder()
                .setDescription(oreDictionary.getName())
                .setBottomRight(null)
                .build();
    }

    public abstract OreDictionary getOreDictionary();
    public abstract Icon getIcon();

    @Override
    public int compareTo(DisplayOreDictionary other) {
        return getOreDictionary().compareTo(other.getOreDictionary());
    }
}
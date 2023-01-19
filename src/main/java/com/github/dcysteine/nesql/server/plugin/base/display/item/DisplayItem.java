package com.github.dcysteine.nesql.server.plugin.base.display.item;

import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.util.StringUtil;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayService;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class DisplayItem implements Comparable<DisplayItem> {
    public static DisplayItem create(Item item, BaseDisplayService service) {
        ImmutableList<String> nbt = ImmutableList.of();
        if (item.hasNbt()) {
            nbt = ImmutableList.copyOf(StringUtil.prettyPrintNbt(item.getNbt()).split("\n"));
        }

        return new AutoValue_DisplayItem(
                item, buildIcon(item, service),
                nbt, ImmutableList.copyOf(item.getTooltip().split("\n")),
                service.getAdditionalInfo(item));
    }

    public static Icon buildIcon(Item item, BaseDisplayService service) {
        return Icon.builder()
                .setDescription(item.getLocalizedName())
                .setUrl(Table.ITEM.getViewUrl(item))
                .setImage(item.getImageFilePath())
                .build();
    }

    public abstract Item getItem();
    public abstract Icon getIcon();
    public abstract ImmutableList<String> getNbt();
    public abstract ImmutableList<String> getTooltip();
    public abstract ImmutableList<InfoPanel> getAdditionalInfo();

    @Override
    public int compareTo(DisplayItem other) {
        return getItem().compareTo(other.getItem());
    }
}

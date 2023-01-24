package com.github.dcysteine.nesql.server.plugin.base.display.item;

import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.util.MinecraftUtil;
import com.github.dcysteine.nesql.server.common.util.StringUtil;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayService;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import java.util.Optional;

@AutoValue
public abstract class DisplayItem implements Comparable<DisplayItem> {
    public static DisplayItem create(Item item, BaseDisplayService service) {
        ImmutableList<String> nbt = ImmutableList.of();
        if (item.hasNbt()) {
            nbt = ImmutableList.copyOf(StringUtil.prettyPrintNbt(item.getNbt()).split("\n"));
        }

        String giveCommand = MinecraftUtil.buildGiveCommand(item, 1);
        Optional<String> giveStackCommand = Optional.empty();
        if (item.getMaxStackSize() > 1) {
            giveStackCommand = Optional.of(
                    MinecraftUtil.buildGiveCommand(item, item.getMaxStackSize()));
        }

        return new AutoValue_DisplayItem(
                item, buildIcon(item, service),
                nbt, ImmutableList.copyOf(item.getTooltip().split("\n")),
                giveCommand, giveStackCommand, service.getAdditionalInfo(item));
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

    /** Returns a Minecraft {@code /give} command giving a single item. */
    public abstract String getGiveCommand();

    /**
     * Returns a Minecraft {@code /give} command giving a stack of the item.
     * Will be set if and only if the maximum stack size is greater than 1.
     */
    public abstract Optional<String> getGiveStackCommand();

    public abstract ImmutableList<InfoPanel> getAdditionalInfo();

    @Override
    public int compareTo(DisplayItem other) {
        return getItem().compareTo(other.getItem());
    }
}

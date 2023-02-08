package com.github.dcysteine.nesql.server.plugin.base.display.item;

import com.github.dcysteine.nesql.server.common.Constants;
import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.Optional;

@AutoValue
public abstract class DisplayItemGroup implements Comparable<DisplayItemGroup> {
    public static DisplayItemGroup create(ItemGroup itemGroup, DisplayService service) {
        ImmutableList<Icon> itemStacks =
                itemGroup.getItemStacks().stream()
                        .sorted()
                        .map(itemStack -> DisplayItemStack.buildIcon(itemStack, service))
                        .collect(ImmutableList.toImmutableList());
        int size = itemStacks.size();

        Optional<Icon> onlyItemStackIcon = Optional.empty();
        if (size == 1) {
            onlyItemStackIcon = Optional.of(Iterables.getOnlyElement(itemStacks));
        }

        return new AutoValue_DisplayItemGroup(
                itemGroup, buildIcon(itemGroup, service), onlyItemStackIcon,
                size, itemStacks, service.buildAdditionalInfo(ItemGroup.class, itemGroup));
    }

    public static Icon buildIcon(ItemGroup itemGroup, DisplayService service) {
        String url = Table.ITEM_GROUP.getViewUrl(itemGroup);

        if (!itemGroup.getItemStacks().isEmpty()) {
            int size = itemGroup.getItemStacks().size();
            String description =
                    String.format("Item Group (%s item stacks)", NumberUtil.formatInteger(size));
            Icon innerIcon =
                    DisplayItemStack.buildIcon(
                            itemGroup.getItemStacks().iterator().next(), service);
            if (size == 1) {
                description = String.format("Item Group (%s)", innerIcon.getDescription());
            }

            return innerIcon.toBuilder()
                    .setDescription(description)
                    .setUrl(url)
                    .setTopLeft(NumberUtil.formatInteger(size))
                    .build();
        } else {
            return Icon.builder()
                    .setDescription("Item Group (empty)")
                    .setUrl(url)
                    .setImage(Constants.MISSING_IMAGE)
                    .setTopLeft("0")
                    .build();
        }
    }

    public abstract ItemGroup getItemGroup();
    public abstract Icon getIcon();

    /** Will be set if and only if this item group contains exactly one item stack. */
    public abstract Optional<Icon> getOnlyItemStackIcon();

    public abstract int getSize();
    public abstract ImmutableList<Icon> getItemStacks();
    public abstract ImmutableList<InfoPanel> getAdditionalInfo();

    @Override
    public int compareTo(DisplayItemGroup other) {
        return getItemGroup().compareTo(other.getItemGroup());
    }
}
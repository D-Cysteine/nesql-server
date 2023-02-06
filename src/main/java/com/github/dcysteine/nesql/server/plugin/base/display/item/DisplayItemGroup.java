package com.github.dcysteine.nesql.server.plugin.base.display.item;

import com.github.dcysteine.nesql.server.common.Constants;
import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayService;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;

import java.util.Comparator;
import java.util.Optional;

@AutoValue
public abstract class DisplayItemGroup implements Comparable<DisplayItemGroup> {
    public static DisplayItemGroup create(ItemGroup itemGroup, BaseDisplayService service) {
        ImmutableSortedSet<DisplayWildcardItemStack> wildcardItemStacks =
                itemGroup.getWildcardItemStacks().stream()
                        .map(wildcardItemStack ->
                                DisplayWildcardItemStack.create(wildcardItemStack, service))
                        .collect(
                                ImmutableSortedSet.toImmutableSortedSet(Comparator.naturalOrder()));

        ImmutableList<Icon> allItemStacks =
                itemGroup.getAllItemStacks().stream()
                        .sorted()
                        .map(itemStack -> DisplayItemStack.buildIcon(itemStack, service))
                        .collect(ImmutableList.toImmutableList());
        int size = allItemStacks.size();
        int directSize = itemGroup.getItemStacks().size();
        int wildcardSize = itemGroup.getResolvedWildcardItemStacks().size();

        Optional<Icon> onlyItemStackIcon = Optional.empty();
        if (size == 1) {
            onlyItemStackIcon = Optional.of(Iterables.getOnlyElement(allItemStacks));
        }

        return new AutoValue_DisplayItemGroup(
                itemGroup, buildIcon(itemGroup, service), onlyItemStackIcon,
                size, directSize, wildcardSize, allItemStacks, wildcardItemStacks,
                service.getAdditionalInfo(itemGroup));
    }

    public static Icon buildIcon(ItemGroup itemGroup, BaseDisplayService service) {
        String url = Table.ITEM_GROUP.getViewUrl(itemGroup);

        Icon icon;
        if (!itemGroup.getAllItemStacks().isEmpty()) {
            int size = itemGroup.getAllItemStacks().size();
            String description =
                    String.format("Item Group (%s item stacks)", NumberUtil.formatInteger(size));
            Icon innerIcon =
                    DisplayItemStack.buildIcon(
                            itemGroup.getAllItemStacks().iterator().next(), service);
            if (size == 1) {
                description = String.format("Item Group (%s)", innerIcon.getDescription());
            }

            icon = innerIcon.toBuilder()
                    .setDescription(description)
                    .setUrl(url)
                    .setTopLeft(NumberUtil.formatInteger(size))
                    .build();
        } else {
            icon = Icon.builder()
                    .setDescription("Item Group (empty)")
                    .setUrl(url)
                    .setImage(Constants.MISSING_IMAGE)
                    .setTopLeft("0")
                    .build();
        }
        return icon;
    }

    public abstract ItemGroup getItemGroup();
    public abstract Icon getIcon();

    /** Will be set if and only if this item group contains exactly one item stack. */
    public abstract Optional<Icon> getOnlyItemStackIcon();

    public abstract int getSize();
    public abstract int getDirectSize();
    public abstract int getWildcardSize();
    public abstract ImmutableList<Icon> getAllItemStacks();
    public abstract ImmutableSortedSet<DisplayWildcardItemStack> getWildcardItemStacks();
    public abstract ImmutableList<InfoPanel> getAdditionalInfo();

    @Override
    public int compareTo(DisplayItemGroup other) {
        return getItemGroup().compareTo(other.getItemGroup());
    }
}
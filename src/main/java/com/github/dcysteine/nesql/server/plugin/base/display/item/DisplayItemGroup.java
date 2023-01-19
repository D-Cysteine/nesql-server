package com.github.dcysteine.nesql.server.plugin.base.display.item;

import com.github.dcysteine.nesql.server.common.Constants;
import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayService;
import com.github.dcysteine.nesql.server.plugin.base.spec.ItemSpec;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.github.dcysteine.nesql.sql.base.item.WildcardItemStack;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import java.util.SortedSet;

@AutoValue
public abstract class DisplayItemGroup implements Comparable<DisplayItemGroup> {
    public static DisplayItemGroup create(ItemGroup itemGroup, BaseDisplayService service) {
        return new AutoValue_DisplayItemGroup(
                itemGroup, buildIcon(itemGroup, service), service.getAdditionalInfo(itemGroup));
    }

    public static Icon buildIcon(ItemGroup itemGroup, BaseDisplayService service) {
        ItemRepository itemRepository = service.getItemRepository();

        SortedSet<ItemStack> itemStacks = itemGroup.getItemStacks();
        SortedSet<WildcardItemStack> wildcardItemStacks = itemGroup.getWildcardItemStacks();

        int size = itemStacks.size();
        for (WildcardItemStack wildcardItemStack : wildcardItemStacks) {
            int itemId = wildcardItemStack.getItemId();
            size += itemRepository.findAll(ItemSpec.buildItemIdSpec(itemId)).size();
        }

        String url = Table.ITEM_GROUP.getViewUrl(itemGroup);
        Icon icon;
        if (!itemStacks.isEmpty()) {
            String description =
                    String.format("Item Group (%s item stacks)", NumberUtil.formatInteger(size));
            Icon innerIcon = DisplayItemStack.buildIcon(itemStacks.first(), service);
            if (size == 1) {
                description = String.format("Item Group (%s)", innerIcon.getDescription());
            }

            icon = innerIcon.toBuilder()
                    .setDescription(description)
                    .setUrl(url)
                    .setTopLeft(NumberUtil.formatInteger(size))
                    .build();
        } else if (!wildcardItemStacks.isEmpty()) {
            String description =
                    String.format(
                            "Wildcard Item Group (%s keys, %s item stacks)",
                            NumberUtil.formatInteger(wildcardItemStacks.size()),
                            NumberUtil.formatInteger(size));
            Icon innerIcon = DisplayWildcardItemStack.buildIcon(wildcardItemStacks.first(), service);
            if (wildcardItemStacks.size() == 1) {
                description = String.format("Wildcard Item Group (%s)", innerIcon.getDescription());
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
    public abstract ImmutableList<InfoPanel> getAdditionalInfo();

    @Override
    public int compareTo(DisplayItemGroup other) {
        return getItemGroup().compareTo(other.getItemGroup());
    }
}
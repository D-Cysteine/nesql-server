package com.github.dcysteine.nesql.server.plugin.base.display.item;

import com.github.dcysteine.nesql.server.Main;
import com.github.dcysteine.nesql.server.display.Icon;
import com.github.dcysteine.nesql.server.util.UrlBuilder;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
import com.github.dcysteine.nesql.sql.base.item.WildcardItemStack;
import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class DisplayWildcardItemStack implements Comparable<DisplayWildcardItemStack> {
    public static DisplayWildcardItemStack create(
            WildcardItemStack wildcardItemStack, ItemRepository itemRepository) {
        return new AutoValue_DisplayWildcardItemStack(
                wildcardItemStack, buildIcon(wildcardItemStack, itemRepository));
    }

    public static Icon buildIcon(
            WildcardItemStack wildcardItemStack, ItemRepository itemRepository) {
        List<Item> items = itemRepository.findBaseItemByItemId(wildcardItemStack.getItemId());
        Icon icon;
        if (items.isEmpty()) {
            Main.Logger.error(
                    "Could not find base item for item id: {}", wildcardItemStack.getItemId());

            icon = Icon.builder()
                    .setDescription(
                            String.format(
                                    "Wildcard Item Stack (%d)", wildcardItemStack.getItemId()))
                    .setUrl(UrlBuilder.getMissingUrl())
                    .setTopLeft(Integer.toString(wildcardItemStack.getItemId()))
                    .setBottomRight(Integer.toString(wildcardItemStack.getStackSize()))
                    .build();
        } else {
            Icon itemIcon = DisplayItem.buildIcon(items.get(0));
            icon = itemIcon.toBuilder()
                    .setDescription(
                            String.format("Wildcard Item Stack (%s)", itemIcon.getDescription()))
                    .setTopLeft(Integer.toString(wildcardItemStack.getItemId()))
                    .setBottomRight(Integer.toString(wildcardItemStack.getStackSize()))
                    .build();
        }
        return icon;
    }

    public abstract WildcardItemStack getWildcardItemStack();
    public abstract Icon getIcon();

    @Override
    public int compareTo(DisplayWildcardItemStack other) {
        return getWildcardItemStack().compareTo(other.getWildcardItemStack());
    }
}

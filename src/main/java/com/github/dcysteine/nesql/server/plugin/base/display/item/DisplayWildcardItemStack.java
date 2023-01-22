package com.github.dcysteine.nesql.server.plugin.base.display.item;

import com.github.dcysteine.nesql.server.Main;
import com.github.dcysteine.nesql.server.common.Constants;
import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayService;
import com.github.dcysteine.nesql.server.plugin.base.spec.ItemSpec;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
import com.github.dcysteine.nesql.sql.base.item.WildcardItemStack;
import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class DisplayWildcardItemStack implements Comparable<DisplayWildcardItemStack> {
    public static DisplayWildcardItemStack create(
            WildcardItemStack wildcardItemStack, BaseDisplayService service) {
        return new AutoValue_DisplayWildcardItemStack(
                wildcardItemStack, buildIcon(wildcardItemStack, service));
    }

    public static Icon buildIcon(WildcardItemStack wildcardItemStack, BaseDisplayService service) {
        ItemRepository itemRepository = service.getItemRepository();

        int itemId = wildcardItemStack.getItemId();
        List<Item> items =
                itemRepository.findAll(ItemSpec.buildItemIdSpec(itemId), ItemSpec.DEFAULT_SORT);
        Icon icon;
        if (items.isEmpty()) {
            Main.Logger.error(
                    "Could not find base item for item id: {}", wildcardItemStack.getItemId());

            icon = Icon.builder()
                    .setDescription(
                            String.format(
                                    "Wildcard Item Stack (#%d)", wildcardItemStack.getItemId()))
                    .setUrl(Constants.NOT_FOUND_URL)
                    .setImage(Constants.MISSING_IMAGE)
                    .setTopLeft("*")
                    .setBottomRight(Integer.toString(wildcardItemStack.getStackSize()))
                    .build();
        } else {
            Icon itemIcon = DisplayItem.buildIcon(items.get(0), service);
            icon = itemIcon.toBuilder()
                    .setDescription(
                            String.format("Wildcard Item Stack (%s)", itemIcon.getDescription()))
                    .setTopLeft("*")
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

package com.github.dcysteine.nesql.server.plugin.base.display.item;

import com.github.dcysteine.nesql.server.Main;
import com.github.dcysteine.nesql.server.common.Constants;
import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayService;
import com.github.dcysteine.nesql.server.plugin.base.spec.ItemSpec;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.github.dcysteine.nesql.sql.base.item.WildcardItemStack;
import com.google.auto.value.AutoValue;

import java.util.List;
import java.util.Optional;

@AutoValue
public abstract class DisplayWildcardItemStack implements Comparable<DisplayWildcardItemStack> {
    public static DisplayWildcardItemStack create(
            WildcardItemStack wildcardItemStack, BaseDisplayService service) {
        ItemRepository itemRepository = service.getItemRepository();

        List<Item> items =
                itemRepository.findAll(
                        ItemSpec.buildItemIdSpec(wildcardItemStack.getItemId()),
                        ItemSpec.DEFAULT_SORT);

        Optional<Icon> onlyItemStackIcon = Optional.empty();
        if (items.size() == 1) {
            ItemStack onlyItemStack = new ItemStack(items.get(0), wildcardItemStack.getStackSize());
            onlyItemStackIcon = Optional.of(DisplayItemStack.buildIcon(onlyItemStack, service));
        }

        return new AutoValue_DisplayWildcardItemStack(
                wildcardItemStack, buildIcon(wildcardItemStack, service), onlyItemStackIcon,
                items.size());
    }

    public static Icon buildIcon(WildcardItemStack wildcardItemStack, BaseDisplayService service) {
        ItemRepository itemRepository = service.getItemRepository();

        List<Item> items =
                itemRepository.findAll(
                        ItemSpec.buildItemIdSpec(wildcardItemStack.getItemId()),
                        ItemSpec.DEFAULT_SORT);
        if (items.isEmpty()) {
            Main.Logger.error(
                    "Could not find base item for item id: {}", wildcardItemStack.getItemId());

            return Icon.builder()
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
            return itemIcon.toBuilder()
                    .setDescription(
                            String.format("Wildcard Item Stack (%s)", itemIcon.getDescription()))
                    .setTopLeft("*")
                    .setBottomRight(Integer.toString(wildcardItemStack.getStackSize()))
                    .build();
        }
    }

    public abstract WildcardItemStack getWildcardItemStack();
    public abstract Icon getIcon();

    /** Will be set if and only if this wildcard matches exactly one item stack. */
    public abstract Optional<Icon> getOnlyItemStackIcon();

    public abstract int getSize();

    @Override
    public int compareTo(DisplayWildcardItemStack other) {
        return getWildcardItemStack().compareTo(other.getWildcardItemStack());
    }
}

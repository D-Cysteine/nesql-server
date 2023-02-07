package com.github.dcysteine.nesql.server.plugin.base.display.item;

import com.github.dcysteine.nesql.server.Main;
import com.github.dcysteine.nesql.server.common.Constants;
import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.server.plugin.base.spec.ItemSpec;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
import com.github.dcysteine.nesql.sql.base.item.WildcardItemStack;
import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class DisplayWildcardItemStack implements Comparable<DisplayWildcardItemStack> {
    public static DisplayWildcardItemStack create(
            WildcardItemStack wildcardItemStack, DisplayService service) {
        String itemDamage = "*";
        if (!wildcardItemStack.isWildcardItemDamage()) {
            itemDamage = NumberUtil.formatInteger(wildcardItemStack.getItemDamage());
        }

        String nbt = "*";
        if (!wildcardItemStack.isWildcardNbt()) {
            nbt = wildcardItemStack.getNbt();
        }

        return new AutoValue_DisplayWildcardItemStack(
                wildcardItemStack, buildIcon(wildcardItemStack, service), itemDamage, nbt);
    }

    public static Icon buildIcon(WildcardItemStack wildcardItemStack, DisplayService service) {
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

    /** Will be "*" for wildcard item damage. */
    public abstract String getItemDamage();

    /** Will be "*" for wildcard NBT. */
    public abstract String getNbt();

    @Override
    public int compareTo(DisplayWildcardItemStack other) {
        return getWildcardItemStack().compareTo(other.getWildcardItemStack());
    }
}

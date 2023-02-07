package com.github.dcysteine.nesql.server.plugin.base.display.item;

import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DisplayItemStack implements Comparable<DisplayItemStack> {
    public static DisplayItemStack create(ItemStack itemStack, DisplayService service) {
        return new AutoValue_DisplayItemStack(itemStack, buildIcon(itemStack, service));
    }

    public static Icon buildIcon(ItemStack itemStack, DisplayService service) {
        Icon innerIcon = DisplayItem.buildIcon(itemStack.getItem(), service);
        String description =
                String.format(
                        "%s (%s)",
                        innerIcon.getDescription(),
                        NumberUtil.formatInteger(itemStack.getStackSize()));

        return innerIcon.toBuilder()
                .setDescription(description)
                .setBottomRight(NumberUtil.formatInteger(itemStack.getStackSize()))
                .build();
    }

    public abstract ItemStack getItemStack();
    public abstract Icon getIcon();

    @Override
    public int compareTo(DisplayItemStack other) {
        return getItemStack().compareTo(other.getItemStack());
    }
}

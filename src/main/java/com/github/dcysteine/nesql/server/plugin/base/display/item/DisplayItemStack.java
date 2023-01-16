package com.github.dcysteine.nesql.server.plugin.base.display.item;

import com.github.dcysteine.nesql.server.display.Icon;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayDeps;
import com.github.dcysteine.nesql.server.util.NumberUtil;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DisplayItemStack implements Comparable<DisplayItemStack> {
    public static DisplayItemStack create(ItemStack itemStack, BaseDisplayDeps deps) {
        return new AutoValue_DisplayItemStack(itemStack, buildIcon(itemStack, deps));
    }

    public static Icon buildIcon(ItemStack itemStack, BaseDisplayDeps deps) {
        return DisplayItem.buildIcon(itemStack.getItem(), deps).toBuilder()
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

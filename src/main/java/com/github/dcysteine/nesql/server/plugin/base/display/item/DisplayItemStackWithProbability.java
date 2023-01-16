package com.github.dcysteine.nesql.server.plugin.base.display.item;

import com.github.dcysteine.nesql.server.display.Icon;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayDeps;
import com.github.dcysteine.nesql.server.util.NumberUtil;
import com.github.dcysteine.nesql.sql.base.item.ItemStackWithProbability;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DisplayItemStackWithProbability
        implements Comparable<DisplayItemStackWithProbability> {
    public static DisplayItemStackWithProbability create(
            ItemStackWithProbability itemStack, BaseDisplayDeps deps) {
        return new AutoValue_DisplayItemStackWithProbability(itemStack, buildIcon(itemStack, deps));
    }

    public static Icon buildIcon(ItemStackWithProbability itemStack, BaseDisplayDeps deps) {
        Icon icon = DisplayItemStack.buildIcon(itemStack.withoutProbability(), deps);
        if (NumberUtil.fuzzyEquals(itemStack.getProbability(), 1.0d)) {
            return icon;
        } else {
            return icon.toBuilder()
                    .setTopLeft(NumberUtil.formatPercentage(itemStack.getProbability()))
                    .build();
        }
    }

    public abstract ItemStackWithProbability getItemStack();
    public abstract Icon getIcon();

    @Override
    public int compareTo(DisplayItemStackWithProbability other) {
        return getItemStack().compareTo(other.getItemStack());
    }
}

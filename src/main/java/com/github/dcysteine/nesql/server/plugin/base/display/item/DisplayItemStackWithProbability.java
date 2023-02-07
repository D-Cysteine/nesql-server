package com.github.dcysteine.nesql.server.plugin.base.display.item;

import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.sql.base.item.ItemStackWithProbability;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DisplayItemStackWithProbability
        implements Comparable<DisplayItemStackWithProbability> {
    public static DisplayItemStackWithProbability create(
            ItemStackWithProbability itemStack, DisplayService service) {
        return new AutoValue_DisplayItemStackWithProbability(itemStack, buildIcon(itemStack, service));
    }

    public static Icon buildIcon(ItemStackWithProbability itemStack, DisplayService service) {
        Icon icon = DisplayItemStack.buildIcon(itemStack.withoutProbability(), service);
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

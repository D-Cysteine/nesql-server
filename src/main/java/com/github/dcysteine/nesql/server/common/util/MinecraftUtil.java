package com.github.dcysteine.nesql.server.common.util;

import com.github.dcysteine.nesql.sql.base.item.Item;

/** Contains utility methods related to Minecraft game functionality. */
public class MinecraftUtil {
    // Static class.
    private MinecraftUtil() {}

    /** Returns a Minecraft {@code /give} command that gives the item. */
    public static String buildGiveCommand(Item item, int stackSize) {
        // Trim off trailing space in case NBT is empty string.
        return String.format(
                        "/give @p %s:%s %d %d %s",
                        item.getModId(), item.getInternalName(),
                        stackSize, item.getItemDamage(), item.getNbt())
                .trim();
    }
}

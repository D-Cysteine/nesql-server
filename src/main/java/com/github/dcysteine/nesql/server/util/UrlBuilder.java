package com.github.dcysteine.nesql.server.util;

import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;

/** Helper class containing methods for building URLs. */
public class UrlBuilder {
    // Static class.
    private UrlBuilder() {}

    /** Use this for cases where we lack a valid URL. */
    public static String getMissingUrl() {
        return "~/notfound";
    }

    public static String buildItemUrl(Item item) {
        return "~/item/" + item.getId();
    }

    public static String buildItemGroupUrl(ItemGroup itemGroup) {
        return "~/itemgroup/" + itemGroup.getId();
    }

    public static String buildFluidUrl(Fluid fluid) {
        return "~/fluid/" + fluid.getId();
    }

    public static String buildFluidGroupUrl(FluidGroup fluidGroup) {
        return "~/fluidgroup/" + fluidGroup.getId();
    }

    public static String buildRecipeUrl(Recipe recipe) {
        return "~/recipe/" + recipe.getId();
    }
}

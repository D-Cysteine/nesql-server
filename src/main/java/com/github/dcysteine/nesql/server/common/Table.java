package com.github.dcysteine.nesql.server.common;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.Plugin;
import com.google.common.collect.ImmutableListMultimap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.function.Function;

/** Enum for organizing code relating to tables. */
public enum Table {
    ITEM(Plugin.BASE, "Item", "item"),
    FLUID(Plugin.BASE, "Fluid", "fluid"),
    ITEM_GROUP(Plugin.BASE, "Item Group", "itemgroup"),
    FLUID_GROUP(Plugin.BASE, "Fluid Group", "fluidgroup"),
    RECIPE(Plugin.BASE, "Recipe", "recipe"),
    RECIPE_TYPE(Plugin.BASE, "Recipe Type", "recipetype", "minRecipeCount", "1"),

    /** This table uses {@code ItemGroup}'s {@code view} page. */
    ORE_DICTIONARY(Plugin.FORGE, "Ore Dictionary", "oredictionary"),
    // Fluid Block table is omitted.
    /** This table uses {@code Item}'s {@code view} page. */
    FLUID_CONTAINER(Plugin.FORGE, "Fluid Container", "fluidcontainer"),

    // GregTech Recipe table is omitted.

    ASPECT(Plugin.THAUMCRAFT, "Thaumcraft Aspect", "aspect"),
    /** This table uses {@code Item}'s {@code view} page. */
    ASPECT_ENTRY(Plugin.THAUMCRAFT, "Thaumcraft Aspect Entry", "aspectentry"),

    QUEST(Plugin.QUEST, "Quest", "quest"),
    // Task and Reward tables are omitted.
    QUEST_LINE(Plugin.QUEST, "Quest Line", "questline"),
    ;

    public static final ImmutableListMultimap<Plugin, Table> TABLES =
            Arrays.stream(values())
                    .collect(
                            ImmutableListMultimap.toImmutableListMultimap(
                                    Table::getPlugin,
                                    Function.identity()));

    /** The plugin that owns this table. */
    private final Plugin plugin;

    /** Human-readable display name. */
    private final String name;

    /** URL segment for this table. */
    private final String path;

    /** Default URL parameters for search endpoint. */
    private final String[] defaultParams;

    Table(Plugin plugin, String name, String path, String... defaultParams) {
        this.plugin = plugin;
        this.name = name;
        this.path = path;
        this.defaultParams = defaultParams;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return String.format("%s/%s", plugin.getName(), path);
    }

    /** Returns the URL without the leading '~' needed by Thymeleaf. */
    public String getViewUrlNoPrefix(Identifiable<?> entity) {
        return String.format("/%s/view/%s", getPath(), entity.getId());
    }

    public String getViewUrl(Identifiable<?> entity) {
        return "~" + getViewUrlNoPrefix(entity);
    }

    public String getSearchUrl() {
        return getSearchUrl(defaultParams);
    }

    public String getSearchUrl(String... params) {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException(
                    "params must have even length!\n" + Arrays.toString(params));
        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromUriString(String.format("~/%s/search", getPath()));
        for (int i = 0; i < params.length; i += 2) {
            builder.queryParam(params[i], params[i + 1]);
        }
        return builder.toUriString();
    }
}

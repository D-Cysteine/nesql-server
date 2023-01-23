package com.github.dcysteine.nesql.server.common;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.Plugin;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/** Enum for organizing code relating to tables. */
public enum Table {
    ITEM(Plugin.BASE, "Item", "item"),
    FLUID(Plugin.BASE, "Fluid", "fluid"),
    ITEM_GROUP(Plugin.BASE, "Item Group", "itemgroup"),
    FLUID_GROUP(Plugin.BASE, "Fluid Group", "fluidgroup"),
    RECIPE(Plugin.BASE, "Recipe", "recipe"),
    RECIPE_TYPE(Plugin.BASE, "Recipe Type", "recipetype"),
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

    Table(Plugin plugin, String name, String path) {
        this.plugin = plugin;
        this.name = name;
        this.path = path;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    /** Returns the URL without the leading '~' needed by Thymeleaf. */
    public String getViewUrlNoPrefix(Identifiable<?> entity) {
        return String.format("/%s/view/%s", path, entity.getId());
    }

    public String getViewUrl(Identifiable<?> entity) {
        return "~" + getViewUrlNoPrefix(entity);
    }

    public String getSearchUrl() {
        return String.format("~/%s/search", path);
    }

    public String getSearchUrl(String... params) {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("params must have even length!\n" + params);
        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromUriString(String.format("~/%s/search", path));
        for (int i = 0; i < params.length; i += 2) {
            builder.queryParam(params[i], params[i + 1]);
        }
        return builder.toUriString();
    }
}

package com.github.dcysteine.nesql.server.plugin;

import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.sql.Plugin;
import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/** Service that registers a plugin's {@link InfoPanel} construction methods. */
public abstract class PluginDisplayService {
    protected final Plugin plugin;
    protected final Map<Class<?>, Function<?, List<InfoPanel>>> functionMap;

    protected PluginDisplayService(Plugin plugin) {
        this.plugin = plugin;
        this.functionMap = new HashMap<>();
    }

    public final Plugin getPlugin() {
        return plugin;
    }

    public final <T> List<InfoPanel> buildAdditionalInfo(Class<T> clazz, T entity) {
        if (functionMap.containsKey(clazz)) {
            @SuppressWarnings("unchecked")
            var function = (Function<T, List<InfoPanel>>) functionMap.get(clazz);
            return function.apply(entity);
        } else {
            return ImmutableList.of();
        }
    }

    protected <T> void registerFunction(
            Class<T> clazz, Function<T, List<InfoPanel>> function) {
        functionMap.put(clazz, function);
    }
}

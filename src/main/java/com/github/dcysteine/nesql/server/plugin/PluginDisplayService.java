package com.github.dcysteine.nesql.server.plugin;

import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.sql.Plugin;
import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/** Service that registers a plugin's {@link InfoPanel} construction methods. */
public abstract class PluginDisplayService {
    protected final Plugin plugin;
    // TODO if we want to allow each service to register multiple functions per class, we could use
    // a multimap here.
    protected final Map<Class<?>, BiFunction<?, DisplayService, List<InfoPanel>>> functionMap;

    protected PluginDisplayService(Plugin plugin) {
        this.plugin = plugin;
        this.functionMap = new HashMap<>();
    }

    public final Plugin getPlugin() {
        return plugin;
    }

    public final <T> List<InfoPanel> buildAdditionalInfo(
            Class<T> clazz, T entity, DisplayService service) {
        if (functionMap.containsKey(clazz)) {
            @SuppressWarnings("unchecked")
            var function = (BiFunction<T, DisplayService, List<InfoPanel>>) functionMap.get(clazz);
            return function.apply(entity, service);
        } else {
            return ImmutableList.of();
        }
    }

    protected <T> void registerFunction(
            Class<T> clazz, BiFunction<T, DisplayService, List<InfoPanel>> function) {
        functionMap.put(clazz, function);
    }
}

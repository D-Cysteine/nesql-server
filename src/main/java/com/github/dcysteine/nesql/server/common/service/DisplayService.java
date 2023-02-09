package com.github.dcysteine.nesql.server.common.service;

import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.plugin.PluginDisplayService;
import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeRepository;
import com.github.dcysteine.nesql.sql.quest.QuestRepository;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Helper class that provides dependencies for building display objects.
 *
 * <p>This class is responsible for calling all plugin display services to build {@link InfoPanel}s.
 */
@Service
public class DisplayService {
    @Autowired
    private List<PluginDisplayService> pluginDisplayServices;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private QuestRepository questRepository;

    public ItemRepository getItemRepository() {
        return itemRepository;
    }

    public RecipeRepository getRecipeRepository() {
        return recipeRepository;
    }

    public QuestRepository getQuestRepository() {
        return questRepository;
    }

    public <T> ImmutableList<InfoPanel> buildAdditionalInfo(Class<T> clazz, T entity) {
        ImmutableList.Builder<InfoPanel> builder = ImmutableList.builder();
        pluginDisplayServices.stream()
                .map(service -> service.buildAdditionalInfo(clazz, entity, this))
                .forEach(builder::addAll);
        return builder.build();
    }
}

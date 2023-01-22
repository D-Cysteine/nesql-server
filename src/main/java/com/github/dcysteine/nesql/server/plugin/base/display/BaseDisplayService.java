package com.github.dcysteine.nesql.server.plugin.base.display;

import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Helper class that provides dependencies and methods for building display objects. */
@Service
public class BaseDisplayService {
    @Autowired
    private ItemRepository itemRepository;

    public ItemRepository getItemRepository() {
        return itemRepository;
    }

    public ImmutableList<InfoPanel> getAdditionalInfo(Item item) {
        return ImmutableList.of();
    }

    public ImmutableList<InfoPanel> getAdditionalInfo(Fluid fluid) {
        return ImmutableList.of();
    }

    public ImmutableList<InfoPanel> getAdditionalInfo(ItemGroup itemGroup) {
        return ImmutableList.of();
    }

    public ImmutableList<InfoPanel> getAdditionalInfo(FluidGroup fluidGroup) {
        return ImmutableList.of();
    }

    public ImmutableList<InfoPanel> getAdditionalInfo(Recipe recipe) {
        return ImmutableList.of();
    }

    public ImmutableList<InfoPanel> getAdditionalInfo(RecipeType recipeType) {
        return ImmutableList.of();
    }
}

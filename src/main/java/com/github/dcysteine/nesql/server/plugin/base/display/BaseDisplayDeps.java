package com.github.dcysteine.nesql.server.plugin.base.display;

import com.github.dcysteine.nesql.sql.base.fluid.FluidGroupRepository;
import com.github.dcysteine.nesql.sql.base.fluid.FluidRepository;
import com.github.dcysteine.nesql.sql.base.item.ItemGroupRepository;
import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Makes life easier for us by providing all required dependencies for display objects. */
@Service
public class BaseDisplayDeps {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemGroupRepository itemGroupRepository;

    @Autowired
    private FluidRepository fluidRepository;

    @Autowired
    private FluidGroupRepository fluidGroupRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    public ItemRepository getItemRepository() {
        return itemRepository;
    }

    public ItemGroupRepository getItemGroupRepository() {
        return itemGroupRepository;
    }

    public FluidRepository getFluidRepository() {
        return fluidRepository;
    }

    public FluidGroupRepository getFluidGroupRepository() {
        return fluidGroupRepository;
    }

    public RecipeRepository getRecipeRepository() {
        return recipeRepository;
    }
}

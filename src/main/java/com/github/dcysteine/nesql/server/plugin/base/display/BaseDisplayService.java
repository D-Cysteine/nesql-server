package com.github.dcysteine.nesql.server.plugin.base.display;

import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.display.Link;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.plugin.PluginDisplayService;
import com.github.dcysteine.nesql.sql.Plugin;
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
public class BaseDisplayService extends PluginDisplayService {
    public BaseDisplayService() {
        super(Plugin.BASE);
        registerFunction(Item.class, this::buildItemAdditionalInfo);
        registerFunction(Fluid.class, this::buildFluidAdditionalInfo);
        registerFunction(ItemGroup.class, this::buildItemGroupAdditionalInfo);
        registerFunction(FluidGroup.class, this::buildFluidGroupAdditionalInfo);
        registerFunction(RecipeType.class, this::buildRecipeTypeAdditionalInfo);
    }

    public ImmutableList<InfoPanel> buildItemAdditionalInfo(Item item, DisplayService service) {
        InfoPanel basePanel =
                InfoPanel.builder()
                        .setTitle("Base")
                        .addLink(
                                Link.create(
                                        "bi-search",
                                        "Item groups",
                                        Table.ITEM_GROUP.getSearchUrl("itemId", item.getId())))
                        .addLink(
                                Link.create(
                                        "bi-search",
                                        "Recipe input",
                                        Table.RECIPE.getSearchUrl("inputItemId", item.getId())))
                        .addLink(
                                Link.create(
                                        "bi-search",
                                        "Recipe output",
                                        Table.RECIPE.getSearchUrl(
                                                "outputItemId", item.getId())))
                        .build();

        return ImmutableList.of(basePanel);
    }

    public ImmutableList<InfoPanel> buildFluidAdditionalInfo(Fluid fluid, DisplayService service) {
        InfoPanel basePanel =
                InfoPanel.builder()
                        .setTitle("Base")
                        .addLink(
                                Link.create(
                                        "bi-search",
                                        "Fluid groups",
                                        Table.FLUID_GROUP.getSearchUrl("fluidId", fluid.getId())))
                        .addLink(
                                Link.create(
                                        "bi-search",
                                        "Recipe input",
                                        Table.RECIPE.getSearchUrl(
                                                "inputFluidId", fluid.getId())))
                        .addLink(
                                Link.create(
                                        "bi-search",
                                        "Recipe output",
                                        Table.RECIPE.getSearchUrl(
                                                "outputFluidId", fluid.getId())))
                        .build();

        return ImmutableList.of(basePanel);
    }

    public ImmutableList<InfoPanel> buildItemGroupAdditionalInfo(
            ItemGroup itemGroup, DisplayService service) {
        InfoPanel basePanel =
                InfoPanel.builder()
                        .setTitle("Base")
                        .addLink(
                                Link.create(
                                        "bi-search",
                                        "Items",
                                        Table.ITEM.getSearchUrl(
                                                "itemGroupId", itemGroup.getId())))
                        .addLink(
                                Link.create(
                                        "bi-search",
                                        "Recipe input",
                                        Table.RECIPE.getSearchUrl(
                                                "inputItemGroupId", itemGroup.getId())))
                        .build();

        return ImmutableList.of(basePanel);
    }

    public ImmutableList<InfoPanel> buildFluidGroupAdditionalInfo(
            FluidGroup fluidGroup, DisplayService service) {
        InfoPanel basePanel =
                InfoPanel.builder()
                        .setTitle("Base")
                        .addLink(
                                Link.create(
                                        "bi-search",
                                        "Fluids",
                                        Table.FLUID.getSearchUrl(
                                                "fluidGroupId", fluidGroup.getId())))
                        .addLink(
                                Link.create(
                                        "bi-search",
                                        "Recipe input",
                                        Table.RECIPE.getSearchUrl(
                                                "inputFluidGroupId", fluidGroup.getId())))
                        .build();

        return ImmutableList.of(basePanel);
    }

    public ImmutableList<InfoPanel> buildRecipeTypeAdditionalInfo(
            RecipeType recipeType, DisplayService service) {
        InfoPanel basePanel =
                InfoPanel.builder()
                        .setTitle("Base")
                        .addLink(
                                Link.create(
                                        "bi-search",
                                        "Recipes",
                                        Table.RECIPE.getSearchUrl(
                                                "recipeTypeId", recipeType.getId())))
                        .build();

        return ImmutableList.of(basePanel);
    }
}

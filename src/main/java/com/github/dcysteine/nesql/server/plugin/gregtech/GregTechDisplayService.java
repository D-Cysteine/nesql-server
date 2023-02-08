package com.github.dcysteine.nesql.server.plugin.gregtech;

import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.server.plugin.PluginDisplayService;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItem;
import com.github.dcysteine.nesql.sql.Plugin;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.gregtech.GregTechRecipe;
import com.github.dcysteine.nesql.sql.gregtech.GregTechRecipeRepository;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/** Helper class that provides dependencies and methods for building display objects. */
@Service
public class GregTechDisplayService extends PluginDisplayService {
    @Autowired
    private GregTechRecipeRepository gregTechRecipeRepository;

    public GregTechDisplayService() {
        super(Plugin.BASE);
        registerFunction(Recipe.class, this::buildRecipeAdditionalInfo);
    }

    public ImmutableList<InfoPanel> buildRecipeAdditionalInfo(
            Recipe recipe, DisplayService service) {
        Optional<GregTechRecipe> gregTechRecipeOptional =
                gregTechRecipeRepository.findOne(
                        GregTechRecipeSpec.buildRecipeIdSpec(recipe.getId()));
        if (gregTechRecipeOptional.isEmpty()) {
            return ImmutableList.of();
        }

        ImmutableList.Builder<InfoPanel> listBuilder = ImmutableList.builder();

        GregTechRecipe gregTechRecipe = gregTechRecipeOptional.get();
        InfoPanel.Builder gregTechPanelBuilder =
                InfoPanel.builder()
                        .setTitle("GregTech")
                        .addProperty("Voltage Tier", gregTechRecipe.getVoltageTier())
                        .addProperty(
                                "Voltage", NumberUtil.formatInteger(gregTechRecipe.getVoltage()))
                        .addProperty(
                                "Amperage", NumberUtil.formatInteger(gregTechRecipe.getAmperage()))
                        .addProperty(
                                "Duration",
                                NumberUtil.formatDouble(gregTechRecipe.getDuration() / 20d)
                                        + " sec")
                        .addProperty("Requires cleanroom", gregTechRecipe.isRequiresCleanroom())
                        .addProperty("Requires low gravity", gregTechRecipe.isRequiresLowGravity());

        gregTechRecipe.getSpecialItems().stream()
                .map(item -> DisplayItem.buildIcon(item, service))
                .forEach(gregTechPanelBuilder::addIcon);

        if (!gregTechRecipe.getAdditionalInfo().isEmpty()) {
            Splitter.on('\n')
                    .split(gregTechRecipe.getAdditionalInfo())
                    .forEach(gregTechPanelBuilder::addText);
        }

        listBuilder.add(gregTechPanelBuilder.build());

        if (!gregTechRecipe.getModOwners().isEmpty()) {
            InfoPanel.Builder modOwnersPanelBuilder = InfoPanel.builder().setTitle("Mod owners");
            gregTechRecipe.getModOwners().forEach(modOwnersPanelBuilder::addText);
            listBuilder.add(modOwnersPanelBuilder.build());
        }

        return listBuilder.build();
    }
}

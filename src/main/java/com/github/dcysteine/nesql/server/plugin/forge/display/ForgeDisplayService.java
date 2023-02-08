package com.github.dcysteine.nesql.server.plugin.forge.display;

import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.display.Link;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.plugin.PluginDisplayService;
import com.github.dcysteine.nesql.server.plugin.forge.spec.OreDictionarySpec;
import com.github.dcysteine.nesql.sql.Plugin;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.forge.OreDictionary;
import com.github.dcysteine.nesql.sql.forge.OreDictionaryRepository;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** Helper class that provides dependencies and methods for building display objects. */
@Service
public class ForgeDisplayService extends PluginDisplayService {
    @Autowired
    private OreDictionaryRepository oreDictionaryRepository;

    public ForgeDisplayService() {
        super(Plugin.BASE);
        registerFunction(Item.class, this::buildItemAdditionalInfo);
        registerFunction(Fluid.class, this::buildFluidAdditionalInfo);
        registerFunction(ItemGroup.class, this::buildItemGroupAdditionalInfo);
    }

    public ImmutableList<InfoPanel> buildItemAdditionalInfo(Item item, DisplayService service) {
        InfoPanel forgePanel =
                InfoPanel.builder()
                        .setTitle("Forge")
                        .addLink(
                                Link.create(
                                        "bi-search",
                                        "Ore dictionary",
                                        Table.ORE_DICTIONARY.getSearchUrl(
                                                "itemId", item.getId())))
                        .build();

        return ImmutableList.of(forgePanel);
    }

    public ImmutableList<InfoPanel> buildFluidAdditionalInfo(Fluid fluid, DisplayService service) {
        InfoPanel forgePanel =
                InfoPanel.builder()
                        .setTitle("Forge")
                        .build();

        return ImmutableList.of(forgePanel);
    }

    public ImmutableList<InfoPanel> buildItemGroupAdditionalInfo(
            ItemGroup itemGroup, DisplayService service) {
        List<OreDictionary> oreDictionaryEntries =
                oreDictionaryRepository.findAll(
                        OreDictionarySpec.buildItemGroupIdSpec(itemGroup.getId()));
        if (oreDictionaryEntries.isEmpty()) {
            return ImmutableList.of();
        }

        InfoPanel.Builder builder = InfoPanel.builder().setTitle("Forge Ore Dictionary");
        oreDictionaryEntries.stream()
                .map(oreDictionary -> DisplayOreDictionary.buildIcon(oreDictionary, service))
                .forEach(builder::addIcon);

        return ImmutableList.of(builder.build());
    }
}

package com.github.dcysteine.nesql.server.plugin.thaumcraft.display;

import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.display.Link;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.plugin.PluginDisplayService;
import com.github.dcysteine.nesql.server.plugin.thaumcraft.spec.AspectEntrySpec;
import com.github.dcysteine.nesql.sql.Plugin;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.thaumcraft.Aspect;
import com.github.dcysteine.nesql.sql.thaumcraft.AspectEntry;
import com.github.dcysteine.nesql.sql.thaumcraft.AspectEntryRepository;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** Helper class that provides dependencies and methods for building display objects. */
@Service
public class ThaumcraftDisplayService extends PluginDisplayService {
    @Autowired
    private AspectEntryRepository aspectEntryRepository;

    public ThaumcraftDisplayService() {
        super(Plugin.BASE);
        registerFunction(Item.class, this::buildItemAdditionalInfo);
        registerFunction(Aspect.class, this::buildAspectAdditionalInfo);
    }

    public ImmutableList<InfoPanel> buildItemAdditionalInfo(Item item, DisplayService service) {
        List<AspectEntry> aspectEntries =
                aspectEntryRepository.findAll(AspectEntrySpec.buildItemIdSpec(item.getId()));
        if (aspectEntries.isEmpty()) {
            return ImmutableList.of();
        }

        InfoPanel.Builder panelBuilder = InfoPanel.builder().setTitle("Thaumcraft Aspects");
        aspectEntries.stream()
                .sorted()
                .map(aspectEntry -> DisplayAspectEntry.buildAspectIcon(aspectEntry, service))
                .forEach(panelBuilder::addIcon);

        return ImmutableList.of(panelBuilder.build());
    }

    public ImmutableList<InfoPanel> buildAspectAdditionalInfo(
            Aspect aspect, DisplayService service) {
        InfoPanel aspectPanel =
                InfoPanel.builder()
                        .setTitle("Thaumcraft Aspects")
                        .addLink(
                                Link.create(
                                        "bi-search",
                                        "Aspect entries",
                                        Table.THAUMCRAFT_ASPECT_ENTRY.getSearchUrl(
                                                "aspectId", aspect.getId())))
                        .build();

        return ImmutableList.of(aspectPanel);
    }
}

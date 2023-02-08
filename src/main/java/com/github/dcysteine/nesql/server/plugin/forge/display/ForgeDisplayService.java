package com.github.dcysteine.nesql.server.plugin.forge.display;

import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.InfoPanel;
import com.github.dcysteine.nesql.server.common.display.Link;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.server.plugin.PluginDisplayService;
import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluid;
import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluidStack;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItem;
import com.github.dcysteine.nesql.server.plugin.forge.spec.FluidBlockSpec;
import com.github.dcysteine.nesql.server.plugin.forge.spec.FluidContainerSpec;
import com.github.dcysteine.nesql.server.plugin.forge.spec.OreDictionarySpec;
import com.github.dcysteine.nesql.sql.Plugin;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.forge.FluidBlock;
import com.github.dcysteine.nesql.sql.forge.FluidBlockRepository;
import com.github.dcysteine.nesql.sql.forge.FluidContainer;
import com.github.dcysteine.nesql.sql.forge.FluidContainerRepository;
import com.github.dcysteine.nesql.sql.forge.OreDictionary;
import com.github.dcysteine.nesql.sql.forge.OreDictionaryRepository;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/** Helper class that provides dependencies and methods for building display objects. */
@Service
public class ForgeDisplayService extends PluginDisplayService {
    @Autowired
    private OreDictionaryRepository oreDictionaryRepository;

    @Autowired
    private FluidBlockRepository fluidBlockRepository;

    @Autowired
    private FluidContainerRepository fluidContainerRepository;

    public ForgeDisplayService() {
        super(Plugin.BASE);
        registerFunction(Item.class, this::buildItemAdditionalInfo);
        registerFunction(Fluid.class, this::buildFluidAdditionalInfo);
        registerFunction(ItemGroup.class, this::buildItemGroupAdditionalInfo);
    }

    public ImmutableList<InfoPanel> buildItemAdditionalInfo(Item item, DisplayService service) {
        ImmutableList.Builder<InfoPanel> listBuilder = ImmutableList.builder();

        InfoPanel oreDictionary =
                InfoPanel.builder()
                        .setTitle("Ore Dictionary")
                        .addLink(
                                Link.create(
                                        "bi-search",
                                        "Ore dictionary",
                                        Table.ORE_DICTIONARY.getSearchUrl(
                                                "itemId", item.getId())))
                        .build();
        listBuilder.add(oreDictionary);

        InfoPanel.Builder fluidRegistryBuilder = InfoPanel.builder().setTitle("Fluid Registry");
        boolean hasFluidRegistryEntry = false;

        Optional<FluidBlock> fluidBlockOptional =
                fluidBlockRepository.findOne(FluidBlockSpec.buildItemIdSpec(item.getId()));
        if (fluidBlockOptional.isPresent()) {
            fluidRegistryBuilder.addIcon(
                    DisplayFluid.buildIcon(fluidBlockOptional.get().getFluid(), service));
            hasFluidRegistryEntry = true;
        }

        Optional<FluidContainer> fluidContainerOptional =
                fluidContainerRepository.findOne(
                        FluidContainerSpec.buildFilledItemIdSpec(item.getId()));
        if (fluidContainerOptional.isPresent()) {
            fluidRegistryBuilder.addIcon(
                    DisplayFluidStack.buildIcon(
                            fluidContainerOptional.get().getFluidStack(), service));
            fluidRegistryBuilder.addIcon(
                    DisplayItem.buildIcon(
                            fluidContainerOptional.get().getEmptyContainer(), service));
            hasFluidRegistryEntry = true;
        }

        List<FluidContainer> emptyContainers =
                fluidContainerRepository.findAll(
                        FluidContainerSpec.buildEmptyItemIdSpec(item.getId()));
        if (!emptyContainers.isEmpty()) {
            emptyContainers.stream()
                    .map(fluidContainer -> DisplayFluidContainer.buildIcon(fluidContainer, service))
                    .forEach(fluidRegistryBuilder::addIcon);
            hasFluidRegistryEntry = true;
        }

        if (hasFluidRegistryEntry) {
            listBuilder.add(fluidRegistryBuilder.build());
        }
        return listBuilder.build();
    }

    public ImmutableList<InfoPanel> buildFluidAdditionalInfo(Fluid fluid, DisplayService service) {
        InfoPanel.Builder fluidRegistryBuilder = InfoPanel.builder().setTitle("Fluid Registry");
        boolean hasFluidRegistryEntry = false;

        Optional<FluidBlock> fluidBlockOptional =
                fluidBlockRepository.findOne(FluidBlockSpec.buildFluidIdSpec(fluid.getId()));
        if (fluidBlockOptional.isPresent()) {
            fluidRegistryBuilder.addIcon(
                    DisplayItem.buildIcon(fluidBlockOptional.get().getBlock(), service));
            hasFluidRegistryEntry = true;
        }

        List<FluidContainer> fluidContainers =
                fluidContainerRepository.findAll(
                        FluidContainerSpec.buildFluidIdSpec(fluid.getId()));
        if (!fluidContainers.isEmpty()) {
            fluidContainers.stream()
                    .map(fluidContainer -> DisplayFluidContainer.buildIcon(fluidContainer, service))
                    .forEach(fluidRegistryBuilder::addIcon);
            hasFluidRegistryEntry = true;
        }

        if (hasFluidRegistryEntry) {
            return ImmutableList.of(fluidRegistryBuilder.build());
        }
        return ImmutableList.of();
    }

    public ImmutableList<InfoPanel> buildItemGroupAdditionalInfo(
            ItemGroup itemGroup, DisplayService service) {
        List<OreDictionary> oreDictionaryEntries =
                oreDictionaryRepository.findAll(
                        OreDictionarySpec.buildItemGroupIdSpec(itemGroup.getId()));
        if (oreDictionaryEntries.isEmpty()) {
            return ImmutableList.of();
        }

        InfoPanel.Builder builder = InfoPanel.builder().setTitle("Ore Dictionary");
        oreDictionaryEntries.stream()
                .map(oreDictionary -> DisplayOreDictionary.buildIcon(oreDictionary, service))
                .forEach(builder::addIcon);

        return ImmutableList.of(builder.build());
    }
}

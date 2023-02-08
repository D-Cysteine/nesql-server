package com.github.dcysteine.nesql.server.plugin.forge.display;

import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.sql.forge.FluidContainer;
import com.github.dcysteine.nesql.sql.forge.OreDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service providing convenient methods for building display objects. */
@Service
public class ForgeDisplayFactory {
    @Autowired
    private DisplayService service;

    public DisplayOreDictionary buildDisplayOreDictionary(OreDictionary oreDictionary) {
        return DisplayOreDictionary.create(oreDictionary, service);
    }

    public Icon buildDisplayOreDictionaryIcon(OreDictionary oreDictionary) {
        return DisplayOreDictionary.buildIcon(oreDictionary, service);
    }

    public DisplayFluidContainer buildDisplayFluidContainer(FluidContainer fluidContainer) {
        return DisplayFluidContainer.create(fluidContainer, service);
    }

    public Icon buildDisplayFluidContainerIcon(FluidContainer fluidContainer) {
        return DisplayFluidContainer.buildIcon(fluidContainer, service);
    }
}

package com.github.dcysteine.nesql.server.plugin.thaumcraft.display;

import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.sql.thaumcraft.Aspect;
import com.github.dcysteine.nesql.sql.thaumcraft.AspectEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service providing convenient methods for building display objects. */
@Service
public class ThaumcraftDisplayFactory {
    @Autowired
    private DisplayService service;

    public DisplayAspect buildDisplayAspect(Aspect aspect) {
        return DisplayAspect.create(aspect, service);
    }

    public Icon buildDisplayAspectIcon(Aspect aspect) {
        return DisplayAspect.buildIcon(aspect, service);
    }

    public DisplayAspectEntry buildDisplayAspectEntry(AspectEntry aspect) {
        return DisplayAspectEntry.create(aspect, service);
    }

    public Icon buildDisplayAspectEntryIcon(AspectEntry aspect) {
        return DisplayAspectEntry.buildIcon(aspect, service);
    }
}

package com.github.dcysteine.nesql.server.plugin.base;

import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluidGroup;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayService;
import com.github.dcysteine.nesql.server.service.SearchService;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping(path = "/fluidgroup")
public class FluidGroupController {
    @Autowired
    private FluidGroupRepository fluidGroupRepository;

    @Autowired
    private BaseDisplayService baseDisplayService;

    @Autowired
    private SearchService searchService;

    @GetMapping(path = "/view/{fluid_group_id}")
    public String view(@PathVariable(name = "fluid_group_id") String id, Model model) {
        Optional<FluidGroup> fluidGroupOptional = fluidGroupRepository.findById(id);
        if (fluidGroupOptional.isEmpty()) {
            return "not_found";
        }
        FluidGroup fluidGroup = fluidGroupOptional.get();
        DisplayFluidGroup displayFluidGroup = baseDisplayService.buildDisplayFluidGroup(fluidGroup);

        model.addAttribute("fluidGroup", fluidGroup);
        model.addAttribute("displayFluidGroup", displayFluidGroup);
        return "plugin/base/fluidgroup/view";
    }

    @GetMapping(path = "/search")
    public String search() {
        // TODO add a search page
        return "redirect:all";
    }

    @GetMapping(path = "/all")
    public String all(@RequestParam(defaultValue = "1") int page, Model model) {
        return searchService.handleGetAll(
                page, model, fluidGroupRepository, baseDisplayService::buildDisplayFluidGroupIcon);
    }
}

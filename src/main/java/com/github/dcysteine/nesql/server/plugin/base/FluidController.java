package com.github.dcysteine.nesql.server.plugin.base;

import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluid;
import com.github.dcysteine.nesql.server.plugin.base.specs.FluidSpecs;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayService;
import com.github.dcysteine.nesql.server.service.SearchService;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.FluidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

@Controller
@RequestMapping(path = "/fluid")
public class FluidController {
    @Autowired
    private FluidRepository fluidRepository;

    @Autowired
    private BaseDisplayService baseDisplayService;

    @Autowired
    private SearchService searchService;

    @GetMapping(path = "/{fluid_id}")
    public String view(@PathVariable(name = "fluid_id") String id, Model model) {
        Optional<Fluid> fluidOptional = fluidRepository.findById(id);
        if (fluidOptional.isEmpty()) {
            return "not_found";
        }
        Fluid fluid = fluidOptional.get();
        DisplayFluid displayFluid = baseDisplayService.buildDisplayFluid(fluid);

        model.addAttribute("fluid", fluid);
        model.addAttribute("displayFluid", displayFluid);
        return "plugin/base/fluid/fluid";
    }

    @GetMapping(path = "/search")
    public String search() {
        return "plugin/base/fluid/search";
    }

    @GetMapping(path = "/all")
    public String all(@RequestParam(defaultValue = "1") int page, Model model) {
        return searchService.handleGetAll(
                page, model, fluidRepository, baseDisplayService::buildDisplayFluid);
    }

    @GetMapping(path = "/searchresults")
    public String searchResults(
            @RequestParam(required = false) Optional<String> localizedName,
            @RequestParam(required = false) Optional<String> internalName,
            @RequestParam(required = false) Optional<Integer> fluidId,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        @Nullable
        Specification<Fluid> localizedNameSpec =
                localizedName
                        .filter(Predicate.not(String::isEmpty))
                        .map(FluidSpecs::buildLocalizedNameSpec).orElse(null);

        @Nullable
        Specification<Fluid> internalNameSpec =
                internalName
                        .filter(Predicate.not(String::isEmpty))
                        .map(FluidSpecs::buildInternalNameSpec).orElse(null);

        @Nullable
        Specification<Fluid> fluidIdSpec =
                fluidId.map(FluidSpecs::buildFluidIdSpec).orElse(null);

        Specification<Fluid> spec =
                Specification.allOf(localizedNameSpec, internalNameSpec, fluidIdSpec);
        return searchService.handleSearch(
                page, model, fluidRepository, spec, baseDisplayService::buildDisplayFluidIcon);
    }
}

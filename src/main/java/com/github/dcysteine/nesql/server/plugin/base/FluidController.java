package com.github.dcysteine.nesql.server.plugin.base;

import com.github.dcysteine.nesql.server.common.SearchResultsLayout;
import com.github.dcysteine.nesql.server.common.util.ParamUtil;
import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluid;
import com.github.dcysteine.nesql.server.plugin.base.spec.FluidSpec;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayFactory;
import com.github.dcysteine.nesql.server.common.service.SearchService;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.FluidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/base/fluid")
public class FluidController {
    @Autowired
    private FluidRepository fluidRepository;

    @Autowired
    private BaseDisplayFactory baseDisplayFactory;

    @Autowired
    private SearchService searchService;

    @GetMapping(path = "/view/{fluid_id}")
    public String view(@PathVariable(name = "fluid_id") String id, Model model) {
        Optional<Fluid> fluidOptional = fluidRepository.findById(id);
        if (fluidOptional.isEmpty()) {
            return "not_found";
        }
        Fluid fluid = fluidOptional.get();
        DisplayFluid displayFluid = baseDisplayFactory.buildDisplayFluid(fluid);

        model.addAttribute("fluid", fluid);
        model.addAttribute("displayFluid", displayFluid);
        return "plugin/base/fluid/view";
    }

    @GetMapping(path = "/search")
    public String search(
            @RequestParam(required = false) Optional<String> localizedName,
            @RequestParam(required = false) Optional<String> modId,
            @RequestParam(required = false) Optional<String> internalName,
            @RequestParam(required = false) Optional<String> nbt,
            @RequestParam(required = false) Optional<String> fluidGroupId,
            @RequestParam(required = false) Optional<String> recipeId,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        List<Specification<Fluid>> specs = new ArrayList<>();
        specs.add(ParamUtil.buildStringSpec(localizedName, FluidSpec::buildLocalizedNameSpec));
        specs.add(ParamUtil.buildStringSpec(modId, FluidSpec::buildModIdSpec));
        specs.add(ParamUtil.buildStringSpec(internalName, FluidSpec::buildInternalNameSpec));
        specs.add(ParamUtil.buildStringSpec(nbt, FluidSpec::buildNbtSpec));
        specs.add(ParamUtil.buildStringSpec(fluidGroupId, FluidSpec::buildFluidGroupSpec));
        specs.add(ParamUtil.buildStringSpec(recipeId, FluidSpec::buildRecipeInputSpec));

        PageRequest pageRequest =
                searchService.buildPageRequest(
                        page, SearchResultsLayout.GRID, FluidSpec.DEFAULT_SORT);
        searchService.handleSearch(
                pageRequest, model, fluidRepository, Specification.allOf(specs),
                baseDisplayFactory::buildDisplayFluidIcon);
        return "plugin/base/fluid/search";
    }
}

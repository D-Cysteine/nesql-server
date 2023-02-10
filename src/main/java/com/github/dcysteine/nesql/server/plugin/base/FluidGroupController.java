package com.github.dcysteine.nesql.server.plugin.base;

import com.github.dcysteine.nesql.server.common.SearchResultsLayout;
import com.github.dcysteine.nesql.server.common.util.ParamUtil;
import com.github.dcysteine.nesql.server.plugin.base.display.fluid.DisplayFluidGroup;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayFactory;
import com.github.dcysteine.nesql.server.plugin.base.spec.FluidGroupSpec;
import com.github.dcysteine.nesql.server.common.service.SearchService;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroupRepository;
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
@RequestMapping(path = "/base/fluidgroup")
public class FluidGroupController {
    @Autowired
    private FluidGroupRepository fluidGroupRepository;

    @Autowired
    private BaseDisplayFactory baseDisplayFactory;

    @Autowired
    private SearchService searchService;

    @GetMapping(path = "/view/{fluid_group_id}")
    public String view(@PathVariable(name = "fluid_group_id") String id, Model model) {
        Optional<FluidGroup> fluidGroupOptional = fluidGroupRepository.findById(id);
        if (fluidGroupOptional.isEmpty()) {
            return "not_found";
        }
        FluidGroup fluidGroup = fluidGroupOptional.get();
        DisplayFluidGroup displayFluidGroup = baseDisplayFactory.buildDisplayFluidGroup(fluidGroup);

        model.addAttribute("fluidGroup", fluidGroup);
        model.addAttribute("displayFluidGroup", displayFluidGroup);
        return "plugin/base/fluidgroup/view";
    }

    @GetMapping(path = "/search")
    public String search(
            @RequestParam(required = false) Optional<String> fluidName,
            @RequestParam(required = false) Optional<String> fluidModId,
            @RequestParam(required = false) Optional<String> fluidId,
            @RequestParam(required = false) Optional<Integer> amount,
            @RequestParam(required = false) Optional<Integer> minSize,
            @RequestParam(required = false) Optional<Integer> maxSize,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        List<Specification<FluidGroup>> specs = new ArrayList<>();
        specs.add(ParamUtil.buildStringSpec(fluidName, FluidGroupSpec::buildFluidNameSpec));
        specs.add(ParamUtil.buildStringSpec(fluidModId, FluidGroupSpec::buildFluidModIdSpec));
        specs.add(ParamUtil.buildStringSpec(fluidId, FluidGroupSpec::buildFluidIdSpec));
        specs.add(ParamUtil.buildSpec(amount, FluidGroupSpec::buildAmountSpec));
        specs.add(ParamUtil.buildSpec(minSize, FluidGroupSpec::buildMinSizeSpec));
        specs.add(ParamUtil.buildSpec(maxSize, FluidGroupSpec::buildMaxSizeSpec));

        PageRequest pageRequest = searchService.buildPageRequest(page, SearchResultsLayout.GRID);
        searchService.handleSearch(
                pageRequest, model, fluidGroupRepository, Specification.allOf(specs),
                baseDisplayFactory::buildDisplayFluidGroupIcon);
        return "plugin/base/fluidgroup/search";
    }
}

package com.github.dcysteine.nesql.server.plugin.thaumcraft;

import com.github.dcysteine.nesql.server.common.SearchResultsLayout;
import com.github.dcysteine.nesql.server.common.service.SearchService;
import com.github.dcysteine.nesql.server.common.util.ParamUtil;
import com.github.dcysteine.nesql.server.plugin.thaumcraft.display.DisplayAspect;
import com.github.dcysteine.nesql.server.plugin.thaumcraft.display.ThaumcraftDisplayFactory;
import com.github.dcysteine.nesql.server.plugin.thaumcraft.spec.AspectSpec;
import com.github.dcysteine.nesql.sql.thaumcraft.Aspect;
import com.github.dcysteine.nesql.sql.thaumcraft.AspectRepository;
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
@RequestMapping(path = "/thaumcraftaspect")
public class AspectController {
    @Autowired
    private AspectRepository aspectRepository;

    @Autowired
    private ThaumcraftDisplayFactory thaumcraftDisplayFactory;

    @Autowired
    private SearchService searchService;

    @GetMapping(path = "/view/{aspect_id}")
    public String view(@PathVariable(name = "aspect_id") String id, Model model) {
        Optional<Aspect> aspectOptional = aspectRepository.findById(id);
        if (aspectOptional.isEmpty()) {
            return "not_found";
        }
        Aspect aspect = aspectOptional.get();
        DisplayAspect displayAspect = thaumcraftDisplayFactory.buildDisplayAspect(aspect);

        model.addAttribute("aspect", aspect);
        model.addAttribute("displayAspect", displayAspect);
        return "plugin/thaumcraft/aspect/view";
    }

    @GetMapping(path = "/search")
    public String search(
            @RequestParam(required = false) Optional<String> aspectName,
            @RequestParam(required = false) Optional<String> componentName,
            @RequestParam(required = false) Optional<String> componentOfName,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        List<Specification<Aspect>> specs = new ArrayList<>();
        specs.add(ParamUtil.buildStringSpec(aspectName, AspectSpec::buildAspectNameSpec));
        specs.add(ParamUtil.buildStringSpec(componentName, AspectSpec::buildComponentNameSpec));
        specs.add(ParamUtil.buildStringSpec(componentOfName, AspectSpec::buildComponentOfNameSpec));

        PageRequest pageRequest =
                searchService.buildPageRequest(
                        page, SearchResultsLayout.GRID, AspectSpec.DEFAULT_SORT);
        searchService.handleSearch(
                pageRequest, model, aspectRepository, Specification.allOf(specs),
                thaumcraftDisplayFactory::buildDisplayAspectIcon);
        return "plugin/thaumcraft/aspect/search";
    }
}

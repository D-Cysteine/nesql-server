package com.github.dcysteine.nesql.server.plugin.thaumcraft;

import com.github.dcysteine.nesql.server.common.SearchResultsLayout;
import com.github.dcysteine.nesql.server.common.service.SearchService;
import com.github.dcysteine.nesql.server.common.util.ParamUtil;
import com.github.dcysteine.nesql.server.plugin.thaumcraft.display.ThaumcraftDisplayFactory;
import com.github.dcysteine.nesql.server.plugin.thaumcraft.spec.AspectEntrySpec;
import com.github.dcysteine.nesql.sql.thaumcraft.AspectEntry;
import com.github.dcysteine.nesql.sql.thaumcraft.AspectEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/thaumcraftaspectentry")
public class AspectEntryController {
    @Autowired
    private AspectEntryRepository aspectEntryRepository;

    @Autowired
    private ThaumcraftDisplayFactory thaumcraftDisplayFactory;

    @Autowired
    private SearchService searchService;

    // No view mapping - we redirect to Item's view.

    @GetMapping(path = "/search")
    public String search(
            @RequestParam(required = false) Optional<String> itemName,
            @RequestParam(required = false) Optional<String> itemModId,
            @RequestParam(required = false) Optional<String> aspectName,
            @RequestParam(required = false) Optional<String> aspectId,
            @RequestParam(required = false) Optional<Integer> minAmount,
            @RequestParam(required = false) Optional<Integer> maxAmount,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        List<Specification<AspectEntry>> specs = new ArrayList<>();
        specs.add(ParamUtil.buildStringSpec(itemName, AspectEntrySpec::buildItemNameSpec));
        specs.add(ParamUtil.buildStringSpec(itemModId, AspectEntrySpec::buildItemModIdSpec));
        specs.add(ParamUtil.buildStringSpec(aspectName, AspectEntrySpec::buildAspectNameSpec));
        specs.add(ParamUtil.buildStringSpec(aspectId, AspectEntrySpec::buildAspectIdSpec));
        specs.add(ParamUtil.buildSpec(minAmount, AspectEntrySpec::buildMinAmountSpec));
        specs.add(ParamUtil.buildSpec(maxAmount, AspectEntrySpec::buildMaxAmountSpec));

        PageRequest pageRequest =
                searchService.buildPageRequest(
                        page, SearchResultsLayout.GRID, AspectEntrySpec.DEFAULT_SORT);
        searchService.handleSearch(
                pageRequest, model, aspectEntryRepository, Specification.allOf(specs),
                thaumcraftDisplayFactory::buildDisplayAspectEntryIcon);
        return "plugin/thaumcraft/aspectentry/search";
    }
}

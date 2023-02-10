package com.github.dcysteine.nesql.server.plugin.forge;

import com.github.dcysteine.nesql.server.common.SearchResultsLayout;
import com.github.dcysteine.nesql.server.common.service.SearchService;
import com.github.dcysteine.nesql.server.common.util.ParamUtil;
import com.github.dcysteine.nesql.server.plugin.forge.display.ForgeDisplayFactory;
import com.github.dcysteine.nesql.server.plugin.forge.spec.OreDictionarySpec;
import com.github.dcysteine.nesql.sql.forge.OreDictionary;
import com.github.dcysteine.nesql.sql.forge.OreDictionaryRepository;
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
@RequestMapping(path = "/forge/oredictionary")
public class OreDictionaryController {
    @Autowired
    private OreDictionaryRepository oreDictionaryRepository;

    @Autowired
    private ForgeDisplayFactory forgeDisplayFactory;

    @Autowired
    private SearchService searchService;

    // No view mapping - we redirect to ItemGroup's view.

    @GetMapping(path = "/search")
    public String search(
            @RequestParam(required = false) Optional<String> itemName,
            @RequestParam(required = false) Optional<String> itemModId,
            @RequestParam(required = false) Optional<String> itemId,
            @RequestParam(required = false) Optional<String> oreName,
            @RequestParam(required = false) Optional<Integer> minSize,
            @RequestParam(required = false) Optional<Integer> maxSize,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        List<Specification<OreDictionary>> specs = new ArrayList<>();
        specs.add(ParamUtil.buildStringSpec(itemName, OreDictionarySpec::buildItemNameSpec));
        specs.add(ParamUtil.buildStringSpec(itemModId, OreDictionarySpec::buildItemModIdSpec));
        specs.add(ParamUtil.buildStringSpec(itemId, OreDictionarySpec::buildItemIdSpec));
        specs.add(ParamUtil.buildStringSpec(oreName, OreDictionarySpec::buildOreNameSpec));
        specs.add(ParamUtil.buildSpec(minSize, OreDictionarySpec::buildMinSizeSpec));
        specs.add(ParamUtil.buildSpec(maxSize, OreDictionarySpec::buildMaxSizeSpec));

        PageRequest pageRequest =
                searchService.buildPageRequest(
                        page, SearchResultsLayout.LIST, OreDictionarySpec.DEFAULT_SORT);
        searchService.handleSearch(
                pageRequest, model, oreDictionaryRepository, Specification.allOf(specs),
                forgeDisplayFactory::buildDisplayOreDictionaryIcon);
        return "plugin/forge/oredictionary/search";
    }
}

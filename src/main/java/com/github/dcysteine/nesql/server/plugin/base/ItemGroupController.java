package com.github.dcysteine.nesql.server.plugin.base;

import com.github.dcysteine.nesql.server.common.SearchResultsLayout;
import com.github.dcysteine.nesql.server.common.util.ParamUtil;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItemGroup;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayFactory;
import com.github.dcysteine.nesql.server.plugin.base.spec.ItemGroupSpec;
import com.github.dcysteine.nesql.server.service.SearchService;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemGroupRepository;
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
@RequestMapping(path = "/itemgroup")
public class ItemGroupController {
    @Autowired
    private ItemGroupRepository itemGroupRepository;

    @Autowired
    private BaseDisplayFactory baseDisplayFactory;

    @Autowired
    private SearchService searchService;

    @GetMapping(path = "/view/{item_group_id}")
    public String view(@PathVariable(name = "item_group_id") String id, Model model) {
        Optional<ItemGroup> itemGroupOptional = itemGroupRepository.findById(id);
        if (itemGroupOptional.isEmpty()) {
            return "not_found";
        }
        ItemGroup itemGroup = itemGroupOptional.get();
        DisplayItemGroup displayItemGroup = baseDisplayFactory.buildDisplayItemGroup(itemGroup);

        model.addAttribute("itemGroup", itemGroup);
        model.addAttribute("displayItemGroup", displayItemGroup);
        return "plugin/base/itemgroup/view";
    }

    @GetMapping(path = "/search")
    public String search(
            @RequestParam(required = false) Optional<String> itemName,
            @RequestParam(required = false) Optional<String> itemModId,
            @RequestParam(required = false) Optional<String> itemId,
            @RequestParam(required = false) Optional<Integer> stackSize,
            @RequestParam(required = false) Optional<Integer> minSize,
            @RequestParam(required = false) Optional<Integer> maxSize,
            @RequestParam(required = false) Optional<Boolean> noWildcard,
            @RequestParam(required = false) Optional<Boolean> hasWildcard,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        List<Specification<ItemGroup>> specs = new ArrayList<>();
        specs.add(ParamUtil.buildStringSpec(itemName, ItemGroupSpec::buildItemNameSpec));
        specs.add(ParamUtil.buildStringSpec(itemModId, ItemGroupSpec::buildItemModIdSpec));
        specs.add(ParamUtil.buildStringSpec(itemId, ItemGroupSpec::buildItemIdSpec));
        specs.add(ParamUtil.buildSpec(stackSize, ItemGroupSpec::buildStackSizeSpec));
        specs.add(ParamUtil.buildSpec(minSize, ItemGroupSpec::buildMinSizeSpec));
        specs.add(ParamUtil.buildSpec(maxSize, ItemGroupSpec::buildMaxSizeSpec));
        specs.add(ParamUtil.buildBooleanSpec(noWildcard, ItemGroupSpec::buildNoWildcardSpec));
        specs.add(ParamUtil.buildBooleanSpec(hasWildcard, ItemGroupSpec::buildHasWildcardSpec));

        PageRequest pageRequest = searchService.buildPageRequest(page, SearchResultsLayout.GRID);
        searchService.handleSearch(
                pageRequest, model, itemGroupRepository, Specification.allOf(specs),
                baseDisplayFactory::buildDisplayItemGroupIcon);
        return "plugin/base/itemgroup/search";
    }
}

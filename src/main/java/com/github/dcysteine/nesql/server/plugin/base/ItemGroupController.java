package com.github.dcysteine.nesql.server.plugin.base;

import com.github.dcysteine.nesql.server.common.SearchResultsLayout;
import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItemGroup;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayFactory;
import com.github.dcysteine.nesql.server.service.SearchService;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String search() {
        // TODO add a search page
        return "redirect:all";
    }

    @GetMapping(path = "/all")
    public String all(@RequestParam(defaultValue = "1") int page, Model model) {
        PageRequest pageRequest = searchService.buildPageRequest(page, SearchResultsLayout.LIST);
        return searchService.handleGetAll(
                pageRequest, model, itemGroupRepository,
                baseDisplayFactory::buildDisplayItemGroupIcon);
    }
}

package com.github.dcysteine.nesql.server.plugin.base;

import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItem;
import com.github.dcysteine.nesql.server.plugin.base.specs.ItemSpecs;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayService;
import com.github.dcysteine.nesql.server.service.SearchService;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
import com.google.common.base.Strings;
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
@RequestMapping(path = "/item")
public class ItemController {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BaseDisplayService baseDisplayService;

    @Autowired
    private SearchService searchService;

    @GetMapping(path = "/{item_id}")
    public String view(@PathVariable(name = "item_id") String id, Model model) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isEmpty()) {
            return "not_found";
        }
        Item item = itemOptional.get();
        DisplayItem displayItem = baseDisplayService.buildDisplayItem(item);

        model.addAttribute("item", item);
        model.addAttribute("displayItem", displayItem);
        return "plugin/base/item/item";
    }

    @GetMapping(path = "/search")
    public String search() {
        return "plugin/base/item/search";
    }

    @GetMapping(path = "/all")
    public String all(@RequestParam(defaultValue = "1") int page, Model model) {
        return searchService.handleGetAll(
                page, model, itemRepository, baseDisplayService::buildDisplayItem);
    }

    @GetMapping(path = "/searchresults")
    public String searchResults(
            @RequestParam(required = false) Optional<String> localizedName,
            @RequestParam(required = false) Optional<String> internalName,
            @RequestParam(required = false) Optional<Integer> itemId,
            @RequestParam(required = false) Optional<Integer> itemDamage,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        @Nullable
        Specification<Item> localizedNameSpec =
                localizedName
                        .filter(Predicate.not(String::isEmpty))
                        .map(ItemSpecs::buildLocalizedNameSpec).orElse(null);

        @Nullable
        Specification<Item> internalNameSpec =
                internalName
                        .filter(Predicate.not(String::isEmpty))
                        .map(ItemSpecs::buildInternalNameSpec).orElse(null);

        @Nullable
        Specification<Item> itemIdSpec =
                itemId.map(ItemSpecs::buildItemIdSpec).orElse(null);

        @Nullable
        Specification<Item> itemDamageSpec =
                itemDamage.map(ItemSpecs::buildItemDamageSpec).orElse(null);

        Specification<Item> spec =
                Specification.allOf(
                        localizedNameSpec, internalNameSpec, itemIdSpec, itemDamageSpec);
        return searchService.handleSearch(
                page, model, itemRepository, spec, baseDisplayService::buildDisplayItemIcon);
    }
}

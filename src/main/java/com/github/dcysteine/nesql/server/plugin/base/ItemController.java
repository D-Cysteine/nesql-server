package com.github.dcysteine.nesql.server.plugin.base;

import com.github.dcysteine.nesql.server.plugin.base.display.item.DisplayItem;
import com.github.dcysteine.nesql.server.plugin.base.spec.ItemSpec;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayFactory;
import com.github.dcysteine.nesql.server.service.SearchService;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
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
    private BaseDisplayFactory baseDisplayFactory;

    @Autowired
    private SearchService searchService;

    @GetMapping(path = "/view/{item_id}")
    public String view(@PathVariable(name = "item_id") String id, Model model) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isEmpty()) {
            return "not_found";
        }
        Item item = itemOptional.get();
        DisplayItem displayItem = baseDisplayFactory.buildDisplayItem(item);

        model.addAttribute("item", item);
        model.addAttribute("displayItem", displayItem);
        return "plugin/base/item/view";
    }

    @GetMapping(path = "/search")
    public String search(
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
                        .map(ItemSpec::buildLocalizedNameSpec).orElse(null);

        @Nullable
        Specification<Item> internalNameSpec =
                internalName
                        .filter(Predicate.not(String::isEmpty))
                        .map(ItemSpec::buildInternalNameSpec).orElse(null);

        @Nullable
        Specification<Item> itemIdSpec =
                itemId.map(ItemSpec::buildItemIdSpec).orElse(null);

        @Nullable
        Specification<Item> itemDamageSpec =
                itemDamage.map(ItemSpec::buildItemDamageSpec).orElse(null);

        Specification<Item> spec =
                Specification.allOf(
                        localizedNameSpec, internalNameSpec, itemIdSpec, itemDamageSpec);
        searchService.handleSearch(
                page, model, itemRepository,
                spec, ItemSpec.DEFAULT_SORT, baseDisplayFactory::buildDisplayItemIcon);
        return "plugin/base/item/search";
    }
}

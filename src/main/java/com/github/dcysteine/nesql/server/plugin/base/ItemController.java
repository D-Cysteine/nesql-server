package com.github.dcysteine.nesql.server.plugin.base;

import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/item")
public class ItemController {
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    public String item(@RequestParam(name = "id") String id, Model model) {
        Item item = itemRepository.findById(id).get();
        model.addAttribute("id", id);
        model.addAttribute("imageFilePath", item.getImageFilePath());
        model.addAttribute("name", item.getInternalName());
        return "plugin/base/item";
    }

    @GetMapping(path="/test")
    public String test(Model model) {
        model.addAttribute("id", "id");
        model.addAttribute("name", "name");
        return "plugin/base/item";
    }
}

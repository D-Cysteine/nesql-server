package com.github.dcysteine.nesql.server.plugin.base;

import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayFactory;
import com.github.dcysteine.nesql.server.plugin.base.display.recipe.DisplayRecipeType;
import com.github.dcysteine.nesql.server.service.SearchService;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping(path = "/recipetype")
public class RecipeTypeController {
    @Autowired
    private RecipeTypeRepository recipeTypeRepository;

    @Autowired
    private BaseDisplayFactory baseDisplayFactory;

    @Autowired
    private SearchService searchService;

    @GetMapping(path = "/view/{recipe_type_id}")
    public String view(@PathVariable(name = "recipe_type_id") String id, Model model) {
        Optional<RecipeType> recipeTypeOptional = recipeTypeRepository.findById(id);
        if (recipeTypeOptional.isEmpty()) {
            return "not_found";
        }
        RecipeType recipeType = recipeTypeOptional.get();
        DisplayRecipeType displayRecipeType = baseDisplayFactory.buildDisplayRecipeType(recipeType);

        model.addAttribute("recipeType", recipeType);
        model.addAttribute("displayRecipeType", displayRecipeType);
        return "plugin/base/recipetype/view";
    }

    @GetMapping(path = "/search")
    public String search() {
        // TODO add a search page
        return "redirect:all";
    }

    @GetMapping(path = "/all")
    public String all(@RequestParam(defaultValue = "1") int page, Model model) {
        return searchService.handleGetAll(
                page, model, recipeTypeRepository, baseDisplayFactory::buildDisplayRecipeTypeIcon);
    }
}

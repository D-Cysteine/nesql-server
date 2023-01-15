package com.github.dcysteine.nesql.server.plugin.base;

import com.github.dcysteine.nesql.server.plugin.base.display.recipe.DisplayRecipe;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayService;
import com.github.dcysteine.nesql.server.service.SearchService;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping(path = "/recipe")
public class RecipeController {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private BaseDisplayService baseDisplayService;

    @Autowired
    private SearchService searchService;

    @GetMapping(path = "/view/{recipe_id}")
    public String view(@PathVariable(name = "recipe_id") String id, Model model) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isEmpty()) {
            return "not_found";
        }
        Recipe recipe = recipeOptional.get();
        DisplayRecipe displayRecipe = baseDisplayService.buildDisplayRecipe(recipe);

        model.addAttribute("recipe", recipe);
        model.addAttribute("displayRecipe", displayRecipe);
        return "plugin/base/recipe/view";
    }

    @GetMapping(path = "/search")
    public String search() {
        // TODO add a search page
        return "redirect:all";
    }

    @GetMapping(path = "/all")
    public String all(@RequestParam(defaultValue = "1") int page, Model model) {
        return searchService.handleGetAll(
                page, model, recipeRepository, baseDisplayService::buildDisplayRecipeIcon);
    }
}

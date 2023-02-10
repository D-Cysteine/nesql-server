package com.github.dcysteine.nesql.server.plugin.base;

import com.github.dcysteine.nesql.server.common.SearchResultsLayout;
import com.github.dcysteine.nesql.server.common.util.ParamUtil;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayFactory;
import com.github.dcysteine.nesql.server.plugin.base.display.recipe.DisplayRecipeType;
import com.github.dcysteine.nesql.server.plugin.base.spec.RecipeTypeSpec;
import com.github.dcysteine.nesql.server.common.service.SearchService;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeTypeRepository;
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
@RequestMapping(path = "/base/recipetype")
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
    public String search(
            @RequestParam(required = false) Optional<String> recipeCategory,
            @RequestParam(required = false) Optional<String> recipeType,
            @RequestParam(required = false) Optional<Long> minRecipeCount,
            @RequestParam(required = false) Optional<Long> maxRecipeCount,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        List<Specification<RecipeType>> specs = new ArrayList<>();
        specs.add(
                ParamUtil.buildStringSpec(recipeCategory, RecipeTypeSpec::buildRecipeCategorySpec));
        specs.add(ParamUtil.buildStringSpec(recipeType, RecipeTypeSpec::buildRecipeTypeSpec));
        specs.add(ParamUtil.buildSpec(minRecipeCount, RecipeTypeSpec::buildMinRecipeCountSpec));
        specs.add(ParamUtil.buildSpec(maxRecipeCount, RecipeTypeSpec::buildMaxRecipeCountSpec));

        PageRequest pageRequest =
                searchService.buildPageRequest(
                        page, SearchResultsLayout.GRID, RecipeTypeSpec.DEFAULT_SORT);
        searchService.handleSearch(
                pageRequest, model, recipeTypeRepository, Specification.allOf(specs),
                baseDisplayFactory::buildDisplayRecipeTypeIcon);
        return "plugin/base/recipetype/search";
    }
}

package com.github.dcysteine.nesql.server.plugin.base;

import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.server.common.util.ParamUtil;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayFactory;
import com.github.dcysteine.nesql.server.plugin.base.spec.RecipeSpec;
import com.github.dcysteine.nesql.server.plugin.base.spec.RecipeTypeSpec;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeRepository;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeTypeRepository;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping(path = "/base/advrecipe")
public class AdvancedRecipeSearchController {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeTypeRepository recipeTypeRepository;

    @Autowired
    private BaseDisplayFactory baseDisplayFactory;

    @GetMapping(path = "/search")
    public String search(
            @RequestParam Map<String, String> params,
            @RequestParam(required = false) Optional<String> recipeCategory,
            @RequestParam(required = false) Optional<String> recipeType,
            @RequestParam(required = false) Optional<String> inputItemName,
            @RequestParam(required = false) Optional<String> inputItemModId,
            @RequestParam(required = false) Optional<String> inputItemId,
            @RequestParam(required = false) Optional<String> inputItemGroupId,
            @RequestParam(required = false) Optional<String> inputFluidName,
            @RequestParam(required = false) Optional<String> inputFluidModId,
            @RequestParam(required = false) Optional<String> inputFluidId,
            @RequestParam(required = false) Optional<String> inputFluidGroupId,
            @RequestParam(required = false) Optional<String> outputItemName,
            @RequestParam(required = false) Optional<String> outputItemModId,
            @RequestParam(required = false) Optional<String> outputItemId,
            @RequestParam(required = false) Optional<String> outputFluidName,
            @RequestParam(required = false) Optional<String> outputFluidModId,
            @RequestParam(required = false) Optional<String> outputFluidId,
            Model model) {
        List<Specification<Recipe>> specs = new ArrayList<>();
        specs.add(ParamUtil.buildStringSpec(recipeCategory, RecipeSpec::buildRecipeCategorySpec));
        specs.add(ParamUtil.buildStringSpec(recipeType, RecipeSpec::buildRecipeTypeSpec));
        specs.add(ParamUtil.buildStringSpec(inputItemName, RecipeSpec::buildInputItemNameSpec));
        specs.add(ParamUtil.buildStringSpec(inputItemModId, RecipeSpec::buildInputItemModIdSpec));
        specs.add(ParamUtil.buildStringSpec(inputItemId, RecipeSpec::buildInputItemIdSpec));
        specs.add(
                ParamUtil.buildStringSpec(inputItemGroupId, RecipeSpec::buildInputItemGroupIdSpec));
        specs.add(ParamUtil.buildStringSpec(inputFluidName, RecipeSpec::buildInputFluidNameSpec));
        specs.add(ParamUtil.buildStringSpec(inputFluidModId, RecipeSpec::buildInputFluidModIdSpec));
        specs.add(ParamUtil.buildStringSpec(inputFluidId, RecipeSpec::buildInputFluidIdSpec));
        specs.add(
                ParamUtil.buildStringSpec(
                        inputFluidGroupId, RecipeSpec::buildInputFluidGroupIdSpec));
        specs.add(ParamUtil.buildStringSpec(outputItemName, RecipeSpec::buildOutputItemNameSpec));
        specs.add(ParamUtil.buildStringSpec(outputItemModId, RecipeSpec::buildOutputItemModIdSpec));
        specs.add(ParamUtil.buildStringSpec(outputItemId, RecipeSpec::buildOutputItemIdSpec));
        specs.add(ParamUtil.buildStringSpec(outputFluidName, RecipeSpec::buildOutputFluidNameSpec));
        specs.add(
                ParamUtil.buildStringSpec(outputFluidModId, RecipeSpec::buildOutputFluidModIdSpec));
        specs.add(ParamUtil.buildStringSpec(outputFluidId, RecipeSpec::buildOutputFluidIdSpec));

        IconBuilder iconBuilder = new IconBuilder(params, specs);
        iconBuilder.buildIcons();
        model.addAttribute("results", iconBuilder.icons);
        model.addAttribute("totalSize", iconBuilder.totalSize);

        return "plugin/base/advrecipe/search";
    }

    /** Helper class that constructs icons for the grouped recipe types. */
    private class IconBuilder {
        private final ImmutableMap<String, String> params;
        private final ImmutableList<Specification<Recipe>> specs;
        private List<Icon> icons;
        private long totalSize;

        private IconBuilder(Map<String, String> params, List<Specification<Recipe>> specs) {
            this.params = ImmutableMap.copyOf(params);
            // Must filter out nulls to avoid a NullPointerException.
            this.specs =
                    specs.stream()
                            .filter(Objects::nonNull)
                            .collect(ImmutableList.toImmutableList());

            this.icons = new ArrayList<>();
            this.totalSize = 0;
        }

        private void buildIcons() {
            List<RecipeType> recipeTypes =
                    recipeTypeRepository.findAll(
                            RecipeTypeSpec.buildMinRecipeCountSpec(1), RecipeTypeSpec.DEFAULT_SORT);
            recipeTypes.forEach(this::buildIcon);
        }

        private void buildIcon(RecipeType recipeType) {
            Specification<Recipe> recipeTypeIdSpec =
                    RecipeSpec.buildRecipeTypeIdSpec(recipeType.getId());
            Iterable<Specification<Recipe>> modifiedSpecs =
                    Iterables.concat(specs, Collections.singleton(recipeTypeIdSpec));

            long size = recipeRepository.count(Specification.allOf(modifiedSpecs));
            if (size == 0) {
                return;
            }
            totalSize += size;

            Map<String, String> modifiedParams = new HashMap<>(params);
            modifiedParams.put("recipeTypeId", recipeType.getId());
            modifiedParams.remove("recipeCategory");
            modifiedParams.remove("recipeType");
            String url = Table.RECIPE.getSearchUrl(modifiedParams);

            Icon icon =
                    baseDisplayFactory.buildDisplayRecipeTypeIcon(recipeType).toBuilder()
                            .setUrl(url)
                            .setBottomRight(NumberUtil.formatCompact(size))
                            .build();
            icons.add(icon);
        }
    }
}

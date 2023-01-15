package com.github.dcysteine.nesql.server.plugin.base.display.item;

import com.github.dcysteine.nesql.server.display.Icon;
import com.github.dcysteine.nesql.server.plugin.base.display.recipe.DisplayRecipe;
import com.github.dcysteine.nesql.server.util.StringUtil;
import com.github.dcysteine.nesql.server.util.UrlBuilder;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroupRepository;
import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeRepository;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class DisplayItem implements Comparable<DisplayItem> {
    public static DisplayItem create(
            Item item,
            ItemRepository itemRepository,
            ItemGroupRepository itemGroupRepository,
            RecipeRepository recipeRepository) {
        ImmutableList<Icon> recipesWithInput =
                recipeRepository.findByItemInput(item.getId()).stream()
                        .sorted()
                        .map(DisplayRecipe::buildIcon)
                        .collect(ImmutableList.toImmutableList());
        ImmutableList<Icon> recipesWithWildcardInput =
                recipeRepository.findByWildcardItemInput(item.getItemId()).stream()
                        .sorted()
                        .map(DisplayRecipe::buildIcon)
                        .collect(ImmutableList.toImmutableList());
        ImmutableList<Icon> recipesWithOutput =
                recipeRepository.findByItemOutput(item.getId()).stream()
                        .sorted()
                        .map(DisplayRecipe::buildIcon)
                        .collect(ImmutableList.toImmutableList());

        ImmutableList<Icon> itemGroupsContaining =
                itemGroupRepository.findByItem(item.getId()).stream()
                        .sorted()
                        .map(itemGroup -> DisplayItemGroup.buildIcon(itemGroup, itemRepository))
                        .collect(ImmutableList.toImmutableList());
        ImmutableList<Icon> itemGroupsContainingWildcard =
                itemGroupRepository.findByWildcardItemId(item.getItemId()).stream()
                        .sorted()
                        .map(itemGroup -> DisplayItemGroup.buildIcon(itemGroup, itemRepository))
                        .collect(ImmutableList.toImmutableList());

        ImmutableList<String> nbt = ImmutableList.of();
        if (item.hasNbt()) {
            nbt = ImmutableList.copyOf(StringUtil.prettyPrintNbt(item.getNbt()).split("\n"));
        }

        return new AutoValue_DisplayItem(
                item, buildIcon(item),
                nbt, ImmutableList.copyOf(item.getTooltip().split("\n")),
                recipesWithInput, recipesWithWildcardInput, recipesWithOutput,
                itemGroupsContaining, itemGroupsContainingWildcard);
    }

    public static Icon buildIcon(Item item) {
        return Icon.builder()
                .setDescription(item.getLocalizedName())
                .setUrl(UrlBuilder.buildItemUrl(item))
                .setImage(item.getImageFilePath())
                .build();
    }

    public abstract Item getItem();
    public abstract Icon getIcon();
    public abstract ImmutableList<String> getNbt();
    public abstract ImmutableList<String> getTooltip();
    public abstract ImmutableList<Icon> getRecipesWithInput();
    public abstract ImmutableList<Icon> getRecipesWithWildcardInput();
    public abstract ImmutableList<Icon> getRecipesWithOutput();
    public abstract ImmutableList<Icon> getItemGroupsContaining();
    public abstract ImmutableList<Icon> getItemGroupsContainingWildcard();

    @Override
    public int compareTo(DisplayItem other) {
        return getItem().compareTo(other.getItem());
    }
}

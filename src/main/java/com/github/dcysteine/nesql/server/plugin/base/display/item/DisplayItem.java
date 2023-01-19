package com.github.dcysteine.nesql.server.plugin.base.display.item;

import com.github.dcysteine.nesql.server.display.Icon;
import com.github.dcysteine.nesql.server.plugin.base.display.BaseDisplayDeps;
import com.github.dcysteine.nesql.server.util.StringUtil;
import com.github.dcysteine.nesql.server.util.UrlBuilder;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class DisplayItem implements Comparable<DisplayItem> {
    public static DisplayItem create(Item item, BaseDisplayDeps deps) {
        ImmutableList<Icon> recipesWithInput = ImmutableList.of();
        ImmutableList<Icon> recipesWithWildcardInput = ImmutableList.of();
        ImmutableList<Icon> recipesWithOutput = ImmutableList.of();
        ImmutableList<Icon> itemGroupsContaining = ImmutableList.of();
        ImmutableList<Icon> itemGroupsContainingWildcard = ImmutableList.of();

        ImmutableList<String> nbt = ImmutableList.of();
        if (item.hasNbt()) {
            nbt = ImmutableList.copyOf(StringUtil.prettyPrintNbt(item.getNbt()).split("\n"));
        }

        return new AutoValue_DisplayItem(
                item, buildIcon(item, deps),
                nbt, ImmutableList.copyOf(item.getTooltip().split("\n")),
                recipesWithInput, recipesWithWildcardInput, recipesWithOutput,
                itemGroupsContaining, itemGroupsContainingWildcard);
    }

    public static Icon buildIcon(Item item, BaseDisplayDeps deps) {
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

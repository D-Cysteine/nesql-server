package com.github.dcysteine.nesql.server.service;

import com.github.dcysteine.nesql.server.util.UrlBuilder;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroupRepository;
import com.github.dcysteine.nesql.sql.base.fluid.FluidRepository;
import com.github.dcysteine.nesql.sql.base.item.ItemGroupRepository;
import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeRepository;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeTypeRepository;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

/** Handles one-box searching by any ID. */
@Service
public class IdSearchService {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    FluidRepository fluidRepository;

    @Autowired
    ItemGroupRepository itemGroupRepository;

    @Autowired
    FluidGroupRepository fluidGroupRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeTypeRepository recipeTypeRepository;

    public String handleIdSearch(String id) {
        for (RepositoryIdSearch<?> search : getSearches()) {
            Optional<String> result = search.search(id);
            if (result.isPresent()) {
                // Trim off the leading "~" returned from UrlBuilder
                return "redirect:" + result.get().substring(1);
            }
        }
        return "not_found";
    }

    private ImmutableList<RepositoryIdSearch<?>> getSearches() {
        return ImmutableList.<RepositoryIdSearch<?>>builder()
                .add(RepositoryIdSearch.create(itemRepository, UrlBuilder::buildItemUrl))
                .add(RepositoryIdSearch.create(fluidRepository, UrlBuilder::buildFluidUrl))
                .add(RepositoryIdSearch.create(itemGroupRepository, UrlBuilder::buildItemGroupUrl))
                .add(
                        RepositoryIdSearch.create(
                                fluidGroupRepository, UrlBuilder::buildFluidGroupUrl))
                .add(RepositoryIdSearch.create(recipeRepository, UrlBuilder::buildRecipeUrl))
                .add(
                        RepositoryIdSearch.create(
                                recipeTypeRepository, UrlBuilder::buildRecipeTypeUrl))
                .build();
    }

    @AutoValue
    protected abstract static class RepositoryIdSearch<T> {
        public static <T> RepositoryIdSearch<T> create(
                JpaRepository<T, String> repository, Function<T, String> urlFunction) {
            return new AutoValue_IdSearchService_RepositoryIdSearch<>(repository, urlFunction);
        }

        public Optional<String> search(String id) {
            return getRepository().findById(id).map(getUrlFunction());
        }

        public abstract JpaRepository<T, String> getRepository();
        public abstract Function<T, String> getUrlFunction();
    }
}

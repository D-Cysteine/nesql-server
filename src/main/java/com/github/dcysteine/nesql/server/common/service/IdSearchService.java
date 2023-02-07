package com.github.dcysteine.nesql.server.common.service;

import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.sql.Identifiable;
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
                return "redirect:" + result.get();
            }
        }
        return "not_found";
    }

    private ImmutableList<RepositoryIdSearch<?>> getSearches() {
        return ImmutableList.<RepositoryIdSearch<?>>builder()
                .add(RepositoryIdSearch.create(itemRepository, Table.ITEM))
                .add(RepositoryIdSearch.create(fluidRepository, Table.FLUID))
                .add(RepositoryIdSearch.create(itemGroupRepository, Table.ITEM_GROUP))
                .add(RepositoryIdSearch.create(fluidGroupRepository, Table.FLUID_GROUP))
                .add(RepositoryIdSearch.create(recipeRepository, Table.RECIPE))
                .add(RepositoryIdSearch.create(recipeTypeRepository, Table.RECIPE_TYPE))
                .build();
    }

    @AutoValue
    protected abstract static class RepositoryIdSearch<T> {
        public static RepositoryIdSearch create(
                JpaRepository<? extends Identifiable<String>, String> repository, Table table) {
            return new AutoValue_IdSearchService_RepositoryIdSearch<>(repository, table);
        }

        public Optional<String> search(String id) {
            return getRepository().findById(id).map(getTable()::getViewUrlNoPrefix);
        }

        public abstract JpaRepository<? extends Identifiable<String>, String> getRepository();
        public abstract Table getTable();
    }
}

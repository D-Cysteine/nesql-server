package com.github.dcysteine.nesql.server.service;

import com.github.dcysteine.nesql.server.common.SearchResultsLayout;
import com.github.dcysteine.nesql.server.config.ExternalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.function.Function;

/** Contains helper logic for handling searching and pagination. */
@Service
public class SearchService {
    @Autowired
    private ExternalConfig externalConfig;

    /** Fallback sort, for any entities that don't have a more meaningful sort. */
    public static final Sort ID_SORT = Sort.by("id");

    public PageRequest buildPageRequest(int page, SearchResultsLayout layout) {
        return buildPageRequest(page, layout, ID_SORT);
    }

    public PageRequest buildPageRequest(int page, SearchResultsLayout layout, Sort sort) {
        // PageRequest uses 0-index page, but we want 1-indexed.
        return PageRequest.of(page - 1, layout.getPageSize(externalConfig), sort);
    }

    public <T extends JpaSpecificationExecutor<R>, R, D> void handleSearch(
            PageRequest pageRequest, Model model, T repository,
            Specification<R> spec, Function<R, D> buildDisplay) {
        Page<D> results = repository.findAll(spec, pageRequest).map(buildDisplay);
        model.addAttribute("page", results);

        String baseUri =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .replaceQueryParam("page")
                        .build().toUriString();
        model.addAttribute("baseUri", baseUri);
    }
}

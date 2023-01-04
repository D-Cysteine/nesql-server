package com.github.dcysteine.nesql.server.service;

import com.github.dcysteine.nesql.server.config.ExternalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
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

    public <T extends JpaSpecificationExecutor<R>, R, D> String handleGetAll(
            int page, Model model, T repository, Function<R, D> buildDisplay) {
        return handleSearch(page, model, repository, Specification.allOf(), buildDisplay);
    }

    public <T extends JpaSpecificationExecutor<R>, R, D> String handleSearch(
            int page, Model model,
            T repository, Specification<R> spec, Function<R, D> buildDisplay) {
        // Pageable uses 0-index page, but we want 1-indexed.
        Pageable pageable = Pageable.ofSize(externalConfig.getPageSize()).withPage(page - 1);
        Page<D> results = repository.findAll(spec, pageable).map(buildDisplay);

        String baseUri =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .replaceQueryParam("page")
                        .toUriString();

        model.addAttribute("page", results);
        model.addAttribute("baseUri", baseUri);
        return "search_results";
    }
}

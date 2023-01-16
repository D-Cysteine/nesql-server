package com.github.dcysteine.nesql.server.service;

import com.github.dcysteine.nesql.server.config.ExternalConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/** Contains helper logic for handling searching and pagination. */
@Service
public class SearchService {
    @Autowired
    private ExternalConfig externalConfig;

    @Autowired
    private HttpServletRequest request;

    /** Fallback sort, for any entities that don't have a more meaningful sort. */
    public static final Sort ID_SORT = Sort.by("id");

    public <T extends JpaSpecificationExecutor<R>, R, D> String handleGetAll(
            int page, Model model, T repository, Function<R, D> buildDisplay) {
        return handleGetAll(page, model, repository, ID_SORT, buildDisplay);
    }

    public <T extends JpaSpecificationExecutor<R>, R, D> String handleGetAll(
            int page, Model model, T repository, Sort sort, Function<R, D> buildDisplay) {
        return handleSearch(
                page, model, repository, Specification.allOf(), sort, buildDisplay);
    }

    public <T extends JpaSpecificationExecutor<R>, R, D> String handleSearch(
            int page, Model model, T repository,
            Specification<R> spec, Function<R, D> buildDisplay) {
        return handleSearch(page, model, repository, spec, ID_SORT, buildDisplay);
    }

    public <T extends JpaSpecificationExecutor<R>, R, D> String handleSearch(
            int page, Model model, T repository,
            Specification<R> spec, Sort sort, Function<R, D> buildDisplay) {
        // PageRequest uses 0-index page, but we want 1-indexed.
        PageRequest pageRequest = PageRequest.of(page - 1, externalConfig.getPageSize(), sort);
        Page<D> results = repository.findAll(spec, pageRequest).map(buildDisplay);

        String baseUri =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .replaceQueryParam("page")
                        .toUriString();
        baseUri = URLDecoder.decode(baseUri, StandardCharsets.UTF_8);

        model.addAttribute("request", request);
        model.addAttribute("page", results);
        model.addAttribute("baseUri", baseUri);
        return "search_results";
    }
}

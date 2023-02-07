package com.github.dcysteine.nesql.server;

import com.github.dcysteine.nesql.server.config.ExternalConfig;
import com.github.dcysteine.nesql.server.common.service.IdSearchService;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.sql.Metadata;
import com.github.dcysteine.nesql.sql.MetadataRepository;
import com.github.dcysteine.nesql.sql.base.fluid.FluidRepository;
import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Serves root-level endpoints. */
@Controller
public class RootController {
    @Autowired
    ExternalConfig externalConfig;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private IdSearchService idSearchService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private FluidRepository fluidRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private MetadataRepository metadataRepository;

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        model.addAttribute("itemCount", NumberUtil.formatInteger(itemRepository.count()));
        model.addAttribute("fluidCount", NumberUtil.formatInteger(fluidRepository.count()));
        model.addAttribute("recipeCount", NumberUtil.formatInteger(recipeRepository.count()));
        model.addAttribute("metadata", metadataRepository.findById(Metadata.ID).get());
        return "index";
    }

    @GetMapping("/idsearch")
    public String idSearch(@RequestParam String id) {
        return idSearchService.handleIdSearch(id);
    }

    @GetMapping("/notfound")
    public String notFound() {
        return "not_found";
    }

    @GetMapping("/shutdown")
    public String shutDown() {
        if (!externalConfig.isShutdownEnabled()) {
            return "shutdown_disabled";
        }

        // We need to actually shut down in a separate thread, so that we can proceed with serving
        // the shutdown page.
        new Thread(() -> SpringApplication.exit(context)).start();
        return "shutdown";
    }
}
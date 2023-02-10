package com.github.dcysteine.nesql.server.plugin.quest;

import com.github.dcysteine.nesql.server.common.SearchResultsLayout;
import com.github.dcysteine.nesql.server.common.service.SearchService;
import com.github.dcysteine.nesql.server.common.util.ParamUtil;
import com.github.dcysteine.nesql.server.plugin.quest.display.DisplayQuestLine;
import com.github.dcysteine.nesql.server.plugin.quest.display.QuestDisplayFactory;
import com.github.dcysteine.nesql.sql.quest.QuestLine;
import com.github.dcysteine.nesql.sql.quest.QuestLineRepository;
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
@RequestMapping(path = "/questline")
public class QuestLineController {
    @Autowired
    private QuestLineRepository questLineRepository;

    @Autowired
    private QuestDisplayFactory questDisplayFactory;

    @Autowired
    private SearchService searchService;

    @GetMapping(path = "/view/{quest_line_id}")
    public String view(@PathVariable(name = "quest_line_id") String id, Model model) {
        Optional<QuestLine> questLineOptional = questLineRepository.findById(id);
        if (questLineOptional.isEmpty()) {
            return "not_found";
        }
        QuestLine questLine = questLineOptional.get();
        DisplayQuestLine displayQuestLine = questDisplayFactory.buildDisplayQuestLine(questLine);

        model.addAttribute("questLine", questLine);
        model.addAttribute("displayQuestLine", displayQuestLine);
        return "plugin/quest/questline/view";
    }

    @GetMapping(path = "/search")
    public String search(
            @RequestParam(required = false) Optional<String> questLineName,
            @RequestParam(required = false) Optional<String> questLineDescription,
            @RequestParam(required = false) Optional<Integer> questLineId,
            @RequestParam(required = false) Optional<String> visibility,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        List<Specification<QuestLine>> specs = new ArrayList<>();
        specs.add(ParamUtil.buildStringSpec(questLineName, QuestLineSpec::buildQuestLineNameSpec));
        specs.add(
                ParamUtil.buildStringSpec(
                        questLineDescription, QuestLineSpec::buildQuestLineDescriptionSpec));
        specs.add(ParamUtil.buildSpec(questLineId, QuestLineSpec::buildQuestLineIdSpec));
        specs.add(ParamUtil.buildStringSpec(visibility, QuestLineSpec::buildVisibilitySpec));

        PageRequest pageRequest =
                searchService.buildPageRequest(
                        page, SearchResultsLayout.GRID, QuestLineSpec.DEFAULT_SORT);
        searchService.handleSearch(
                pageRequest, model, questLineRepository, Specification.allOf(specs),
                questDisplayFactory::buildDisplayQuestLineIcon);
        return "plugin/quest/questline/search";
    }
}

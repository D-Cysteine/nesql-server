package com.github.dcysteine.nesql.server.plugin.quest;

import com.github.dcysteine.nesql.server.common.SearchResultsLayout;
import com.github.dcysteine.nesql.server.common.service.SearchService;
import com.github.dcysteine.nesql.server.common.util.ParamUtil;
import com.github.dcysteine.nesql.server.plugin.quest.display.DisplayQuest;
import com.github.dcysteine.nesql.server.plugin.quest.display.QuestDisplayFactory;
import com.github.dcysteine.nesql.sql.quest.Quest;
import com.github.dcysteine.nesql.sql.quest.QuestRepository;
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
@RequestMapping(path = "/quest/quest")
public class QuestController {
    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private QuestDisplayFactory questDisplayFactory;

    @Autowired
    private SearchService searchService;

    @GetMapping(path = "/view/{quest_id}")
    public String view(@PathVariable(name = "quest_id") String id, Model model) {
        Optional<Quest> questOptional = questRepository.findById(id);
        if (questOptional.isEmpty()) {
            return "not_found";
        }
        Quest quest = questOptional.get();
        DisplayQuest displayQuest = questDisplayFactory.buildDisplayQuest(quest);

        model.addAttribute("quest", quest);
        model.addAttribute("displayQuest", displayQuest);
        return "plugin/quest/quest/view";
    }

    @GetMapping(path = "/search")
    public String search(
            @RequestParam(required = false) Optional<String> questName,
            @RequestParam(required = false) Optional<String> questDescription,
            @RequestParam(required = false) Optional<Integer> questId,
            @RequestParam(required = false) Optional<String> visibility,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        List<Specification<Quest>> specs = new ArrayList<>();
        specs.add(ParamUtil.buildStringSpec(questName, QuestSpec::buildQuestNameSpec));
        specs.add(ParamUtil.buildStringSpec(questDescription, QuestSpec::buildQuestDescriptionSpec));
        specs.add(ParamUtil.buildSpec(questId, QuestSpec::buildQuestIdSpec));
        specs.add(ParamUtil.buildStringSpec(visibility, QuestSpec::buildVisibilitySpec));

        PageRequest pageRequest =
                searchService.buildPageRequest(
                        page, SearchResultsLayout.GRID, QuestSpec.DEFAULT_SORT);
        searchService.handleSearch(
                pageRequest, model, questRepository, Specification.allOf(specs),
                questDisplayFactory::buildDisplayQuestIcon);
        return "plugin/quest/quest/search";
    }
}

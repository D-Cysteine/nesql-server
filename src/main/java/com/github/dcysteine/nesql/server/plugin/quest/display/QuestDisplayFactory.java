package com.github.dcysteine.nesql.server.plugin.quest.display;

import com.github.dcysteine.nesql.server.common.display.Icon;
import com.github.dcysteine.nesql.server.common.service.DisplayService;
import com.github.dcysteine.nesql.sql.quest.Quest;
import com.github.dcysteine.nesql.sql.quest.QuestLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service providing convenient methods for building display objects. */
@Service
public class QuestDisplayFactory {
    @Autowired
    private DisplayService service;

    public DisplayQuest buildDisplayQuest(Quest quest) {
        return DisplayQuest.create(quest, service);
    }

    public Icon buildDisplayQuestIcon(Quest quest) {
        return DisplayQuest.buildIcon(quest, service);
    }

    public DisplayQuestLine buildDisplayQuestLine(QuestLine questLine) {
        return DisplayQuestLine.create(questLine, service);
    }

    public Icon buildDisplayQuestLineIcon(QuestLine questLine) {
        return DisplayQuestLine.buildIcon(questLine, service);
    }
}
